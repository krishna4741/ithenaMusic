package org.jaudiotagger.tag.asf;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.MetadataDescriptor;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTagField;


public class AsfTagTextField extends AsfTagField implements TagTextField {


    public AsfTagTextField(final AsfFieldKey field, final String value) {
        super(field);
        toWrap.setString(value);
    }


    public AsfTagTextField(final MetadataDescriptor source) {
        super(source);
        if (source.getType() == MetadataDescriptor.TYPE_BINARY) {
            throw new IllegalArgumentException(
                    "Cannot interpret binary as string.");
        }
    }


    public AsfTagTextField(final String fieldKey, final String value) {
        super(fieldKey);
        toWrap.setString(value);
    }


    public String getContent() {
        return getDescriptor().getString();
    }


    public String getEncoding() {
        return AsfHeader.ASF_CHARSET.name();
    }


    @Override
    public boolean isEmpty() {
        return Utils.isBlank(getContent());
    }


    public void setContent(final String content) {
        getDescriptor().setString(content);
    }


    public void setEncoding(final String encoding) {
        if (!AsfHeader.ASF_CHARSET.name().equals(encoding)) {
            throw new IllegalArgumentException(
                    "Only UTF-16LE is possible with ASF.");
        }
    }
}
