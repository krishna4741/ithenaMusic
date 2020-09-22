
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.ID3v2LyricLine;
import org.jaudiotagger.tag.datatype.Lyrics3Line;
import org.jaudiotagger.tag.datatype.Lyrics3TimeStamp;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUSLT;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class FieldFrameBodyLYR extends AbstractLyrics3v2FieldFrameBody
{

    private ArrayList<Lyrics3Line> lines = new ArrayList<Lyrics3Line>();


    public FieldFrameBodyLYR()
    {
    }

    public FieldFrameBodyLYR(FieldFrameBodyLYR copyObject)
    {
        super(copyObject);

        Lyrics3Line old;

        for (int i = 0; i < copyObject.lines.size(); i++)
        {
            old = copyObject.lines.get(i);
            this.lines.add(new Lyrics3Line(old));
        }
    }


    public FieldFrameBodyLYR(String line)
    {
        readString(line);
    }


    public FieldFrameBodyLYR(FrameBodySYLT sync)
    {
        addLyric(sync);
    }


    public FieldFrameBodyLYR(FrameBodyUSLT unsync)
    {
        addLyric(unsync);
    }


    public FieldFrameBodyLYR(ByteBuffer byteBuffer) throws InvalidTagException
    {

        this.read(byteBuffer);

    }


    public String getIdentifier()
    {
        return "LYR";
    }


    public void setLyric(String str)
    {
        readString(str);
    }


    public String getLyric()
    {
        return writeString();
    }


    public int getSize()
    {
        int size = 0;
        Lyrics3Line line;

        for (Object line1 : lines)
        {
            line = (Lyrics3Line) line1;
            size += (line.getSize() + 2);
        }

        return size;

        //return size - 2; // cut off the last crlf pair
    }


    public boolean isSubsetOf(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyLYR))
        {
            return false;
        }

        ArrayList<Lyrics3Line> superset = ((FieldFrameBodyLYR) obj).lines;

        for (Object line : lines)
        {
            if (!superset.contains(line))
            {
                return false;
            }
        }

        return super.isSubsetOf(obj);
    }


    public void addLyric(FrameBodySYLT sync)
    {
        // SYLT frames are made of individual lines
        Iterator<ID3v2LyricLine> iterator = sync.iterator();
        Lyrics3Line newLine;
        ID3v2LyricLine currentLine;
        Lyrics3TimeStamp timeStamp;
        HashMap<String, Lyrics3Line> lineMap = new HashMap<String, Lyrics3Line>();

        while (iterator.hasNext())
        {
            currentLine = iterator.next();

            // createField copy to use in new tag
            currentLine = new ID3v2LyricLine(currentLine);
            timeStamp = new Lyrics3TimeStamp("Time Stamp", this);
            timeStamp.setTimeStamp(currentLine.getTimeStamp(), (byte) sync.getTimeStampFormat());

            if (lineMap.containsKey(currentLine.getText()))
            {
                newLine = lineMap.get(currentLine.getText());
                newLine.addTimeStamp(timeStamp);
            }
            else
            {
                newLine = new Lyrics3Line("Lyric Line", this);
                newLine.setLyric(currentLine);
                newLine.setTimeStamp(timeStamp);
                lineMap.put(currentLine.getText(), newLine);
                lines.add(newLine);
            }
        }
    }


    public void addLyric(FrameBodyUSLT unsync)
    {
        // USLT frames are just long text string;
        Lyrics3Line line = new Lyrics3Line("Lyric Line", this);
        line.setLyric(unsync.getLyric());
        lines.add(line);
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyLYR))
        {
            return false;
        }

        FieldFrameBodyLYR object = (FieldFrameBodyLYR) obj;

        return this.lines.equals(object.lines) && super.equals(obj);

    }


    public boolean hasTimeStamp()
    {
        boolean present = false;

        for (Object line : lines)
        {
            if (((Lyrics3Line) line).hasTimeStamp())
            {
                present = true;
            }
        }

        return present;
    }


    public Iterator<Lyrics3Line> iterator()
    {
        return lines.iterator();
    }


    public void read(ByteBuffer byteBuffer) throws InvalidTagException
    {
        String lineString;

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
        lineString = new String(buffer);
        readString(lineString);
    }


    public String toString()
    {
        String str = getIdentifier() + " : ";

        for (Object line : lines)
        {
            str += line.toString();
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


    private void readString(String lineString)
    {
        // now readString each line and put in the vector;
        String token;
        int offset = 0;
        int delim = lineString.indexOf(Lyrics3v2Fields.CRLF);
        lines = new ArrayList<Lyrics3Line>();

        Lyrics3Line line;

        while (delim >= 0)
        {
            token = lineString.substring(offset, delim);
            line = new Lyrics3Line("Lyric Line", this);
            line.setLyric(token);
            lines.add(line);
            offset = delim + Lyrics3v2Fields.CRLF.length();
            delim = lineString.indexOf(Lyrics3v2Fields.CRLF, offset);
        }

        if (offset < lineString.length())
        {
            token = lineString.substring(offset);
            line = new Lyrics3Line("Lyric Line", this);
            line.setLyric(token);
            lines.add(line);
        }
    }


    private String writeString()
    {
        Lyrics3Line line;
        String str = "";

        for (Object line1 : lines)
        {
            line = (Lyrics3Line) line1;
            str += (line.writeString() + Lyrics3v2Fields.CRLF);
        }

        return str;

        //return str.substring(0,str.length()-2); // cut off the last CRLF pair
    }


    protected void setupObjectList()
    {

    }
}
