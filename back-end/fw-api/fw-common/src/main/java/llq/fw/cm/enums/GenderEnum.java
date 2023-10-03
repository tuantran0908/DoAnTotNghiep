package llq.fw.cm.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderEnum {
	Male(1), Female(2);
	private Integer value;

	GenderEnum(Integer i) {
		this.value = i;
	}

	@JsonValue
	public Integer getValue() {
		return value;
	}

	@JsonCreator
	public static GenderEnum forValue(String v) {
		if (v == null) {
			return null;
		}
		GenderEnum genderEnum = Arrays.stream(GenderEnum.values()).filter(p -> {
			return p.getValue().toString().equals(v);
		}).findFirst().orElseThrow(IllegalArgumentException::new);
		return genderEnum;
	}
}
