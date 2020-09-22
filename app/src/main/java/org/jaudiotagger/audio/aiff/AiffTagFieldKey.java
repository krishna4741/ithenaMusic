package org.jaudiotagger.audio.aiff;


public enum AiffTagFieldKey {
    TIMESTAMP("TIMESTAMP");
    
    private String fieldName;

    AiffTagFieldKey(String fieldName)
    {
        this.fieldName = fieldName;
    }
    
    public String getFieldName()
    {
        return fieldName;
    }
}
