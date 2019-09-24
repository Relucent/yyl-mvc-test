package yyl.mvc.common.codec;

/**
 * 编码过程中出现故障时抛出。例如无效数据、无法计算校验和，字符超出预期范围。
 */
@SuppressWarnings("serial")
public class EncoderException extends RuntimeException {

    /**
     * 构造函数，将<code>null</code>作为其详细消息。原因未初始化，随后可以通过调用{@link #initCause}来初始化。
     */
    public EncoderException() {
        super();
    }

    /**
     * 构造函数
     * @param message 异常的详细信息， 可以使用{@link #getMessage()} 方法获取
     */
    public EncoderException(final String message) {
        super(message);
    }

    /**
     * 构造函数，指定的原因和的详细消息
     * @param message 异常的详细信息， 可以使用{@link #getMessage()} 方法获取
     * @param cause 原因异常，可以通过{@link #getCause()} 方法获取
     */
    public EncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数，指定的原因
     * @param cause 原因异常，可以通过{@link #getCause()} 方法获取
     */
    public EncoderException(final Throwable cause) {
        super(cause);
    }

}
