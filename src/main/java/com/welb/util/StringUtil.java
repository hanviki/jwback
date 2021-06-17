//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.welb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public StringUtil() {
    }

    public static boolean isNull(String checkStr) {
        return checkStr == null || checkStr.trim().length() == 0 || checkStr.trim().equalsIgnoreCase("null");
    }

    public static int parseInt(String intStr, int defaultInt) {
        try {
            return Integer.parseInt(intStr);
        } catch (Exception var3) {
            return defaultInt;
        }
    }

    public static int parseInt(String intStr) {
        return parseInt(intStr, 0);
    }

    public static boolean isInt(String str) {
        Pattern pattern = Pattern.compile("(0|[1-9][0-9]*)");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String encodeStr(String str, String charset) {
        if (isNull(str)) {
            return str;
        } else {
            try {
                return new String(str.getBytes(charset));
            } catch (Exception var3) {
                return str;
            }
        }
    }

    public static String trimStr(String str) {
        return str != null ? str.trim() : str;
    }

    public static String subBefore(String src, String term) {
        if (src != null && term != null) {
            int index = src.indexOf(term);
            return index >= 0 ? src.substring(0, index) : src;
        } else {
            return null;
        }
    }

    public static String subAfter(String src, String term) {
        if (src != null && term != null) {
            int index = src.indexOf(term);
            return index >= 0 ? src.substring(index + term.length()) : src;
        } else {
            return null;
        }
    }

    public static String subLastBefore(String src, String term) {
        if (src != null && term != null) {
            int index = src.lastIndexOf(term);
            return index >= 0 ? src.substring(0, index) : src;
        } else {
            return null;
        }
    }

    public static String subLastAfter(String src, String term) {
        if (src != null && term != null) {
            int index = src.lastIndexOf(term);
            return index >= 0 ? src.substring(index + term.length()) : src;
        } else {
            return null;
        }
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        } else {
            if (start < 0) {
                start += str.length();
            }

            if (start < 0) {
                start = 0;
            }

            return start > str.length() ? "" : str.substring(start);
        }
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        } else {
            if (end < 0) {
                end += str.length();
            }

            if (start < 0) {
                start += str.length();
            }

            if (end > str.length()) {
                end = str.length();
            }

            if (start > end) {
                return "";
            } else {
                if (start < 0) {
                    start = 0;
                }

                if (end < 0) {
                    end = 0;
                }

                return str.substring(start, end);
            }
        }
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() <= len ? str : str.substring(0, len);
        }
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() <= len ? str : str.substring(str.length() - len);
        }
    }

    public static boolean exist(String src, String term) {
        if (src != null && term != null) {
            int index = src.indexOf(term);
            return index >= 0;
        } else {
            return false;
        }
    }

    public static String encodeURL(String src) {
        String result = null;

        try {
            result = URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static String decodeURL(String src) throws UnsupportedEncodingException {
        return URLDecoder.decode(src, "UTF-8");
    }

    public static String format() {
        return format("yyyy-MM-dd HH:mm:ss");
    }

    public static String format(String pattern) {
        return format(pattern, new Date());
    }

    public static String format(String pattern, Date date) {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static Date parse(String pattern, String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(text);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return new Date();
        }
    }

    public static boolean rexp(String str, String rex) {
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    public static String join(Object[][] arr) {
        return join(arr, ",");
    }

    public static String join(Object[] arr, String spliter) {
        if (arr == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < arr.length; ++i) {
                if (i != 0) {
                    sb.append(spliter);
                }

                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }

    public static String join(Object[][] arr, String spliter) {
        if (arr == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < arr.length; ++i) {
                if (i != 0) {
                    sb.append(spliter);
                }

                sb.append(join(arr[i], spliter));
            }

            return sb.toString();
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static final String noNull(String string, String defaultString) {
        return isEmpty(string) ? defaultString : string;
    }

    public static final String noNull(String string) {
        return noNull(string, "");
    }

    public static String javaEncode(String txt) {
        if (txt != null && txt.length() != 0) {
            txt = replaceEx(txt, "\\", "\\\\");
            txt = replaceEx(txt, "\r\n", "\n");
            txt = replaceEx(txt, "\n", "\\n");
            txt = replaceEx(txt, "\"", "\\\"");
            txt = replaceEx(txt, "'", "\\'");
            return txt;
        } else {
            return txt;
        }
    }

    public static String javaDecode(String txt) {
        if (txt != null && txt.length() != 0) {
            txt = replaceEx(txt, "\\\\", "\\");
            txt = replaceEx(txt, "\\n", "\n");
            txt = replaceEx(txt, "\\r", "\r");
            txt = replaceEx(txt, "\\\"", "\"");
            txt = replaceEx(txt, "\\'", "'");
            return txt;
        } else {
            return txt;
        }
    }

    public static String replaceEx(String str, String subStr, String reStr) {
        if (str == null) {
            return null;
        } else if (subStr != null && !subStr.equals("") && subStr.length() <= str.length() && reStr != null) {
            StringBuffer sb = new StringBuffer();
            String tmp = str;
            boolean var5 = true;

            while(true) {
                int index = tmp.indexOf(subStr);
                if (index < 0) {
                    sb.append(tmp);
                    return sb.toString();
                }

                sb.append(tmp.substring(0, index));
                sb.append(reStr);
                tmp = tmp.substring(index + subStr.length());
            }
        } else {
            return str;
        }
    }

    public static String escape(String src) {
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length() * 6);

        for(int i = 0; i < src.length(); ++i) {
            char j = src.charAt(i);
            if (!Character.isDigit(j) && !Character.isLowerCase(j) && !Character.isUpperCase(j)) {
                if (j < 256) {
                    sb.append("%");
                    if (j < 16) {
                        sb.append("0");
                    }

                    sb.append(Integer.toString(j, 16));
                } else {
                    sb.append("%u");
                    sb.append(Integer.toString(j, 16));
                }
            } else {
                sb.append(j);
            }
        }

        return sb.toString();
    }

    public static String unescape(String src) {
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length());
        int lastPos = 0;
        boolean var3 = false;

        while(lastPos < src.length()) {
            int pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                char ch;
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char)Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    sb.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char)Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    sb.append(ch);
                    lastPos = pos + 3;
                }
            } else if (pos == -1) {
                sb.append(src.substring(lastPos));
                lastPos = src.length();
            } else {
                sb.append(src.substring(lastPos, pos));
                lastPos = pos;
            }
        }

        return sb.toString();
    }

    /**
     * 32位随机数
     * @return
     */
    public static String generatorId() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return id;
    }

    /**
     * 20位随机数
     * @return
     */
    public static String getGuid() {
        long now = System.currentTimeMillis();
        String info=now+"";
        int i = (int)(Math.random()*900000 + 1000000);
        String myStr = Integer.toString(i);

        return info+myStr;
    }

    /**
     * 生成六位随机数
     * @return
     */
    public static String generateRandomNum()
    {
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }

}
