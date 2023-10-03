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

import llq.fw.cm.models.Bill;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.BillServiceImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/bill")
public class BillController {
	@Autowired
	BillServiceImpl billServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(Bill bill,Pageable pageable) { 
		BaseResponse baseResponse=billServiceImpl.search(bill, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/search-select2")
	public ResponseEntity<BaseResponse> searchSelect2(Bill bill,Pageable pageable) { 
		BaseResponse baseResponse=billServiceImpl.searchSelect2(bill, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=billServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Bill bill) {
		BaseResponse baseResponse=billServiceImpl.create(bill);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  Bill bill) {
		BaseResponse baseResponse=billServiceImpl.update(bill);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=billServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
