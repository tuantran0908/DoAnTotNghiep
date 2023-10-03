package llq.fw.cm.enums;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BillEnumConverter implements AttributeConverter<BillEnum, String>{

    @Override
    public String convertToDatabaseColumn(BillEnum attribute) {
    	if (attribute == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
    
    @Override
    public BillEnum convertToEntityAttribute(String dbData) {
    	if(dbData==null) {
    		return null;
    	}
    	BillEnum statusEnum= Arrays.stream(BillEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(dbData);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
    }
}