
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class EncodingChunk extends Chunk {


    private final List<String> strings;


    public EncodingChunk(final BigInteger chunkLen) {
        super(GUID.GUID_ENCODING, chunkLen);
        this.strings = new ArrayList<String>();
    }


    public void addString(final String toAdd) {
        this.strings.add(toAdd);
    }


    public Collection<String> getStrings() {
        return new ArrayList<String>(this.strings);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super
                .prettyPrint(prefix));
        this.strings.iterator();
        for (final String string : this.strings) {
            result.append(prefix).append("  | : ").append(string).append(
                    Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }
}