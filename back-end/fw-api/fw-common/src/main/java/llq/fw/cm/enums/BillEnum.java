package llq.fw.cm.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BillEnum {
	CHUATHANHTOAN(1), DATHANHTOAN(2),HUY(0);
	private Integer value;

	BillEnum(Integer i) {
		this.value = i;
	}

	@JsonValue
	public Integer getValue() {
		return value;
	}

	@JsonCreator
	public static BillEnum forValue(String v) {
		if (v == null) {
			return null;
		}
		BillEnum genderEnum = Arrays.stream(BillEnum.values()).filter(p -> {
			return p.getValue().toString().equals(v);
		}).findFirst().orElseThrow(IllegalArgumentException::new);
		return genderEnum;
	}
}
