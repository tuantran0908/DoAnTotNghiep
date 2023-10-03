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

import llq.fw.cm.models.CartDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.CartDetailSearchRequest;
import llq.fw.services.CartDetailServiceImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/cart-detail")
public class CartDetailController {
	@Autowired
	CartDetailServiceImpl cartDetailServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(CartDetailSearchRequest cartDetail,Pageable pageable) {
		BaseResponse baseResponse=cartDetailServiceImpl.search(cartDetail, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=cartDetailServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/count/{id}")
	public ResponseEntity<BaseResponse> getCountCartDetail(@PathVariable Long id) {
		BaseResponse baseResponse=cartDetailServiceImpl.getCountCartDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody CartDetail cartDetail) {
		BaseResponse baseResponse=cartDetailServiceImpl.create(cartDetail);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  CartDetail cartDetail) {
		BaseResponse baseResponse=cartDetailServiceImpl.update(cartDetail);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=cartDetailServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
