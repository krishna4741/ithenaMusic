
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class ContentDescriptionReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_CONTENTDESCRIPTION };


    protected ContentDescriptionReader() {
        // NOTHING toDo
    }


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    private int[] getStringSizes(final InputStream stream) throws IOException {
        final int[] result = new int[5];
        for (int i = 0; i < result.length; i++) {
            result[i] = Utils.readUINT16(stream);
        }
        return result;
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long chunkStart) throws IOException {
        final BigInteger chunkSize = Utils.readBig64(stream);

        final int[] stringSizes = getStringSizes(stream);


        final String[] strings = new String[stringSizes.length];
        for (int i = 0; i < strings.length; i++) {
            if (stringSizes[i] > 0) {
                strings[i] = Utils
                        .readFixedSizeUTF16Str(stream, stringSizes[i]);
            }
        }

        final ContentDescription result = new ContentDescription(chunkStart,
                chunkSize);
        if (stringSizes[0] > 0) {
            result.setTitle(strings[0]);
        }
        if (stringSizes[1] > 0) {
            result.setAuthor(strings[1]);
        }
        if (stringSizes[2] > 0) {
            result.setCopyright(strings[2]);
        }
        if (stringSizes[3] > 0) {
            result.setComment(strings[3]);
        }
        if (stringSizes[4] > 0) {
            result.setRating(strings[4]);
        }
        return result;
    }
}
