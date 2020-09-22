
package org.jaudiotagger.logging;



public class PlainTextTagDisplayFormatter extends AbstractTagDisplayFormatter
{
    private static PlainTextTagDisplayFormatter formatter;

    StringBuffer sb = new StringBuffer();
    StringBuffer indent = new StringBuffer();

    public PlainTextTagDisplayFormatter()
    {

    }

    public void openHeadingElement(String type, String value)
    {
        addElement(type, value);
        increaseLevel();
    }

    public void openHeadingElement(String type, boolean value)
    {
        openHeadingElement(type, String.valueOf(value));
    }

    public void openHeadingElement(String type, int value)
    {
        openHeadingElement(type, String.valueOf(value));
    }

    public void closeHeadingElement(String type)
    {
        decreaseLevel();
    }

    public void increaseLevel()
    {
        level++;
        indent.append("  ");
    }

    public void decreaseLevel()
    {
        level--;
        indent = new StringBuffer(indent.substring(0, indent.length() - 2));
    }

    public void addElement(String type, String value)
    {
        sb.append(indent).append(type).append(":").append(value).append('\n');
    }

    public void addElement(String type, int value)
    {
        addElement(type, String.valueOf(value));
    }

    public void addElement(String type, boolean value)
    {
        addElement(type, String.valueOf(value));
    }

    public String toString()
    {
        return sb.toString();
    }

    public static AbstractTagDisplayFormatter getInstanceOf()
    {
        if (formatter == null)
        {
            formatter = new PlainTextTagDisplayFormatter();
        }
        return formatter;
    }
}
