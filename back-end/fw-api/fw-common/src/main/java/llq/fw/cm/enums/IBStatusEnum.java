package llq.fw.cm.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IBStatusEnum {

	Y(1), N(0);
/*
 * 1: Hoạt động|0: Không hoạt động
 * 1: Nhan |0: Không nhan -> SMS, NOTIFICATION
 * 
 */
	private Integer value;

	IBStatusEnum(Integer i) {
		this.value = i;
	}
	@JsonValue
	public Integer getValue() {
		return value;
	}

	@JsonCreator
	public static IBStatusEnum forValue(String v) {
		if(v==null) {
			return null;
		}
		IBStatusEnum statusEnum= Arrays.stream(IBStatusEnum.values()).filter(
				p -> {
					return p.getValue().toString().equals(v);
				}).findFirst().orElseThrow(IllegalArgumentException::new);
		return statusEnum;
	}

}
