
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;


public abstract class StreamChunk extends Chunk {


    private boolean contentEncrypted;


    private int streamNumber;


    private long streamSpecificDataSize;


    private long timeOffset;


    private final GUID type;


    private long typeSpecificDataSize;


    public StreamChunk(final GUID streamType, final BigInteger chunkLen) {
        super(GUID.GUID_STREAM, chunkLen);
        assert GUID.GUID_AUDIOSTREAM.equals(streamType)
                || GUID.GUID_VIDEOSTREAM.equals(streamType);
        this.type = streamType;
    }


    public int getStreamNumber() {
        return this.streamNumber;
    }


    public long getStreamSpecificDataSize() {
        return this.streamSpecificDataSize;
    }


    public GUID getStreamType() {
        return this.type;
    }


    public long getTimeOffset() {
        return this.timeOffset;
    }


    public long getTypeSpecificDataSize() {
        return this.typeSpecificDataSize;
    }


    public boolean isContentEncrypted() {
        return this.contentEncrypted;
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(prefix).append("  |-> Stream number: ").append(
                getStreamNumber()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Type specific data size  : ")
                .append(getTypeSpecificDataSize()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Stream specific data size: ")
                .append(getStreamSpecificDataSize()).append(
                        Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Time Offset              : ")
                .append(getTimeOffset()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Content Encryption       : ")
                .append(isContentEncrypted()).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }


    public void setContentEncrypted(final boolean cntEnc) {
        this.contentEncrypted = cntEnc;
    }


    public void setStreamNumber(final int streamNum) {
        this.streamNumber = streamNum;
    }


    public void setStreamSpecificDataSize(final long strSpecDataSize) {
        this.streamSpecificDataSize = strSpecDataSize;
    }


    public void setTimeOffset(final long timeOffs) {
        this.timeOffset = timeOffs;
    }


    public void setTypeSpecificDataSize(final long typeSpecDataSize) {
        this.typeSpecificDataSize = typeSpecDataSize;
    }
}