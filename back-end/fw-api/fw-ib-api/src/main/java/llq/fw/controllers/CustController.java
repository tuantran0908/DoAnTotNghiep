package llq.fw.controllers;

import java.util.ArrayList;

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

import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustAcc;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.ForgotPasswordRequest;
import llq.fw.payload.request.UpdatePasswordRequest;
import llq.fw.payload.request.gen.CustSearchRequest;
import llq.fw.services.CustAccServiceImpl;
import llq.fw.services.CustServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/cust")
public class CustController {
	@Autowired
	CustServiceImpl custServiceImpl;
	@Autowired
	CustAccServiceImpl custAccServiceImpl;
	
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(CustSearchRequest custRequest,Pageable pageable) {
		BaseResponse baseResponse=custServiceImpl.search(custRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/search") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchCust(CustSearchRequest custRequest,Pageable pageable) {
		BaseResponse baseResponse=custServiceImpl.searchCust(custRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Cust custRequest) {
		BaseResponse baseResponse=custServiceImpl.create(custRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody Cust custRequest) {
		BaseResponse baseResponse=custServiceImpl.update(custRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=custServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=custServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@PostMapping("/updatepassword") // doi mat khau
	public ResponseEntity<BaseResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
		BaseResponse baseResponse=custServiceImpl.updatePassword(updatePasswordRequest);
		return ResponseEntity.ok(baseResponse);
	}
	
	@PutMapping("/updatetruyvan")
	public ResponseEntity<BaseResponse> updateTruyVan(@RequestBody Cust cust) {
		BaseResponse baseResponse = custServiceImpl.saveSAll(cust);
		return ResponseEntity.ok(baseResponse);
	}
	
	@PutMapping("/updategiaodich")
	public ResponseEntity<BaseResponse> updateGiaoDich(@RequestBody Cust cust) {
		BaseResponse baseResponse = custServiceImpl.saveAllGiaoDich(cust);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/getcode")
	public ResponseEntity<BaseResponse> getAllCode() {
		BaseResponse baseResponse=custServiceImpl.getAllCodes();
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/gettel")
	public ResponseEntity<BaseResponse> getAllTel() {
		BaseResponse baseResponse=custServiceImpl.getAllTels();
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/getemail")
	public ResponseEntity<BaseResponse> getAllEmail() {
		BaseResponse baseResponse=custServiceImpl.getAllEmails();
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/checkTel")
	public ResponseEntity<BaseResponse> checkTel(String value, String custId) {
		BaseResponse baseResponse =custServiceImpl.checkTel(value, Long.parseLong(custId));
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/checkEmail")
	public ResponseEntity<BaseResponse> checkEmail(String value, String custId) {
		BaseResponse baseResponse =custServiceImpl.checkEmail(value, Long.parseLong(custId));
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("/checkIdno")
	public ResponseEntity<BaseResponse> checkIdno(String value, String custId) {
		BaseResponse baseResponse =custServiceImpl.checkIdno(value, Long.parseLong(custId));
		return ResponseEntity.ok(baseResponse);
	}
}

