package org.jaudiotagger.tag.asf;

import org.jaudiotagger.audio.asf.data.ContentBranding;

import org.jaudiotagger.audio.asf.data.ContainerType;
import org.jaudiotagger.audio.asf.data.MetadataDescriptor;
import org.jaudiotagger.tag.asf.AbstractAsfTagImageField;
import org.jaudiotagger.tag.asf.AsfFieldKey;


public class AsfTagBannerField extends AbstractAsfTagImageField
{


    public AsfTagBannerField() {
        super(AsfFieldKey.BANNER_IMAGE);
    }


    public AsfTagBannerField(final MetadataDescriptor descriptor) {
        super(descriptor);
        assert descriptor.getName().equals(
                AsfFieldKey.BANNER_IMAGE.getFieldName());
    }


    public AsfTagBannerField(final byte[] imageData) {
        super(new MetadataDescriptor(ContainerType.CONTENT_BRANDING,
                AsfFieldKey.BANNER_IMAGE.getFieldName(),
                MetadataDescriptor.TYPE_BINARY));
        this.toWrap.setBinaryValue(imageData);
    }


    @Override
    public int getImageDataSize() {
        return this.toWrap.getRawDataSize();
    }


    @Override
    public byte[] getRawImageData() {
        return getRawContent();
    }

}
