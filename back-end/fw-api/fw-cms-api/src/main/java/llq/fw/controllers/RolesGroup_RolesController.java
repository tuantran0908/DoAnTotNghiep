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
import llq.fw.cm.models.RolesGroup_Roles;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.RolesGroupSearchRequest;
import llq.fw.services.RolesGroupServiceImpl;
import llq.fw.services.RolesGroup_RolesServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/roles-group-roles")
public class RolesGroup_RolesController {
	@Autowired
	RolesGroup_RolesServiceImpl rolesGroup_RolesServiceImpl;
	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(RolesGroup_Roles rolesGroup_Roles,Pageable pageable) { 
		BaseResponse baseResponse=rolesGroup_RolesServiceImpl.search(rolesGroup_Roles, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse=rolesGroup_RolesServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody RolesGroup_Roles rolesGroup_Roles) {
		BaseResponse baseResponse=rolesGroup_RolesServiceImpl.create(rolesGroup_Roles);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestBody  RolesGroup_Roles rolesGroup_Roles) {
		BaseResponse baseResponse=rolesGroup_RolesServiceImpl.update(rolesGroup_Roles);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse=rolesGroup_RolesServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
