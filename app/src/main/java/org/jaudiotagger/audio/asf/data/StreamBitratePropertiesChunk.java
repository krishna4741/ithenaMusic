
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class StreamBitratePropertiesChunk extends Chunk {


    private final List<Long> bitRates;


    private final List<Integer> streamNumbers;


    public StreamBitratePropertiesChunk(final BigInteger chunkLen) {
        super(GUID.GUID_STREAM_BITRATE_PROPERTIES, chunkLen);
        this.bitRates = new ArrayList<Long>();
        this.streamNumbers = new ArrayList<Integer>();
    }


    public void addBitrateRecord(final int streamNum, final long averageBitrate) {
        this.streamNumbers.add(streamNum);
        this.bitRates.add(averageBitrate);
    }


    public long getAvgBitrate(final int streamNumber) {
        final Integer seach = streamNumber;
        final int index = this.streamNumbers.indexOf(seach);
        long result;
        if (index == -1) {
            result = -1;
        } else {
            result = this.bitRates.get(index);
        }
        return result;
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        for (int i = 0; i < this.bitRates.size(); i++) {
            result.append(prefix).append("  |-> Stream no. \"").append(
                    this.streamNumbers.get(i)).append(
                    "\" has an average bitrate of \"").append(
                    this.bitRates.get(i)).append('"').append(
                    Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }

}
