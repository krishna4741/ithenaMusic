
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.mp4.atom.Mp4NameBox;
import org.jaudiotagger.tag.reference.PictureTypes;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Mp4TagCoverField extends Mp4TagBinaryField
{

    //Type
    private Mp4FieldType imageType;

    //Contains the size of each atom including header, required because may only have data atom or
    //may have data and name atom
    private int dataAndHeaderSize;


    public Mp4TagCoverField()
    {
        super(Mp4FieldKey.ARTWORK.getFieldName());
    }


    public int getDataAndHeaderSize()
    {
        return dataAndHeaderSize;
    }


    public Mp4TagCoverField(ByteBuffer raw,Mp4FieldType imageType) throws UnsupportedEncodingException
    {
        super(Mp4FieldKey.ARTWORK.getFieldName(), raw);
        this.imageType=imageType;
        if(!Mp4FieldType.isCoverArtType(imageType))
        {
            logger.warning(ErrorMessage.MP4_IMAGE_FORMAT_IS_NOT_TO_EXPECTED_TYPE.getMsg(imageType));
        }
    }


    public Mp4TagCoverField(byte[] data)
    {
        super(Mp4FieldKey.ARTWORK.getFieldName(), data);

        //Read signature
        if (ImageFormats.binaryDataIsPngFormat(data))
        {
            imageType = Mp4FieldType.COVERART_PNG;
        }
        else if (ImageFormats.binaryDataIsJpgFormat(data))
        {
            imageType = Mp4FieldType.COVERART_JPEG;
        }
        else if (ImageFormats.binaryDataIsGifFormat(data))
        {
            imageType = Mp4FieldType.COVERART_GIF;
        }
        else if (ImageFormats.binaryDataIsBmpFormat(data))
        {
            imageType = Mp4FieldType.COVERART_BMP;
        }
        else
        {
            logger.warning(ErrorMessage.GENERAL_UNIDENITIFED_IMAGE_FORMAT.getMsg());
            imageType = Mp4FieldType.COVERART_PNG;
        }
    }

  

    public Mp4FieldType getFieldType()
    {
        return imageType;
    }

    public boolean isBinary()
    {
        return true;
    }


    public String toString()
    {
        return imageType +":" + dataBytes.length + "bytes";
    }

    protected void build(ByteBuffer raw)
    {
        Mp4BoxHeader header = new Mp4BoxHeader(raw);
        dataSize = header.getDataLength();
        dataAndHeaderSize = header.getLength();

        //Skip the version and length fields
        raw.position(raw.position() + Mp4DataBox.PRE_DATA_LENGTH);

        //Read the raw data into byte array
        this.dataBytes = new byte[dataSize - Mp4DataBox.PRE_DATA_LENGTH];
        raw.get(dataBytes,0,dataBytes.length);

        //Is there room for another atom (remember actually passed all the data so unless Covr is last atom
        //there will be room even though more likely to be for the text top level atom)
        int positionAfterDataAtom = raw.position();
        if (raw.position() + Mp4BoxHeader.HEADER_LENGTH <= raw.limit())
        {
            //Is there a following name field (not the norm)
            Mp4BoxHeader nameHeader = new Mp4BoxHeader(raw);
            if (nameHeader.getId().equals(Mp4NameBox.IDENTIFIER))
            {
                dataSize += nameHeader.getDataLength();
                dataAndHeaderSize += nameHeader.getLength();
            }
            else
            {
                raw.position(positionAfterDataAtom);
            }
        }

        //After returning buffers position will be after the end of this atom
    }


    public static String getMimeTypeForImageType(Mp4FieldType imageType)
    {
        if(imageType==Mp4FieldType.COVERART_PNG)
        {
            return ImageFormats.MIME_TYPE_PNG;
        }
        else if(imageType==Mp4FieldType.COVERART_JPEG)
        {
            return ImageFormats.MIME_TYPE_JPEG;
        }
        else if(imageType==Mp4FieldType.COVERART_GIF)
        {
            return ImageFormats.MIME_TYPE_GIF;
        }
        else if(imageType==Mp4FieldType.COVERART_BMP)
        {
            return ImageFormats.MIME_TYPE_BMP;
        }
        else
        {
            return null;
        }
    }
}
