
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.InvalidFrameException;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.AbstractDataType;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public abstract class AbstractID3v2FrameBody extends AbstractTagFrameBody
{
    protected static final String TYPE_BODY = "body";



    private int size;



    protected AbstractID3v2FrameBody()
    {
    }


    protected AbstractID3v2FrameBody(AbstractID3v2FrameBody copyObject)
    {
        super(copyObject);
    }


    protected AbstractID3v2FrameBody(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super();
        setSize(frameSize);
        this.read(byteBuffer);

    }


    public abstract String getIdentifier();



    public int getSize()
    {
        return size;
    }


    public void setSize(int size)
    {
        this.size = size;
    }


    public void setSize()
    {
        size = 0;
        for (AbstractDataType object : objectList)
        {
            size += object.getSize();
        }
        }


    public boolean equals(Object obj)
    {
        return (obj instanceof AbstractID3v2FrameBody) && super.equals(obj);
        }


    //TODO why don'timer we just slice byteBuffer, set limit to size and convert readByteArray to take a ByteBuffer
    //then we wouldn'timer have to temporary allocate space for the buffer, using lots of needless memory
    //and providing extra work for the garbage collector.
    public void read(ByteBuffer byteBuffer) throws InvalidTagException
    {
        int size = getSize();
        logger.config("Reading body for" + this.getIdentifier() + ":" + size);

        //Allocate a buffer to the size of the Frame Body and read from file
        byte[] buffer = new byte[size];
        byteBuffer.get(buffer);

        //Offset into buffer, incremented by length of previous dataType
        //this offset is only used internally to decide where to look for the next
        //dataType within a frameBody, it does not decide where to look for the next frame body
        int offset = 0;

        //Go through the ObjectList of the Frame reading the data into the
        for (AbstractDataType object : objectList)
        //correct dataType.
        {
            logger.finest("offset:" + offset);

            //The read has extended further than the defined frame size (ok to extend upto
            //size because the next datatype may be of length 0.)
            if (offset > (size))
            {
                logger.warning("Invalid Size for FrameBody");
                throw new InvalidFrameException("Invalid size for Frame Body");
            }

            //Try and load it with data from the Buffer
            //if it fails frame is invalid
            try
            {
                object.readByteArray(buffer, offset);
            }
            catch (InvalidDataTypeException e)
            {
                logger.warning("Problem reading datatype within Frame Body:" + e.getMessage());
                throw e;
            }
            //Increment Offset to start of next datatype.
            offset += object.getSize();
        }
    }


    public void write(ByteArrayOutputStream tagBuffer)

    {
        logger.config("Writing frame body for" + this.getIdentifier() + ":Est Size:" + size);
        //Write the various fields to file in order
        for (AbstractDataType object : objectList)
        {
            byte[] objectData = object.writeByteArray();
            if (objectData != null)
            {
                try
                {
                    tagBuffer.write(objectData);
                }
                catch (IOException ioe)
                {
                    //This could never happen coz not writing to file, so convert to RuntimeException
                    throw new RuntimeException(ioe);
                }
            }
        }
        setSize();
        logger.config("Written frame body for" + this.getIdentifier() + ":Real Size:" + size);

    }


    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_BODY, "");
        for (AbstractDataType nextObject : objectList)
        {
            nextObject.createStructure();
        }
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_BODY);
    }
}
