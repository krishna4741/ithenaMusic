package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.AsfExtendedHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;


public class AsfExtHeaderReader extends ChunkContainerReader<AsfExtendedHeader> {


    private final static GUID[] APPLYING = { GUID.GUID_HEADER_EXTENSION };


    public AsfExtHeaderReader(
            final List<Class<? extends ChunkReader>> toRegister,
            final boolean readChunkOnce) {
        super(toRegister, readChunkOnce);
    }


    public boolean canFail() {
        return false;
    }


    @Override
    protected AsfExtendedHeader createContainer(final long streamPosition,
            final BigInteger chunkLength, final InputStream stream)
            throws IOException {
        Utils.readGUID(stream); // First reserved field (should be a specific
        // GUID.
        Utils.readUINT16(stream); // Second reserved field (should always be 6)
        final long extensionSize = Utils.readUINT32(stream);
        assert extensionSize == 0 || extensionSize >= 24;
        assert chunkLength.subtract(BigInteger.valueOf(46)).longValue() == extensionSize;
        return new AsfExtendedHeader(streamPosition, chunkLength);
    }
    

    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }

}
