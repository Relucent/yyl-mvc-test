package yyl.mvc.plugin.security.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;

import com.github.relucent.base.common.codec.Utf8;

/**
 * 密码编码(加密)器实现类<br>
 * 它采用SHA-256算法，迭代1024次，使用一个密钥(site-wide secret)以及8位随机盐对原密码进行加密。 <br>
 * 随机盐确保相同的密码使用多次时，产生的哈希都不同； 对hash算法迭代执行1024次增强了安全性，使暴力破解变得更困难。 <br>
 * 算法来源 spring-security-core-3.1.2.RELEASE <br>
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
		byte[] digested = decode(encodedPassword);
		byte[] salt = EncodingUtils.subArray(digested, 0, this.saltGenerator.getKeyLength());
		return matches(digested, digest(rawPassword, salt));
	}

	private String encode(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digest(rawPassword, salt);
		return Hex.encodeHexString(digest);
	}

	private byte[] digest(CharSequence rawPassword, byte[] salt) {
		byte[] digest = this.digester.digest(EncodingUtils.concatenate(new byte[][] { salt, this.secret, Utf8.encode(rawPassword) }));
		return EncodingUtils.concatenate(new byte[][] { salt, digest });
	}

	private byte[] decode(CharSequence encodedPassword) {
		try {
			return Hex.decodeHex(encodedPassword.toString().toCharArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean matches(byte[] expected, byte[] actual) {
		if (expected.length != actual.length) {
			return false;
		}
		int result = 0;
		for (int i = 0; i < expected.length; ++i) {
			result |= expected[i] ^ actual[i];
		}
		return (result == 0);
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
