
package org.jaudiotagger.tag;

import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;
import org.jaudiotagger.tag.id3.framebody.ID3v24FrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.lyrics3.Lyrics3v2Fields;
import org.jaudiotagger.tag.options.PadNumberOption;
import org.jaudiotagger.tag.reference.GenreTypes;
import org.jaudiotagger.tag.reference.ID3V2Version;
import org.jaudiotagger.tag.reference.Languages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class TagOptionSingleton
{

    private static HashMap<String, TagOptionSingleton> tagOptionTable = new HashMap<String, TagOptionSingleton>();


    private static String DEFAULT = "default";


    private static String defaultOptions = DEFAULT;


    private HashMap<Class<? extends ID3v24FrameBody>, LinkedList<String>> keywordMap = new HashMap<Class<? extends ID3v24FrameBody>, LinkedList<String>>();


    private HashMap<String, Boolean> lyrics3SaveFieldMap = new HashMap<String, Boolean>();


    private HashMap<String, String> parenthesisMap = new HashMap<String, String>();


    private HashMap<String, String> replaceWordMap = new HashMap<String, String>();



    private String language = "eng";



    private boolean filenameTagSave = false;


    private boolean id3v1Save = true;


    private boolean id3v1SaveAlbum = true;


    private boolean id3v1SaveArtist = true;


    private boolean id3v1SaveComment = true;


    private boolean id3v1SaveGenre = true;


    private boolean id3v1SaveTitle = true;


    private boolean id3v1SaveTrack = true;


    private boolean id3v1SaveYear = true;



    private boolean id3v2PaddingCopyTag = true;


    private boolean id3v2PaddingWillShorten = false;


    private boolean id3v2Save = true;



    private boolean lyrics3KeepEmptyFieldIfRead = false;


    private boolean lyrics3Save = true;


    private boolean lyrics3SaveEmptyField = false;


    private boolean originalSavedAfterAdjustingID3v2Padding = true;



    private byte timeStampFormat = 2;


    private int numberMP3SyncFrame = 3;


    private boolean unsyncTags = false;


    private boolean removeTrailingTerminatorOnWrite = true;


    private byte id3v23DefaultTextEncoding = TextEncoding.ISO_8859_1;


    private byte id3v24DefaultTextEncoding = TextEncoding.ISO_8859_1;


    private byte id3v24UnicodeTextEncoding = TextEncoding.UTF_16;



    private boolean resetTextEncodingForExistingFrames = false;


    private boolean truncateTextWithoutErrors = false;


    private boolean padNumbers = false;


    private PadNumberOption padNumberTotalLength = PadNumberOption.PAD_ONE_ZERO;


    private boolean isAndroid = false;

    private boolean isAPICDescriptionITunesCompatible = false;


    private boolean isEncodeUTF16BomAsLittleEndian = true;


     //TODO Not Actually Used yet, originally intended for dealing with ratings and genres
    private int playerCompatability=-1;


    private long writeChunkSize=5000000;

    private boolean isWriteMp4GenresAsText=false;

    private boolean isWriteMp3GenresAsText=false;

    private ID3V2Version id3v2Version = ID3V2Version.ID3_V23;


    private TagOptionSingleton()
    {
        setToDefault();
    }



    public static TagOptionSingleton getInstance()
    {
        return getInstance(defaultOptions);
    }


    public static TagOptionSingleton getInstance(String instanceKey)
    {
        TagOptionSingleton tagOptions = tagOptionTable.get(instanceKey);

        if (tagOptions == null)
        {
            tagOptions = new TagOptionSingleton();
            tagOptionTable.put(instanceKey, tagOptions);
        }

        return tagOptions;
    }


    public void setFilenameTagSave(boolean filenameTagSave)
    {
        this.filenameTagSave = filenameTagSave;
    }


    public boolean isFilenameTagSave()
    {
        return filenameTagSave;
    }


    public void setID3V2Version(ID3V2Version  id3v2Version)
    {
        this.id3v2Version = id3v2Version;
    }


    public ID3V2Version getID3V2Version()
    {
        return id3v2Version;
    }



    public void setInstanceKey(String instanceKey)
    {
        TagOptionSingleton.defaultOptions = instanceKey;
    }


    public static String getInstanceKey()
    {
        return defaultOptions;
    }



    public void setId3v1Save(boolean id3v1Save)
    {
        this.id3v1Save = id3v1Save;
    }


    public boolean isId3v1Save()
    {
        return id3v1Save;
    }


    public void setId3v1SaveAlbum(boolean id3v1SaveAlbum)
    {
        this.id3v1SaveAlbum = id3v1SaveAlbum;
    }


    public boolean isId3v1SaveAlbum()
    {
        return id3v1SaveAlbum;
    }


    public void setId3v1SaveArtist(boolean id3v1SaveArtist)
    {
        this.id3v1SaveArtist = id3v1SaveArtist;
    }


    public boolean isId3v1SaveArtist()
    {
        return id3v1SaveArtist;
    }


    public void setId3v1SaveComment(boolean id3v1SaveComment)
    {
        this.id3v1SaveComment = id3v1SaveComment;
    }


    public boolean isId3v1SaveComment()
    {
        return id3v1SaveComment;
    }


    public void setId3v1SaveGenre(boolean id3v1SaveGenre)
    {
        this.id3v1SaveGenre = id3v1SaveGenre;
    }


    public boolean isId3v1SaveGenre()
    {
        return id3v1SaveGenre;
    }


    public void setId3v1SaveTitle(boolean id3v1SaveTitle)
    {
        this.id3v1SaveTitle = id3v1SaveTitle;
    }


    public boolean isId3v1SaveTitle()
    {
        return id3v1SaveTitle;
    }


    public void setId3v1SaveTrack(boolean id3v1SaveTrack)
    {
        this.id3v1SaveTrack = id3v1SaveTrack;
    }


    public boolean isId3v1SaveTrack()
    {
        return id3v1SaveTrack;
    }


    public void setId3v1SaveYear(boolean id3v1SaveYear)
    {
        this.id3v1SaveYear = id3v1SaveYear;
    }


    public boolean isId3v1SaveYear()
    {
        return id3v1SaveYear;
    }



    public void setId3v2PaddingCopyTag(boolean id3v2PaddingCopyTag)
    {
        this.id3v2PaddingCopyTag = id3v2PaddingCopyTag;
    }


    public boolean isId3v2PaddingCopyTag()
    {
        return id3v2PaddingCopyTag;
    }



    public void setId3v2PaddingWillShorten(boolean id3v2PaddingWillShorten)
    {
        this.id3v2PaddingWillShorten = id3v2PaddingWillShorten;
    }


    public boolean isId3v2PaddingWillShorten()
    {
        return id3v2PaddingWillShorten;
    }


    public void setId3v2Save(boolean id3v2Save)
    {
        this.id3v2Save = id3v2Save;
    }


    public boolean isId3v2Save()
    {
        return id3v2Save;
    }



    public Iterator<Class<? extends ID3v24FrameBody>> getKeywordIterator()
    {
        return keywordMap.keySet().iterator();
    }


    public Iterator<String> getKeywordListIterator(Class<? extends ID3v24FrameBody> id3v2_4FrameBody)
    {
        return keywordMap.get(id3v2_4FrameBody).iterator();
    }


    public void setLanguage(String lang)
    {
        if (Languages.getInstanceOf().getIdToValueMap().containsKey(lang))
        {
            language = lang;
        }
    }


    public String getLanguage()
    {
        return language;
    }


    public void setLyrics3KeepEmptyFieldIfRead(boolean lyrics3KeepEmptyFieldIfRead)
    {
        this.lyrics3KeepEmptyFieldIfRead = lyrics3KeepEmptyFieldIfRead;
    }


    public boolean isLyrics3KeepEmptyFieldIfRead()
    {
        return lyrics3KeepEmptyFieldIfRead;
    }


    public void setLyrics3Save(boolean lyrics3Save)
    {
        this.lyrics3Save = lyrics3Save;
    }


    public boolean isLyrics3Save()
    {
        return lyrics3Save;
    }


    public void setLyrics3SaveEmptyField(boolean lyrics3SaveEmptyField)
    {
        this.lyrics3SaveEmptyField = lyrics3SaveEmptyField;
    }


    public boolean isLyrics3SaveEmptyField()
    {
        return lyrics3SaveEmptyField;
    }


    public void setLyrics3SaveField(String id, boolean save)
    {
        this.lyrics3SaveFieldMap.put(id, save);
    }


    public boolean getLyrics3SaveField(String id)
    {
        return lyrics3SaveFieldMap.get(id);
    }


    public HashMap<String, Boolean> getLyrics3SaveFieldMap()
    {
        return lyrics3SaveFieldMap;
    }


    public String getNewReplaceWord(String oldWord)
    {
        return replaceWordMap.get(oldWord);
    }


    public void setNumberMP3SyncFrame(int numberMP3SyncFrame)
    {
        this.numberMP3SyncFrame = numberMP3SyncFrame;
    }


    public int getNumberMP3SyncFrame()
    {
        return numberMP3SyncFrame;
    }


    public Iterator<String> getOldReplaceWordIterator()
    {
        return replaceWordMap.keySet().iterator();
    }


    public boolean isOpenParenthesis(String open)
    {
        return parenthesisMap.containsKey(open);
    }


    public Iterator<String> getOpenParenthesisIterator()
    {
        return parenthesisMap.keySet().iterator();
    }


    public void setOriginalSavedAfterAdjustingID3v2Padding(boolean originalSavedAfterAdjustingID3v2Padding)
    {
        this.originalSavedAfterAdjustingID3v2Padding = originalSavedAfterAdjustingID3v2Padding;
    }


    public boolean isOriginalSavedAfterAdjustingID3v2Padding()
    {
        return originalSavedAfterAdjustingID3v2Padding;
    }



    public void setTimeStampFormat(byte tsf)
    {
        if ((tsf == 1) || (tsf == 2))
        {
            timeStampFormat = tsf;
        }
    }


    public byte getTimeStampFormat()
    {
        return timeStampFormat;
    }


    public void setToDefault()
    {
        keywordMap = new HashMap<Class<? extends ID3v24FrameBody>, LinkedList<String>>();
        filenameTagSave = false;
        id3v1Save = true;
        id3v1SaveAlbum = true;
        id3v1SaveArtist = true;
        id3v1SaveComment = true;
        id3v1SaveGenre = true;
        id3v1SaveTitle = true;
        id3v1SaveTrack = true;
        id3v1SaveYear = true;
        id3v2PaddingCopyTag = true;
        id3v2PaddingWillShorten = false;
        id3v2Save = true;
        language = "eng";
        lyrics3KeepEmptyFieldIfRead = false;
        lyrics3Save = true;
        lyrics3SaveEmptyField = false;
        lyrics3SaveFieldMap = new HashMap<String, Boolean>();
        numberMP3SyncFrame = 3;
        parenthesisMap = new HashMap<String, String>();
        replaceWordMap = new HashMap<String, String>();
        timeStampFormat = 2;
        unsyncTags = false;
        removeTrailingTerminatorOnWrite = true;
        id3v23DefaultTextEncoding = TextEncoding.ISO_8859_1;
        id3v24DefaultTextEncoding = TextEncoding.ISO_8859_1;
        id3v24UnicodeTextEncoding = TextEncoding.UTF_16;
        resetTextEncodingForExistingFrames = false;
        truncateTextWithoutErrors = false;
        padNumbers = false;
        isAPICDescriptionITunesCompatible=false;
        isAndroid = false;
        isEncodeUTF16BomAsLittleEndian = true;
        writeChunkSize=5000000;
        isWriteMp4GenresAsText=false;
        padNumberTotalLength = PadNumberOption.PAD_ONE_ZERO;
        id3v2Version = ID3V2Version.ID3_V23;
        //default all lyrics3 fields to save. id3v1 fields are individual
        // settings. id3v2 fields are always looked at to save.
        Iterator<String> iterator = Lyrics3v2Fields.getInstanceOf().getIdToValueMap().keySet().iterator();
        String fieldId;

        while (iterator.hasNext())
        {
            fieldId = iterator.next();
            lyrics3SaveFieldMap.put(fieldId, true);
        }

        try
        {
            addKeyword(FrameBodyCOMM.class, "ultimix");
            addKeyword(FrameBodyCOMM.class, "dance");
            addKeyword(FrameBodyCOMM.class, "mix");
            addKeyword(FrameBodyCOMM.class, "remix");
            addKeyword(FrameBodyCOMM.class, "rmx");
            addKeyword(FrameBodyCOMM.class, "live");
            addKeyword(FrameBodyCOMM.class, "cover");
            addKeyword(FrameBodyCOMM.class, "soundtrack");
            addKeyword(FrameBodyCOMM.class, "version");
            addKeyword(FrameBodyCOMM.class, "acoustic");
            addKeyword(FrameBodyCOMM.class, "original");
            addKeyword(FrameBodyCOMM.class, "cd");
            addKeyword(FrameBodyCOMM.class, "extended");
            addKeyword(FrameBodyCOMM.class, "vocal");
            addKeyword(FrameBodyCOMM.class, "unplugged");
            addKeyword(FrameBodyCOMM.class, "acapella");
            addKeyword(FrameBodyCOMM.class, "edit");
            addKeyword(FrameBodyCOMM.class, "radio");
            addKeyword(FrameBodyCOMM.class, "original");
            addKeyword(FrameBodyCOMM.class, "album");
            addKeyword(FrameBodyCOMM.class, "studio");
            addKeyword(FrameBodyCOMM.class, "instrumental");
            addKeyword(FrameBodyCOMM.class, "unedited");
            addKeyword(FrameBodyCOMM.class, "karoke");
            addKeyword(FrameBodyCOMM.class, "quality");
            addKeyword(FrameBodyCOMM.class, "uncensored");
            addKeyword(FrameBodyCOMM.class, "clean");
            addKeyword(FrameBodyCOMM.class, "dirty");

            addKeyword(FrameBodyTIPL.class, "f.");
            addKeyword(FrameBodyTIPL.class, "feat");
            addKeyword(FrameBodyTIPL.class, "feat.");
            addKeyword(FrameBodyTIPL.class, "featuring");
            addKeyword(FrameBodyTIPL.class, "ftng");
            addKeyword(FrameBodyTIPL.class, "ftng.");
            addKeyword(FrameBodyTIPL.class, "ft.");
            addKeyword(FrameBodyTIPL.class, "ft");

            iterator = GenreTypes.getInstanceOf().getValueToIdMap().keySet().iterator();

            while (iterator.hasNext())
            {
                addKeyword(FrameBodyCOMM.class, iterator.next());
            }
        }
        catch (TagException ex)
        {
            // this shouldn'timer happen, indicates coding error
            throw new RuntimeException(ex);
        }


        addReplaceWord("v.", "vs.");
        addReplaceWord("vs.", "vs.");
        addReplaceWord("versus", "vs.");
        addReplaceWord("f.", "feat.");
        addReplaceWord("feat", "feat.");
        addReplaceWord("featuring", "feat.");
        addReplaceWord("ftng.", "feat.");
        addReplaceWord("ftng", "feat.");
        addReplaceWord("ft.", "feat.");
        addReplaceWord("ft", "feat.");


        iterator = this.getKeywordListIterator(FrameBodyTIPL.class);


        addParenthesis("(", ")");
        addParenthesis("[", "]");
        addParenthesis("{", "}");
        addParenthesis("<", ">");
    }



    public void addKeyword(Class<? extends ID3v24FrameBody> id3v2FrameBodyClass, String keyword) throws TagException
    {
        if (!AbstractID3v2FrameBody.class.isAssignableFrom(id3v2FrameBodyClass))
        {
            throw new TagException("Invalid class type. Must be AbstractId3v2FrameBody " + id3v2FrameBodyClass);
        }

        if ((keyword != null) && (keyword.length() > 0))
        {
            LinkedList<String> keywordList;

            if (!keywordMap.containsKey(id3v2FrameBodyClass))
            {
                keywordList = new LinkedList<String>();
                keywordMap.put(id3v2FrameBodyClass, keywordList);
            }
            else
            {
                keywordList = keywordMap.get(id3v2FrameBodyClass);
            }

            keywordList.add(keyword);
        }
    }


    public void addParenthesis(String open, String close)
    {
        parenthesisMap.put(open, close);
    }


    public void addReplaceWord(String oldWord, String newWord)
    {
        replaceWordMap.put(oldWord, newWord);
    }


    public boolean isUnsyncTags()
    {
        return unsyncTags;
    }


    public void setUnsyncTags(boolean unsyncTags)
    {
        this.unsyncTags = unsyncTags;
    }


    public boolean isRemoveTrailingTerminatorOnWrite()
    {
        return removeTrailingTerminatorOnWrite;
    }


    public void setRemoveTrailingTerminatorOnWrite(boolean removeTrailingTerminatorOnWrite)
    {
        this.removeTrailingTerminatorOnWrite = removeTrailingTerminatorOnWrite;
    }


    public byte getId3v23DefaultTextEncoding()
    {
        return id3v23DefaultTextEncoding;
    }


    public void setId3v23DefaultTextEncoding(byte id3v23DefaultTextEncoding)
    {
        if ((id3v23DefaultTextEncoding == TextEncoding.ISO_8859_1) || (id3v23DefaultTextEncoding == TextEncoding.UTF_16))
        {
            this.id3v23DefaultTextEncoding = id3v23DefaultTextEncoding;
        }
    }


    public byte getId3v24DefaultTextEncoding()
    {
        return id3v24DefaultTextEncoding;
    }


    public void setId3v24DefaultTextEncoding(byte id3v24DefaultTextEncoding)
    {
        if ((id3v24DefaultTextEncoding == TextEncoding.ISO_8859_1) || (id3v24DefaultTextEncoding == TextEncoding.UTF_16) || (id3v24DefaultTextEncoding == TextEncoding.UTF_16BE) || (id3v24DefaultTextEncoding == TextEncoding.UTF_8))
        {
            this.id3v24DefaultTextEncoding = id3v24DefaultTextEncoding;
        }

    }


    public byte getId3v24UnicodeTextEncoding()
    {
        return id3v24UnicodeTextEncoding;
    }


    public void setId3v24UnicodeTextEncoding(byte id3v24UnicodeTextEncoding)
    {
        if ((id3v24UnicodeTextEncoding == TextEncoding.UTF_16) || (id3v24UnicodeTextEncoding == TextEncoding.UTF_16BE) || (id3v24UnicodeTextEncoding == TextEncoding.UTF_8))
        {
            this.id3v24UnicodeTextEncoding = id3v24UnicodeTextEncoding;
        }
    }


    public boolean isResetTextEncodingForExistingFrames()
    {
        return resetTextEncodingForExistingFrames;
    }


    public void setResetTextEncodingForExistingFrames(boolean resetTextEncodingForExistingFrames)
    {
        this.resetTextEncodingForExistingFrames = resetTextEncodingForExistingFrames;
    }


    public boolean isTruncateTextWithoutErrors()
    {
        return truncateTextWithoutErrors;
    }


    public void setTruncateTextWithoutErrors(boolean truncateTextWithoutErrors)
    {
        this.truncateTextWithoutErrors = truncateTextWithoutErrors;
    }

    public boolean isPadNumbers()
    {
        return padNumbers;
    }

    public void setPadNumbers(boolean padNumbers)
    {
        this.padNumbers = padNumbers;
    }


    public int getPlayerCompatability()
    {
        return playerCompatability;
    }

    public void setPlayerCompatability(int playerCompatability)
    {
        this.playerCompatability = playerCompatability;
    }


    public boolean isEncodeUTF16BomAsLittleEndian()
    {
        return isEncodeUTF16BomAsLittleEndian;
    }

    public void setEncodeUTF16BomAsLittleEndian(boolean encodeUTF16BomAsLittleEndian)
    {
        isEncodeUTF16BomAsLittleEndian = encodeUTF16BomAsLittleEndian;
    }


    public long getWriteChunkSize()
    {
        return writeChunkSize;
    }

    public void setWriteChunkSize(long writeChunkSize)
    {
        this.writeChunkSize = writeChunkSize;
    }


    public boolean isWriteMp4GenresAsText()
    {
        return isWriteMp4GenresAsText;
    }

    public void setWriteMp4GenresAsText(boolean writeMp4GenresAsText)
    {
        isWriteMp4GenresAsText = writeMp4GenresAsText;
    }


    public boolean isWriteMp3GenresAsText()
    {
        return isWriteMp3GenresAsText;
    }

    public void setWriteMp3GenresAsText(boolean writeMp3GenresAsText)
    {
        isWriteMp3GenresAsText = writeMp3GenresAsText;
    }


    public PadNumberOption getPadNumberTotalLength()
    {
        return padNumberTotalLength;
    }

    public void setPadNumberTotalLength(PadNumberOption padNumberTotalLength)
    {
        this.padNumberTotalLength = padNumberTotalLength;
    }


    public boolean isAPICDescriptionITunesCompatible()
    {
        return isAPICDescriptionITunesCompatible;
    }

    public void setAPICDescriptionITunesCompatible(boolean APICDescriptionITunesCompatible)
    {
        isAPICDescriptionITunesCompatible = APICDescriptionITunesCompatible;
    }
}
