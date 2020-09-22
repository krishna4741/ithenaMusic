
package org.jaudiotagger.audio;

import org.jaudiotagger.audio.generic.Utils;

import java.io.FileFilter;
import java.io.File;


public class AudioFileFilter implements FileFilter
{

    private final boolean allowDirectories;

    public AudioFileFilter( boolean allowDirectories)
    {
        this.allowDirectories=allowDirectories;
    }

    public AudioFileFilter()
    {
        this(true);
    }


    public boolean accept(File f)
    {
        if (f.isHidden() || !f.canRead())
        {
            return false;
        }

        if (f.isDirectory())
        {
            return allowDirectories;
        }

        String ext = Utils.getExtension(f);

        try
        {
            if (SupportedFileFormat.valueOf(ext.toUpperCase()) != null)
            {
                return true;
            }
        }
        catch(IllegalArgumentException iae)
        {
            //Not known enum value
            return false;    
        }
        return false;
	}
}
