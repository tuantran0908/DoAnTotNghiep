package llq.fw.cm.common;

import java.util.List;
import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import llq.fw.cm.enums.IBStatusEnumQConverter;
import llq.fw.cm.enums.rolesgroup.TypeEnumQConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper.registerModule(new Hibernate5Module());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.setTimeZone(TimeZone.getDefault());
                // replace Hibernate4Module() with the proper class for your hibernate version.
            }
        }
	}
	@Override
	public void addFormatters (FormatterRegistry registry) {
	    registry.addConverter(new IBStatusEnumQConverter());
	    registry.addConverter(new TypeEnumQConverter());
	    
	    registry.addConverter(new IBStatusEnumQConverter());
	    
	}
}