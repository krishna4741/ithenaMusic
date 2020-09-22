package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ContentBranding;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class ContentBrandingReader implements ChunkReader {

    private final static GUID[] APPLYING = { GUID.GUID_CONTENT_BRANDING };


    protected ContentBrandingReader() {
        // NOTHING toDo
    }


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long streamPosition) throws IOException {
        assert GUID.GUID_CONTENT_BRANDING.equals(guid);
        final BigInteger chunkSize = Utils.readBig64(stream);
        final long imageType = Utils.readUINT32(stream);
        assert imageType >= 0 && imageType <= 3 : imageType;
        final long imageDataSize = Utils.readUINT32(stream);
        assert imageType > 0 || imageDataSize == 0 : imageDataSize;
        assert imageDataSize < Integer.MAX_VALUE;
        final byte[] imageData = Utils.readBinary(stream, imageDataSize);
        final long copyRightUrlLen = Utils.readUINT32(stream);
        final String copyRight = new String(Utils.readBinary(stream,
                copyRightUrlLen));
        final long imageUrlLen = Utils.readUINT32(stream);
        final String imageUrl = new String(Utils
                .readBinary(stream, imageUrlLen));
        final ContentBranding result = new ContentBranding(streamPosition,
                chunkSize);
        result.setImage(imageType, imageData);
        result.setCopyRightURL(copyRight);
        result.setBannerImageURL(imageUrl);
        return result;
    }

}
