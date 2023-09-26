package yyl.mvc.plugin.spring.convert;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.github.relucent.base.common.convert.ConvertUtil;

public class DateConverter implements Converter<String, Date> {

    public Date convert(String text) {
        return ConvertUtil.toDate(text, null);
    }

}
