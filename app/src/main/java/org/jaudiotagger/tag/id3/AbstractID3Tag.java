
package org.jaudiotagger.tag.id3;

import java.util.logging.Logger;


public abstract class AbstractID3Tag extends AbstractTag
{
    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");

    public AbstractID3Tag()
    {
    }

    protected static final String TAG_RELEASE = "ID3v";

    //The purpose of this is to provide the filename that should be used when writing debug messages
    //when problems occur reading or writing to file, otherwise it is difficult to track down the error
    //when processing many files
    private String loggingFilename = "";


    public String getIdentifier()
    {
        return TAG_RELEASE + getRelease() + "." + getMajorVersion() + "." + getRevision();
    }


    public abstract byte getRelease();



    public abstract byte getMajorVersion();


    public abstract byte getRevision();


    public AbstractID3Tag(AbstractID3Tag copyObject)
    {
        super(copyObject);
    }




    protected String getLoggingFilename()
    {
        return loggingFilename;
    }


    protected void setLoggingFilename(String loggingFilename)
    {
        this.loggingFilename = loggingFilename;
    }
}
