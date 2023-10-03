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

import llq.fw.cm.models.TransLotDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.TransLotDetailServiceImpl;
import llq.fw.payload.request.gen.TransLotDetailSearchRequest;
@RestController
@RequestMapping("api/translotdetail")
public class TransLotDetailController {
	@Autowired
	TransLotDetailServiceImpl transLotDetailServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(TransLotDetailSearchRequest transLotDetailRequest,Pageable pageable) {
		BaseResponse baseResponse=transLotDetailServiceImpl.search(transLotDetailRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody TransLotDetail transLotDetailRequest) {
		BaseResponse baseResponse=transLotDetailServiceImpl.create(transLotDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody TransLotDetail transLotDetailRequest) {
		BaseResponse baseResponse=transLotDetailServiceImpl.update(transLotDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLotDetailServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLotDetailServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transLotDetailServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

