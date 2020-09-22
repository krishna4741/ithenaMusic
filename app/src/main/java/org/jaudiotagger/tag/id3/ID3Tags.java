
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.TagException;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;


public class ID3Tags
{
    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");


    private ID3Tags()
    {
    }


    public static boolean isID3v22FrameIdentifier(String identifier)
    {
        //If less than 3 cant be an identifier
        if (identifier.length() < 3)
        {
            return false;
        }
        //If 3 is it a known identifier
        else return identifier.length() == 3 && ID3v22Frames.getInstanceOf().getIdToValueMap().containsKey(identifier);
    }


    public static boolean isID3v23FrameIdentifier(String identifier)
    {
        return identifier.length() >= 4 && ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier.substring(0, 4));
    }


    public static boolean isID3v24FrameIdentifier(String identifier)
    {
        return identifier.length() >= 4 && ID3v24Frames.getInstanceOf().getIdToValueMap().containsKey(identifier.substring(0, 4));
    }


    static public long getWholeNumber(Object value)
    {
        long number;
        if (value instanceof String)
        {
            number = Long.parseLong((String) value);
        }
        else if (value instanceof Byte)
        {
            number = (Byte) value;
        }
        else if (value instanceof Short)
        {
            number = (Short) value;
        }
        else if (value instanceof Integer)
        {
            number = (Integer) value;
        }
        else if (value instanceof Long)
        {
            number = (Long) value;
        }
        else
        {
            throw new IllegalArgumentException("Unsupported value class: " + value.getClass().getName());
        }
        return number;
    }


    public static String convertFrameID22To23(String identifier)
    {
        if (identifier.length() < 3)
        {
            return null;
        }
        return ID3Frames.convertv22Tov23.get((String)identifier.subSequence(0, 3));
    }


    public static String convertFrameID22To24(String identifier)
    {
        //Idv22 identifiers are only of length 3 times
        if (identifier.length() < 3)
        {
            return null;
        }
        //Has idv22 been mapped to v23
        String id = ID3Frames.convertv22Tov23.get(identifier.substring(0, 3));
        if (id != null)
        {
            //has v2.3 been mapped to v2.4
            String v23id = ID3Frames.convertv23Tov24.get(id);
            if (v23id == null)
            {
                //if not it may be because v2.3 and and v2.4 are same so wont be
                //in mapping
                if (ID3v24Frames.getInstanceOf().getIdToValueMap().get(id) != null)
                {
                    return id;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return v23id;
            }
        }
        else
        {
            return null;
        }
    }


    public static String convertFrameID23To22(String identifier)
    {
        if (identifier.length() < 4)
        {
            return null;
        }

        //If it is a v23 identifier
        if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier))
        {
            //If only name has changed  v22 and modified in v23 return result of.
            return ID3Frames.convertv23Tov22.get(identifier.substring(0, 4));
        }
        return null;
    }


    public static String convertFrameID23To24(String identifier)
    {
        if (identifier.length() < 4)
        {
            return null;
        }

        //If it is a ID3v23 identifier
        if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier))
        {
            //If no change between ID3v23 and ID3v24 should be in ID3v24 list.
            if (ID3v24Frames.getInstanceOf().getIdToValueMap().containsKey(identifier))
            {
                return identifier;
            }
            //If only name has changed  ID3v23 and modified in ID3v24 return result of.
            else
            {
                return ID3Frames.convertv23Tov24.get(identifier.substring(0, 4));
            }
        }
        return null;
    }


    public static String forceFrameID22To23(String identifier)
    {
        return ID3Frames.forcev22Tov23.get(identifier);
    }


    public static String forceFrameID23To22(String identifier)
    {
        return ID3Frames.forcev23Tov22.get(identifier);
    }


    public static String forceFrameID23To24(String identifier)
    {
        return ID3Frames.forcev23Tov24.get(identifier);
    }


    public static String forceFrameID24To23(String identifier)
    {
        return ID3Frames.forcev24Tov23.get(identifier);
    }


    public static String convertFrameID24To23(String identifier)
    {
        String id;
        if (identifier.length() < 4)
        {
            return null;
        }
        id = ID3Frames.convertv24Tov23.get(identifier);
        if (id == null)
        {
            if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier))
            {
                id = identifier;
            }
        }
        return id;
    }


    public static Object copyObject(Object copyObject)
    {
        Constructor<?> constructor;
        Class<?>[] constructorParameterArray;
        Object[] parameterArray;
        if (copyObject == null)
        {
            return null;
        }
        try
        {
            constructorParameterArray = new Class[1];
            constructorParameterArray[0] = copyObject.getClass();
            constructor = copyObject.getClass().getConstructor(constructorParameterArray);
            parameterArray = new Object[1];
            parameterArray[0] = copyObject;
            return constructor.newInstance(parameterArray);
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException("NoSuchMethodException: Error finding constructor to create copy:"+copyObject.getClass().getName());
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException("IllegalAccessException: No access to run constructor to create copy"+copyObject.getClass().getName());
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException("InstantiationException: Unable to instantiate constructor to copy"+copyObject.getClass().getName());
        }
        catch (java.lang.reflect.InvocationTargetException ex)
        {
            if (ex.getCause() instanceof Error)
            {
                throw (Error) ex.getCause();
            }
            else if (ex.getCause() instanceof RuntimeException)
            {
                throw (RuntimeException) ex.getCause();
            }
            else
            {
                throw new IllegalArgumentException("InvocationTargetException: Unable to invoke constructor to create copy");
            }
        }
    }


    public static long findNumber(String str) throws TagException
    {
        return findNumber(str, 0);
    }


    public static long findNumber(String str, int offset) throws TagException
    {
        if (str == null)
        {
            throw new NullPointerException("String is null");
        }
        if ((offset < 0) || (offset >= str.length()))
        {
            throw new IndexOutOfBoundsException("Offset to image string is out of bounds: offset = " + offset + ", string.length()" + str.length());
        }
        int i;
        int j;
        long num;
        i = offset;
        while (i < str.length())
        {
            if (((str.charAt(i) >= '0') && (str.charAt(i) <= '9')) || (str.charAt(i) == '-'))
            {
                break;
            }
            i++;
        }
        j = i + 1;
        while (j < str.length())
        {
            if (((str.charAt(j) < '0') || (str.charAt(j) > '9')))
            {
                break;
            }
            j++;
        }
        if ((j <= str.length()) && (j > i))
        {
            num = Long.parseLong(str.substring(i, j));
        }
        else
        {
            throw new TagException("Unable to find integer in string: " + str);
        }
        return num;
    }


    static public String stripChar(String str, char ch)
    {
        if (str != null)
        {
            char[] buffer = new char[str.length()];
            int next = 0;
            for (int i = 0; i < str.length(); i++)
            {
                if (str.charAt(i) != ch)
                {
                    buffer[next++] = str.charAt(i);
                }
            }
            return new String(buffer, 0, next);
        }
        else
        {
            return null;
        }
    }


    public static String truncate(String str, int len)
    {
        if (str == null)
        {
            return null;
        }
        if (len < 0)
        {
            return null;
        }
        if (str.length() > len)
        {
            return str.substring(0, len);
        }
        else
        {
            return str;
        }
    }

}
