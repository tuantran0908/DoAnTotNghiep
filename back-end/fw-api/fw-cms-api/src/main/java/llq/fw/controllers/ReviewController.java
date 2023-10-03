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

import llq.fw.cm.models.Review;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.ReviewSearchRequest;
import llq.fw.services.ReviewServiceImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/review")
public class ReviewController {
	@Autowired
	ReviewServiceImpl reviewServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(ReviewSearchRequest review,Pageable pageable) { 
		BaseResponse baseResponse=reviewServiceImpl.search(review, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=reviewServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Review review) {
		BaseResponse baseResponse=reviewServiceImpl.create(review);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  Review review) {
		BaseResponse baseResponse=reviewServiceImpl.update(review);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=reviewServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
