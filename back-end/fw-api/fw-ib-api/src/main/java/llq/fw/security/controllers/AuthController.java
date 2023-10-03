package llq.fw.security.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import llq.fw.cm.common.Constants;
import llq.fw.cm.models.ContactInfo;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.Function;
import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.models.RefreshtokenIb;
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.models.RolesGroupFun;
import llq.fw.cm.models.RolesGroupSub;
import llq.fw.cm.models.Subfunction;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.repository.RolesGroupFunRepository;
import llq.fw.cm.security.repository.RolesGroupRepository;
import llq.fw.cm.security.repository.RolesGroupSubRepository;
import llq.fw.cm.security.repository.UserRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.ForgotPasswordRequest;
import llq.fw.security.exception.TokenRefreshException;
import llq.fw.security.payload.request.LoginRequest;
import llq.fw.security.payload.request.SignupRequest;
import llq.fw.security.payload.request.TokenRefreshRequest;
import llq.fw.security.payload.response.JwtResponse;
import llq.fw.security.payload.response.MessageResponse;
import llq.fw.security.payload.response.TokenRefreshResponse;
import llq.fw.security.security.jwt.JwtUtils;
import llq.fw.security.security.services.RefreshTokenIbService;
import llq.fw.services.ContactInfoServiceImpl;
import llq.fw.services.CustServiceImpl;
import llq.fw.services.TermsOfUseServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustRepository custRepository;
	@Autowired
	RolesGroupRepository rolesGroupRepository;
	@Autowired
	RolesGroupFunRepository rolesGroupFunRepository;
	@Autowired
	RolesGroupSubRepository rolesGroupSubRepository;

	@Autowired
	CustServiceImpl custServiceImpl;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenIbService refreshTokenIbService;
	
	@Autowired
	ContactInfoServiceImpl contactInfoServiceImpl;
	
	@Autowired
	TermsOfUseServiceImpl termsOfUseServiceImpl;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustDetailsImpl userDetails = (CustDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshtokenIb refreshToken = refreshTokenIbService.createRefreshToken(userDetails.getId());

		return ResponseEntity.ok(new JwtResponse(Constants.OK, "", jwt, refreshToken.getToken(), userDetails.getId(),
				userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
		if (custRepository.existsByCode(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}


		// Create new user's account
		Cust user = new Cust();

		user.setCode(signUpRequest.getUsername());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

	
		custRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenIbService.findByToken(requestRefreshToken).map(refreshTokenIbService::verifyExpiration)
				.map(RefreshtokenIb::getCust).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getCode());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		CustDetailsImpl userDetails = (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Long userId = userDetails.getId();
		refreshTokenIbService.deleteByCustId(userId);
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}

	@PostMapping("/forgotpassword") // quen mat khau
	public ResponseEntity<BaseResponse> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
		BaseResponse baseResponse=custServiceImpl.forgotPassword(forgotPasswordRequest);
		return ResponseEntity.ok(baseResponse);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/getContactInfo") // get contact info
	public ResponseEntity<BaseResponse> getContact() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		BaseResponse baseResponse = contactInfoServiceImpl.getContactInfo();
		return ResponseEntity.ok().headers(headers).body(baseResponse);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/getdk") // get all dieu khoan
	public ResponseEntity<BaseResponse> getAllDK(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		BaseResponse baseResponse = termsOfUseServiceImpl.getAll();
		return ResponseEntity.ok().headers(headers).body(baseResponse);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/getdetail/{id}") //get dieu khoan by id
	public ResponseEntity<BaseResponse> getDetailById(@PathVariable java.lang.Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		BaseResponse baseResponse=termsOfUseServiceImpl.getDetail(id);
		return ResponseEntity.ok().headers(headers).body(baseResponse);
	}
}
