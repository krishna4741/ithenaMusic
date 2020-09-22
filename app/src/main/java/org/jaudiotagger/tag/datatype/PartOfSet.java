package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.options.PadNumberOption;
import org.jaudiotagger.utils.EqualsUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings({"EmptyCatchBlock"})
public class PartOfSet extends AbstractString
{

    public PartOfSet(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }


    public PartOfSet(PartOfSet object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof PartOfSet))
        {
            return false;
        }

        PartOfSet that = (PartOfSet) obj;

        return EqualsUtil.areEqual(value, that.value);
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.finest("Reading from array from offset:" + offset);

        //Get the Specified Decoder
        String charSetName = getTextEncodingCharSet();
        CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();

        //Decode sliced inBuffer
        ByteBuffer inBuffer = ByteBuffer.wrap(arr, offset, arr.length - offset).slice();
        CharBuffer outBuffer = CharBuffer.allocate(arr.length - offset);
        decoder.reset();
        CoderResult coderResult = decoder.decode(inBuffer, outBuffer, true);
        if (coderResult.isError())
        {
            logger.warning("Decoding error:" + coderResult.toString());
        }
        decoder.flush(outBuffer);
        outBuffer.flip();

        //Store value
        String stringValue = outBuffer.toString();
        value = new PartOfSetValue(stringValue);

        //SetSize, important this is correct for finding the next datatype
        setSize(arr.length - offset);
        logger.config("Read SizeTerminatedString:" + value + " size:" + size);
    }


    public byte[] writeByteArray()
    {
        String value = getValue().toString();
        byte[] data;
        //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
        try
        {
            if (TagOptionSingleton.getInstance().isRemoveTrailingTerminatorOnWrite())
            {
                if (value.length() > 0)
                {
                    if (value.charAt(value.length() - 1) == '\0')
                    {
                        value = value.substring(0, value.length() - 1);
                    }
                }
            }

            String charSetName = getTextEncodingCharSet();
            if (charSetName.equals(TextEncoding.CHARSET_UTF_16))
            {
                charSetName = TextEncoding.CHARSET_UTF_16_LE_ENCODING_FORMAT;
                CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
                encoder.onMalformedInput(CodingErrorAction.IGNORE);
                encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

                //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
                ByteBuffer bb = encoder.encode(CharBuffer.wrap('\ufeff' + value));
                data = new byte[bb.limit()];
                bb.get(data, 0, bb.limit());

            }
            else
            {
                CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
                ByteBuffer bb = encoder.encode(CharBuffer.wrap(value));
                encoder.onMalformedInput(CodingErrorAction.IGNORE);
                encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

                data = new byte[bb.limit()];
                bb.get(data, 0, bb.limit());
            }
        }
        //Should never happen so if does throw a RuntimeException
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            throw new RuntimeException(ce);
        }
        setSize(data.length);
        return data;
    }


    protected String getTextEncodingCharSet()
    {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        logger.finest("text encoding:" + textEncoding + " charset:" + charSetName);
        return charSetName;
    }


    public static class PartOfSetValue
    {
        private static final Pattern trackNoPatternWithTotalCount;
        private static final Pattern trackNoPattern;

        static
        {
            //Match track/total pattern allowing for extraneous nulls ecetera at the end
            trackNoPatternWithTotalCount = Pattern.compile("([0-9]+)/([0-9]+)(.*)", Pattern.CASE_INSENSITIVE);
            trackNoPattern = Pattern.compile("([0-9]+)(.*)", Pattern.CASE_INSENSITIVE);
        }

        private static final String SEPARATOR = "/";
        private Integer count;
        private Integer total;
        private String  extra;   //Any extraneous info such as null chars
        private String  rawText;   // raw text representation used to actually save the data IF !TagOptionSingleton.getInstance().isPadNumbers()
        private String  rawCount;  //count value as provided
        private String  rawTotal;  //total value as provided
        
        public PartOfSetValue()
        {
            rawText = "";
        }


        public PartOfSetValue(String value)
        {
            this.rawText = value;
            initFromValue(value);
        }


        public PartOfSetValue(Integer count, Integer total)
        {
            this.count      = count;
            this.rawCount   = count.toString();
            this.total      = total;
            this.rawTotal   = total.toString();
            resetValueFromCounts();
        }


        private void initFromValue(String value)
        {
            try
            {
                Matcher m = trackNoPatternWithTotalCount.matcher(value);
                if (m.matches())
                {
                    this.extra = m.group(3);
                    this.count = Integer.parseInt(m.group(1));
                    this.rawCount=m.group(1);
                    this.total = Integer.parseInt(m.group(2));
                    this.rawTotal=m.group(2);
                    return;
                }

                m = trackNoPattern.matcher(value);
                if (m.matches())
                {
                    this.extra = m.group(2);
                    this.count = Integer.parseInt(m.group(1));
                    this.rawCount = m.group(1);
                }
            }
            catch (NumberFormatException nfe)
            {
                //#JAUDIOTAGGER-366 Could occur if actually value is a long not an int
                this.count = 0;
            }
        }

        private void resetValueFromCounts()
        {
            StringBuffer sb = new StringBuffer();
            if(rawCount!=null)
            {
                sb.append(rawCount);
            }
            else
            {
                sb.append("0");
            }
            if(rawTotal!=null)
            {
                sb.append(SEPARATOR + rawTotal);
            }
            if(extra!=null)
            {
                sb.append(extra);
            }
            this.rawText = sb.toString();
       }

        public Integer getCount()
        {
            return count;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setCount(Integer count)
        {
            this.count      = count;
            this.rawCount   = count.toString();
            resetValueFromCounts();
        }

        public void setTotal(Integer total)
        {
            this.total      = total;
            this.rawTotal   = total.toString();
            resetValueFromCounts();

        }

        public void setCount(String count)
        {
            try
            {
                this.count = Integer.parseInt(count);
                this.rawCount = count;
                resetValueFromCounts();
            }
            catch (NumberFormatException nfe)
            {

            }
        }

        public void setTotal(String total)
        {
            try
            {
                this.total      = Integer.parseInt(total);
                this.rawTotal   = total;
                resetValueFromCounts();
            }
            catch (NumberFormatException nfe)
            {

            }
        }

        public String getRawValue()
        {
            return rawText;
        }

        public void setRawValue(String value)
        {
            this.rawText = value;
            initFromValue(value);
        }


        public String getCountAsText()
        {
            //Don'timer Pad
            StringBuffer sb = new StringBuffer();
            if (!TagOptionSingleton.getInstance().isPadNumbers())
            {
                return rawCount;
            }
            else
            {
                padNumber(sb, count, TagOptionSingleton.getInstance().getPadNumberTotalLength());
            }
            return sb.toString();
        }


        private void padNumber(StringBuffer sb, Integer count,PadNumberOption padNumberLength)
        {
            if (count != null)
            {
                if(padNumberLength==PadNumberOption.PAD_ONE_ZERO)
                {
                    if (count > 0 && count < 10)
                    {
                        sb.append("0").append(count);
                    }
                    else
                    {
                        sb.append(count.intValue());
                    }
                }
                else if(padNumberLength==PadNumberOption.PAD_TWO_ZERO)
                {
                    if (count > 0 && count < 10)
                    {
                        sb.append("00").append(count);
                    }
                    else if (count > 9 && count < 100)
                    {
                        sb.append("0").append(count);
                    }
                    else
                    {
                        sb.append(count.intValue());
                    }
                }
                else if(padNumberLength==PadNumberOption.PAD_THREE_ZERO)
                {
                    if (count > 0 && count < 10)
                    {
                        sb.append("000").append(count);
                    }
                    else if (count > 9 && count < 100)
                    {
                        sb.append("00").append(count);
                    }
                    else if (count > 99 && count < 1000)
                    {
                        sb.append("0").append(count);
                    }
                    else
                    {
                        sb.append(count.intValue());
                    }
                }
            }    
        }

        public String getTotalAsText()
        {
            //Don'timer Pad
            StringBuffer sb = new StringBuffer();
            if (!TagOptionSingleton.getInstance().isPadNumbers())
            {
                return rawTotal;
            }
            else
            {
                padNumber(sb, total, TagOptionSingleton.getInstance().getPadNumberTotalLength());

            }
            return sb.toString();
        }

        public String toString()
        {

            //Don'timer Pad
            StringBuffer sb = new StringBuffer();
            if (!TagOptionSingleton.getInstance().isPadNumbers())
            {
                return rawText;
            }
            else
            {
                if (count != null)
                {
                    padNumber(sb, count, TagOptionSingleton.getInstance().getPadNumberTotalLength());
                }
                else if (total != null)
                {
                    padNumber(sb, 0, TagOptionSingleton.getInstance().getPadNumberTotalLength());
                }
                if (total != null)
                {
                    sb.append(SEPARATOR);
                    padNumber(sb, total, TagOptionSingleton.getInstance().getPadNumberTotalLength());
                }
                if (extra != null)
                {
                    sb.append(extra);
                }
            }
            return sb.toString();
        }


        public boolean equals(Object obj)
        {
            if (obj == this)
            {
                return true;
            }

            if (!(obj instanceof PartOfSetValue))
            {
                return false;
            }

            PartOfSetValue that = (PartOfSetValue) obj;

            return EqualsUtil.areEqual(getCount(), that.getCount())
                    && EqualsUtil.areEqual(getTotal(), that.getTotal());
        }
    }

    public PartOfSetValue getValue()
    {
        return (PartOfSetValue) value;
    }

    public String toString()
    {
        return value.toString();
    }
}
