
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;


public abstract class AbstractFrameBodyUrlLink extends AbstractID3v2FrameBody
{


    protected AbstractFrameBodyUrlLink()
    {
        super();
    }


    protected AbstractFrameBodyUrlLink(AbstractFrameBodyUrlLink body)
    {
        super(body);
    }


    public AbstractFrameBodyUrlLink(String urlLink)
    {
        setObjectValue(DataTypes.OBJ_URLLINK, urlLink);
    }


    protected AbstractFrameBodyUrlLink(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    public String getUserFriendlyValue()
    {
        return getUrlLink();
    }


    public void setUrlLink(String urlLink)
    {
        if (urlLink == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        setObjectValue(DataTypes.OBJ_URLLINK, urlLink);
    }


    public String getUrlLink()
    {
        return (String) getObjectValue(DataTypes.OBJ_URLLINK);
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        CharsetEncoder encoder = Charset.forName(TextEncoding.CHARSET_ISO_8859_1).newEncoder();
        String origUrl = getUrlLink();
        if (!encoder.canEncode(origUrl))
        {
            //ALL W Frames only support ISO-8859-1 for the url itself, if unable to encode let us assume
            //the link just needs url encoding
            setUrlLink(encodeURL(origUrl));

            //We still cant convert so just set log error and set to blank to allow save to continue
            if (!encoder.canEncode(getUrlLink()))
            {
                logger.warning(ErrorMessage.MP3_UNABLE_TO_ENCODE_URL.getMsg(origUrl));
                setUrlLink("");
            }
            //it was ok, just note the modification made
            else
            {
                logger.warning(ErrorMessage.MP3_URL_SAVED_ENCODED.getMsg(origUrl, getUrlLink()));
            }
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_URLLINK, this));
    }


    private String encodeURL(String url)
    {
        try
        {
            final String[] splitURL = url.split("(?<!/)/(?!/)", -1);
            final StringBuffer sb = new StringBuffer(splitURL[0]);
            for (int i = 1; i < splitURL.length; i++)
            {
                sb.append("/").append(URLEncoder.encode(splitURL[i], "utf-8"));
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException uee)
        {
            //Should never happen as utf-8 is always availablebut in case it does we just return the utl
            //unmodified
            logger.warning("Uable to url encode because utf-8 charset not available:" + uee.getMessage());
            return url;
        }
    }
}