
package org.jaudiotagger.logging;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;


public class XMLTagDisplayFormatter extends AbstractTagDisplayFormatter
{
    private static XMLTagDisplayFormatter formatter;

    protected static final String xmlOpenStart = "<";
    protected static final String xmlOpenEnd = ">";
    protected static final String xmlCloseStart = "</";
    protected static final String xmlCloseEnd = ">";
    protected static final String xmlSingleTagClose = " />";
    protected static final String xmlCDataTagOpen = "<![CDATA[";
    protected static final String xmlCDataTagClose = "]]>";


    StringBuffer sb = new StringBuffer();

    public XMLTagDisplayFormatter()
    {

    }


    public static String xmlOpen(String xmlName)
    {
        return xmlOpenStart + xmlName + xmlOpenEnd;
    }

    public static String xmlOpenHeading(String name, String data)
    {
        return (xmlOpen(name + " id=\"" + data + "\""));
    }



    public static String xmlCData(String xmlData)
    {
        char tempChar;
        StringBuffer replacedString = new StringBuffer();
        for (int i = 0; i < xmlData.length(); i++)
        {
            tempChar = xmlData.charAt(i);
            if ((Character.isLetterOrDigit(tempChar)) || (Character.isSpaceChar(tempChar)))
            {
                replacedString.append(tempChar);
            }
            else
            {
                replacedString.append("#x").append(Character.digit(tempChar, 16));
            }
        }
        return xmlCDataTagOpen + replacedString + xmlCDataTagClose;
    }


    public static String xmlClose(String xmlName)
    {
        return xmlCloseStart + xmlName + xmlCloseEnd;
    }

    public static String xmlSingleTag(String data)
    {
        return xmlOpenStart + data + xmlSingleTagClose;
    }

    public static String xmlFullTag(String xmlName, String data)
    {
        return xmlOpen(xmlName) + xmlCData(data) + xmlClose(xmlName);
    }


    public void openHeadingElement(String type, String value)
    {
        if (value.length() == 0)
        {
            sb.append(xmlOpen(type));
        }
        else
        {
            sb.append(xmlOpenHeading(type, replaceXMLCharacters(value)));
        }
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
        sb.append(xmlClose(type));
    }

    public void addElement(String type, String value)
    {
        sb.append(xmlFullTag(type, replaceXMLCharacters(value)));
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



    public static String replaceXMLCharacters(String xmlData)
    {
        StringBuffer sb = new StringBuffer();
        StringCharacterIterator sCI = new StringCharacterIterator(xmlData);
        for (char c = sCI.first(); c != CharacterIterator.DONE; c = sCI.next())
        {
            switch (c)
            {
                case'&':
                    sb.append("&amp;");
                    break;
                case'<':
                    sb.append("&lt;");
                    break;
                case'>':
                    sb.append("&gt;");
                    break;
                case'"':
                    sb.append("&quot;");
                    break;
                case'\'':
                    sb.append("&apos;");
                    break;


                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
