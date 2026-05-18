package yyl.mvc.plugin.security.encoder;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 密码编码(加密)器实现类(MD5实现)
 * @author _YaoYiLang
 * @version 2017-01-01
 */
//测试密码：
//admin    : 21232f297a57a5a743894a0e4a801fc3
//password : 5f4dcc3b5aa765d61d8327deb882cf99
//111111   : 96e79218965eb72c92a549dd5a330112
public class PasswordEncoderMd5 implements PasswordEncoder {

	/**
	 * 生成散列密码(目前采用MD5加密,密码加密不可逆)<br/>
	 * @param password 原始密码
	 * @return 加密后的密码
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		return DigestUtils.md5Hex(rawPassword.toString());
	}

    /**
     * 验证从存储中获取的编码密码是否与提交的原始密码匹配
     * @param rawPassword     要进行编码和匹配的原始密码
     * @param encodedPassword 来自存储的已编码密码，用于比较
     * @return 如果原始密码在编码后与来自的编码密码匹配则返回true，否则返回false
     */
	@Override
	public boolean matches(CharSequence rawPassword, CharSequence encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
