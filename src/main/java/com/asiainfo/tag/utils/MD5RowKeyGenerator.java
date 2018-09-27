package com.asiainfo.tag.utils;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/9
 * Time: 上午11:13
 * Description: No Description
 */
public class MD5RowKeyGenerator {

    public static synchronized Object generate(String oriRowKey) {
        return generatePrefix(oriRowKey) + oriRowKey;
    }

    public static synchronized String getMD5(String oriRowKey) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = oriRowKey.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized Object generatePrefix(String oriRowKey) {
        String result = getMD5(oriRowKey);
        return result.substring(1, 2) + result.substring(3, 4) + result.substring(5, 6);
    }

    public static void main(String[] args) {
        System.out.println(MD5RowKeyGenerator.getMD5("13477343118"));
        System.out.println(MD5RowKeyGenerator.generatePrefix("13477343118"));
    }
}
