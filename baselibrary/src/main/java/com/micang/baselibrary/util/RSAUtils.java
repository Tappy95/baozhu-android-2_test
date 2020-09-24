package com.micang.baselibrary.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RSAUtils {

    private static final String SIGN_TYPE_RSA = "RSA";

    private static final String SIGN_TYPE_RSA2 = "RSA2";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * RSA/RSA2 生成签名
     *
     */
    public static String rsaSign(String content,String privateKey,String signType,String charset){
        PrivateKey priKey = null;
        java.security.Signature signature = null;
        try{
            System.out.println("请求参数生成的字符串为:" + content);
            if (SIGN_TYPE_RSA.equals(signType)) {
                priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
                signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            } else if (SIGN_TYPE_RSA2.equals(signType)) {
                priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
                signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
            } else {
                throw new Exception("不是支持的签名类型 : : signType=" + signType);
            }
            signature.initSign(priKey);

            if (charset==null || charset.isEmpty()) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
    String sign=new String(Base64.encodeBase64(signed));
            System.out.println("请求生成的签名为:" + sign);
    return sign;
        }catch (Exception ex){
            return "";
        }


    }

    /**
     * 验签方法
     *
     *
     * @return
     */
    public static boolean rsaCheck(Map map, String sign) throws Exception {
        java.security.Signature signature = null;
        String signType = map.get("sign_type").toString();
        String privateKey = map.get("privateKey").toString();
        String charset = map.get("charset").toString();
        String content = map.get("content").toString();
        String publicKey = map.get("publicKey").toString();
        System.out.println(">>验证的签名为:" + sign);
        System.out.println(">>生成签名的参数为:" + content);
        PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
        if (SIGN_TYPE_RSA.equals(signType)) {
            signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        } else if (SIGN_TYPE_RSA2.equals(signType)) {
            signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        } else {
            throw new Exception("不是支持的签名类型 : signType=" + signType);
        }
        signature.initVerify(pubKey);

        if (charset==null || charset.isEmpty()) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || (algorithm==null || algorithm.isEmpty())) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 把参数合成成字符串
     *
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        // app_id,method,charset,sign_type,version,bill_type,timestamp,bill_date
        String[] sign_param = sortedParams.get("sign_param").split(",");// 生成签名所需的参数
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < sign_param.length; i++) {
            keys.add(sign_param[i]);
        }
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            /*if ("biz_content".equals(key)) {
                content.append(
                        (index == 0 ? "" : "&") + key + "={\"bill_date\":\"" + sortedParams.get("bill_date") + "\",")
                        .append("\"bill_type\":\"" + sortedParams.get("bill_type") + "\"}");
                index++;
            } else {*/
            String value = sortedParams.get(key);
            if ((key!=null&& !key.isEmpty()) && (value!=null&& !value.isEmpty())) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
//            }
        }
        return content.toString();
    }

    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();

        io(reader, writer, -1);
        return writer.toString();
    }

    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }

        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}
