package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;

import java.io.IOException;
import java.io.InputStream;


public interface ChunkReader {


    boolean canFail();


    GUID[] getApplyingIds();


    Chunk read(GUID guid, InputStream stream, long streamPosition)
            throws IOException;
}
