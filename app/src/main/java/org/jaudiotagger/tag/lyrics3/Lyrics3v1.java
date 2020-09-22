

package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.id3.AbstractTag;
import org.jaudiotagger.tag.id3.ID3Tags;
import org.jaudiotagger.tag.id3.ID3v1Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class Lyrics3v1 extends AbstractLyrics3
{

    private String lyric = "";


    public Lyrics3v1()
    {
    }

    public Lyrics3v1(Lyrics3v1 copyObject)
    {
        super(copyObject);
        this.lyric = copyObject.lyric;
    }

    public Lyrics3v1(AbstractTag mp3Tag)
    {
        if (mp3Tag != null)
        {
            Lyrics3v2 lyricTag;

            if (mp3Tag instanceof Lyrics3v1)
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            else if (mp3Tag instanceof Lyrics3v2)
            {
                lyricTag = (Lyrics3v2) mp3Tag;
            }
            else
            {
                lyricTag = new Lyrics3v2(mp3Tag);
            }

            FieldFrameBodyLYR lyricField;
            lyricField = (FieldFrameBodyLYR) lyricTag.getField("LYR").getBody();
            this.lyric = lyricField.getLyric();
        }
    }


    public Lyrics3v1(ByteBuffer byteBuffer) throws TagNotFoundException, java.io.IOException
    {
        try
        {
            this.read(byteBuffer);
        }
        catch (TagException e)
        {
            e.printStackTrace();
        }
    }


    public String getIdentifier()
    {
        return "Lyrics3v1.00";
    }


    public void setLyric(String lyric)
    {
        this.lyric = ID3Tags.truncate(lyric, 5100);
    }


    public String getLyric()
    {
        return lyric;
    }


    public int getSize()
    {
        return "LYRICSBEGIN".length() + lyric.length() + "LYRICSEND".length();
    }


    public boolean isSubsetOf(Object obj)
    {
        return (obj instanceof Lyrics3v1) && (((Lyrics3v1) obj).lyric.contains(this.lyric));

    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof Lyrics3v1))
        {
            return false;
        }

        Lyrics3v1 object = (Lyrics3v1) obj;

        return this.lyric.equals(object.lyric) && super.equals(obj);

    }


    public Iterator iterator()
    {

        throw new java.lang.UnsupportedOperationException("Method iterator() not yet implemented.");
    }


    public boolean seek(ByteBuffer byteBuffer)
    {
        return false;
    }


    public void read(ByteBuffer byteBuffer) throws TagException
    {
        byte[] buffer = new byte[5100 + 9 + 11];
        String lyricBuffer;

        if (!seek(byteBuffer))
        {
            throw new TagNotFoundException("ID3v1 tag not found");
        }

        byteBuffer.get(buffer);
        lyricBuffer = new String(buffer);

        lyric = lyricBuffer.substring(0, lyricBuffer.indexOf("LYRICSEND"));
    }


    public boolean seek(RandomAccessFile file) throws IOException
    {
        byte[] buffer = new byte[5100 + 9 + 11];
        String lyricsEnd;
        String lyricsStart;
        long offset;

        // check right before the ID3 1.0 tag for the lyrics tag
        file.seek(file.length() - 128 - 9);
        file.read(buffer, 0, 9);
        lyricsEnd = new String(buffer, 0, 9);

        if (lyricsEnd.equals("LYRICSEND"))
        {
            offset = file.getFilePointer();
        }
        else
        {
            // check the end of the file for a lyrics tag incase an ID3
            // tag wasn'timer placed after it.
            file.seek(file.length() - 9);
            file.read(buffer, 0, 9);
            lyricsEnd = new String(buffer, 0, 9);

            if (lyricsEnd.equals("LYRICSEND"))
            {
                offset = file.getFilePointer();
            }
            else
            {
                return false;
            }
        }

        // the tag can at most only be 5100 bytes
        offset -= (5100 + 9 + 11);
        file.seek(offset);
        file.read(buffer);
        lyricsStart = new String(buffer);

        // search for the tag
        int i = lyricsStart.indexOf("LYRICSBEGIN");

        if (i == -1)
        {
            return false;
        }

        file.seek(offset + i + 11);

        return true;
    }


    public String toString()
    {
        String str = getIdentifier() + " " + this.getSize() + "\n";

        return str + lyric;
    }


    public void write(RandomAccessFile file) throws IOException
    {
        String str;
        int offset;
        byte[] buffer;
        ID3v1Tag id3v1tag;

        id3v1tag = null;

        delete(file);
        file.seek(file.length());

        buffer = new byte[lyric.length() + 11 + 9];

        str = "LYRICSBEGIN";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i] = (byte) str.charAt(i);
        }

        offset = str.length();

        str = ID3Tags.truncate(lyric, 5100);

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        str = "LYRICSEND";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        file.write(buffer, 0, offset);

        if (id3v1tag != null)
        {
            id3v1tag.write(file);
        }
    }

}