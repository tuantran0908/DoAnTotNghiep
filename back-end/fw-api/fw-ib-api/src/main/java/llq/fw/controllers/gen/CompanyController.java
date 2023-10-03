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

import llq.fw.cm.models.Company;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.services.gen.CompanyServiceImpl;
import llq.fw.payload.request.gen.CompanySearchRequest;
@RestController
@RequestMapping("api/company")
public class CompanyController {
	@Autowired
	CompanyServiceImpl companyServiceImpl;
	@GetMapping // Tim kiem theo nhieu dieu kien
	public ResponseEntity<BaseResponse> searchAll(CompanySearchRequest companyRequest,Pageable pageable) {
		BaseResponse baseResponse=companyServiceImpl.search(companyRequest, pageable);
		return ResponseEntity.ok(baseResponse);
	}
	@PostMapping // Tao moi 
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestBody Company companyRequest) {
		BaseResponse baseResponse=companyServiceImpl.create(companyRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@PutMapping // Cap nhat 
	public ResponseEntity<BaseResponse> update(@RequestBody Company companyRequest) {
		BaseResponse baseResponse=companyServiceImpl.update(companyRequest);
		return ResponseEntity.ok(baseResponse);
	}
	@DeleteMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=companyServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
	
	@GetMapping("{id}") // Xoa
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=companyServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}
	@GetMapping("keyexist/{id}") // check exist
	public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
		BaseResponse baseResponse=companyServiceImpl.checkExist(id);
		return ResponseEntity.ok(baseResponse);
	}
}

