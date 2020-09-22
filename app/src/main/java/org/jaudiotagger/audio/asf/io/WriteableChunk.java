package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.GUID;

import java.io.IOException;
import java.io.OutputStream;


public interface WriteableChunk {


    long getCurrentAsfChunkSize();


    GUID getGuid();


    boolean isEmpty();


    long writeInto(OutputStream out) throws IOException;
}
