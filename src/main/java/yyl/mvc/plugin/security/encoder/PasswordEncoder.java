package yyl.mvc.plugin.security.encoder;

/**
 * 密码编码(加密)器
 * @author _YaoYiLang
 * @version 2017-01-01
 */
public interface PasswordEncoder {

    /**
     * 对原始密码进行编码
     * @param rawPassword 原始密码
     * @return 编码后的字符串
     */
    String encode(CharSequence rawPassword);

    /**
     * 验证从存储中获取的编码密码是否与提交的原始密码匹配
     * @param rawPassword     要进行编码和匹配的原始密码
     * @param encodedPassword 来自存储的已编码密码，用于比较
     * @return 如果原始密码在编码后与来自的编码密码匹配则返回true，否则返回false
     */
    boolean matches(CharSequence rawPassword, CharSequence encodedPassword);
}
