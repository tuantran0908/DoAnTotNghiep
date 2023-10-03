package llq.fw.controllers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.GiaoTrinhSearchRequest;
import llq.fw.payload.response.GiaoTrinhRepsonse;
import llq.fw.services.GiaoTrinhServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/giao-trinh")
public class GiaoTrinhController {
	private static final Logger logger = LoggerFactory.getLogger(GiaoTrinhController.class);
	@Autowired
	GiaoTrinhServiceImpl giaoTrinhServiceImpl;

	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(GiaoTrinhSearchRequest giaoTrinh, Pageable pageable) {
		BaseResponse baseResponse = giaoTrinhServiceImpl.search(giaoTrinh, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("/search-select2")
	public ResponseEntity<BaseResponse> searchSelect2(GiaoTrinh giaoTrinh, Pageable pageable) {
		BaseResponse baseResponse = giaoTrinhServiceImpl.searchSelect2(giaoTrinh, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse = giaoTrinhServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestPart("data") @Valid GiaoTrinh giaoTrinh,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		if (!ObjectUtils.isEmpty(file)) {
			try {
				Blob blob = BlobProxy.generateProxy(file.getBytes());
				giaoTrinh.setImage(blob);
			} catch (IOException e) {
				logger.error("error", e);
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
			}
		}

		baseResponse = giaoTrinhServiceImpl.create(giaoTrinh);
		return ResponseEntity.ok(baseResponse);

	}

	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestPart("data") @Valid GiaoTrinhRepsonse giaoTrinhRequest,@RequestParam(name="file",required = false) MultipartFile file) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		GiaoTrinh giaoTrinh= new GiaoTrinh();
		try {
			giaoTrinh= giaoTrinhServiceImpl.byteToBlob(giaoTrinhRequest);
			if (!ObjectUtils.isEmpty(file)) {
			Blob blob=  BlobProxy.generateProxy( file.getBytes());
			giaoTrinh.setImage(blob);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		baseResponse = giaoTrinhServiceImpl.update(giaoTrinh);
		return ResponseEntity.ok(baseResponse);

	}

	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse = giaoTrinhServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("");
	}

}
