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

import llq.fw.cm.models.TermsOfUse;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.gen.TermsOfUseSearchRequest;
import llq.fw.services.TermsOfUseServiceImpl;
@RestController
@RequestMapping("api/termsofuse")
public class TermsOfUseController {
	@Autowired
	TermsOfUseServiceImpl termsOfUseServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(TermsOfUseSearchRequest termsOfUseRequest,Pageable pageable) {
		BaseResponse baseResponse=termsOfUseServiceImpl.search(termsOfUseRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody TermsOfUse termsOfUseRequest) {
		BaseResponse baseResponse=termsOfUseServiceImpl.create(termsOfUseRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody TermsOfUse termsOfUseRequest) {
		BaseResponse baseResponse=termsOfUseServiceImpl.update(termsOfUseRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=termsOfUseServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=termsOfUseServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=termsOfUseServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("getdk")
	public ResponseEntity<BaseResponse> getAllDK(){
		BaseResponse baseResponse = termsOfUseServiceImpl.getAll();
		return ResponseEntity.ok(baseResponse);
	}
}

