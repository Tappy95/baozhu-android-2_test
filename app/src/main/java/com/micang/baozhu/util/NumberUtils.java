package com.micang.baozhu.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/5/27 15:19
 * @describe describe
 */
public class NumberUtils {
    public static boolean checkMob(String mobile) {
        if (EmptyUtils.isEmpty(mobile)) {
            ToastUtils.show("请输入手机号码");
            return false;
        } else if (mobile.length() < 11) {
            ToastUtils.show("请输入11位手机号码");
            return false;
        } else {
            if (isPhoneNo(mobile)) {
                return true;
            } else {
                ToastUtils.show("请输入11位手机号码");
                return false;
            }
        }
    }

    /**
     * 手机号校验
     *
     * @param phoneNo
     * @return
     */
    public static boolean isPhoneNo(String phoneNo) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }
    /**
     * 字符串 千位符
     *
     * @param num
     * @return
     */
    public static String num2thousand(String num) {
        String numStr = "";
        if (num == null || num.trim().length() < 1) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }
}
