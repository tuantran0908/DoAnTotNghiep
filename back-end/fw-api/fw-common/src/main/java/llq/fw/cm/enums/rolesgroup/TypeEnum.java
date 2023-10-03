package llq.fw.cm.enums.rolesgroup;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeEnum {
	USER(1), CHUCDANH(2);
/*
 * 1:USER|2:Chức danh
 * 
 */
	private Integer value;

	TypeEnum(Integer i) {
		this.value = i;
	}
	@JsonValue
	public Integer getValue() {
		return value;
	}

	@JsonCreator
	public static TypeEnum forValue(String v) {
		if(v==null) {
			return null;
		}
		TypeEnum statusEnum= Arrays.stream(TypeEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(v);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
	}

}
