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

import llq.fw.cm.models.TransSchedulesDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.TranSearchRequest;
import llq.fw.payload.request.gen.TransSchedulesDetailSearchRequest;
import llq.fw.services.TransSchedulesDetailServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/transschedulesdetail")
public class TransSchedulesDetailController {
	@Autowired
	TransSchedulesDetailServiceImpl transSchedulesDetailServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(TranSearchRequest tranSearch,Pageable pageable) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.search(tranSearch, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody TransSchedulesDetail transSchedulesDetailRequest) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.create(transSchedulesDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody TransSchedulesDetail transSchedulesDetailRequest) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.update(transSchedulesDetailRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("getdetail") // Xoa
	public ResponseEntity<BaseResponse> getDetailByCode(java.lang.String code) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.getDetailByCode(code);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=transSchedulesDetailServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

