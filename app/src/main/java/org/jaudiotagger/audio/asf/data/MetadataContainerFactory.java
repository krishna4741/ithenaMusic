package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;


public final class MetadataContainerFactory {


    private final static MetadataContainerFactory INSTANCE = new MetadataContainerFactory();


    public static MetadataContainerFactory getInstance() {
        return INSTANCE;
    }


    private MetadataContainerFactory() {
        // Hidden
    }


    public MetadataContainer createContainer(final ContainerType type) {
        return createContainer(type, 0, BigInteger.ZERO);
    }


    public MetadataContainer createContainer(final ContainerType type,
            final long pos, final BigInteger chunkSize) {
        MetadataContainer result;
        if (type == ContainerType.CONTENT_DESCRIPTION) {
            result = new ContentDescription(pos, chunkSize);
        } else if (type == ContainerType.CONTENT_BRANDING) {
            result = new ContentBranding(pos, chunkSize);
        } else {
            result = new MetadataContainer(type, pos, chunkSize);
        }
        return result;
    }


    public MetadataContainer[] createContainers(final ContainerType[] types) {
        assert types != null;
        final MetadataContainer[] result = new MetadataContainer[types.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = createContainer(types[i]);
        }
        return result;
    }

}
