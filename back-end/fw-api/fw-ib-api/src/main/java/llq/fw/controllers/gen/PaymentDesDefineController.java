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

import llq.fw.cm.models.PaymentDesDefine;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.PaymentDesDefineServiceImpl;
import llq.fw.payload.request.gen.PaymentDesDefineSearchRequest;
@RestController
@RequestMapping("api/paymentdesdefine")
public class PaymentDesDefineController {
	@Autowired
	PaymentDesDefineServiceImpl paymentDesDefineServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(PaymentDesDefineSearchRequest paymentDesDefineRequest,Pageable pageable) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.search(paymentDesDefineRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody PaymentDesDefine paymentDesDefineRequest) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.create(paymentDesDefineRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody PaymentDesDefine paymentDesDefineRequest) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.update(paymentDesDefineRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=paymentDesDefineServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

