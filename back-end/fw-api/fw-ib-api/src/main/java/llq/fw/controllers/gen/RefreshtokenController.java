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

import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.RefreshtokenServiceImpl;
import llq.fw.payload.request.gen.RefreshtokenSearchRequest;
@RestController
@RequestMapping("api/refreshtoken")
public class RefreshtokenController {
	@Autowired
	RefreshtokenServiceImpl refreshtokenServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(RefreshtokenSearchRequest refreshtokenRequest,Pageable pageable) {
		BaseResponse baseResponse=refreshtokenServiceImpl.search(refreshtokenRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Refreshtoken refreshtokenRequest) {
		BaseResponse baseResponse=refreshtokenServiceImpl.create(refreshtokenRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody Refreshtoken refreshtokenRequest) {
		BaseResponse baseResponse=refreshtokenServiceImpl.update(refreshtokenRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=refreshtokenServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=refreshtokenServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=refreshtokenServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

