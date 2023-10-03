package llq.fw.cm.common;

public class Constants {
	public final static String OK ="OK";
	public final static String ERROR ="ERROR";
	public final static Long LIMIT_LOGIN_FALSE = 3l;//số lần nhập sai mật khẩu liên tiếp
	public final static String QUALITY_PASSWORD = "82";//Số lượng bản ghi password his cần lấy
	
	public enum FwError{
		THANHCONG("THANHCONG","Thành công!"),
		KHONGTHANHCONG("KHONGTHANHCONG","Không thành công!"),
		DLKHONGTONTAI("DLKHONGTONTAI","Dữ liệu không tồn tại!"),
		DUPLICATEPASSWORD("DUPLICATEPASSWORD","Mật khẩu mới không được trùng với mật khẩu cũ!"),
		PASSWORDNOTFOUND("PASSWORDNOTFOUND","Mật khẩu hiện tại không chính xác!"),
		PASSWORDFAILLIMIT("PASSWORDFAILLIMIT","Bạn đã nhập sai mật khẩu. Tài khoản sẽ bị thoát"),
		DLDATONTAI("DLDATONTAI","Dữ liệu đã tồn tại!");
	    private final String code;
	    private final String message;
	    FwError(String code,String message) {
	    	this.code=code;
	    	this.message=message;
	    }
		public String getCode() {
			return code;
		}
		public String getMessage() {
			return message;
		}
	    
	}

	public final static class Prequency {
		public final static String NGAY = "1";
		public final static String TUAN = "2";
		public final static String THANG = "3";
		public final static String QUY = "4";
		public final static String NAM = "5";
	}

}