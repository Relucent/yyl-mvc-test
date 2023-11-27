package yyl.mvc.common.matcher;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.github.relucent.base.common.logging.Logger;

import yyl.mvc.common.annotations.PermitAll;
import yyl.mvc.common.util.PathMatchUtil;
import yyl.mvc.common.web.ControllerUtil;

/**
 * URL匹配工具类
 */
public class PermitUrlMatcher {

	// ===================================Methods=============================================
	/** 记录日志的对象. */
	private static final Logger LOGGER = Logger.getLogger(ControllerUtil.class);

	/** 不需要进行身份认证直接可以访问的URL */
	private String[] permitAllUrls = {};

	/** 需要进行身份认证才能访问的URL */
	private String[] authenticatedUrls = { "/**" };

	// =======================Constructors=============================================
	public PermitUrlMatcher(ApplicationContext applicationContext, PermitUrlMatcherOption option) {
		Set<String> permitAllUrlSet = new TreeSet<>();
		final Pattern pathVariablePattern = Pattern.compile("\\{(.*?)\\}");
		try {
			RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
			Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				RequestMappingInfo info = entry.getKey();
				HandlerMethod handler = entry.getValue();
				PermitAll ax = getAnnotation(handler, PermitAll.class);
				if (ax == null) {
					continue;
				}

				Set<String> patterns = null;
				if (patterns == null) {
					PathPatternsRequestCondition pprc = info.getPathPatternsCondition();
					if (pprc != null) {
						patterns = pprc.getPatternValues();
					}
				}
				if (patterns == null) {
					PatternsRequestCondition prc = info.getPatternsCondition();
					if (prc != null) {
						patterns = prc.getPatterns();
					}
				}
				if (patterns == null) {
					LOGGER.error("RequestMappingInfo[{}] patterns is null", info);
					continue;
				}
				for (String url : patterns) {
					String path = pathVariablePattern.matcher(url).replaceAll("*");
					permitAllUrlSet.add(path);
				}
			}
		} catch (NoClassDefFoundError e) {
			LOGGER.warn("Application not support mvc#RequestMappingHandlerMapping");
		}

		String[] permitAllUrls = option.getPermitAllUrls();
		String[] endpointUrls = option.getEndpointUrls();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("[@EndpointUrls=]");
			for (String url : option.getEndpointUrls()) {
				LOGGER.info(url);
			}
			LOGGER.info("[@PermitAllUrls=]");
			for (String url : permitAllUrls) {
				LOGGER.info(url);
			}
			for (String url : permitAllUrlSet) {
				LOGGER.info(url);
			}
		}
		CollectionUtils.addAll(permitAllUrlSet, permitAllUrls);
		CollectionUtils.addAll(permitAllUrlSet, endpointUrls);
		this.permitAllUrls = permitAllUrlSet.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
		this.authenticatedUrls = option.getAuthenticatedUrls();
	}

	// ==========================Methods=============================================
	/**
	 * 获得需要认证才能访问的URL地址
	 * @return 需要权限拦截的URL地址
	 */
	public String[] getAuthenticatedUrls() {
		String[] urls = this.authenticatedUrls;
		return Arrays.copyOf(urls, urls.length);
	}

	/**
	 * 获得不需要进行身份认证直接可以访问的URL地址
	 * @return 不需要进行身份认证直接可以访问的URL地址
	 */
	public String[] getPermitAllUrls() {
		String[] urls = this.permitAllUrls;
		return Arrays.copyOf(urls, urls.length);
	}

	/**
	 * 判断路径是否需要身份认证
	 * @param path 比较的路径
	 * @return 路径是否需要身份认证
	 */
	public boolean matchAuthenticatedUrl(String path) {
		return PathMatchUtil.matchAny(authenticatedUrls, path);
	}

	/**
	 * 判断路径是否不需要身份认证
	 * @param path 比较的路径
	 * @return 路径是否不需要身份认证
	 */
	public boolean matchPermitUrl(String path) {
		return PathMatchUtil.matchAny(permitAllUrls, path);
	}

	/**
	 * 获得处理方法指定注解（包括所属类的注解）
	 * @param 处理程序方法 {@link HandlerMethod}
	 * @param annotationType 注解类型
	 * @return 指定的注解
	 */
	private static <A extends Annotation> A getAnnotation(HandlerMethod handler, Class<A> annotationType) {
		A annotation = handler.getMethodAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		Class<?> beanType = handler.getBeanType();
		annotation = AnnotationUtils.findAnnotation(beanType, annotationType);
		return annotation;
	}

	// =======================InnerClass===============================================
	public static class PermitUrlMatcherOption {

		/** 不需要进行身份认证直接可以访问的URL */
		private String[] permitAllUrls = {};

		/** 需要进行身份认证才能访问的URL */
		private String[] authenticatedUrls = { "/rest/**" };

		/** 端点URL（不需要进行身份认证） */
		private String[] endpointUrls = {
				// 监控
				"/actuator/**", "/actuator/**/**",
				// 网关 Swagger
				"/doc.html", "/v2/api-docs/**", "/v3/api-docs/**", //
				"/swagger-resources/**", "/webjars/**", "/swagger/**", "/swagger-ui/**", //
				// 网站图标
				"/favicon.ico", //
				// 静态资源
				"/", "/index.html", "/__/**", "/views/**"//
		};

		public String[] getPermitAllUrls() {
			return permitAllUrls;
		}

		public void setPermitAllUrls(String[] permitAllUrls) {
			this.permitAllUrls = permitAllUrls;
		}

		public String[] getAuthenticatedUrls() {
			return authenticatedUrls;
		}

		public void setAuthenticatedUrls(String[] authenticatedUrls) {
			this.authenticatedUrls = authenticatedUrls;
		}

		public String[] getEndpointUrls() {
			return endpointUrls;
		}

		public void setEndpointUrls(String[] endpointUrls) {
			this.endpointUrls = endpointUrls;
		}
	}
}
