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

import llq.fw.cm.models.BankReceiving;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.BankReceivingServiceImpl;
import llq.fw.payload.request.gen.BankReceivingSearchRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/bankreceiving")
public class BankReceivingController {
	@Autowired
	BankReceivingServiceImpl bankReceivingServiceImpl;

	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(BankReceivingSearchRequest bankReceivingRequest, Pageable pageable) {
		BaseResponse baseResponse = bankReceivingServiceImpl.search(bankReceivingRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping // Tao moi
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody BankReceiving bankReceivingRequest) {
		BaseResponse baseResponse = bankReceivingServiceImpl.create(bankReceivingRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@PutMapping // Cap nhat
	public ResponseEntity<BaseResponse> update(@RequestBody BankReceiving bankReceivingRequest) {
		BaseResponse baseResponse = bankReceivingServiceImpl.update(bankReceivingRequest);
		return ResponseEntity.ok(baseResponse);
	}

	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = bankReceivingServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = bankReceivingServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.String id) {
		BaseResponse baseResponse = bankReceivingServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("getallbankreceive") // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> getAllBankReceive() {
		BaseResponse baseResponse = bankReceivingServiceImpl.getAllBank();
		return ResponseEntity.ok(baseResponse);
	}
}
