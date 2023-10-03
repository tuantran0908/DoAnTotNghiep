package llq.fw.controllers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.validation.Valid;

import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.payload.request.UserSearchRequest;
import llq.fw.payload.response.UserResponse;
import llq.fw.services.UserServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserServiceImpl userServiceImpl;

	@GetMapping
	public ResponseEntity<BaseResponse> searchAll(UserSearchRequest user, Pageable pageable) {
		BaseResponse baseResponse = userServiceImpl.search(user, pageable);
		return ResponseEntity.ok(baseResponse);
	}

	@GetMapping("{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable Long id) {
		BaseResponse baseResponse = userServiceImpl.getDetail(id);
		return ResponseEntity.ok(baseResponse);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(@RequestPart("data") @Valid User user,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		if (!ObjectUtils.isEmpty(file)) {
			try {
				Blob blob = BlobProxy.generateProxy(file.getBytes());
				user.setAvatar(blob);
			} catch (IOException e) {
				logger.error("error", e);
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
			}
		}

		baseResponse = userServiceImpl.create(user);
		return ResponseEntity.ok(baseResponse);
	}

	@PutMapping
	public ResponseEntity<BaseResponse> update(@RequestPart("data") @Valid UserResponse userRequest,@RequestParam(name="file",required = false) MultipartFile file) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		User user= new User();
		try {
			user= userServiceImpl.byteToBlob(userRequest);
			if (!ObjectUtils.isEmpty(file)) {
			Blob blob=  BlobProxy.generateProxy( file.getBytes());
			user.setAvatar(blob);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		baseResponse = userServiceImpl.update(user);
		return ResponseEntity.ok(baseResponse);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
		BaseResponse baseResponse = userServiceImpl.delete(id);
		return ResponseEntity.ok(baseResponse);
	}
}
