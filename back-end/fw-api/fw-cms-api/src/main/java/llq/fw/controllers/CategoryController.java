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

import llq.fw.cm.models.Category;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.CategoryServiceImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/category")
public class CategoryController {
	@Autowired
	CategoryServiceImpl categoryServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(Category category,Pageable pageable) { 
		BaseResponse baseResponse=categoryServiceImpl.search(category, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/all")
	public ResponseEntity<BaseResponse> getAllCategory() { 
		BaseResponse baseResponse=categoryServiceImpl.getAllCategory();
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/search-select2")
	public ResponseEntity<BaseResponse> searchSelect2(Category category,Pageable pageable) { 
		BaseResponse baseResponse=categoryServiceImpl.searchSelect2(category, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=categoryServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Category category) {
		BaseResponse baseResponse=categoryServiceImpl.create(category);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  Category category) {
		BaseResponse baseResponse=categoryServiceImpl.update(category);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=categoryServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
