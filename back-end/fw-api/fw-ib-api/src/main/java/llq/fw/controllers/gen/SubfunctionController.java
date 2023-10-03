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

import llq.fw.cm.models.Subfunction;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.SubfunctionServiceImpl;
import llq.fw.payload.request.gen.SubfunctionSearchRequest;
@RestController
@RequestMapping("api/subfunction")
public class SubfunctionController {
	@Autowired
	SubfunctionServiceImpl subfunctionServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(SubfunctionSearchRequest subfunctionRequest,Pageable pageable) {
		BaseResponse baseResponse=subfunctionServiceImpl.search(subfunctionRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Subfunction subfunctionRequest) {
		BaseResponse baseResponse=subfunctionServiceImpl.create(subfunctionRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody Subfunction subfunctionRequest) {
		BaseResponse baseResponse=subfunctionServiceImpl.update(subfunctionRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=subfunctionServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=subfunctionServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.String id) {
		BaseResponse baseResponse=subfunctionServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

