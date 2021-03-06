package yyl.mvc.plugin.spring.json;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import yyl.mvc.plugin.jackson.MyObjectMapper;

/**
 * Jackson_ObjectMapper的工厂类
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

	@Override
	public ObjectMapper getObject() throws Exception {
		return MyObjectMapper.INSTANCE;
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
