package yyl.mvc.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>安全许可标记</h3><br>
 * 用于标记内部请求接口： 通过网关过滤和权限控制，允许内部系统之间调用无需鉴权，同时对浏览器直接访问做限制，避免接口对外暴露的安全问题。
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {
}
