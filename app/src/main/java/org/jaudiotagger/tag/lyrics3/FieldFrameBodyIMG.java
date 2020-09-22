

package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.Lyrics3Image;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class FieldFrameBodyIMG extends AbstractLyrics3v2FieldFrameBody
{

    private ArrayList<Lyrics3Image> images = new ArrayList<Lyrics3Image>();


    public FieldFrameBodyIMG()
    {
    }

    public FieldFrameBodyIMG(FieldFrameBodyIMG copyObject)
    {
        super(copyObject);

        Lyrics3Image old;

        for (int i = 0; i < copyObject.images.size(); i++)
        {
            old = copyObject.images.get(i);
            this.images.add(new Lyrics3Image(old));
        }
    }


    public FieldFrameBodyIMG(String imageString)
    {
        readString(imageString);
    }


    public FieldFrameBodyIMG(Lyrics3Image image)
    {
        images.add(image);
    }


    public FieldFrameBodyIMG(ByteBuffer byteBuffer) throws InvalidTagException
    {
        this.read(byteBuffer);
    }


    public String getIdentifier()
    {
        return "IMG";
    }


    public int getSize()
    {
        int size = 0;
        Lyrics3Image image;

        for (Object image1 : images)
        {
            image = (Lyrics3Image) image1;
            size += (image.getSize() + 2); // addField CRLF pair
        }

        return size - 2; // cut off trailing crlf pair
    }


    public boolean isSubsetOf(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyIMG))
        {
            return false;
        }

        ArrayList<Lyrics3Image> superset = ((FieldFrameBodyIMG) obj).images;

        for (Object image : images)
        {
            if (!superset.contains(image))
            {
                return false;
            }
        }

        return super.isSubsetOf(obj);
    }


    public void setValue(String value)
    {
        readString(value);
    }


    public String getValue()
    {
        return writeString();
    }


    public void addImage(Lyrics3Image image)
    {
        images.add(image);
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyIMG))
        {
            return false;
        }

        FieldFrameBodyIMG object = (FieldFrameBodyIMG) obj;

        return this.images.equals(object.images) && super.equals(obj);

    }


    public Iterator<Lyrics3Image> iterator()
    {
        return images.iterator();
    }


    public void read(ByteBuffer byteBuffer) throws InvalidTagException
    {
        String imageString;

        byte[] buffer = new byte[5];

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5);

        int size = Integer.parseInt(new String(buffer, 0, 5));

        if ((size == 0) && (!TagOptionSingleton.getInstance().isLyrics3KeepEmptyFieldIfRead()))
        {
            throw new InvalidTagException("Lyircs3v2 Field has size of zero.");
        }

        buffer = new byte[size];

        // read the SIZE length description
        byteBuffer.get(buffer);
        imageString = new String(buffer);
        readString(imageString);
    }


    public String toString()
    {
        String str = getIdentifier() + " : ";

        for (Object image : images)
        {
            str += (image.toString() + " ; ");
        }

        return str;
    }


    public void write(RandomAccessFile file) throws java.io.IOException
    {
        int size;
        int offset = 0;
        byte[] buffer = new byte[5];
        String str;

        size = getSize();
        str = Integer.toString(size);

        for (int i = 0; i < (5 - str.length()); i++)
        {
            buffer[i] = (byte) '0';
        }

        offset += (5 - str.length());

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        file.write(buffer, 0, 5);

        if (size > 0)
        {
            str = writeString();
            buffer = new byte[str.length()];

            for (int i = 0; i < str.length(); i++)
            {
                buffer[i] = (byte) str.charAt(i);
            }

            file.write(buffer);
        }
    }


    private void readString(String imageString)
    {
        // now read each picture and put in the vector;
        Lyrics3Image image;
        String token;
        int offset = 0;
        int delim = imageString.indexOf(Lyrics3v2Fields.CRLF);
        images = new ArrayList<Lyrics3Image>();

        while (delim >= 0)
        {
            token = imageString.substring(offset, delim);
            image = new Lyrics3Image("Image", this);
            image.setFilename(token);
            images.add(image);
            offset = delim + Lyrics3v2Fields.CRLF.length();
            delim = imageString.indexOf(Lyrics3v2Fields.CRLF, offset);
        }

        if (offset < imageString.length())
        {
            token = imageString.substring(offset);
            image = new Lyrics3Image("Image", this);
            image.setFilename(token);
            images.add(image);
        }
    }


    private String writeString()
    {
        String str = "";
        Lyrics3Image image;

        for (Object image1 : images)
        {
            image = (Lyrics3Image) image1;
            str += (image.writeString() + Lyrics3v2Fields.CRLF);
        }

        if (str.length() > 2)
        {
            return str.substring(0, str.length() - 2);
        }

        return str;
    }



    protected void setupObjectList()
    {

    }
}
