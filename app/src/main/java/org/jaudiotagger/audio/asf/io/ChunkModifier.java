package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.GUID;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface ChunkModifier {


    boolean isApplicable(GUID guid);


    ModificationResult modify(GUID guid, InputStream source,
            OutputStream destination) throws IOException;

}
