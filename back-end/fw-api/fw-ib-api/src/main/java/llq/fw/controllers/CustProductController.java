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

import llq.fw.cm.models.CustProduct;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.gen.CustProductSearchRequest;
import llq.fw.services.CustProductServiceImpl;
@RestController
@RequestMapping("api/custproduct")
public class CustProductController {
	@Autowired
	CustProductServiceImpl custProductServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(CustProductSearchRequest custProductRequest,Pageable pageable) {
		BaseResponse baseResponse=custProductServiceImpl.search(custProductRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody CustProduct custProductRequest) {
		BaseResponse baseResponse=custProductServiceImpl.create(custProductRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody CustProduct custProductRequest) {
		BaseResponse baseResponse=custProductServiceImpl.update(custProductRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=custProductServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=custProductServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=custProductServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

