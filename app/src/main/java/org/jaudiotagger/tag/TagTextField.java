
package org.jaudiotagger.tag;


public interface TagTextField extends TagField
{


    public String getContent();


    public String getEncoding();


    public void setContent(String content);


    public void setEncoding(String encoding);
}