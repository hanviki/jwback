package com.welb.util;

/**
 * @author luoyaozu
 * @title: CheckPassword
 * @projectName xh-360appraisal-interface
 * @description: 密码校验
 * @date 2019/9/2922:52
 */
public class CheckPassword {
    /**
     * 包含大小写字母及数字且在8-16位
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isUpperCase = false;//定义一个boolean值，用来表示是否包含大写字母
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含小写字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isUpperCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isUpperCase = true;
            }
            else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,16}$";
        boolean isRight = isDigit && isLowerCase&&isUpperCase && str.matches(regex);
        return isRight;
    }
}
