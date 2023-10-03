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

import llq.fw.cm.models.TransLimitDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.TransLimitDetailServiceImpl;
import llq.fw.payload.request.gen.TransLimitDetailSearchRequest;
@RestController
@RequestMapping("api/translimitdetail")
public class TransLimitDetailController {
	@Autowired
	TransLimitDetailServiceImpl transLimitDetailServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(TransLimitDetailSearchRequest transLimitDetailRequest,Pageable pageable) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.search(transLimitDetailRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody TransLimitDetail transLimitDetailRequest) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.create(transLimitDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody TransLimitDetail transLimitDetailRequest) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.update(transLimitDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLimitDetailServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

