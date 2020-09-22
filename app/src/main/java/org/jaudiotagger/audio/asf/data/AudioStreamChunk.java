
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;


public final class AudioStreamChunk extends StreamChunk {

    public final static String[][] CODEC_DESCRIPTIONS = {
            { "161", " (Windows Media Audio (ver 7,8,9))" },
            { "162", " (Windows Media Audio 9 series (Professional))" },
            { "163", "(Windows Media Audio 9 series (Lossless))" },
            { "7A21", " (GSM-AMR (CBR))" }, { "7A22", " (GSM-AMR (VBR))" } };

    public final static long WMA = 0x161;

    public final static long WMA_CBR = 0x7A21;

    public final static long WMA_LOSSLESS = 0x163;

    public final static long WMA_PRO = 0x162;


    public final static long WMA_VBR = 0x7A22;


    private long averageBytesPerSec;


    private int bitsPerSample;


    private long blockAlignment;


    private long channelCount;


    private byte[] codecData = new byte[0];


    private long compressionFormat;


    private GUID errorConcealment;


    private long samplingRate;


    public AudioStreamChunk(final BigInteger chunkLen) {
        super(GUID.GUID_AUDIOSTREAM, chunkLen);
    }


    public long getAverageBytesPerSec() {
        return this.averageBytesPerSec;
    }


    public int getBitsPerSample() {
        return this.bitsPerSample;
    }


    public long getBlockAlignment() {
        return this.blockAlignment;
    }


    public long getChannelCount() {
        return this.channelCount;
    }


    public byte[] getCodecData() {
        return this.codecData.clone();
    }


    public String getCodecDescription() {
        final StringBuilder result = new StringBuilder(Long
                .toHexString(getCompressionFormat()));
        String furtherDesc = " (Unknown)";
        for (final String[] aCODEC_DESCRIPTIONS : CODEC_DESCRIPTIONS) {
            if (aCODEC_DESCRIPTIONS[0].equalsIgnoreCase(result.toString())) {
                furtherDesc = aCODEC_DESCRIPTIONS[1];
                break;
            }
        }
        if (result.length() % 2 == 0) {
            result.insert(0, "0x");
        } else {
            result.insert(0, "0x0");
        }
        result.append(furtherDesc);
        return result.toString();
    }


    public long getCompressionFormat() {
        return this.compressionFormat;
    }


    public GUID getErrorConcealment() {
        return this.errorConcealment;
    }


    public int getKbps() {
        return (int) getAverageBytesPerSec() * 8 / 1000;
    }


    public long getSamplingRate() {
        return this.samplingRate;
    }


    public boolean isErrorConcealed() {
        return getErrorConcealment().equals(
                GUID.GUID_AUDIO_ERROR_CONCEALEMENT_INTERLEAVED);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(prefix).append("  |-> Audio info:").append(
                Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Bitrate : ").append(getKbps())
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Channels : ").append(
                getChannelCount()).append(" at ").append(getSamplingRate())
                .append(" Hz").append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Bits per Sample: ").append(
                getBitsPerSample()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Formatcode: ").append(
                getCodecDescription()).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }


    public void setAverageBytesPerSec(final long avgeBytesPerSec) {
        this.averageBytesPerSec = avgeBytesPerSec;
    }


    public void setBitsPerSample(final int bps) {
        this.bitsPerSample = bps;
    }


    public void setBlockAlignment(final long align) {
        this.blockAlignment = align;
    }


    public void setChannelCount(final long channels) {
        this.channelCount = channels;
    }


    public void setCodecData(final byte[] codecSpecificData) {
        if (codecSpecificData == null) {
            throw new IllegalArgumentException();
        }
        this.codecData = codecSpecificData.clone();
    }


    public void setCompressionFormat(final long cFormatCode) {
        this.compressionFormat = cFormatCode;
    }


    public void setErrorConcealment(final GUID errConc) {
        this.errorConcealment = errConc;
    }


    public void setSamplingRate(final long sampRate) {
        this.samplingRate = sampRate;
    }
}
