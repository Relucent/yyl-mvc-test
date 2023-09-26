package yyl.mvc.plugin.spring.convert;

import org.springframework.core.convert.converter.Converter;

import com.github.relucent.base.common.collection.Listx;
import com.github.relucent.base.common.json.JsonUtil;

public class ListxConverter implements Converter<String, Listx> {
    public Listx convert(String text) {
        return JsonUtil.toList(text);
    }
}
