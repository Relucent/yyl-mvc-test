package yyl.mvc.plugin.jackson.databind;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.relucent.base.common.time.DateUtil;

/**
 * 日期序列化
 */
public class DatePowerSerializer extends JsonSerializer<Date> {

    /**
     * Singleton instance to use.
     */
    public static final DatePowerSerializer INSTANCE = new DatePowerSerializer();

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String text = DateUtil.format(date);
        gen.writeString(text);
    }

}
