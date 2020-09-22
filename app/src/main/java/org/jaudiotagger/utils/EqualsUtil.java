package org.jaudiotagger.utils;



public final class EqualsUtil
{

    static public boolean areEqual(boolean aThis, boolean aThat)
    {
        //System.out.println("boolean");
        return aThis == aThat;
    }

    static public boolean areEqual(char aThis, char aThat)
    {
        //System.out.println("char");
        return aThis == aThat;
    }

    static public boolean areEqual(long aThis, long aThat)
    {

        //System.out.println("long");
        return aThis == aThat;
    }

    static public boolean areEqual(float aThis, float aThat)
    {
        //System.out.println("float");
        return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
    }

    static public boolean areEqual(double aThis, double aThat)
    {
        //System.out.println("double");
        return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
    }


    static public boolean areEqual(Object aThis, Object aThat)
    {
        //System.out.println("Object");
        return aThis == null ? aThat == null : aThis.equals(aThat);
    }
}


