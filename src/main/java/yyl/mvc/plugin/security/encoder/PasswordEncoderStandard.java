package yyl.mvc.plugin.security.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import com.github.relucent.base.common.codec.Utf8;

/**
 * <h3>标准密码编码器实现类</h3><br>
 * 它采用SHA-256算法，迭代1024次，使用一个密钥(site-wide secret)以及8位随机盐对原密码进行加密。 <br>
 * 随机盐确保相同的密码使用多次时，产生的哈希都不同； 对hash算法迭代执行1024次增强了安全性，使暴力破解变得更困难。 <br>
 * 实现细节：<br>
 * 使用 SHA-256 算法进行密码加密<br>
 * 迭代 1024 次 → 增加破解难度<br>
 * 随机 8 字节盐 → 防止彩虹表攻击<br>
 * 全局系统秘钥 → 增加额外安全层<br>
 * 虽然有多次迭代、盐和系统秘钥，但仍不足以抵御现代攻击（如 GPU 暴力破解）。仅用于遗留系统兼容，不再推荐用于新系统。<br>
 * 算法来源：spring-security-core-3.1.2.RELEASE <br>
 * 参考代码：org.springframework.security.crypto.password.StandardPasswordEncoder<br>
 * @author _YaoYiLang
 * @version 2017-01-01
 */
public class PasswordEncoderStandard implements PasswordEncoder {

    private final Digester digester;
    private final byte[] secret;
    private final BytesKeyGenerator saltGenerator;
    private static final int DEFAULT_ITERATIONS = 1024;

    public PasswordEncoderStandard() {
        this("");
    }

    public PasswordEncoderStandard(CharSequence secret) {
        this("SHA-256", secret);
    }

    private PasswordEncoderStandard(String algorithm, CharSequence secret) {
        this.digester = new Digester(algorithm, DEFAULT_ITERATIONS);
        this.secret = Utf8.encode(secret);
        this.saltGenerator = new BytesKeyGenerator();
    }

    public String encode(CharSequence rawPassword) {
        return encode(rawPassword, this.saltGenerator.generateKey());
    }

    @Override
    public boolean matches(CharSequence rawPassword, CharSequence encodedPassword) {
        byte[] digested = decodeHex(encodedPassword);
        byte[] salt = EncodingUtils.subArray(digested, 0, this.saltGenerator.getKeyLength());
        return EncodingUtils.isEqual(digested, digest(rawPassword, salt));
    }

    private String encode(CharSequence rawPassword, byte[] salt) {
        byte[] digest = digest(rawPassword, salt);
        return Hex.encodeHexString(digest);
    }

    private byte[] digest(CharSequence rawPassword, byte[] salt) {
        byte[] digest = this.digester
                .digest(EncodingUtils.concatenate(new byte[][] { salt, this.secret, Utf8.encode(rawPassword) }));
        return EncodingUtils.concatenate(new byte[][] { salt, digest });
    }

    private byte[] decodeHex(CharSequence encodedPassword) {
        try {
            char[] data = encodedPassword.toString().toCharArray();
            int length = encodedPassword.length();
            if ((length / 2) * 2 != length) {
                data = Arrays.copyOf(data, length - 1);
            }
            return Hex.decodeHex(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class EncodingUtils {
        public static byte[] concatenate(byte[][] arrays) {
            int length = 0;
            for (byte[] array : arrays) {
                length += array.length;
            }
            byte[] newArray = new byte[length];
            int destPos = 0;
            for (byte[] array : arrays) {
                System.arraycopy(array, 0, newArray, destPos, array.length);
                destPos += array.length;
            }
            return newArray;
        }

        public static byte[] subArray(byte[] array, int beginIndex, int endIndex) {
            int length = endIndex - beginIndex;
            byte[] subarray = new byte[length];
            System.arraycopy(array, beginIndex, subarray, 0, length);
            return subarray;
        }

        public static boolean isEqual(byte[] digesta, byte[] digestb) {
            if (digesta == digestb) {
                return true;
            }
            if (digesta == null || digestb == null || digesta.length != digestb.length) {
                return false;
            }
            int result = 0;
            // time-constant comparison
            for (int i = 0; i < digesta.length; i++) {
                result |= digesta[i] ^ digestb[i];
            }
            return result == 0;
        }
    }

    static class BytesKeyGenerator {
        private final SecureRandom random;
        private final int keyLength;
        private static final int DEFAULT_KEY_LENGTH = 8;

        public BytesKeyGenerator() {
            this(DEFAULT_KEY_LENGTH);
        }

        public BytesKeyGenerator(int keyLength) {
            this.random = new SecureRandom();
            this.keyLength = keyLength;
        }

        public int getKeyLength() {
            return this.keyLength;
        }

        public byte[] generateKey() {
            byte[] bytes = new byte[this.keyLength];
            this.random.nextBytes(bytes);
            return bytes;
        }
    }

    static class Digester {
        private final MessageDigest messageDigest;
        private final int iterations;

        public Digester(String algorithm, int iterations) {
            try {
                this.messageDigest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("No such hashing algorithm", e);
            }

            this.iterations = iterations;
        }

        public byte[] digest(byte[] value) {
            synchronized (this.messageDigest) {
                for (int i = 0; i < this.iterations; ++i) {
                    value = this.messageDigest.digest(value);
                }
                return value;
            }
        }
    }
}
