
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;

import java.io.File;


public class AudioFileModificationAdapter implements AudioFileModificationListener
{


    public void fileModified(AudioFile original, File temporary) throws ModifyVetoException
    {
        // Nothing to do
    }


    public void fileOperationFinished(File result)
    {
        // Nothing to do
    }


    public void fileWillBeModified(AudioFile file, boolean delete) throws ModifyVetoException
    {
        // Nothing to do
    }


    public void vetoThrown(AudioFileModificationListener cause, AudioFile original, ModifyVetoException veto)
    {
        // Nothing to do
    }

}
