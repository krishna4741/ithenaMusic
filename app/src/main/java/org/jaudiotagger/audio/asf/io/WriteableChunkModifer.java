package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class WriteableChunkModifer implements ChunkModifier {


    private final WriteableChunk writableChunk;


    public WriteableChunkModifer(final WriteableChunk chunk) {
        this.writableChunk = chunk;
    }


    public boolean isApplicable(final GUID guid) {
        return guid.equals(this.writableChunk.getGuid());
    }


    public ModificationResult modify(final GUID guid, final InputStream chunk,
            OutputStream destination) throws IOException { // NOPMD by Christian Laireiter on 5/9/09 5:03 PM
        int chunkDiff = 0;
        long newSize = 0;
        long oldSize = 0;

        assert (destination = new CountingOutputstream(destination)) != null;
        if (!this.writableChunk.isEmpty()) {
            newSize = this.writableChunk.writeInto(destination);
            assert newSize == this.writableChunk.getCurrentAsfChunkSize();

            assert ((CountingOutputstream) destination).getCount() == newSize;
            if (guid == null) {
                chunkDiff++;
            }

        }
        if (guid != null) {
            assert isApplicable(guid);
            if (this.writableChunk.isEmpty()) {
                chunkDiff--;
            }
            oldSize = Utils.readUINT64(chunk);
            chunk.skip(oldSize - 24);
        }
        return new ModificationResult(chunkDiff, (newSize - oldSize), guid);
    }

}
