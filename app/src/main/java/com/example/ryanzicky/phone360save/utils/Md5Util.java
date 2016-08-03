package com.example.ryanzicky.phone360save.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ryanzicky on 2016/8/1.
 */

public class Md5Util {

    /**
     * 给指定字符串按照md5算法加密
     * @param psd 需要加密的密码 加盐处理
     * @return 处理后的密码
     */
    public static String encoder(String psd) {
        try {
            //加盐处理
            psd = psd + "mobilesave";
            //1.指定加密算法类型
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2.将需要加密的字符串中转换成byte类型的数组，然后进行随机哈希过程
            byte[] bs = digest.digest(psd.getBytes());
            //3.循环遍历bs，然后让其生成32位字符串，固定写法
            //4.拼接字符串过程
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bs){
                int i = b & 0xff;
                //int类型的i需要转换成16禁止字符
                String hexString = Integer.toHexString(i);
                if(hexString.length() < 2){
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
