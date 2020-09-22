package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;


public final class AsfExtendedHeader extends ChunkContainer {


    public AsfExtendedHeader(final long pos, final BigInteger length) {
        super(GUID.GUID_HEADER_EXTENSION, pos, length);
    }


    public ContentDescription getContentDescription() {
        return (ContentDescription) getFirst(GUID.GUID_CONTENTDESCRIPTION,
                ContentDescription.class);
    }


    public MetadataContainer getExtendedContentDescription() {
        return (MetadataContainer) getFirst(
                GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, MetadataContainer.class);
    }


    public LanguageList getLanguageList() {
        return (LanguageList) getFirst(GUID.GUID_LANGUAGE_LIST,
                LanguageList.class);
    }


    public MetadataContainer getMetadataLibraryObject() {
        return (MetadataContainer) getFirst(GUID.GUID_METADATA_LIBRARY,
                MetadataContainer.class);
    }


    public MetadataContainer getMetadataObject() {
        return (MetadataContainer) getFirst(GUID.GUID_METADATA,
                MetadataContainer.class);
    }

}
