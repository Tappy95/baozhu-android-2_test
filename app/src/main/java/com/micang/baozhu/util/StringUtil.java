package com.micang.baozhu.util;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;


import com.micang.baozhu.AppContext;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {
    public static final String LARGE_SEPARATOR = ";";
    public static final String SMALL_SEPARATOR = ",";
    public static final String PLUS = "+";

    public static final int COMMON_ERROR = -1;
    /**
     * 随机字符串母串
     */
    public static final String RANDOM_RANGE = "abcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * 只带正负号和数字的整数正则表达式
     */
    public static final String INTEGER_REG = "[-+]?\\d+";

    private StringUtil() {

    }
    public static String convertFormatNum(String var0) {
        String var1 = "";
        if (TextUtils.isEmpty(var0)) {
            var1 = "0";
        } else {
            String[] var2 = var0.split(",");
            if (var2 == null || var2.length <= 0) {
                return "0";
            }

            for(int var3 = 0; var3 < var2.length; ++var3) {
                var1 = var1 + var2[var3];
            }
        }

        return var1;
    }
    public static String formatNum(long var0) {
        NumberFormat var2 = NumberFormat.getInstance();
        var2.setMinimumFractionDigits(0);
        var2.setMaximumFractionDigits(0);
        var2.setMaximumIntegerDigits(18);
        var2.setMinimumIntegerDigits(0);
        return var2.format(var0);
    }
    public static String formatFolat(float var0) {
        String var1 = (new DecimalFormat("0.00")).format((double)var0);
        if (TextUtils.isEmpty(var1)) {
            var1 = "0";
        }

        return var1;
    }
    /**
     * 作者：s00219869 描述：分隔字符串
     *
     * @param sign   ：要查的字符串
     * @param source ：源字符串
     * @return 返回分隔后的字符串向量
     */
    public static List<String> splitString(String sign, String source) {
        List<String> result = new ArrayList<String>();

        if (isStringEmpty(source)) {
            return result;
        }

        int i = 0;
        int j;
        while (i <= source.length()) {
            j = source.indexOf(sign, i);
            // 不包含分割字段
            if (j < 0) {
                j = source.length();
            }
            // 长度大于1的子字符串
            if (j > i) {
                result.add(source.substring(i, j));
            }
            i = j + 1;
        }

        return result;
    }

    /**
     * 方法名称：parserReturnCodeToInt 作者：wangjian 方法描述：将错误码字符串转化为INT 输入参数：@param
     * code 输入参数：@return 返回类型：int： 备注：
     */
    public static int parserReturnCodeToInt(String code) {
        if (isStringEmpty(code)) {
            return -1;
        }
        if (!code.matches(INTEGER_REG)) {
            return -1;
        }

        return Integer.parseInt(code);
    }

    public static float getDecimalZero(float number) {
//       return (float)(Math.round(number*100)/100);
        int scale = 0;//设置位数
        int roundingMode = 1;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((float) number);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    public static float getDecimalPrice(float number) {
//       return (float)(Math.round(number*100)/100);
        int scale = 2;//设置位数
        int roundingMode = 1;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((float) number);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    public static float getDecimalOne(float number) {
//        return (float)(Math.round(number*100)/10);
        int scale = 1;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((float) number);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    /**
     * 方法名称：isStringEmpty 作者：wujingdong 方法描述：判断字符串是否为空（null or ""） 输入参数：@param
     * source 输入参数：@return 返回类型：boolean 备注：
     */
    public static boolean isStringEmpty(String source) {
        return null == source || "".equals(source.trim()) || "null".equals(source);
    }

    public static boolean isNotEmpty(String source) {
        return !isStringEmpty(source);
    }

    public static boolean contains(String whole, String part) {
        if (StringUtil.isStringEmpty(whole) || StringUtil.isStringEmpty(part)) {
            return false;
        }

        return whole.toLowerCase(Locale.getDefault())
                .contains(part.toLowerCase(Locale.getDefault()));
    }

    /**
     * 清除所有的非数字
     *
     * @param number
     * @return
     */
    public static String replaceAllSymbol(String number) {
        if (null == number) {
            return null;
        }

        String regx = "\\D";

        return number.replaceAll(regx, "");
    }

    /**
     * 方法名称：stringToInt 作者：LuoTianjia 方法描述： String 转换为 int 输入参数:@param str 输入参数:@return
     * 返回类型：int 异常情况返回 -1 备注：
     */
    public static int stringToInt(String str) {
        return stringToInt(str, COMMON_ERROR);
    }

    public static int stringToInt(String str, int defaultValue) {
        if (isStringEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long stringToLong(String str, long defalut) {
        if (isStringEmpty(str)) {
            return defalut;
        }

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defalut;
        }
    }

    public static float stringToFloat(String str, float defaultValue) {
        if (isStringEmpty(str)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 删除指定位置的字符
     *
     * @param source 原字符串
     * @param pos    删除位置
     * @param c      字符
     * @return 删除后的字符串
     */
    public static String remove(String source, int pos, char c) {
        String result = source;

        if ((source == null) || "".equals(source)) {
            return "";
        }

        if ((pos < 0) || (pos >= source.length())) {
            return result;
        }

        if (c == source.charAt(pos)) {
            result = source.substring(pos + 1);
        }

        return result;
    }

    /**
     * 移除空行
     *
     * @param source
     * @return
     */
    public static String removeBlankLine(String source) {
        if (source == null) {
            return null;
        }

        source = source.trim();
        return source.replaceAll("(?m)^\\s+", "");
    }

    /**
     * 方法名称：fintElement 作者：wangjian 方法描述：查找xml中的字段 输入参数：@param source
     * 输入参数：@param startTag 输入参数：@param endTag 输入参数：@return 返回类型：String： 备注：
     */
    public static String findStringElement(String source, String startTag, String endTag) {
        return findStringElement(source, startTag, endTag, null);
    }

    /**
     * 从字符串中查找第一个子串
     *
     * @param source       原字符串
     * @param startTag     开始子串
     * @param endTag       结束子串
     * @param defaultValue 默认返回值
     * @return String
     */
    public static String findStringElement(String source, String startTag, String endTag,
                                           String defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        int i = source.indexOf(startTag);
        int j = source.indexOf(endTag, i);
        if ((i != -1) && (j != -1) && j > i) {
            return source.substring(i + startTag.length(), j);
        }
        return defaultValue;
    }

    /**
     * Function: 从字符串中查找最后一个子串
     */
    public static String findLastStringElement(String source, String startTag, String endTag,
                                               String defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        int i = source.lastIndexOf(startTag);
        int j = source.indexOf(endTag, i);
        if ((i != -1) && (j != -1) && j > i) {
            return source.substring(i + startTag.length(), j);
        }
        return defaultValue;
    }

    public static int findIntElement(String source, String startTag, String endTag,
                                     int defaultValue) {
        String temp = findStringElement(source, startTag, endTag);
        return stringToInt(temp, defaultValue);
    }

    public static float findFloatElement(String source, String startTag, String endTag,
                                         float defaultValue) {
        String temp = findStringElement(source, startTag, endTag);
        return stringToFloat(temp, defaultValue);
    }

    /**
     * 随机获取一个字符串
     *
     * @param length 指定长度
     * @return String 随机字符串
     */
    public static String getRandomString(int length) {
        if (length == 0) {
            length = 10;
        }
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(RANDOM_RANGE.length());
            sb.append(RANDOM_RANGE.charAt(number));
        }
        return sb.toString();
    }

    public static String filterLog(String s) {
        if (StringUtil.isNotEmpty(s)) {
            return String.valueOf(s.charAt(s.length() - 1));
        }

        return null;
    }

    /**
     * 解析IP地址
     *
     * @param address IP地址字符串
     * @return String[]
     */
    public static String[] parserIPAddress(String address) {
        InetAddress[] servers = null;
        try {
            servers = InetAddress.getAllByName(address);
        } catch (Exception e) {
        }
        if (servers != null) {
            String[] ips = new String[servers.length];
            for (int i = 0; i < servers.length; i++) {
                ips[i] = servers[i].getHostAddress();
            }
            return ips;
        } else {
            return new String[]{address};
        }
    }

    /**
     * 判断字符串是否是中文
     * <p>
     * [\u4e00-\u9fa5] //匹配中文字符
     * <p>
     * ^[1-9]\d*$    //匹配正整数
     * ^[A-Za-z]+$   //匹配由26个英文字母组成的字符串
     * ^[A-Z]+$      //匹配由26个英文字母的大写组成的字符串
     * ^[a-z]+$      //匹配由26个英文字母的小写组成的字符串
     * <p>
     * ^[A-Za-z0-9]+$ //匹配由数字和26个英文字母组成的字符串
     *
     * @param str
     * @return
     */
    public static boolean isAllChinese(String str) {
        return str.matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 判断char是否是中文
     *
     * @param c char
     * @return true：中文；false：非中文
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 判断字符串是否以中文开头
     *
     * @param str 字符串
     * @return true;false
     */
    public static boolean startWithChinese(String str) {
        if (isStringEmpty(str)) {
            return false;
        }
        char first = str.toCharArray()[0];
        return isChinese(first);
    }

    /**
     * 获取字符串索引字母
     *
     * @param str 字符串
     * @return 大写英文字母，其余为#
     */
    public static String getIndex(String str) {
        if (StringUtil.isStringEmpty(str)) {
            return "#";
        }
        String index = String.valueOf(str.charAt(0)).toUpperCase(Locale.getDefault());
        if (index.matches("[A-Z]")) {
            return index;
        }
        return "#";
    }

    public static String getFormatDate(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    public static <T> String listToString(List<T> list) {
        if (list == null) {
            return "{null}";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (T o : list) {
            sb.append("[").append(o).append("],");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("}");
        return sb.toString();
    }

    public static boolean compareVersion(String oldVersion, String newVersion) {
        boolean flag = false;
        String startips[] = oldVersion.split("\\.");
        String endIps[] = newVersion.split("\\.");
        for (int i = 0; i < startips.length; i++) {
            if (Integer.parseInt(endIps[i]) > Integer.parseInt(startips[i])) {
                flag = true;
                break;
            } else {
                if (Integer.parseInt(endIps[i]) == Integer.parseInt(startips[i])) {
                    continue;
                } else {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 判断是否邮箱正则表达
     *
     * @return 号码
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否全数字
     *
     * @param str 字符串
     * @return 号码
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 手机号码格式匹配
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 含数字和字母的密码验证
     *
     * @param psw
     * @return 是否符合强度测试 false：密码强度不够
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean checkPwdFormat(String psw) {
        if (psw == null || psw.isEmpty()) {
            return false;
        }
        // 密码验证的正则表达式:由数字和字母组成，并且要同时含有数字和字母，且长度要在8-16位之间。
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(psw);
        return m.matches();
    }

    public static String getHexString(byte[] rawValue) {
        if (rawValue != null && rawValue.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(rawValue.length);
            for (byte byteChar : rawValue) {
                stringBuilder.append(String.format("%02X", byteChar));
            }
            return "0x" + stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static String hexString2binaryString(byte[] bs) {
        StringBuffer bString = new StringBuffer();
        String ZERO = "00000000";
        for (int i = 0; i < bs.length; i++) {
            String s = Integer.toBinaryString(bs[i]);
            if (s.length() > 8) {
                s = s.substring(s.length() - 8);
            } else if (s.length() < 8) {
                s = ZERO.substring(s.length()) + s;
            }
            bString.append(s);
        }

        if (bString != null) {
            return bString.toString();
        } else {
            return "";
        }
    }

    public static byte[] parseHexStringToBytes(final String hex) {
        String tmp = null;
        if (hex.startsWith("0x")) {
            tmp = hex.substring(2).replaceAll("[^[0-9][a-f]]", "");
        } else {
            tmp = hex;
        }
        byte[] bytes = new byte[tmp.length() / 2]; // 字符串中的每一个字母都是一个字节

        String part = "";

        for (int i = 0; i < bytes.length; ++i) {
            part = "0x" + tmp.substring(i * 2, i * 2 + 2);
            bytes[i] = Long.decode(part).byteValue();
        }

        return bytes;
    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String format2Num(double num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    /**
     * 保留1位小数
     *
     * @param num
     * @return
     */
    public static String format1Num(double num) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(num);
    }

    /**
     * 判断手机号码 长度11 第一位必须为1 第二位不能为 1 2
     *
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (!phone.startsWith("1") || phone.length() != 11) {
            ToastUtils.show("请输入正确的手机号码");
            return false;
        }
        String Num2 = phone.substring(1, 2);
        if (Num2.equals("2") || Num2.equals("1") || Num2.equals("0")) {
            ToastUtils.show("请输入正确的手机号码");
            return false;
        }
        return true;
    }


    /**
     * 是否含有emoji图标
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    /**
     * 根据资源目录Color获取16进制值
     *
     * @param resid
     * @return
     */
    public static String getHexColorStr(int resid) {
        StringBuffer stringBuffer = new StringBuffer();
        int color = AppContext.getInstance().getResources().getColor(resid);

        stringBuffer.append("#");
//        stringBuffer.append(Integer.toHexString(Color.alpha(color)));
        stringBuffer.append(Integer.toHexString(Color.red(color)));
        stringBuffer.append(Integer.toHexString(Color.green(color)));
        stringBuffer.append(Integer.toHexString(Color.blue(color)));
        return stringBuffer.toString();
    }

    public static boolean isHttpUrl(String advertImageUrl) {
        if (EmptyUtils.isNotEmpty(advertImageUrl) && advertImageUrl.startsWith("http")) {
            return true;
        }
        return false;

    }
}
