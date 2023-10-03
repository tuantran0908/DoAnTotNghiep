package llq.fw.cm.enums;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class IBStatusEnumConverter implements AttributeConverter<IBStatusEnum, String>{

    @Override
    public String convertToDatabaseColumn(IBStatusEnum attribute) {
    	if (attribute == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
    
    @Override
    public IBStatusEnum convertToEntityAttribute(String dbData) {
    	if(dbData==null) {
    		return null;
    	}
    	IBStatusEnum statusEnum= Arrays.stream(IBStatusEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(dbData);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
    }
}