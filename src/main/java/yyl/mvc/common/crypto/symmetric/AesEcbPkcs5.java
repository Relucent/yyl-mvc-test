package yyl.mvc.common.crypto.symmetric;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES工具类，使用 JDK 默认的 AES/ECB/PKCS5Padding 算法实现
 */
public class AesEcbPkcs5 {

    // ==============================Fields===========================================
    /** {AES} */
    private static final String ALGORITHM = "AES";
    /** {AES/ECB/PKCS5Padding} */
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    /** 加密器线程持有 */
    private static final ThreadLocal<Cipher> AES_CIPHER_HOLDER = ThreadLocal.withInitial(() -> getCipher());
    /** 默认KEY长度 */
    private static final int KEY_SIZE = 128;

    // =================================Constructors===========================================
    protected AesEcbPkcs5() {
    }

    // ==============================Methods==========================================
    /**
     * 加密数据
     * @param plaintext 明文
     * @param secretkey 密钥
     * @return 密文
     */
    public static byte[] encrypt(byte[] plaintext, SecretKey secretkey) {
        try {
            Cipher cipher = AES_CIPHER_HOLDER.get();
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密数据
     * @param ciphertext 密文
     * @param secretkey 密钥
     * @return 明文
     */
    public static byte[] decrypt(byte[] ciphertext, SecretKey secretkey) {
        try {
            if (ciphertext == null) {
                return null;
            }
            Cipher cipher = AES_CIPHER_HOLDER.get();
            cipher.init(Cipher.DECRYPT_MODE, secretkey);
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得秘钥 {@link SecretKey}
     * @param data 密钥数据内容
     * @return 密钥
     */
    public static SecretKey getSecretKey(byte[] data) {
        int length = data.length;
        if (length <= 16) {
            length = 16;// (128/8)
        } else if (length <= 32) {
            length = 32;// (256/8)
        } else {
            length = 64;// (512/8)
        }
        byte[] key = new byte[length];
        System.arraycopy(data, 0, key, 0, Math.min(key.length, data.length));
        return new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * 生成随机秘钥 {@link SecretKey}
     * @param algorithm 对称加密算法
     * @return 密钥(128位)
     */
    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = getKeyGenerator();
            keyGenerator.init(KEY_SIZE);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            return null;
        }
    }

    // ==============================ToolMethods======================================
    /**
     * 获得加密解密功能类{@link Cipher}
     * @return 加密解密功能类
     */
    private static Cipher getCipher() {
        try {
            return Cipher.getInstance(TRANSFORMATION);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取秘密（对称）密钥生成器{@link KeyGenerator}
     * @param algorithm 对称加密算法
     * @return 秘密（对称）密钥生成器
     */
    private static KeyGenerator getKeyGenerator() {
        try {
            return KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
