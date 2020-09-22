package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.LanguageList;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class LanguageListReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_LANGUAGE_LIST };


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long streamPosition) throws IOException {
        assert GUID.GUID_LANGUAGE_LIST.equals(guid);
        final BigInteger chunkLen = Utils.readBig64(stream);

        final int readUINT16 = Utils.readUINT16(stream);

        final LanguageList result = new LanguageList(streamPosition, chunkLen);
        for (int i = 0; i < readUINT16; i++) {
            final int langIdLen = (stream.read() & 0xFF);
            final String langId = Utils
                    .readFixedSizeUTF16Str(stream, langIdLen);
            // langIdLen = 2 bytes for each char and optionally one zero
            // termination character
            assert langId.length() == langIdLen / 2 - 1
                    || langId.length() == langIdLen / 2;
            result.addLanguage(langId);
        }

        return result;
    }

}
