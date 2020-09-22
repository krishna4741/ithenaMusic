package org.jaudiotagger.audio.flac.metadatablock;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.InvalidFrameException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;



public class MetadataBlockDataPicture implements MetadataBlockData, TagField
{
    public static final String IMAGE_IS_URL = "-->";

    private int pictureType;
    private String mimeType ="";
    private String description;
    private int width;
    private int height;
    private int colourDepth;
    private int indexedColouredCount;
    private byte[] imageData;

    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac.MetadataBlockDataPicture");

    private void initFromByteBuffer(ByteBuffer rawdata) throws IOException, InvalidFrameException
    {
        //Picture Type
        pictureType = rawdata.getInt();
        if (pictureType >= PictureTypes.getInstanceOf().getSize())
        {
            throw new InvalidFrameException("PictureType was:" + pictureType + "but the maximum allowed is " + (PictureTypes.getInstanceOf().getSize() - 1));
        }

        //MimeType
        int mimeTypeSize = rawdata.getInt();
        mimeType = getString(rawdata, mimeTypeSize, "ISO-8859-1");

        //Description
        int descriptionSize = rawdata.getInt();
        description = getString(rawdata, descriptionSize, "UTF-8");

        //Image width
        width = rawdata.getInt();

        //Image height
        height = rawdata.getInt();

        //Colour Depth
        colourDepth = rawdata.getInt();

        //Indexed Colour Count
        indexedColouredCount = rawdata.getInt();

        //ImageData
        int rawdataSize = rawdata.getInt();
        imageData = new byte[rawdataSize];
        rawdata.get(imageData);

        logger.config("Read image:" + this.toString());
    }


    public MetadataBlockDataPicture(ByteBuffer rawdata) throws IOException, InvalidFrameException
    {
        initFromByteBuffer(rawdata);
    }


    //TODO check for buffer underflows see http://research.eeye.com/html/advisories/published/AD20071115.html
    public MetadataBlockDataPicture(MetadataBlockHeader header, RandomAccessFile raf) throws IOException, InvalidFrameException
    {
        ByteBuffer rawdata = ByteBuffer.allocate(header.getDataLength());
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength())
        {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();
        initFromByteBuffer(rawdata);


    }


    public MetadataBlockDataPicture(byte[] imageData, int pictureType, String mimeType, String description, int width, int height, int colourDepth, int indexedColouredCount)
    {
        //Picture Type
        this.pictureType = pictureType;

        //MimeType
        if(mimeType!=null)
        {
            this.mimeType = mimeType;
        }

        //Description
        this.description = description;

        this.width = width;

        this.height = height;

        this.colourDepth = colourDepth;

        this.indexedColouredCount = indexedColouredCount;
        //ImageData
        this.imageData = imageData;
    }

    private String getString(ByteBuffer rawdata, int length, String charset) throws IOException
    {
        byte[] tempbuffer = new byte[length];
        rawdata.get(tempbuffer);
        return new String(tempbuffer, charset);
    }

    public byte[] getBytes()
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Utils.getSizeBEInt32(pictureType));
            baos.write(Utils.getSizeBEInt32(mimeType.length()));
            baos.write(mimeType.getBytes("ISO-8859-1"));
            baos.write(Utils.getSizeBEInt32(description.length()));
            baos.write(description.getBytes("UTF-8"));
            baos.write(Utils.getSizeBEInt32(width));
            baos.write(Utils.getSizeBEInt32(height));
            baos.write(Utils.getSizeBEInt32(colourDepth));
            baos.write(Utils.getSizeBEInt32(indexedColouredCount));
            baos.write(Utils.getSizeBEInt32(imageData.length));
            baos.write(imageData);
            return baos.toByteArray();

        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe.getMessage());
        }
    }

    public int getLength()
    {
        return getBytes().length;
    }

    public int getPictureType()
    {
        return pictureType;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getDescription()
    {
        return description;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getColourDepth()
    {
        return colourDepth;
    }

    public int getIndexedColourCount()
    {
        return indexedColouredCount;
    }

    public byte[] getImageData()
    {
        return imageData;
    }


    public boolean isImageUrl()
    {
        return getMimeType().equals(IMAGE_IS_URL);
    }


    public String getImageUrl()
    {
        if (isImageUrl())
        {
            return Utils.getString(getImageData(), 0, getImageData().length, TextEncoding.CHARSET_ISO_8859_1);
        }
        else
        {
            return "";
        }
    }

    public String toString()
    {
        return PictureTypes.getInstanceOf().getValueForId(pictureType) + ":" + mimeType + ":" + description + ":" + "width:" + width + ":height:" + height + ":colourdepth:" + colourDepth + ":indexedColourCount:" + indexedColouredCount + ":image size in bytes:" + imageData.length;
    }


    public void copyContent(TagField field)
    {
        throw new UnsupportedOperationException();
    }


    public String getId()
    {
        return FieldKey.COVER_ART.name();
    }


    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        return getBytes();
    }


    public boolean isBinary()
    {
        return true;
    }


    public void isBinary(boolean b)
    {
        //Do nothing, always true
    }


    public boolean isCommon()
    {
        return true;
    }


    public boolean isEmpty()
    {
        return false;
    }


}
