
package org.jaudiotagger.tag;

import java.io.UnsupportedEncodingException;


public interface TagField
{


    public void copyContent(TagField field);


    public String getId();


    public byte[] getRawContent() throws UnsupportedEncodingException;


    public boolean isBinary();


    public void isBinary(boolean b);


    public boolean isCommon();


    public boolean isEmpty();


    public String toString();
}