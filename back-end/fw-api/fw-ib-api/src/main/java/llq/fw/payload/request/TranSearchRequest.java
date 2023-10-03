package llq.fw.payload.request;
import java.util.Date;

import llq.fw.cm.enums.trans.SchedulesEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranSearchRequest {
	private java.lang.String type;
	private SchedulesEnum schedule;
	private java.lang.String taiKhoanNguon;
	private java.lang.String taiKhoanThuHuong;
	private java.lang.String tenNguoiThuHuong;
	private Date thoiGianLapLenhTu;
	private Date thoiGianLapLenhDen;
	private Date thoiGianDuyetLenhTu;
	private Date thoiGianDuyetLenhDen;
	private java.lang.String userLapLenh;
	private java.lang.String userDuyetLenh;
	private java.lang.String status;
	private java.lang.String paymentStatus;
	private java.lang.String maGiaoDich;
	private java.lang.String khoangTienTu;
	private java.lang.String khoangTienDen;
}
