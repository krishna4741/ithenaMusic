package org.jaudiotagger.audio.aiff;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;


public class AiffAudioHeader extends GenericAudioHeader {

    public enum FileType {
        AIFFTYPE,
        AIFCTYPE
    }
    
    public enum Endian {
        BIG_ENDIAN,
        LITTLE_ENDIAN
    }
    
    private FileType fileType;
    private Date timestamp;
    private Endian endian;
    private String audioEncoding;
    private String name;
    private String author;
    private String copyright;
    
    private List<String> applicationIdentifiers;
    private List<String> comments;
    
    public AiffAudioHeader() {
        applicationIdentifiers = new ArrayList<String> ();
        comments = new ArrayList<String> ();
        endian = Endian.BIG_ENDIAN;
    }


    public Date getTimestamp () {
        return timestamp;
    }
    

    public void setTimestamp (Date d) {
        timestamp = d;
    }
    

    public FileType getFileType () {
        return fileType;
    }
    

    public void setFileType (FileType typ) {
        fileType = typ;
    }
    

    public String getAuthor () {
        return author;
    }
    

    public void setAuthor (String a) {
        author = a;
    }
    

    public String getName () {
        return name;
    }
    

    public void setName (String n) {
        name = n;
    }
    

    public String getCopyright () {
        return copyright;
    }
    

    public void setCopyright (String c) {
        copyright = c;
    }

    
    

    public Endian getEndian () {
        return endian;
    }
    

    public void setEndian (Endian e) {
        endian = e;
    }
    

    public List<String> getApplicationIdentifiers () {
        return applicationIdentifiers;
    }
    

    public void addApplicationIdentifier (String id) {
        applicationIdentifiers.add (id);
    }


    public List<String> getAnnotations () {
        return applicationIdentifiers;
    }
    

    public void addAnnotation (String a) {
        applicationIdentifiers.add (a);
    }


    public List<String> getComments () {
        return comments;
    }
    

    public void addComment (String c) {
        comments.add (c);
    }


    public String getAudioEncoding () {
        return audioEncoding;
    }
    

    public void setAudioEncoding (String s) {
        audioEncoding = s;
    }
}
