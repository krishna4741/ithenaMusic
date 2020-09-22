
package org.jaudiotagger.tag.lyrics3;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Lyrics3v1Iterator implements Iterator<String>
{

    private Lyrics3v1 tag = null;


    private int lastIndex = 0;


    private int removeIndex = 0;


    public Lyrics3v1Iterator(Lyrics3v1 lyrics3v1Tag)
    {
        tag = lyrics3v1Tag;
    }


    public boolean hasNext()
    {
        return !((tag.getLyric().indexOf('\n', lastIndex) < 0) && (lastIndex > tag.getLyric().length()));
    }


    public String next()
    {
        int nextIndex = tag.getLyric().indexOf('\n', lastIndex);

        removeIndex = lastIndex;

        String line;

        if (lastIndex >= 0)
        {
            if (nextIndex >= 0)
            {
                line = tag.getLyric().substring(lastIndex, nextIndex);
            }
            else
            {
                line = tag.getLyric().substring(lastIndex);
            }

            lastIndex = nextIndex;
        }
        else
        {
            throw new NoSuchElementException("Iteration has no more elements.");
        }

        return line;
    }


    public void remove()
    {
        String lyric = tag.getLyric().substring(0, removeIndex) + tag.getLyric().substring(lastIndex);
        tag.setLyric(lyric);
    }
}