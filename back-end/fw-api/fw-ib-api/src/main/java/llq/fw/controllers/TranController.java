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

import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.Tran;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.TranSearchRequest;
import llq.fw.payload.request.TransRequest;
import llq.fw.services.TranServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/tran")
public class TranController {
	@Autowired
	TranServiceImpl tranServiceImpl;

	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(TranSearchRequest tranRequest, Pageable pageable) {
		BaseResponse baseResponse = tranServiceImpl.search(tranRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping // Tao moi
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Tran tranRequest) {
		BaseResponse baseResponse = tranServiceImpl.create(tranRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping("/createtrans") // Tao moi
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> createTrans(@RequestBody TransRequest tranRequest) {

		// lay du lieu tu Cust
		Cust cust = new Cust();
		cust.setId(66L);
		cust.setCode("123456789TL13");
		cust.setFullname("Nam Đờ Đờ");
		cust.setPosition(PositionEnum.forValue("1"));
		//

		BaseResponse baseResponse = tranServiceImpl.createTrans(tranRequest, cust);
		return ResponseEntity.ok(baseResponse);
	}

	@PutMapping // Cap nhat
	public ResponseEntity<BaseResponse> update(@RequestBody Tran tranRequest) {
		BaseResponse baseResponse = tranServiceImpl.update(tranRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = tranServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = tranServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = tranServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}
