
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Iterator;


public class ModificationHandler implements AudioFileModificationListener
{


    private Vector<AudioFileModificationListener> listeners = new Vector<AudioFileModificationListener>();


    public void addAudioFileModificationListener(AudioFileModificationListener l)
    {
        if (!this.listeners.contains(l))
        {
            this.listeners.add(l);
        }
    }


    public void fileModified(AudioFile original, File temporary) throws ModifyVetoException
    {
        for (AudioFileModificationListener listener : this.listeners)
        {
            AudioFileModificationListener current = listener;
            try
            {
                current.fileModified(original, temporary);
            }
            catch (ModifyVetoException e)
            {
                vetoThrown(current, original, e);
                throw e;
            }
        }
    }


    public void fileOperationFinished(File result)
    {
        for (AudioFileModificationListener listener : this.listeners)
        {
            AudioFileModificationListener current = listener;
            current.fileOperationFinished(result);
        }
    }


    public void fileWillBeModified(AudioFile file, boolean delete) throws ModifyVetoException
    {
        for (AudioFileModificationListener listener : this.listeners)
        {
            AudioFileModificationListener current = listener;
            try
            {
                current.fileWillBeModified(file, delete);
            }
            catch (ModifyVetoException e)
            {
                vetoThrown(current, file, e);
                throw e;
            }
        }
    }


    public void removeAudioFileModificationListener(AudioFileModificationListener l)
    {
        if (this.listeners.contains(l))
        {
            this.listeners.remove(l);
        }
    }


    public void vetoThrown(AudioFileModificationListener cause, AudioFile original, ModifyVetoException veto)
    {
        for (AudioFileModificationListener listener : this.listeners)
        {
            AudioFileModificationListener current = listener;
            current.vetoThrown(cause, original, veto);
        }
    }
}
