package org.jaudiotagger.tag.asf;

import org.jaudiotagger.audio.asf.data.MetadataDescriptor;
import org.jaudiotagger.tag.TagField;

import java.io.ByteArrayInputStream;
import java.io.IOException;


abstract class AbstractAsfTagImageField extends AsfTagField
{


    public AbstractAsfTagImageField(final AsfFieldKey field) {
        super(field);
    }


    public AbstractAsfTagImageField(final MetadataDescriptor source) {
        super(source);
    }


    public AbstractAsfTagImageField(final String fieldKey) {
        super(fieldKey);
    }


    public abstract int getImageDataSize();


    public abstract byte[] getRawImageData();

}
