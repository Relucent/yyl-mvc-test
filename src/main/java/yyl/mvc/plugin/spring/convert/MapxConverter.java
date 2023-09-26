package yyl.mvc.plugin.spring.convert;

import org.springframework.core.convert.converter.Converter;

import com.github.relucent.base.common.collection.Mapx;
import com.github.relucent.base.common.json.JsonUtil;

public class MapxConverter implements Converter<String, Mapx> {
    public Mapx convert(String text) {
        return JsonUtil.toMap(text);
    }
}
