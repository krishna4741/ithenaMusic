
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;

import java.io.File;


public interface AudioFileModificationListener
{


    public void fileModified(AudioFile original, File temporary) throws ModifyVetoException;


    public void fileOperationFinished(File result);


    public void fileWillBeModified(AudioFile file, boolean delete) throws ModifyVetoException;


    public void vetoThrown(AudioFileModificationListener cause, AudioFile original, ModifyVetoException veto);

}
