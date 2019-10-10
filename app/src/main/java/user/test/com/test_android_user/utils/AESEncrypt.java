package user.test.com.test_android_user.utils;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;


public class AESEncrypt {
    private static final String TAG = "AESEncrypt";
    private static final String algorithmStr = "AES";
    public static final String DEFAULT_SECRET = "Dzcx96822";
    static SecretKey desKey =null;
    /**
     * AES加密字符串base64
     *
     * @param content  需要被加密的字符串
     * @param password 密码
     * @return 密文
     */
    public static String encrypt(String content, String password) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(password)) {
            return null;
        }
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance(algorithmStr);
            // 利用用户密码作为随机数初始化出
            kgen.init(128, new SecureRandom(password.getBytes()));
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            // 根据用户密码，生成一个密钥
            desKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            byte[] enCodeFormat = desKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.encodeToString(result,Base64.NO_WRAP);

        } catch (Exception e) {
            Log.e(TAG, "encrypt异常: " + e);
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param base64content AES加密过过的内容
     * @param password      密码
     * @return 明文
     */
    public static String decrypt(String base64content, String password) {
        if (StringUtils.isBlank(base64content) || StringUtils.isBlank(password)) {
            return null;
        }
        byte[] content = Base64.decode(base64content,Base64.NO_WRAP);
        try {
//            KeyGenerator kgen = KeyGenerator.getInstance(algorithmStr);// 创建AES的Key生产者
//            kgen.init(128, new SecureRandom(password.getBytes()));
//            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = desKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return new String(result,"utf-8"); // 明文
        } catch (Exception e) {
            Log.e(TAG, "encrypt异常: " + e);
        }
        return null;
    }


    public static void main() {
        String content = "中华人民共和国";
        String password = "中华人民共产党";
        Log.i(TAG, "加密之前：" + content);
        // 加密
        String str = encrypt(content, password);
        Log.i(TAG, "加密后的内容：" + str);
        // 解密
        str = decrypt(str, password);
        Log.i(TAG, "解密后的内容：" + str);
    }
}
