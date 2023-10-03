package llq.fw.cm.enums.rolesgroup;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;

public class TypeEnumQConverter implements Converter<String, TypeEnum> {
	@Override
	public TypeEnum convert(String source) {
		TypeEnum statusEnum= Arrays.stream(TypeEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(source);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
	}
	
	
}