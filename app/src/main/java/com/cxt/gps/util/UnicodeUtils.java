package com.cxt.gps.util;


import android.text.TextUtils;

public class UnicodeUtils {


    /***
     * unicode 编码与解码
     * v\u003d0; cookie2\u003d161b41dbe306333ef031fccf315df69a;
     * 转成： v=0; cookie2=161b41dbe306333ef031fccf315df69a;
     * @param unicode
     * @return
     */
    public static String unicode2String(String unicode){
        if(TextUtils.isEmpty(unicode))return null;
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=unicode.indexOf("\\u", pos)) != -1){
            sb.append(unicode.substring(pos, i));
            if(i+5 < unicode.length()){
                pos = i+6;
                sb.append((char) Integer.parseInt(unicode.substring(i+2, i+6), 16));
            }
        }

        if (pos < unicode.length()){
            sb.append(unicode.substring(pos));
        }

        return sb.toString();
    }

    public static String string2Unicode(String string) {

        if(TextUtils.isEmpty(string))return null;
        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }
}
