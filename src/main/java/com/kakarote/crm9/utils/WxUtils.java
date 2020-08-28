package com.kakarote.crm9.utils;


import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author clunt
 * <p>微信手机号解密</p>
 * */
public class WxUtils {
    /**
     *    * 微信 数据解密<br/>
     *    * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充<br/>
     *    * 对称解密的目标密文:encrypted=Base64_Decode(encryptData)<br/>
     *    * 对称解密秘钥:key = Base64_Decode(session_key),aeskey是16字节<br/>
     *    * 对称解密算法初始向量:iv = Base64_Decode(iv),同样是16字节<br/>
     *    *
     *    * @param encrypted 目标密文
     *    * @param session_key 会话ID
     *    * @param iv 加密算法的初始向量
     *
     */
    public static JSONObject wxDecrypt(String encrypted, String session_key, String iv) {
// 被加密的数据
        Base64.Decoder decoder = Base64.getDecoder();

        byte[] dataByte = decoder.decode(encrypted);
        // 加密秘钥
        byte[] keyByte = decoder.decode(session_key);
        // 偏移量
        byte[] ivByte = decoder.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
