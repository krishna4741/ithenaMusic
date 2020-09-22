
package org.jaudiotagger.tag.asf;

import org.jaudiotagger.audio.asf.data.MetadataDescriptor;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;


public class AsfTagField implements TagField, Cloneable {


    protected MetadataDescriptor toWrap;


    public AsfTagField(final AsfFieldKey field) {
        assert field != null;
        this.toWrap = new MetadataDescriptor(field.getHighestContainer(), field
                .getFieldName(), MetadataDescriptor.TYPE_STRING);
    }


    public AsfTagField(final MetadataDescriptor source) {
        assert source != null;
        // XXX Copy ? maybe not really.
        this.toWrap = source.createCopy();
    }


    public AsfTagField(final String fieldKey) {
        assert fieldKey != null;
        this.toWrap = new MetadataDescriptor(AsfFieldKey.getAsfFieldKey(
                fieldKey).getHighestContainer(), fieldKey,
                MetadataDescriptor.TYPE_STRING);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public void copyContent(final TagField field) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    public MetadataDescriptor getDescriptor() {
        return this.toWrap;
    }


    public String getId() {
        return this.toWrap.getName();
    }


    public byte[] getRawContent() {
        return this.toWrap.getRawData();
    }


    public boolean isBinary() {
        return this.toWrap.getType() == MetadataDescriptor.TYPE_BINARY;
    }


    public void isBinary(final boolean value) {
        if (!value && isBinary()) {
            throw new UnsupportedOperationException("No conversion supported.");
        }
        this.toWrap.setBinaryValue(this.toWrap.getRawData());
    }


    public boolean isCommon() {
        // HashSet is safe against null comparison
        return AsfTag.COMMON_FIELDS.contains(AsfFieldKey
                .getAsfFieldKey(getId()));
    }


    public boolean isEmpty() {
        return this.toWrap.isEmpty();
    }


    @Override
    public String toString() {
        return this.toWrap.getString();
    }

}