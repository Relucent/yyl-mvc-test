package yyl.mvc.plugin.security.encoder;

/**
 * 密码编码(加密)器实现类(不进行加密操作)
 * @author _YaoYiLang
 * @version 2017-01-01
 */
public class PasswordEncoderNoOp implements PasswordEncoder {

	/**
	 * 编码<br/>
	 * @param password 原始密码
	 * @return 加密后的密码
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

    /**
     * 验证从存储中获取的编码密码是否与提交的原始密码匹配
     * @param rawPassword     要进行编码和匹配的原始密码
     * @param encodedPassword 来自存储的已编码密码，用于比较
     * @return 如果原始密码在编码后与来自的编码密码匹配则返回true，否则返回false
     */
	@Override
	public boolean matches(CharSequence rawPassword, CharSequence encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}
}
