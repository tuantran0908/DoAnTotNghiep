package llq.fw.cm.enums;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;

public class BillEnumQConverter implements Converter<String, BillEnum> {
	@Override
	public BillEnum convert(String source) {
		BillEnum statusEnum= Arrays.stream(BillEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(source);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
	}
	
	
}