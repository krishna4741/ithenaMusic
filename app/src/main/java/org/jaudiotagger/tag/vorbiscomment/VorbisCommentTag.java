
package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;

import static org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey.VENDOR;

import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;


public class VorbisCommentTag extends AbstractTag
{
    private static EnumMap<FieldKey, VorbisCommentFieldKey> tagFieldToOggField = new EnumMap<FieldKey, VorbisCommentFieldKey>(FieldKey.class);

    static
    {
        tagFieldToOggField.put(FieldKey.ALBUM, VorbisCommentFieldKey.ALBUM);
        tagFieldToOggField.put(FieldKey.ALBUM_ARTIST, VorbisCommentFieldKey.ALBUMARTIST);
        tagFieldToOggField.put(FieldKey.ALBUM_ARTIST_SORT, VorbisCommentFieldKey.ALBUMARTISTSORT);
        tagFieldToOggField.put(FieldKey.ALBUM_SORT, VorbisCommentFieldKey.ALBUMSORT);
        tagFieldToOggField.put(FieldKey.ARTIST, VorbisCommentFieldKey.ARTIST);
        tagFieldToOggField.put(FieldKey.ARTISTS, VorbisCommentFieldKey.ARTISTS);
        tagFieldToOggField.put(FieldKey.AMAZON_ID, VorbisCommentFieldKey.ASIN);
        tagFieldToOggField.put(FieldKey.ARTIST_SORT, VorbisCommentFieldKey.ARTISTSORT);
        tagFieldToOggField.put(FieldKey.BARCODE, VorbisCommentFieldKey.BARCODE);
        tagFieldToOggField.put(FieldKey.BPM, VorbisCommentFieldKey.BPM);
        tagFieldToOggField.put(FieldKey.CATALOG_NO, VorbisCommentFieldKey.CATALOGNUMBER);
        tagFieldToOggField.put(FieldKey.COMMENT, VorbisCommentFieldKey.COMMENT);
        tagFieldToOggField.put(FieldKey.COMPOSER, VorbisCommentFieldKey.COMPOSER);
        tagFieldToOggField.put(FieldKey.COMPOSER_SORT, VorbisCommentFieldKey.COMPOSERSORT);
        tagFieldToOggField.put(FieldKey.CONDUCTOR, VorbisCommentFieldKey.CONDUCTOR);
        tagFieldToOggField.put(FieldKey.COVER_ART, VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);
        tagFieldToOggField.put(FieldKey.CUSTOM1, VorbisCommentFieldKey.CUSTOM1);
        tagFieldToOggField.put(FieldKey.CUSTOM2, VorbisCommentFieldKey.CUSTOM2);
        tagFieldToOggField.put(FieldKey.CUSTOM3, VorbisCommentFieldKey.CUSTOM3);
        tagFieldToOggField.put(FieldKey.CUSTOM4, VorbisCommentFieldKey.CUSTOM4);
        tagFieldToOggField.put(FieldKey.CUSTOM5, VorbisCommentFieldKey.CUSTOM5);                                        
        tagFieldToOggField.put(FieldKey.DISC_NO, VorbisCommentFieldKey.DISCNUMBER);
        tagFieldToOggField.put(FieldKey.DISC_SUBTITLE, VorbisCommentFieldKey.DISCSUBTITLE);
        tagFieldToOggField.put(FieldKey.DISC_TOTAL, VorbisCommentFieldKey.DISCTOTAL);
        tagFieldToOggField.put(FieldKey.ENCODER, VorbisCommentFieldKey.VENDOR);     //Known as vendor in VorbisComment
        tagFieldToOggField.put(FieldKey.FBPM, VorbisCommentFieldKey.FBPM);
        tagFieldToOggField.put(FieldKey.GENRE, VorbisCommentFieldKey.GENRE);
        tagFieldToOggField.put(FieldKey.GROUPING, VorbisCommentFieldKey.GROUPING);
        tagFieldToOggField.put(FieldKey.ISRC, VorbisCommentFieldKey.ISRC);
        tagFieldToOggField.put(FieldKey.IS_COMPILATION, VorbisCommentFieldKey.COMPILATION);
        tagFieldToOggField.put(FieldKey.KEY, VorbisCommentFieldKey.KEY);
        tagFieldToOggField.put(FieldKey.LANGUAGE, VorbisCommentFieldKey.LANGUAGE);
        tagFieldToOggField.put(FieldKey.LYRICIST, VorbisCommentFieldKey.LYRICIST);
        tagFieldToOggField.put(FieldKey.LYRICS, VorbisCommentFieldKey.LYRICS);
        tagFieldToOggField.put(FieldKey.MEDIA, VorbisCommentFieldKey.MEDIA);
        tagFieldToOggField.put(FieldKey.MOOD, VorbisCommentFieldKey.MOOD);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_ARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_DISC_ID, VorbisCommentFieldKey.MUSICBRAINZ_DISCID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASEARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASEID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, VorbisCommentFieldKey.MUSICBRAINZ_RELEASEGROUPID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, VorbisCommentFieldKey.RELEASECOUNTRY);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_STATUS, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMSTATUS);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_RELEASETRACKID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMTYPE);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_TRACKID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_WORK_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORKID);
        tagFieldToOggField.put(FieldKey.OCCASION, VorbisCommentFieldKey.OCCASION);
        tagFieldToOggField.put(FieldKey.ORIGINAL_ALBUM, VorbisCommentFieldKey.ORIGINAL_ALBUM);
        tagFieldToOggField.put(FieldKey.ORIGINAL_ARTIST, VorbisCommentFieldKey.ORIGINAL_ARTIST);
        tagFieldToOggField.put(FieldKey.ORIGINAL_LYRICIST, VorbisCommentFieldKey.ORIGINAL_LYRICIST);
        tagFieldToOggField.put(FieldKey.ORIGINAL_YEAR, VorbisCommentFieldKey.ORIGINAL_YEAR);
        tagFieldToOggField.put(FieldKey.MUSICIP_ID, VorbisCommentFieldKey.MUSICIP_PUID);
        tagFieldToOggField.put(FieldKey.QUALITY, VorbisCommentFieldKey.QUALITY);
        tagFieldToOggField.put(FieldKey.RATING, VorbisCommentFieldKey.RATING);
        tagFieldToOggField.put(FieldKey.RECORD_LABEL, VorbisCommentFieldKey.LABEL);
        tagFieldToOggField.put(FieldKey.REMIXER, VorbisCommentFieldKey.REMIXER);
        tagFieldToOggField.put(FieldKey.TAGS, VorbisCommentFieldKey.TAGS);
        tagFieldToOggField.put(FieldKey.SCRIPT, VorbisCommentFieldKey.SCRIPT);
        tagFieldToOggField.put(FieldKey.SUBTITLE, VorbisCommentFieldKey.SUBTITLE);
        tagFieldToOggField.put(FieldKey.TEMPO, VorbisCommentFieldKey.TEMPO);
        tagFieldToOggField.put(FieldKey.TITLE, VorbisCommentFieldKey.TITLE);
        tagFieldToOggField.put(FieldKey.TITLE_SORT, VorbisCommentFieldKey.TITLESORT);
        tagFieldToOggField.put(FieldKey.TRACK, VorbisCommentFieldKey.TRACKNUMBER);
        tagFieldToOggField.put(FieldKey.TRACK_TOTAL, VorbisCommentFieldKey.TRACKTOTAL);
        tagFieldToOggField.put(FieldKey.URL_DISCOGS_ARTIST_SITE, VorbisCommentFieldKey.URL_DISCOGS_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_DISCOGS_RELEASE_SITE, VorbisCommentFieldKey.URL_DISCOGS_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.URL_LYRICS_SITE, VorbisCommentFieldKey.URL_LYRICS_SITE);
        tagFieldToOggField.put(FieldKey.URL_OFFICIAL_ARTIST_SITE, VorbisCommentFieldKey.URL_OFFICIAL_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_OFFICIAL_RELEASE_SITE, VorbisCommentFieldKey.URL_OFFICIAL_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.YEAR, VorbisCommentFieldKey.DATE);

        tagFieldToOggField.put(FieldKey.ENGINEER, VorbisCommentFieldKey.ENGINEER);
        tagFieldToOggField.put(FieldKey.PRODUCER, VorbisCommentFieldKey.PRODUCER);
        tagFieldToOggField.put(FieldKey.DJMIXER, VorbisCommentFieldKey.DJMIXER);
        tagFieldToOggField.put(FieldKey.MIXER, VorbisCommentFieldKey.MIXER);
        tagFieldToOggField.put(FieldKey.ARRANGER, VorbisCommentFieldKey.ARRANGER);
        tagFieldToOggField.put(FieldKey.ACOUSTID_FINGERPRINT, VorbisCommentFieldKey.ACOUSTID_FINGERPRINT);
        tagFieldToOggField.put(FieldKey.ACOUSTID_ID, VorbisCommentFieldKey.ACOUSTID_ID);
        tagFieldToOggField.put(FieldKey.COUNTRY, VorbisCommentFieldKey.COUNTRY);
    }

    //This is the vendor string that will be written if no other is supplied. Should be the name of the software
    //that actually encoded the file in the first place.
    public static final String DEFAULT_VENDOR = "jaudiotagger";


    VorbisCommentTag()
    {

    }


    public static VorbisCommentTag createNewTag()
    {
        VorbisCommentTag tag = new VorbisCommentTag();
        tag.setVendor(DEFAULT_VENDOR);
        return tag;
    }


    public String getVendor()
    {
        return getFirst(VENDOR.getFieldName());
    }


    public void setVendor(String vendor)
    {
        if (vendor == null)
        {
            vendor = DEFAULT_VENDOR;
        }
        super.setField(new VorbisCommentTagField(VENDOR.getFieldName(), vendor));
    }

    protected boolean isAllowedEncoding(String enc)
    {
        return enc.equals(VorbisHeader.CHARSET_UTF_8);
    }

    public String toString()
    {
        return "OGG " + super.toString();
    }


    @Override
    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException,FieldDataInvalidException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return createField(tagFieldToOggField.get(genericKey), value);
    }


    public TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws KeyNotFoundException,FieldDataInvalidException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }

        return new VorbisCommentTagField(vorbisCommentFieldKey.getFieldName(), value);
    }


    public TagField createField(String vorbisCommentFieldKey, String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        return new VorbisCommentTagField(vorbisCommentFieldKey, value);
    }


    public List<TagField> getFields(FieldKey genericKey) throws KeyNotFoundException
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFields(vorbisCommentFieldKey.getFieldName());
    }



    public List<String> getAll(FieldKey genericKey) throws KeyNotFoundException
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getAll(vorbisCommentFieldKey.getFieldName());
    }


    public List<TagField> get(VorbisCommentFieldKey vorbisCommentKey) throws KeyNotFoundException
    {
        if (vorbisCommentKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFields(vorbisCommentKey.getFieldName());
    }

    public String getValue(FieldKey genericKey,int index) throws KeyNotFoundException
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getItem(vorbisCommentFieldKey.getFieldName(),index);
    }


    public String getFirst(VorbisCommentFieldKey vorbisCommentKey) throws KeyNotFoundException
    {
        if (vorbisCommentKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFirst(vorbisCommentKey.getFieldName());
    }


    public boolean hasField(FieldKey genericKey)
    {
        VorbisCommentFieldKey vorbisFieldKey = tagFieldToOggField.get(genericKey);
        return getFields(vorbisFieldKey.getFieldName()).size() != 0;
    }


    public boolean hasField(VorbisCommentFieldKey vorbisFieldKey)
    {
        return getFields(vorbisFieldKey.getFieldName()).size() != 0;
    }


    public void deleteField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        deleteField(vorbisCommentFieldKey);
    }


    public void deleteField(VorbisCommentFieldKey vorbisCommentFieldKey) throws KeyNotFoundException
    {
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        super.deleteField(vorbisCommentFieldKey.getFieldName());
    }




    public byte[] getArtworkBinaryData()
    {
        String base64data = this.getFirst(VorbisCommentFieldKey.COVERART);
        byte[] rawdata = Base64Coder.decode(base64data.toCharArray());
        return rawdata;
    }


    public String getArtworkMimeType()
    {
        return this.getFirst(VorbisCommentFieldKey.COVERARTMIME);
    }


    public boolean isEmpty()
    {
        return fields.size() <= 1;
    }


    public void addField(TagField field)
    {
        if (field.getId().equals(VorbisCommentFieldKey.VENDOR.getFieldName()))
        {
            super.setField(field);
        }
        else
        {
            super.addField(field);
        }
    }

     public TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return getFirstField(tagFieldToOggField.get(genericKey).getFieldName());
    }


    public List<Artwork> getArtworkList()
    {
        List<Artwork>  artworkList  = new ArrayList<Artwork>(1);

        //Read Old Format
        if(getArtworkBinaryData()!=null & getArtworkBinaryData().length>0)
        {
            Artwork artwork= ArtworkFactory.getNew();
            artwork.setMimeType(getArtworkMimeType());
            artwork.setBinaryData(getArtworkBinaryData());
            artworkList.add(artwork);
        }

        //New Format (Supports Multiple Images)
        List<TagField> metadataBlockPics = this.get(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);
        for(TagField tagField:metadataBlockPics)
        {

            try
            {
                byte[] imageBinaryData = Base64Coder.decode(((TagTextField)tagField).getContent());
                MetadataBlockDataPicture coverArt = new MetadataBlockDataPicture(ByteBuffer.wrap(imageBinaryData));
                Artwork artwork=ArtworkFactory.createArtworkFromMetadataBlockDataPicture(coverArt);
                artworkList.add(artwork);
            }
            catch(IOException ioe)
            {
                throw new RuntimeException(ioe);
            }
            catch(InvalidFrameException ife)
            {
                throw new RuntimeException(ife);
            }
        }
        return artworkList;
    }



      private MetadataBlockDataPicture createMetadataBlockDataPicture(Artwork artwork) throws FieldDataInvalidException
      {
          if(artwork.isLinked())
          {
               return new MetadataBlockDataPicture(
                      Utils.getDefaultBytes(artwork.getImageUrl(), TextEncoding.CHARSET_ISO_8859_1),
                      artwork.getPictureType(),
                      MetadataBlockDataPicture.IMAGE_IS_URL,
                      "",
                      0,
                      0,
                      0,
                      0);
          }
          else
          {
              if(!artwork.setImageFromData())
              {
                  throw new FieldDataInvalidException("Unable to create MetadataBlockDataPicture from buffered");
              }
              return new MetadataBlockDataPicture(artwork.getBinaryData(),
                      artwork.getPictureType(),
                      artwork.getMimeType(),
                      artwork.getDescription(),
                      artwork.getWidth(),
                      artwork.getHeight(),
                      0,
                      0);
          }
      }


      public TagField createField(Artwork artwork) throws FieldDataInvalidException
      {
        try
        {
            char[] testdata = Base64Coder.encode(createMetadataBlockDataPicture(artwork).getRawContent());
            String base64image = new String(testdata);
            TagField imageTagField  = createField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE, base64image);
            return imageTagField;
        }
        catch(UnsupportedEncodingException uee)
        {
            throw new RuntimeException(uee);
        }
    }


    @Override
    public void setField(Artwork artwork) throws FieldDataInvalidException
    {
        //Set field
        this.setField(createField(artwork));

        //If worked okay above then that should be first artwork and if we still had old coverart format
        //that should be removed
        if(this.getFirst(VorbisCommentFieldKey.COVERART).length()>0)
        {
            this.deleteField(VorbisCommentFieldKey.COVERART);
            this.deleteField(VorbisCommentFieldKey.COVERARTMIME);
        }
    }


    public void addField(Artwork artwork) throws FieldDataInvalidException
    {
        this.addField(createField(artwork));
    }


    @Deprecated
    public void setArtworkField(byte[] data, String mimeType)
    {
        char[] testdata = Base64Coder.encode(data);
        String base64image = new String(testdata);
        VorbisCommentTagField dataField = new VorbisCommentTagField(VorbisCommentFieldKey.COVERART.getFieldName(), base64image);
        VorbisCommentTagField mimeField = new VorbisCommentTagField(VorbisCommentFieldKey.COVERARTMIME.getFieldName(), mimeType);

        setField(dataField);
        setField(mimeField);

    }


    public void setField(String vorbisCommentKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(vorbisCommentKey,value);
        setField(tagfield);
    }


    public void addField(String vorbisCommentKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(vorbisCommentKey,value);
        addField(tagfield);
    }


    public void deleteArtworkField() throws KeyNotFoundException
    {
        //New Method
        this.deleteField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);

        //Old Method
        this.deleteField(VorbisCommentFieldKey.COVERART);
        this.deleteField(VorbisCommentFieldKey.COVERARTMIME);
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }

}

