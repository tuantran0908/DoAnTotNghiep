package llq.fw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import llq.fw.cm.models.Bill;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.CartDetailSearchRequest;
import llq.fw.payload.request.ReportSearchRequest;
import llq.fw.services.BillServiceImpl;
import llq.fw.services.ReportServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/report")
public class ReportController {
	@Autowired
	ReportServiceImpl reportServiceImpl;
	@GetMapping("/quantity")
	public ResponseEntity<BaseResponse> getTotalQuantity() {
		BaseResponse baseResponse=reportServiceImpl.getTotalQuantity();
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/category")
	public ResponseEntity<BaseResponse> getTotalCategory() {
		BaseResponse baseResponse=reportServiceImpl.getTotalCategory();
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/giao-trinh")
	public ResponseEntity<BaseResponse> searchGiaoTrinh(ReportSearchRequest reportSearchRequest,Pageable pageable) {
		BaseResponse baseResponse=reportServiceImpl.searchGiaoTrinh(reportSearchRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/reviews")
	public ResponseEntity<BaseResponse> searchReviews(ReportSearchRequest reportSearchRequest,Pageable pageable) {
		BaseResponse baseResponse=reportServiceImpl.searchReviews(reportSearchRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/bills")
	public ResponseEntity<BaseResponse> searchBills(ReportSearchRequest reportSearchRequest,Pageable pageable) {
		BaseResponse baseResponse=reportServiceImpl.searchBills(reportSearchRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
}
