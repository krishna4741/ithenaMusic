
package org.jaudiotagger.tag.id3.valuepair;

import java.util.HashMap;
import java.util.Map;


//TODO identifying PICT, bit more difficult because in certain formats has an empty 512byte header
public class  ImageFormats
{
    public static final String V22_JPG_FORMAT = "JPG";
    public static final String V22_PNG_FORMAT = "PNG";
    public static final String V22_GIF_FORMAT = "GIF";
    public static final String V22_BMP_FORMAT = "BMP";
    public static final String V22_TIF_FORMAT = "TIF";
    public static final String V22_PDF_FORMAT = "PDF";
    public static final String V22_PIC_FORMAT = "PIC";


    public static final String MIME_TYPE_JPEG = "image/jpeg";
    public static final String MIME_TYPE_PNG  = "image/png";
    public static final String MIME_TYPE_GIF  = "image/gif";
    public static final String MIME_TYPE_BMP  = "image/bmp";
    public static final String MIME_TYPE_TIFF = "image/tiff";
    public static final String MIME_TYPE_PDF  = "image/pdf";
    public static final String MIME_TYPE_PICT = "image/x-pict";


    public static final String MIME_TYPE_JPG  = "image/jpg";

    private static Map<String, String> imageFormatsToMimeType = new HashMap<String, String>();
    private static Map<String, String> imageMimeTypeToFormat = new HashMap <String, String>();

    static
    {
        imageFormatsToMimeType.put(V22_JPG_FORMAT, MIME_TYPE_JPEG);
        imageFormatsToMimeType.put(V22_PNG_FORMAT, MIME_TYPE_PNG);
        imageFormatsToMimeType.put(V22_GIF_FORMAT, MIME_TYPE_GIF);
        imageFormatsToMimeType.put(V22_BMP_FORMAT, MIME_TYPE_BMP);
        imageFormatsToMimeType.put(V22_TIF_FORMAT, MIME_TYPE_TIFF);
        imageFormatsToMimeType.put(V22_PDF_FORMAT, MIME_TYPE_PDF);
        imageFormatsToMimeType.put(V22_PIC_FORMAT, MIME_TYPE_PICT);

        String value;
        for (String key : imageFormatsToMimeType.keySet())
        {
            value = imageFormatsToMimeType.get(key);
            imageMimeTypeToFormat.put(value, key);
        }

        //The mapping isn'timer one-one lets add other mimetypes
        imageMimeTypeToFormat.put(MIME_TYPE_JPG, V22_JPG_FORMAT);
    }


    public static String getMimeTypeForFormat(String format)
    {
        return imageFormatsToMimeType.get(format);
    }


    public static String getFormatForMimeType(String mimeType)
    {
        return imageMimeTypeToFormat.get(mimeType);
    }


    public static boolean binaryDataIsPngFormat(byte[] data)
    {
        //Read signature
        if(data.length<4)
        {
            return false;
        }
        return (0x89 == (data[0] & 0xff)) && (0x50 == (data[1] & 0xff)) && (0x4E == (data[2] & 0xff)) && (0x47 == (data[3] & 0xff));
    }


    public static boolean binaryDataIsJpgFormat(byte[] data)
    {
        if(data.length<4)
        {
            return false;
        }
        //Read signature
        //Can be Can be FF D8 FF DB (samsung) , FF D8 FF E0 (standard) or FF D8 FF E1 or some other formats
        //see http://www.garykessler.net/library/file_sigs.html
        //FF D8 is SOI Marker, FFE0 or FFE1 is JFIF Marker
        return (0xff == (data[0] & 0xff)) && (0xd8 == (data[1] & 0xff)) && (0xff == (data[2] & 0xff)) && (0xdb <= (data[3] & 0xff));
    }


    public static boolean binaryDataIsGifFormat(byte[] data)
    {
        if(data.length<3)
        {
            return false;
        }
        //Read signature
        return (0x47 == (data[0] & 0xff)) && (0x49 == (data[1] & 0xff)) && (0x46 == (data[2] & 0xff));
    }


    public static boolean binaryDataIsBmpFormat(byte[] data)
    {
        if(data.length<2)
        {
            return false;
        }
        //Read signature
        return (0x42 == (data[0] & 0xff)) && (0x4d == (data[1] & 0xff));
    }


    public static boolean binaryDataIsPdfFormat(byte[] data)
    {
        if(data.length<4)
        {
            return false;
        }
        //Read signature
        return (0x25 == (data[0] & 0xff)) && (0x50 == (data[1] & 0xff)) && (0x44 == (data[2] & 0xff)) && (0x46 == (data[3] & 0xff));
    }


    public static boolean binaryDataIsTiffFormat(byte[] data)
    {
        if(data.length<4)
        {
            return false;
        }
        //Read signature Intel
        return (
                ((0x49 == (data[0] & 0xff)) && (0x49 == (data[1] & 0xff)) && (0x2a == (data[2] & 0xff)) && (0x00 == (data[3] & 0xff)))
                ||
                ((0x4d == (data[0] & 0xff)) && (0x4d == (data[1] & 0xff)) && (0x00 == (data[2] & 0xff)) && (0x2a == (data[3] & 0xff)))
                );
    }


    public static boolean isPortableFormat(byte[] data)
    {
        return binaryDataIsPngFormat(data) ||  binaryDataIsJpgFormat(data) ||  binaryDataIsGifFormat(data);     
    }


    public static String getMimeTypeForBinarySignature(byte[] data)
    {
        if(binaryDataIsPngFormat(data))
        {
            return MIME_TYPE_PNG;
        }
        else if(binaryDataIsJpgFormat(data))
        {
            return MIME_TYPE_JPEG;
        }
        else if(binaryDataIsGifFormat(data))
        {
            return MIME_TYPE_GIF;
        }
        else if(binaryDataIsBmpFormat(data))
        {
            return MIME_TYPE_BMP;
        }
        else if(binaryDataIsPdfFormat(data))
        {
            return MIME_TYPE_PDF;
        }
        else if(binaryDataIsTiffFormat(data))
        {
            return MIME_TYPE_TIFF;
        }
        else
        {
            return null;
        }
    }
}
