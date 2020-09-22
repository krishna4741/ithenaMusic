package org.jaudiotagger.tag.asf;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.reference.PictureTypes;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;

import java.io.UnsupportedEncodingException;
import java.util.*;


public final class AsfTag extends AbstractTag
{

    private static class AsfFieldIterator implements Iterator<AsfTagField>
    {


        private final Iterator<TagField> fieldIterator;


        public AsfFieldIterator(final Iterator<TagField> iterator)
        {
            assert iterator != null;
            this.fieldIterator = iterator;
        }


        public boolean hasNext()
        {
            return this.fieldIterator.hasNext();
        }


        public AsfTagField next()
        {
            return (AsfTagField) this.fieldIterator.next();
        }


        public void remove()
        {
            this.fieldIterator.remove();
        }
    }


    public final static Set<AsfFieldKey> COMMON_FIELDS;


    private static final EnumMap<FieldKey, AsfFieldKey> tagFieldToAsfField;

    // Mapping from generic key to asf key

    static
    {
        tagFieldToAsfField = new EnumMap<FieldKey, AsfFieldKey>(FieldKey.class);
        tagFieldToAsfField.put(FieldKey.ALBUM, AsfFieldKey.ALBUM);
        tagFieldToAsfField.put(FieldKey.ALBUM_ARTIST, AsfFieldKey.ALBUM_ARTIST);
        tagFieldToAsfField.put(FieldKey.ALBUM_ARTIST_SORT, AsfFieldKey.ALBUM_ARTIST_SORT);
        tagFieldToAsfField.put(FieldKey.ALBUM_SORT, AsfFieldKey.ALBUM_SORT);
        tagFieldToAsfField.put(FieldKey.AMAZON_ID, AsfFieldKey.AMAZON_ID);
        tagFieldToAsfField.put(FieldKey.ARTIST, AsfFieldKey.AUTHOR);
        tagFieldToAsfField.put(FieldKey.ARTIST_SORT, AsfFieldKey.ARTIST_SORT);
        tagFieldToAsfField.put(FieldKey.ARTISTS, AsfFieldKey.ARTISTS);
        tagFieldToAsfField.put(FieldKey.BARCODE, AsfFieldKey.BARCODE);
        tagFieldToAsfField.put(FieldKey.BPM, AsfFieldKey.BPM);
        tagFieldToAsfField.put(FieldKey.CATALOG_NO, AsfFieldKey.CATALOG_NO);
        tagFieldToAsfField.put(FieldKey.COMMENT, AsfFieldKey.DESCRIPTION);
        tagFieldToAsfField.put(FieldKey.COMPOSER, AsfFieldKey.COMPOSER);
        tagFieldToAsfField.put(FieldKey.COMPOSER_SORT, AsfFieldKey.COMPOSER_SORT);
        tagFieldToAsfField.put(FieldKey.CONDUCTOR, AsfFieldKey.CONDUCTOR);
        tagFieldToAsfField.put(FieldKey.COVER_ART, AsfFieldKey.COVER_ART);
        tagFieldToAsfField.put(FieldKey.CUSTOM1, AsfFieldKey.CUSTOM1);
        tagFieldToAsfField.put(FieldKey.CUSTOM2, AsfFieldKey.CUSTOM2);
        tagFieldToAsfField.put(FieldKey.CUSTOM3, AsfFieldKey.CUSTOM3);
        tagFieldToAsfField.put(FieldKey.CUSTOM4, AsfFieldKey.CUSTOM4);
        tagFieldToAsfField.put(FieldKey.CUSTOM5, AsfFieldKey.CUSTOM5);
        tagFieldToAsfField.put(FieldKey.DISC_NO, AsfFieldKey.DISC_NO);
        tagFieldToAsfField.put(FieldKey.DISC_SUBTITLE, AsfFieldKey.DISC_SUBTITLE);
        tagFieldToAsfField.put(FieldKey.DISC_TOTAL, AsfFieldKey.DISC_TOTAL);
        tagFieldToAsfField.put(FieldKey.ENCODER, AsfFieldKey.ENCODER);
        tagFieldToAsfField.put(FieldKey.FBPM, AsfFieldKey.FBPM);
        tagFieldToAsfField.put(FieldKey.GENRE, AsfFieldKey.GENRE);
        tagFieldToAsfField.put(FieldKey.GROUPING, AsfFieldKey.GROUPING);
        tagFieldToAsfField.put(FieldKey.ISRC, AsfFieldKey.ISRC);
        tagFieldToAsfField.put(FieldKey.IS_COMPILATION, AsfFieldKey.IS_COMPILATION);
        tagFieldToAsfField.put(FieldKey.KEY, AsfFieldKey.INITIAL_KEY);
        tagFieldToAsfField.put(FieldKey.LANGUAGE, AsfFieldKey.LANGUAGE);
        tagFieldToAsfField.put(FieldKey.LYRICIST, AsfFieldKey.LYRICIST);
        tagFieldToAsfField.put(FieldKey.LYRICS, AsfFieldKey.LYRICS);
        tagFieldToAsfField.put(FieldKey.MEDIA, AsfFieldKey.MEDIA);
        tagFieldToAsfField.put(FieldKey.MOOD, AsfFieldKey.MOOD);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_ARTISTID, AsfFieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_DISC_ID, AsfFieldKey.MUSICBRAINZ_DISC_ID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, AsfFieldKey.MUSICBRAINZ_ORIGINAL_RELEASEID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASEARTISTID, AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASEID, AsfFieldKey.MUSICBRAINZ_RELEASEID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, AsfFieldKey.MUSICBRAINZ_RELEASEGROUPID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, AsfFieldKey.MUSICBRAINZ_RELEASETRACKID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASE_STATUS, AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_RELEASE_TYPE, AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_TRACK_ID, AsfFieldKey.MUSICBRAINZ_TRACK_ID);
        tagFieldToAsfField.put(FieldKey.MUSICBRAINZ_WORK_ID, AsfFieldKey.MUSICBRAINZ_WORKID);
        tagFieldToAsfField.put(FieldKey.MUSICIP_ID, AsfFieldKey.MUSICIP_ID);
        tagFieldToAsfField.put(FieldKey.OCCASION, AsfFieldKey.OCCASION);
        tagFieldToAsfField.put(FieldKey.ORIGINAL_ARTIST, AsfFieldKey.ORIGINAL_ARTIST);
        tagFieldToAsfField.put(FieldKey.ORIGINAL_ALBUM, AsfFieldKey.ORIGINAL_ALBUM);
        tagFieldToAsfField.put(FieldKey.ORIGINAL_LYRICIST, AsfFieldKey.ORIGINAL_LYRICIST);
        tagFieldToAsfField.put(FieldKey.ORIGINAL_YEAR, AsfFieldKey.ORIGINAL_YEAR);
        tagFieldToAsfField.put(FieldKey.RATING, AsfFieldKey.USER_RATING);
        tagFieldToAsfField.put(FieldKey.RECORD_LABEL, AsfFieldKey.RECORD_LABEL);
        tagFieldToAsfField.put(FieldKey.QUALITY, AsfFieldKey.QUALITY);
        tagFieldToAsfField.put(FieldKey.REMIXER, AsfFieldKey.REMIXER);
        tagFieldToAsfField.put(FieldKey.SCRIPT, AsfFieldKey.SCRIPT);
        tagFieldToAsfField.put(FieldKey.SUBTITLE, AsfFieldKey.SUBTITLE);
        tagFieldToAsfField.put(FieldKey.TAGS, AsfFieldKey.TAGS);
        tagFieldToAsfField.put(FieldKey.TEMPO, AsfFieldKey.TEMPO);
        tagFieldToAsfField.put(FieldKey.TITLE, AsfFieldKey.TITLE);
        tagFieldToAsfField.put(FieldKey.TITLE_SORT, AsfFieldKey.TITLE_SORT);
        tagFieldToAsfField.put(FieldKey.TRACK, AsfFieldKey.TRACK);
        tagFieldToAsfField.put(FieldKey.TRACK_TOTAL, AsfFieldKey.TRACK_TOTAL);
        tagFieldToAsfField.put(FieldKey.URL_DISCOGS_ARTIST_SITE, AsfFieldKey.URL_DISCOGS_ARTIST_SITE);
        tagFieldToAsfField.put(FieldKey.URL_DISCOGS_RELEASE_SITE, AsfFieldKey.URL_DISCOGS_RELEASE_SITE);
        tagFieldToAsfField.put(FieldKey.URL_LYRICS_SITE, AsfFieldKey.URL_LYRICS_SITE);
        tagFieldToAsfField.put(FieldKey.URL_OFFICIAL_ARTIST_SITE, AsfFieldKey.URL_OFFICIAL_ARTIST_SITE);
        tagFieldToAsfField.put(FieldKey.URL_OFFICIAL_RELEASE_SITE, AsfFieldKey.URL_OFFICIAL_RELEASE_SITE);
        tagFieldToAsfField.put(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, AsfFieldKey.URL_WIKIPEDIA_ARTIST_SITE);
        tagFieldToAsfField.put(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, AsfFieldKey.URL_WIKIPEDIA_RELEASE_SITE);
        tagFieldToAsfField.put(FieldKey.YEAR, AsfFieldKey.YEAR);
        tagFieldToAsfField.put(FieldKey.ENGINEER, AsfFieldKey.ENGINEER);
        tagFieldToAsfField.put(FieldKey.PRODUCER, AsfFieldKey.PRODUCER);
        tagFieldToAsfField.put(FieldKey.DJMIXER, AsfFieldKey.DJMIXER);
        tagFieldToAsfField.put(FieldKey.MIXER, AsfFieldKey.MIXER);
        tagFieldToAsfField.put(FieldKey.ARRANGER, AsfFieldKey.ARRANGER);
        tagFieldToAsfField.put(FieldKey.ACOUSTID_FINGERPRINT, AsfFieldKey.ACOUSTID_FINGERPRINT);
        tagFieldToAsfField.put(FieldKey.ACOUSTID_ID, AsfFieldKey.ACOUSTID_ID);
        tagFieldToAsfField.put(FieldKey.COUNTRY, AsfFieldKey.COUNTRY);
    }

    static
    {
        COMMON_FIELDS = new HashSet<AsfFieldKey>();
        COMMON_FIELDS.add(AsfFieldKey.ALBUM);
        COMMON_FIELDS.add(AsfFieldKey.AUTHOR);
        COMMON_FIELDS.add(AsfFieldKey.DESCRIPTION);
        COMMON_FIELDS.add(AsfFieldKey.GENRE);
        COMMON_FIELDS.add(AsfFieldKey.TITLE);
        COMMON_FIELDS.add(AsfFieldKey.TRACK);
        COMMON_FIELDS.add(AsfFieldKey.YEAR);
    }


    private final boolean copyFields;


    public AsfTag()
    {
        this(false);
    }


    public AsfTag(final boolean copy)
    {
        super();
        this.copyFields = copy;
    }


    public AsfTag(final Tag source, final boolean copy) throws UnsupportedEncodingException
    {
        this(copy);
        copyFrom(source);
    }


    @Override
    // TODO introduce copy idea to all formats
    public void addField(final TagField field)
    {
        if (isValidField(field))
        {
            if (AsfFieldKey.isMultiValued(field.getId()))
            {
                super.addField(copyFrom(field));
            }
            else
            {
                super.setField(copyFrom(field));
            }
        }
    }


    public void addCopyright(final String copyRight)
    {
        addField(createCopyrightField(copyRight));
    }


    public void addRating(final String rating)
    {
        addField(createRatingField(rating));
    }


    private void copyFrom(final Tag source)
    {
        final Iterator<TagField> fieldIterator = source.getFields();
        // iterate over all fields
        while (fieldIterator.hasNext())
        {
            final TagField copy = copyFrom(fieldIterator.next());
            if (copy != null)
            {
                super.addField(copy);
            }
        }
    }


    private TagField copyFrom(final TagField source)
    {
        TagField result;
        if (isCopyingFields())
        {
            if (source instanceof AsfTagField)
            {
                try
                {
                    result = (TagField) ((AsfTagField) source).clone();
                }
                catch (CloneNotSupportedException e)
                {
                    result = new AsfTagField(((AsfTagField) source).getDescriptor());
                }
            }
            else if (source instanceof TagTextField)
            {
                final String content = ((TagTextField) source).getContent();
                result = new AsfTagTextField(source.getId(), content);
            }
            else
            {
                throw new RuntimeException("Unknown Asf Tag Field class:" // NOPMD
                        // by
                        // Christian
                        // Laireiter
                        // on
                        // 5/9/09
                        // 5:44
                        // PM
                        + source.getClass());
            }
        }
        else
        {
            result = source;
        }
        return result;
    }



    public AsfTagCoverField createField(final Artwork artwork)
    {
        return new AsfTagCoverField(artwork.getBinaryData(), artwork.getPictureType(), artwork.getDescription(), artwork.getMimeType());
    }


    public AsfTagCoverField createArtworkField(final byte[] data)
    {
        return new AsfTagCoverField(data, PictureTypes.DEFAULT_ID, null, null);
    }


    public AsfTagTextField createCopyrightField(final String content)
    {
        return new AsfTagTextField(AsfFieldKey.COPYRIGHT, content);
    }


    public AsfTagTextField createRatingField(final String content)
    {
        return new AsfTagTextField(AsfFieldKey.RATING, content);
    }


    public AsfTagTextField createField(final AsfFieldKey asfFieldKey, final String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (asfFieldKey == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        switch (asfFieldKey)
        {
            case COVER_ART:
                throw new UnsupportedOperationException("Cover Art cannot be created using this method");
            case BANNER_IMAGE:
                throw new UnsupportedOperationException("Banner Image cannot be created using this method");
            default:
                return new AsfTagTextField(asfFieldKey.getFieldName(), value);
        }
    }


    @Override
    public AsfTagTextField createField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (genericKey == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        final AsfFieldKey asfFieldKey = tagFieldToAsfField.get(genericKey);
        if (asfFieldKey == null)
        {
            throw new KeyNotFoundException(genericKey.toString());
        }
        return createField(asfFieldKey, value);
    }


    public void deleteField(final AsfFieldKey fieldKey)
    {
        super.deleteField(fieldKey.getFieldName());
    }


    @Override
    public void deleteField(final FieldKey fieldKey) throws KeyNotFoundException
    {
        if (fieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        super.deleteField(tagFieldToAsfField.get(fieldKey).getFieldName());
    }


    public List<TagField> getFields(final FieldKey fieldKey) throws KeyNotFoundException
    {
        if (fieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFields(tagFieldToAsfField.get(fieldKey).getFieldName());
    }


    public List<String> getAll(FieldKey genericKey) throws KeyNotFoundException
    {
        AsfFieldKey asfFieldKey = tagFieldToAsfField.get(genericKey);
        if (asfFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getAll(asfFieldKey.getFieldName());
    }


    public List<Artwork> getArtworkList()
    {
        final List<TagField> coverartList = getFields(FieldKey.COVER_ART);
        final List<Artwork> artworkList = new ArrayList<Artwork>(coverartList.size());

        for (final TagField next : coverartList)
        {
            final AsfTagCoverField coverArt = (AsfTagCoverField) next;
            final Artwork artwork = ArtworkFactory.getNew();
            artwork.setBinaryData(coverArt.getRawImageData());
            artwork.setMimeType(coverArt.getMimeType());
            artwork.setDescription(coverArt.getDescription());
            artwork.setPictureType(coverArt.getPictureType());
            artworkList.add(artwork);
        }
        return artworkList;
    }


    public Iterator<AsfTagField> getAsfFields()
    {
        if (!isCopyingFields())
        {
            throw new IllegalStateException("Since the field conversion is not enabled, this method cannot be executed");
        }
        return new AsfFieldIterator(getFields());
    }


    public List<TagField> getCopyright()
    {
        return getFields(AsfFieldKey.COPYRIGHT.getFieldName());
    }


    @Override
    public String getFirst(final FieldKey genericKey) throws KeyNotFoundException
    {
        return getValue(genericKey, 0);
    }


    public String getFirst(AsfFieldKey asfKey) throws KeyNotFoundException
    {
        if (asfKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFirst(asfKey.getFieldName());
    }


    public String getValue(final FieldKey genericKey, int index) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getItem(tagFieldToAsfField.get(genericKey).getFieldName(), index);
    }


    public String getFirstCopyright()
    {
        return getFirst(AsfFieldKey.COPYRIGHT.getFieldName());
    }


    @Override
    public AsfTagField getFirstField(final FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return (AsfTagField) super.getFirstField(tagFieldToAsfField.get(genericKey).getFieldName());
    }


    public String getFirstRating()
    {
        return getFirst(AsfFieldKey.RATING.getFieldName());
    }


    public List<TagField> getRating()
    {
        return getFields(AsfFieldKey.RATING.getFieldName());
    }


    @Override
    protected boolean isAllowedEncoding(final String enc)
    {
        return AsfHeader.ASF_CHARSET.name().equals(enc);
    }


    public boolean isCopyingFields()
    {
        return this.copyFields;
    }


    // TODO introduce this concept to all formats
    private boolean isValidField(final TagField field)
    {
        if (field == null)
        {
            return false;
        }

        if (!(field instanceof AsfTagField))
        {
            return false;
        }

        return !field.isEmpty();
    }


    @Override
    // TODO introduce copy idea to all formats
    public void setField(final TagField field)
    {
        if (isValidField(field))
        {
            // Copy only occurs if flag setField
            super.setField(copyFrom(field));
        }
    }


    public void setCopyright(final String Copyright)
    {
        setField(createCopyrightField(Copyright));
    }


    public void setRating(final String rating)
    {
        setField(createRatingField(rating));
    }


    public boolean hasField(FieldKey genericKey)
    {
        AsfFieldKey mp4FieldKey = tagFieldToAsfField.get(genericKey);
        return getFields(mp4FieldKey.getFieldName()).size() != 0;
    }


    public boolean hasField(AsfFieldKey asfFieldKey)
    {
        return getFields(asfFieldKey.getFieldName()).size() != 0;
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }
}
