package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.logging.ErrorMessage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class LanguageList extends Chunk {


    private final List<String> languages = new ArrayList<String>();


    public LanguageList() {
        super(GUID.GUID_LANGUAGE_LIST, 0, BigInteger.ZERO);
    }


    public LanguageList(final long pos, final BigInteger size) {
        super(GUID.GUID_LANGUAGE_LIST, pos, size);
    }


    public void addLanguage(final String language) {
        if (language.length() < MetadataDescriptor.MAX_LANG_INDEX) {
            if (!this.languages.contains(language)) {
                this.languages.add(language);
            }
        } else {
            throw new IllegalArgumentException(
                    ErrorMessage.WMA_LENGTH_OF_LANGUAGE_IS_TOO_LARGE
                            .getMsg(language.length() * 2 + 2));
        }
    }


    public String getLanguage(final int index) {
        return this.languages.get(index);
    }


    public int getLanguageCount() {
        return this.languages.size();
    }


    public List<String> getLanguages() {
        return new ArrayList<String>(this.languages);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        for (int i = 0; i < getLanguageCount(); i++) {
            result.append(prefix);
            result.append("  |-> ");
            result.append(i);
            result.append(" : ");
            result.append(getLanguage(i));
            result.append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }


    public void removeLanguage(final int index) {
        this.languages.remove(index);
    }
}
