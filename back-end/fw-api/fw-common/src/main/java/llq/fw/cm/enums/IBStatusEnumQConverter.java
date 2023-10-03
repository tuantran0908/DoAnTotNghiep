package llq.fw.cm.enums;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;

public class IBStatusEnumQConverter implements Converter<String, IBStatusEnum> {
	@Override
	public IBStatusEnum convert(String source) {
		IBStatusEnum statusEnum= Arrays.stream(IBStatusEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(source);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
	}
	
	
}