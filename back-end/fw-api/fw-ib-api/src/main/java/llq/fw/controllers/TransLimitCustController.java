package llq.fw.controllers;

import java.util.List;

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

import llq.fw.cm.models.Cust;
import llq.fw.cm.models.TransLimitCust;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.gen.TransLimitCustSearchRequest;
import llq.fw.services.TransLimitCustServiceImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/translimitcust")
public class TransLimitCustController {
    @Autowired
    TransLimitCustServiceImpl transLimitCustServiceImpl;
    @GetMapping // Tim kiem theo nhieu dieu kien
    public ResponseEntity<BaseResponse> searchAll(TransLimitCustSearchRequest transLimitCustRequest,Pageable pageable) {
        BaseResponse baseResponse=transLimitCustServiceImpl.search(transLimitCustRequest, pageable);
        return ResponseEntity.ok(baseResponse);
    }
    @PostMapping // Tao moi 
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> create(@RequestBody TransLimitCust transLimitCustRequest) {
        BaseResponse baseResponse=transLimitCustServiceImpl.create(transLimitCustRequest);
        return ResponseEntity.ok(baseResponse);
    }
    @PutMapping // Cap nhat 
    public ResponseEntity<BaseResponse> update(@RequestBody List<TransLimitCust> transLimitCustRequest) {
        BaseResponse baseResponse=transLimitCustServiceImpl.updateAll(transLimitCustRequest);
        return ResponseEntity.ok(baseResponse);
    }
    @DeleteMapping("{id}") // Xoa
    public ResponseEntity<BaseResponse> delete(@PathVariable java.lang.Long id) {
        BaseResponse baseResponse=transLimitCustServiceImpl.delete(id);
        return ResponseEntity.ok(baseResponse);
    }
    
    @GetMapping("{id}") // Xoa
    public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
        BaseResponse baseResponse=transLimitCustServiceImpl.getDetail(id);
        return ResponseEntity.ok(baseResponse);
    }
    @GetMapping("keyexist/{id}") // check exist
    public ResponseEntity<BaseResponse> checkExist(@PathVariable java.lang.Long id) {
        BaseResponse baseResponse=transLimitCustServiceImpl.checkExist(id);
        return ResponseEntity.ok(baseResponse);
    }
    @PostMapping("/getbycust") // get detail translimitcust by cust
    public ResponseEntity<BaseResponse> getDetailTransLimitCust(@RequestBody Cust cust) {
        BaseResponse baseResponse=transLimitCustServiceImpl.getDetailByCust(cust);
        return ResponseEntity.ok(baseResponse);
    }
    @GetMapping("/getcust") // get  cust
    public ResponseEntity<BaseResponse> getCust(Long position) {
        BaseResponse baseResponse=transLimitCustServiceImpl.getCust(position);
        return ResponseEntity.ok(baseResponse);
    }
    
    @GetMapping("/getcustbycode") // get  cust by code
    public Cust getCust(String code) {
        return transLimitCustServiceImpl.getCustByCode(code);
    }
}
