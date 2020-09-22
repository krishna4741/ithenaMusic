
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;


public class VideoStreamChunk extends StreamChunk {


    private byte[] codecId = new byte[0];


    private long pictureHeight;


    private long pictureWidth;


    public VideoStreamChunk(final BigInteger chunkLen) {
        super(GUID.GUID_VIDEOSTREAM, chunkLen);
    }


    public byte[] getCodecId() {
        return this.codecId.clone();
    }


    public String getCodecIdAsString() {
        String result;
        if (this.codecId == null) {
            result = "Unknown"; 
        } else {
            result = new String(getCodecId());
        }
        return result;
    }


    public long getPictureHeight() {
        return this.pictureHeight;
    }


    public long getPictureWidth() {
        return this.pictureWidth;
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.insert(0, Utils.LINE_SEPARATOR + prefix + "|->VideoStream");
        result.append(prefix).append("Video info:")
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("      |->Width  : ").append(
                getPictureWidth()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("      |->Heigth : ").append(
                getPictureHeight()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("      |->Codec  : ").append(
                getCodecIdAsString()).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }


    public void setCodecId(final byte[] codecIdentifier) {
        this.codecId = codecIdentifier.clone();
    }


    public void setPictureHeight(final long picHeight) {
        this.pictureHeight = picHeight;
    }


    public void setPictureWidth(final long picWidth) {
        this.pictureWidth = picWidth;
    }
}