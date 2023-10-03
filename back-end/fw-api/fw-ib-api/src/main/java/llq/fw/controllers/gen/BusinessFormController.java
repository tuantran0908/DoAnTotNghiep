package llq.fw.controllers.gen;

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

import llq.fw.cm.models.BusinessForm;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.BusinessFormServiceImpl;
import llq.fw.payload.request.gen.BusinessFormSearchRequest;
@RestController
@RequestMapping("api/businessform")
public class BusinessFormController {
	@Autowired
	BusinessFormServiceImpl businessFormServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(BusinessFormSearchRequest businessFormRequest,Pageable pageable) {
		BaseResponse baseResponse=businessFormServiceImpl.search(businessFormRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody BusinessForm businessFormRequest) {
		BaseResponse baseResponse=businessFormServiceImpl.create(businessFormRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody BusinessForm businessFormRequest) {
		BaseResponse baseResponse=businessFormServiceImpl.update(businessFormRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=businessFormServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=businessFormServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=businessFormServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

