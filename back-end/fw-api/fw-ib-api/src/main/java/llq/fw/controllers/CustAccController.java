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
import llq.fw.services.AccountServiceImpl;
import llq.fw.services.CustAccServiceImpl;
import llq.fw.payload.request.gen.CustAccSearchRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/custacc")
public class CustAccController {
	@Autowired
	CustAccServiceImpl custAccServiceImpl;
	@Autowired
	AccountServiceImpl accountServiceImpl;

	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(CustAccSearchRequest custAccRequest, Pageable pageable) {
		BaseResponse baseResponse = custAccServiceImpl.search(custAccRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping // Tao moi
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody CustAcc custAccRequest) {
		BaseResponse baseResponse = custAccServiceImpl.create(custAccRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@PutMapping // Cap nhat
	public ResponseEntity<BaseResponse> update(@RequestBody CustAcc custAccRequest) {
		BaseResponse baseResponse = custAccServiceImpl.update(custAccRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custAccServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custAccServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custAccServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/sdd") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchSDd(String id) {
		BaseResponse baseResponse = accountServiceImpl.searchSAccDD(Long.parseLong(id));
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/tdd") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchTDd(String id) {
		BaseResponse baseResponse = accountServiceImpl.searchTAccDD(Long.parseLong(id));
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/fd") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchFd(String id) {
		BaseResponse baseResponse = accountServiceImpl.searchAccFD(Long.parseLong(id));
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/ln") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchLn(String id) {
		BaseResponse baseResponse = accountServiceImpl.searchAccLN(Long.parseLong(id));
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/accdd") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> getDd() {
		BaseResponse baseResponse = accountServiceImpl.getAccDD();
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/accfd") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> getFd() {
		BaseResponse baseResponse = accountServiceImpl.getAccFD();
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/accln") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> getLn() {
		BaseResponse baseResponse = accountServiceImpl.getAccLN();
		return ResponseEntity.ok(baseResponse);
	}

	// quandev
	@PostMapping("/getcusbyid")
	public ResponseEntity<BaseResponse> getCusById(@RequestBody Cust cus) {

		BaseResponse baseResponse = new BaseResponse();
		// Fix Cust
		Cust cust = new Cust();
		cust.setTAllDd("0");

		if (cust.getTAllDd().equals("1")) {
			// Get tu core
			baseResponse = accountServiceImpl.getAccDD();
		} else {
			baseResponse = custAccServiceImpl.getCusAccById(cus.getId());
		}

		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping("/getcusbyacc")
	public ResponseEntity<BaseResponse> getCusByAcc(@RequestBody CustAcc cusAcc) {

		BaseResponse baseResponse = new BaseResponse();
		// Fix Cust
		Cust cust = new Cust();
		cust.setTAllDd("0");

		baseResponse = custAccServiceImpl.getCusAccByAcc(cusAcc.getAcc());

		return ResponseEntity.ok(baseResponse);
	}
	// end quandev

}
