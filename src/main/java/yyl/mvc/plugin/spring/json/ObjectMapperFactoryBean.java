package yyl.mvc.plugin.spring.json;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.relucent.base.plugin.jackson.JacksonHandler;

/**
 * Jackson_ObjectMapper的工厂类
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

	@Override
	public ObjectMapper getObject() throws Exception {
		return JacksonHandler.getDefaultObjectMapper();
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
