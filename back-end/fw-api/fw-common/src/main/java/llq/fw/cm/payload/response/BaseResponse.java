package llq.fw.cm.payload.response;

import llq.fw.cm.common.Constants.FwError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
	private String errorCode ;
	private String errorMessage;
	private Object data;
	public BaseResponse() {
		this.errorCode=FwError.THANHCONG.getCode();
		this.errorMessage=FwError.THANHCONG.getMessage();
	}
	public BaseResponse(String errorCode,String errorMessage) {
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
	}
	public BaseResponse(FwError fwError) {
		this.errorCode=fwError.getCode();
		this.errorMessage=fwError.getMessage();
	}
	public void setFwError(FwError fwError) {
		this.errorCode=fwError.getCode();
		this.errorMessage=fwError.getMessage();
	}
	
}
