package llq.fw.controllers;

import java.math.BigDecimal;
import java.util.List;

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

import llq.fw.cm.models.BankReceiving;
import llq.fw.cm.models.CustContact;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.gen.CustContactSearchRequest;
import llq.fw.services.CustContactServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/custcontact")
public class CustContactController {
	@Autowired
	CustContactServiceImpl custContactServiceImpl;

	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(CustContactSearchRequest custContactRequest, Pageable pageable) {
		BaseResponse baseResponse = custContactServiceImpl.search(custContactRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping // Tao moi
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody CustContact custContactRequest) {
		BaseResponse baseResponse = custContactServiceImpl.create(custContactRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@PutMapping // Cap nhat
	public ResponseEntity<BaseResponse> update(@RequestBody CustContact custContactRequest) {
		BaseResponse baseResponse = custContactServiceImpl.update(custContactRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custContactServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custContactServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse = custContactServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/getDsBank")
	public List<BankReceiving> getDsBankReceiving() {
		return custContactServiceImpl.getDsBankReceiving();
	}

	@GetMapping("/getcustcontact")
	public ResponseEntity<BaseResponse> getCustContact(CustContactSearchRequest custContactRequest) {

		custContactRequest.setCustId(new BigDecimal(34));

		BaseResponse baseResponse = custContactServiceImpl.getCutsContact(custContactRequest);
		return ResponseEntity.ok(baseResponse);
	}
}
