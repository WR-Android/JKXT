package wang.com.jkxttest;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016-12-23.
 */
public class Md5
{
    public static String md5(String string, String slat)
    {
       /* if (TextUtils.isEmpty(string))
        {
            return "";
        }*/
        if (string.equals(null))
        {
            return "";
        }
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            String result = "";
            for (byte b : bytes)
            {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1)
                {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String md5(String m_szLongID)
    {
        if (TextUtils.isEmpty(m_szLongID))
        {
            return "";
        }
        MessageDigest m = null;
        try
        {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
// get md5 bytes
        byte p_md5Data[] = m.digest();
// create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++)
        {
            int b = (0xFF & p_md5Data[i]);
// if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
// add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }

    public static void main(String[] args)  {
        md5("B6D3C573", DataInfo.key);
        System.out.println( md5("B6D3C573",DataInfo.key));
    }
}
