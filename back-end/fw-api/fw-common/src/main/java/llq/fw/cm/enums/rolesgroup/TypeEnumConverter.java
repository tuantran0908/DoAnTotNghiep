package llq.fw.cm.enums.rolesgroup;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TypeEnumConverter implements AttributeConverter<TypeEnum, String>{

    @Override
    public String convertToDatabaseColumn(TypeEnum attribute) {
    	if (attribute == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
    
    @Override
    public TypeEnum convertToEntityAttribute(String dbData) {
    	if(dbData==null) {
    		return null;
    	}
    	TypeEnum statusEnum= Arrays.stream(TypeEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(dbData);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
    }
}
