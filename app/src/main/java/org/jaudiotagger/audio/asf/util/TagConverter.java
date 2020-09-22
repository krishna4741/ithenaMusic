
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.data.*;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.asf.*;
import org.jaudiotagger.tag.reference.GenreTypes;

import java.util.Iterator;
import java.util.List;


public final class TagConverter {


    public static void assignCommonTagValues(Tag tag,
            MetadataContainer description) {
        assert description.getContainerType() == ContainerType.EXTENDED_CONTENT;
        MetadataDescriptor tmp;
        if (!Utils.isBlank(tag.getFirst(FieldKey.ALBUM))) {
            tmp = new MetadataDescriptor(description.getContainerType(),
                    AsfFieldKey.ALBUM.getFieldName(),
                    MetadataDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirst(FieldKey.ALBUM));
            description.removeDescriptorsByName(tmp.getName());
            description.addDescriptor(tmp);
        } else {
            description.removeDescriptorsByName(AsfFieldKey.ALBUM
                    .getFieldName());
        }
        if (!Utils.isBlank(tag.getFirst(FieldKey.TRACK))) {
            tmp = new MetadataDescriptor(description.getContainerType(),
                    AsfFieldKey.TRACK.getFieldName(),
                    MetadataDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirst(FieldKey.TRACK));
            description.removeDescriptorsByName(tmp.getName());
            description.addDescriptor(tmp);
        } else {
            description.removeDescriptorsByName(AsfFieldKey.TRACK
                    .getFieldName());
        }
        if (!Utils.isBlank(tag.getFirst(FieldKey.YEAR))) {
            tmp = new MetadataDescriptor(description.getContainerType(),
                    AsfFieldKey.YEAR.getFieldName(),
                    MetadataDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirst(FieldKey.YEAR));
            description.removeDescriptorsByName(tmp.getName());
            description.addDescriptor(tmp);
        } else {
            description
                    .removeDescriptorsByName(AsfFieldKey.YEAR.getFieldName());
        }
        if (!Utils.isBlank(tag.getFirst(FieldKey.GENRE))) {
            // Write Genre String value
            tmp = new MetadataDescriptor(description.getContainerType(),
                    AsfFieldKey.GENRE.getFieldName(),
                    MetadataDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirst(FieldKey.GENRE));
            description.removeDescriptorsByName(tmp.getName());
            description.addDescriptor(tmp);
            Integer genreNum = GenreTypes.getInstanceOf().getIdForName(
                    tag.getFirst(FieldKey.GENRE));
            // ..and if it is one of the standard genre types used the id as
            // well
            if (genreNum != null) {
                tmp = new MetadataDescriptor(description.getContainerType(),
                        AsfFieldKey.GENRE_ID.getFieldName(),
                        MetadataDescriptor.TYPE_STRING);
                tmp.setStringValue("(" + genreNum + ")");
                description.removeDescriptorsByName(tmp.getName());
                description.addDescriptor(tmp);
            } else {
                description.removeDescriptorsByName(AsfFieldKey.GENRE_ID
                        .getFieldName());
            }
        } else {
            description.removeDescriptorsByName(AsfFieldKey.GENRE
                    .getFieldName());
            description.removeDescriptorsByName(AsfFieldKey.GENRE_ID
                    .getFieldName());
        }
    }


    public static AsfTag createTagOf(AsfHeader source) {
        // TODO do we need to copy here.
        AsfTag result = new AsfTag(true);
        for (int i = 0; i < ContainerType.values().length; i++) {
            MetadataContainer current = source
                    .findMetadataContainer(ContainerType.values()[i]);
            if (current != null) {
                List<MetadataDescriptor> descriptors = current.getDescriptors();
                for (MetadataDescriptor descriptor : descriptors) {
                    AsfTagField toAdd;
                    if (descriptor.getType() == MetadataDescriptor.TYPE_BINARY) {
                        if (descriptor.getName().equals(
                                AsfFieldKey.COVER_ART.getFieldName())) {
                            toAdd = new AsfTagCoverField(descriptor);
                        } else if (descriptor.getName().equals(
                                AsfFieldKey.BANNER_IMAGE.getFieldName())) {
                            toAdd = new AsfTagBannerField(descriptor);
                        } else {
                            toAdd = new AsfTagField(descriptor);
                        }
                    } else {
                        toAdd = new AsfTagTextField(descriptor);
                    }
                    result.addField(toAdd);
                }
            }
        }
        return result;
    }


    public static MetadataContainer[] distributeMetadata(final AsfTag tag) {
        final Iterator<AsfTagField> asfFields = tag.getAsfFields();
        final MetadataContainer[] createContainers = MetadataContainerFactory
                .getInstance().createContainers(ContainerType.getOrdered());
        boolean assigned;
        AsfTagField current;
        while (asfFields.hasNext()) {
            current = asfFields.next();
            assigned = false;
            for (int i = 0; !assigned && i < createContainers.length; i++) {
                if (ContainerType.areInCorrectOrder(createContainers[i]
                        .getContainerType(), AsfFieldKey.getAsfFieldKey(
                        current.getId()).getHighestContainer())) {
                    if (createContainers[i].isAddSupported(current
                            .getDescriptor())) {
                        createContainers[i].addDescriptor(current
                                .getDescriptor());
                        assigned = true;
                    }
                }
            }
            assert assigned;
        }
        return createContainers;
    }


    private TagConverter() {
        // Nothing to do.
    }

}