package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.io.WriteableChunk;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.*;


public class MetadataContainer extends Chunk implements WriteableChunk {


    private final static class DescriptorPointer {


        private MetadataDescriptor desc;


        public DescriptorPointer(final MetadataDescriptor descriptor) {
            setDescriptor(descriptor);
        }


        @Override
        public boolean equals(final Object obj) {
            boolean result = obj == this;
            if (obj instanceof DescriptorPointer && !result) {
                final MetadataDescriptor other = ((DescriptorPointer) obj).desc;
                result = this.desc.getName().equals(other.getName());
                result &= this.desc.getLanguageIndex() == other
                        .getLanguageIndex();
                result &= this.desc.getStreamNumber() == other
                        .getStreamNumber();
            }
            return result;
        }


        @Override
        public int hashCode() {
            int hashCode;
            hashCode = this.desc.getName().hashCode();
            hashCode = hashCode * 31 + this.desc.getLanguageIndex();
            hashCode = hashCode * 31 + this.desc.getStreamNumber();
            return hashCode;
        }


        protected DescriptorPointer setDescriptor(
                final MetadataDescriptor descriptor) {
            assert descriptor != null;
            this.desc = descriptor;
            return this;
        }
    }


    private static ContainerType determineType(final GUID guid)
            throws IllegalArgumentException {
        assert guid != null;
        ContainerType result = null;
        for (final ContainerType curr : ContainerType.values()) {
            if (curr.getContainerGUID().equals(guid)) {
                result = curr;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException(
                    "Unknown metadata container specified by GUID ("
                            + guid.toString() + ")");
        }
        return result;
    }


    private final ContainerType containerType;


    private final Map<DescriptorPointer, List<MetadataDescriptor>> descriptors = new Hashtable<DescriptorPointer, List<MetadataDescriptor>>();


    private final DescriptorPointer perfPoint = new DescriptorPointer(
            new MetadataDescriptor(""));


    public MetadataContainer(final ContainerType type) {
        this(type, 0, BigInteger.ZERO);
    }


    public MetadataContainer(final ContainerType type, final long pos,
            final BigInteger size) {
        super(type.getContainerGUID(), pos, size);
        this.containerType = type;
    }


    public MetadataContainer(final GUID containerGUID, final long pos,
            final BigInteger size) {
        this(determineType(containerGUID), pos, size);
    }


    public final void addDescriptor(final MetadataDescriptor toAdd)
            throws IllegalArgumentException {
        // check with throwing exceptions
        this.containerType.assertConstraints(toAdd.getName(), toAdd
                .getRawData(), toAdd.getType(), toAdd.getStreamNumber(), toAdd
                .getLanguageIndex());
        // validate containers capabilities
        if (!isAddSupported(toAdd)) {
            throw new IllegalArgumentException(
                    "Descriptor cannot be added, see isAddSupported(...)");
        }

        // Search for descriptor list by name, language and stream.
        List<MetadataDescriptor> list;
        synchronized (this.perfPoint) {
            list = this.descriptors.get(this.perfPoint.setDescriptor(toAdd));
        }
        if (list == null) {
            list = new ArrayList<MetadataDescriptor>();
            this.descriptors.put(new DescriptorPointer(toAdd), list);
        } else {
            if (!list.isEmpty() && !this.containerType.isMultiValued()) {
                throw new IllegalArgumentException(
                        "Container does not allow multiple values of descriptors with same name, language index and stream number");
            }
        }
        list.add(toAdd);
    }


    protected final MetadataDescriptor assertDescriptor(final String key) {
        return assertDescriptor(key, MetadataDescriptor.TYPE_STRING);
    }


    protected final MetadataDescriptor assertDescriptor(final String key,
            final int type) {
        MetadataDescriptor desc;
        final List<MetadataDescriptor> descriptorsByName = getDescriptorsByName(key);
        if (descriptorsByName == null || descriptorsByName.isEmpty()) {
            desc = new MetadataDescriptor(getContainerType(), key, type);
            addDescriptor(desc);
        } else {
            desc = descriptorsByName.get(0);
        }
        return desc;
    }


    public final boolean containsDescriptor(final MetadataDescriptor lookup) {
        assert lookup != null;
        return this.descriptors.containsKey(this.perfPoint
                .setDescriptor(lookup));
    }


    public final ContainerType getContainerType() {
        return this.containerType;
    }


    public long getCurrentAsfChunkSize() {

        long result = 26;
        for (final MetadataDescriptor curr : getDescriptors()) {
            result += curr.getCurrentAsfSize(this.containerType);
        }
        return result;
    }


    public final int getDescriptorCount() {
        return this.getDescriptors().size();
    }


    public final List<MetadataDescriptor> getDescriptors() {
        final List<MetadataDescriptor> result = new ArrayList<MetadataDescriptor>();
        for (final List<MetadataDescriptor> curr : this.descriptors.values()) {
            result.addAll(curr);
        }
        return result;
    }


    public final List<MetadataDescriptor> getDescriptorsByName(final String name) {
        assert name != null;
        final List<MetadataDescriptor> result = new ArrayList<MetadataDescriptor>();
        final Collection<List<MetadataDescriptor>> values = this.descriptors
                .values();
        for (final List<MetadataDescriptor> currList : values) {
            if (!currList.isEmpty() && currList.get(0).getName().equals(name)) {
                result.addAll(currList);
            }
        }
        return result;
    }


    protected final String getValueFor(final String name) {
        String result = "";
        final List<MetadataDescriptor> descs = getDescriptorsByName(name);
        if (descs != null) {
            assert descs.size() <= 1;
            if (!descs.isEmpty()) {
                result = descs.get(0).getString();
            }
        }
        return result;
    }


    public final boolean hasDescriptor(final String name) {
        return !getDescriptorsByName(name).isEmpty();
    }


    public boolean isAddSupported(final MetadataDescriptor descriptor) {
        boolean result = getContainerType().checkConstraints(
                descriptor.getName(), descriptor.getRawData(),
                descriptor.getType(), descriptor.getStreamNumber(),
                descriptor.getLanguageIndex()) == null;
        // Now check if there is already a value contained.
        if (result && !getContainerType().isMultiValued()) {
            synchronized (this.perfPoint) {
                final List<MetadataDescriptor> list = this.descriptors
                        .get(this.perfPoint.setDescriptor(descriptor));
                if (list != null) {
                    result = list.isEmpty();
                }
            }
        }
        return result;
    }


    public final boolean isEmpty() {
        boolean result = true;
        if (getDescriptorCount() != 0) {
            final Iterator<MetadataDescriptor> iterator = getDescriptors()
                    .iterator();
            while (result && iterator.hasNext()) {
                result &= iterator.next().isEmpty();
            }
        }
        return result;
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        for (final MetadataDescriptor curr : getDescriptors()) {
            result.append(prefix).append("  |-> ");
            result.append(curr);
            result.append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }


    public final void removeDescriptorsByName(final String name) {
        assert name != null;
        final Iterator<List<MetadataDescriptor>> iterator = this.descriptors
                .values().iterator();
        while (iterator.hasNext()) {
            final List<MetadataDescriptor> curr = iterator.next();
            if (!curr.isEmpty() && curr.get(0).getName().equals(name)) {
                iterator.remove();
            }
        }
    }


    protected final void setStringValue(final String name, final String value) {
        assertDescriptor(name).setStringValue(value);
    }


    public long writeInto(final OutputStream out) throws IOException {
        final long chunkSize = getCurrentAsfChunkSize();
        final List<MetadataDescriptor> descriptorList = getDescriptors();
        out.write(getGuid().getBytes());
        Utils.writeUINT64(chunkSize, out);
        Utils.writeUINT16(descriptorList.size(), out);
        for (final MetadataDescriptor curr : descriptorList) {
            curr.writeInto(out, this.containerType);
        }
        return chunkSize;
    }
}
