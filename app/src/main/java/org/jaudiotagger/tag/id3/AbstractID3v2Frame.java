
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;
import org.jaudiotagger.tag.id3.framebody.FrameBodyEncrypted;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.utils.EqualsUtil;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.logging.Level;


public abstract class AbstractID3v2Frame extends AbstractTagFrame implements TagTextField
{

    protected static final String TYPE_FRAME = "frame";
    protected static final String TYPE_FRAME_SIZE = "frameSize";
    protected static final String UNSUPPORTED_ID = "Unsupported";

    //Frame identifier
    protected String identifier = "";

    //Frame Size
    protected int frameSize;

    //The purpose of this is to provide the filename that should be used when writing debug messages
    //when problems occur reading or writing to file, otherwise it is difficult to track down the error
    //when processing many files
    private String loggingFilename = "";


    protected abstract int getFrameIdSize();


    protected abstract int getFrameSizeSize();


    protected abstract int getFrameHeaderSize();


    protected AbstractID3v2Frame()
    {
        }


    StatusFlags statusFlags = null;


    EncodingFlags encodingFlags = null;


    public AbstractID3v2Frame(AbstractID3v2Frame frame)
    {
        super(frame);
    }


    public AbstractID3v2Frame(AbstractID3v2FrameBody body)
    {
        this.frameBody = body;
        this.frameBody.setHeader(this);
    }


    //TODO the identifier checks should be done in the relevent subclasses
    public AbstractID3v2Frame(String identifier)
    {
        logger.config("Creating empty frame of type" + identifier);
        this.identifier = identifier;

        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date.
        try
        {
            Class<AbstractID3v2FrameBody> c = (Class<AbstractID3v2FrameBody>) Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            frameBody = c.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            logger.severe(cnfe.getMessage());
            frameBody = new FrameBodyUnsupported(identifier);
        }
        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE, "InstantiationException:" + identifier, ie);
            throw new RuntimeException(ie);
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE, "IllegalAccessException:" + identifier, iae);
            throw new RuntimeException(iae);
        }
        frameBody.setHeader(this);
        if (this instanceof ID3v24Frame)
        {
            frameBody.setTextEncoding(TagOptionSingleton.getInstance().getId3v24DefaultTextEncoding());
        }
        else if (this instanceof ID3v23Frame)
        {
            frameBody.setTextEncoding(TagOptionSingleton.getInstance().getId3v23DefaultTextEncoding());
        }

        logger.config("Created empty frame of type" + identifier);
    }


    protected String getLoggingFilename()
    {
        return loggingFilename;
    }


    protected void setLoggingFilename(String loggingFilename)
    {
        this.loggingFilename = loggingFilename;
    }


    //TODO, this is confusing only returns the frameId, which does not neccessarily uniquely
    //identify the frame
    public String getId()
    {
        return getIdentifier();
    }


    public String getIdentifier()
    {
        return identifier;
    }

    //TODO:needs implementing but not sure if this method is required at all
    public void copyContent(TagField field)
    {

    }


    protected AbstractID3v2FrameBody readEncryptedBody(String identifier, ByteBuffer byteBuffer, int frameSize)
            throws InvalidFrameException, InvalidDataTypeException 
    {
        try
        {
            AbstractID3v2FrameBody frameBody = new  FrameBodyEncrypted(identifier,byteBuffer, frameSize);
            frameBody.setHeader(this);
            return frameBody;
        }
        catch(InvalidTagException ite)
        {
            throw new InvalidDataTypeException(ite);
        }
    }

    protected boolean isPadding(byte[] buffer)
    {
        if(
                (buffer[0]=='\0')&&
                (buffer[1]=='\0')&&
                (buffer[2]=='\0')&&
                (buffer[3]=='\0')
           )
        {
            return true;
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    //TODO using reflection is rather slow perhaps we should change this
    protected AbstractID3v2FrameBody readBody(String identifier, ByteBuffer byteBuffer, int frameSize)
            throws InvalidFrameException, InvalidDataTypeException
    {
        //Use reflection to map id to frame body, which makes things much easier
        //to keep things up to date,although slight performance hit.
        logger.finest("Creating framebody:start");

        AbstractID3v2FrameBody frameBody;
        try
        {
            Class<AbstractID3v2FrameBody> c = (Class<AbstractID3v2FrameBody>) Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            Class<?>[] constructorParameterTypes = {Class.forName("java.nio.ByteBuffer"), Integer.TYPE};
            Object[] constructorParameterValues = {byteBuffer, frameSize};
            Constructor<AbstractID3v2FrameBody> construct = c.getConstructor(constructorParameterTypes);
            frameBody = (construct.newInstance(constructorParameterValues));
        }
        //No class defined for this frame type,use FrameUnsupported
        catch (ClassNotFoundException cex)
        {
            logger.config(getLoggingFilename() + ":" + "Identifier not recognised:" + identifier + " using FrameBodyUnsupported");
            try
            {
                frameBody = new FrameBodyUnsupported(byteBuffer, frameSize);
            }
            //Should only throw InvalidFrameException but unfortunately legacy hierachy forces
            //read method to declare it can throw InvalidtagException
            catch (InvalidFrameException ife)
            {
                throw ife;
            }
            catch (InvalidTagException te)
            {
                throw new InvalidFrameException(te.getMessage());
            }
        }
        //An error has occurred during frame instantiation, if underlying cause is an unchecked exception or error
        //propagate it up otherwise mark this frame as invalid
        catch (InvocationTargetException ite)
        {
            logger.severe(getLoggingFilename() + ":" + "An error occurred within abstractID3v2FrameBody for identifier:" + identifier + ":" + ite.getCause().getMessage());
            if (ite.getCause() instanceof Error)
            {
                throw (Error) ite.getCause();
            }
            else if (ite.getCause() instanceof RuntimeException)
            {
                throw (RuntimeException) ite.getCause();
            }
            else if(ite.getCause() instanceof  InvalidFrameException )
            {
                throw (InvalidFrameException)ite.getCause();
            }
            else if(ite.getCause() instanceof  InvalidDataTypeException )
            {
                throw (InvalidDataTypeException)ite.getCause();
            }
            else
            {
                throw new InvalidFrameException(ite.getCause().getMessage());
            }
        }
        //No Such Method should not happen
        catch (NoSuchMethodException sme)
        {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "No such method:" + sme.getMessage(), sme);
            throw new RuntimeException(sme.getMessage());
        }
        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "Instantiation exception:" + ie.getMessage(), ie);
            throw new RuntimeException(ie.getMessage());
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "Illegal access exception :" + iae.getMessage(), iae);
            throw new RuntimeException(iae.getMessage());
        }
        logger.finest(getLoggingFilename() + ":" + "Created framebody:end" + frameBody.getIdentifier());
        frameBody.setHeader(this);
        return frameBody;
    }


    protected String readIdentifier(ByteBuffer byteBuffer) throws PaddingException,InvalidFrameException
    {
        byte[] buffer = new byte[getFrameIdSize()];

        if (byteBuffer.position() + getFrameHeaderSize() >= byteBuffer.limit())
        {
            logger.warning(getLoggingFilename() + ":" + "No space to find another frame:");
            throw new InvalidFrameException(getLoggingFilename() + ":" + "No space to find another frame");
        }

        //Read the Frame Identifier
        byteBuffer.get(buffer, 0, getFrameIdSize());

        if(isPadding(buffer))
        {
            throw new PaddingException(getLoggingFilename() + ":only padding found");
        }

        identifier = new String(buffer);
        logger.fine(getLoggingFilename() + ":" + "Identifier is" + identifier);
        return identifier;
    }


    @SuppressWarnings("unchecked")
    //TODO using reflection is rather slow perhaps we should change this
    protected AbstractID3v2FrameBody readBody(String identifier, AbstractID3v2FrameBody body) throws InvalidFrameException
    {

        AbstractID3v2FrameBody frameBody;
        try
        {
            Class<AbstractID3v2FrameBody> c = (Class<AbstractID3v2FrameBody>) Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            Class<?>[] constructorParameterTypes = {body.getClass()};
            Object[] constructorParameterValues = {body};
            Constructor<AbstractID3v2FrameBody> construct = c.getConstructor(constructorParameterTypes);
            frameBody = (construct.newInstance(constructorParameterValues));
        }
        catch (ClassNotFoundException cex)
        {
            logger.config("Identifier not recognised:" + identifier + " unable to create framebody");
            throw new InvalidFrameException("FrameBody" + identifier + " does not exist");
        }
        //If suitable constructor does not exist
        catch (NoSuchMethodException sme)
        {
            logger.log(Level.SEVERE, "No such method:" + sme.getMessage(), sme);
            throw new InvalidFrameException("FrameBody" + identifier + " does not have a constructor that takes:" + body.getClass().getName());
        }
        catch (InvocationTargetException ite)
        {
            logger.severe("An error occurred within abstractID3v2FrameBody");
            logger.log(Level.SEVERE, "Invocation target exception:" + ite.getCause().getMessage(), ite.getCause());
            if (ite.getCause() instanceof Error)
            {
                throw (Error) ite.getCause();
            }
            else if (ite.getCause() instanceof RuntimeException)
            {
                throw (RuntimeException) ite.getCause();
            }
            else
            {
                throw new InvalidFrameException(ite.getCause().getMessage());
            }
        }

        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE, "Instantiation exception:" + ie.getMessage(), ie);
            throw new RuntimeException(ie.getMessage());
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE, "Illegal access exception :" + iae.getMessage(), iae);
            throw new RuntimeException(iae.getMessage());
        }

        logger.finer("frame Body created" + frameBody.getIdentifier());
        frameBody.setHeader(this);
        return frameBody;
    }

    public byte[] getRawContent()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        write(baos);
        return baos.toByteArray();
    }

    public abstract void write(ByteArrayOutputStream tagBuffer);


    public void isBinary(boolean b)
    {
        //do nothing because whether or not a field is binary is defined by its id and is immutable
    }


    public boolean isEmpty()
    {
        AbstractTagFrameBody body = this.getBody();
        if (body == null)
        {
            return true;
        }
        //TODO depends on the body
        return false;
    }

    public StatusFlags getStatusFlags()
    {
        return statusFlags;
    }

    public EncodingFlags getEncodingFlags()
    {
        return encodingFlags;
    }

    public class StatusFlags
    {
        protected static final String TYPE_FLAGS = "statusFlags";

        protected byte originalFlags;
        protected byte writeFlags;

        protected StatusFlags()
        {

        }


        public byte getOriginalFlags()
        {
            return originalFlags;
        }


        public byte getWriteFlags()
        {
            return writeFlags;
        }

        public void createStructure()
        {
        }

        public boolean equals(Object obj)
        {
            if ( this == obj ) return true;

            if (!(obj instanceof StatusFlags))
            {
                return false;
            }
            StatusFlags that = (StatusFlags) obj;


            return
                  EqualsUtil.areEqual(this.getOriginalFlags(), that.getOriginalFlags()) &&
                  EqualsUtil.areEqual(this.getWriteFlags(), that.getWriteFlags()) ;

        }
    }

    class EncodingFlags
    {
        protected static final String TYPE_FLAGS = "encodingFlags";

        protected byte flags;

        protected EncodingFlags()
        {
            resetFlags();
        }

        protected EncodingFlags(byte flags)
        {
            setFlags(flags);
        }

        public byte getFlags()
        {
            return flags;
        }

        public void setFlags(byte flags)
        {
            this.flags = flags;
        }

        public void resetFlags()
        {
            setFlags((byte) 0);
        }

        public void createStructure()
        {
        }

        public boolean equals(Object obj)
        {
            if ( this == obj ) return true;

            if (!(obj instanceof EncodingFlags))
            {
                return false;
            }
            EncodingFlags that = (EncodingFlags) obj;


            return EqualsUtil.areEqual(this.getFlags(), that.getFlags());

        }
    }


    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_FRAME, getIdentifier());
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_FRAME);
    }

    public boolean equals(Object obj)
    {
        if ( this == obj ) return true;
        if (!(obj instanceof AbstractID3v2Frame))
        {
            return false;
        }

        AbstractID3v2Frame that = (AbstractID3v2Frame) obj;
        return super.equals(that);
    }


    public String getContent()
    {
        return getBody().getUserFriendlyValue();
    }


    public String getEncoding()
    {
        return TextEncoding.getInstanceOf().getValueForId(this.getBody().getTextEncoding());
    }


    public void setContent(String content)
    {
        throw new UnsupportedOperationException("Not implemented please use the generic tag methods for setting content");
    }

}
