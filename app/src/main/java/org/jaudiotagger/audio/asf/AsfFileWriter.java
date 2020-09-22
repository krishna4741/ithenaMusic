
package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ChunkContainer;
import org.jaudiotagger.audio.asf.data.MetadataContainer;
import org.jaudiotagger.audio.asf.io.*;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


public class AsfFileWriter extends AudioFileWriter {


    @Override
    protected void deleteTag(final RandomAccessFile raf,
            final RandomAccessFile tempRaf) throws CannotWriteException,
            IOException {
        writeTag(new AsfTag(true), raf, tempRaf);
    }

    private boolean[] searchExistence(final ChunkContainer container,
            final MetadataContainer[] metaContainers) {
        assert container != null;
        assert metaContainers != null;
        final boolean[] result = new boolean[metaContainers.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = container.hasChunkByGUID(metaContainers[i]
                    .getContainerType().getContainerGUID());
        }
        return result;
    }


    @Override
    protected void writeTag(final Tag tag, final RandomAccessFile raf,
            final RandomAccessFile rafTemp) throws CannotWriteException,
            IOException {

        final AsfHeader sourceHeader = AsfHeaderReader.readTagHeader(raf);
        raf.seek(0); // Reset for the streamer

        // TODO not convinced that we need to copy fields here
        final AsfTag copy = new AsfTag(tag, true);
        final MetadataContainer[] distribution = TagConverter
                .distributeMetadata(copy);
        final boolean[] existHeader = searchExistence(sourceHeader,
                distribution);
        final boolean[] existExtHeader = searchExistence(sourceHeader
                .getExtendedHeader(), distribution);
        // Modifiers for the asf header object
        final List<ChunkModifier> headerModifier = new ArrayList<ChunkModifier>();
        // Modifiers for the asf header extension object
        final List<ChunkModifier> extHeaderModifier = new ArrayList<ChunkModifier>();
        for (int i = 0; i < distribution.length; i++) {
            final WriteableChunkModifer modifier = new WriteableChunkModifer(
                    distribution[i]);
            if (existHeader[i]) {
                // Will remove or modify chunks in ASF header
                headerModifier.add(modifier);
            } else if (existExtHeader[i]) {
                // Will remove or modify chunks in extended header
                extHeaderModifier.add(modifier);
            } else {
                // Objects (chunks) will be added here.
                if (i == 0 || i == 2 || i == 1) {
                    // Add content description and extended content description
                    // at header for maximum compatibility
                    headerModifier.add(modifier);
                } else {
                    // For now, the rest should be created at extended header
                    // since other positions aren'timer known.
                    extHeaderModifier.add(modifier);
                }
            }
        }
        // only addField an AsfExtHeaderModifier, if there is actually something to
        // change (performance)
        if (!extHeaderModifier.isEmpty()) {
            headerModifier.add(new AsfExtHeaderModifier(extHeaderModifier));
        }
        new AsfStreamer()
                .createModifiedCopy(new RandomAccessFileInputstream(raf),
                        new RandomAccessFileOutputStream(rafTemp),
                        headerModifier);
    }

}