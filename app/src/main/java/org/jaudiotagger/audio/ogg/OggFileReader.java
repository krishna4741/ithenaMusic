
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.ogg.util.OggInfoReader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;


public class OggFileReader extends AudioFileReader
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    private OggInfoReader ir;
    private OggVorbisTagReader vtr;

    public OggFileReader()
    {
        ir = new OggInfoReader();
        vtr = new OggVorbisTagReader();
    }

    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        return ir.read(raf);
    }

    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException
    {
        return vtr.read(raf);
    }


    public OggPageHeader readOggPageHeader(RandomAccessFile raf, int count) throws CannotReadException, IOException
    {
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        while (count > 0)
        {
            raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
            pageHeader = OggPageHeader.read(raf);
            count--;
        }
        return pageHeader;
    }


    public void summarizeOggPageHeaders(File oggFile) throws CannotReadException, IOException
    {
        RandomAccessFile raf = new RandomAccessFile(oggFile, "r");

        while (raf.getFilePointer() < raf.length())
        {
            System.out.println("pageHeader starts at absolute file position:" + raf.getFilePointer());
            OggPageHeader pageHeader = OggPageHeader.read(raf);
            System.out.println("pageHeader finishes at absolute file position:" + raf.getFilePointer());
            System.out.println(pageHeader + "\n");
            raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
        }
        System.out.println("Raf File Pointer at:" + raf.getFilePointer() + "File Size is:" + raf.length());
        raf.close();
    }


    public void shortSummarizeOggPageHeaders(File oggFile) throws CannotReadException, IOException
    {
        RandomAccessFile raf = new RandomAccessFile(oggFile, "r");

        int i = 0;
        while (raf.getFilePointer() < raf.length())
        {
            System.out.println("pageHeader starts at absolute file position:" + raf.getFilePointer());
            OggPageHeader pageHeader = OggPageHeader.read(raf);
            System.out.println("pageHeader finishes at absolute file position:" + raf.getFilePointer());
            System.out.println(pageHeader + "\n");
            raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
            i++;
            if(i>=5)
            {
                break;
            }
        }
        System.out.println("Raf File Pointer at:" + raf.getFilePointer() + "File Size is:" + raf.length());
        raf.close();
    }
}

