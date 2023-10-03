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

import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.RolesGroupSearchRequest;
import llq.fw.services.RolesGroupServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/roles-group")
public class RolesGroupController {
	@Autowired
	RolesGroupServiceImpl rolesGroupServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(RolesGroupSearchRequest rolesGroup,Pageable pageable) { 
		BaseResponse baseResponse=rolesGroupServiceImpl.search(rolesGroup, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("/search-select2")
	public ResponseEntity<BaseResponse> searchSelect2(RolesGroup rolesGroup,Pageable pageable) { 
		BaseResponse baseResponse=rolesGroupServiceImpl.searchSelect2(rolesGroup, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=rolesGroupServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody RolesGroup rolesGroup) {
		BaseResponse baseResponse=rolesGroupServiceImpl.create(rolesGroup);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  RolesGroup rolesGroup) {
		BaseResponse baseResponse=rolesGroupServiceImpl.update(rolesGroup);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=rolesGroupServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
