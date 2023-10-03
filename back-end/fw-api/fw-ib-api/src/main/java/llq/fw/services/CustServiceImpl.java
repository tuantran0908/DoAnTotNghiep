package llq.fw.services;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import llq.fw.cm.common.Constants;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.enums.cust.CustStatusEnum;
import llq.fw.cm.enums.cust.NotificationEnum;
import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.enums.cust.SmsEnum;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustAcc;
import llq.fw.cm.models.CustProduct;
import llq.fw.cm.models.PasswordHi;
import llq.fw.cm.models.SysParameter;
import llq.fw.cm.models.TransLimitCust;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CustAccRepository;
import llq.fw.cm.security.repository.CustProductRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.repository.PasswordHisRepository;
import llq.fw.cm.security.repository.SysParameterRepository;
import llq.fw.cm.security.repository.TransLimitCustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.ForgotPasswordRequest;
import llq.fw.payload.request.UpdatePasswordRequest;
import llq.fw.payload.request.gen.CustSearchRequest;
@Component
public class CustServiceImpl implements BaseService<BaseResponse, Cust, CustSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	CustRepository custRepository;
	@Autowired
	CustAccRepository custAccRepository;
	@Autowired
	CustAccServiceImpl 	custAccServiceImpl;
	@Autowired
	CustProductRepository custProductRepository;
	@Autowired
	CustProductServiceImpl custProductServiceImpl;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	PasswordHisRepository passwordHiRepository;
	@Autowired
	SysParameterRepository sysParameterRepository;
	@Autowired
	TransLimitCustServiceImpl transLimitCustServiceImpl;
	@Autowired
	TransLimitCustRepository transLimitCustRepository;
	@Override
	public BaseResponse create(Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			if(cust.getId()!= null) {
				var custCur = custRepository.findById(cust.getId()).orElse(null);
				if (custCur != null) {
					baseResponse.setFwError(FwError.DLDATONTAI);
					return baseResponse;
				}
			}
			CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
			String code;
			if(currentCust == null){
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}else {
				cust.setCompany(currentCust.getCompany());
				cust.setPassword(encoder.encode(currentCust.getPassword()));
				if(cust.getPosition().equals(PositionEnum.DUYETLENH)) {
					code = genCode(currentCust, "DL");
				}else {
					code = genCode(currentCust, "TL");
				}
			}
			//NamDD: set default value
			cust.setStatus(CustStatusEnum.CHOKICHHOAT);
			cust.setSms(SmsEnum.Y);
			cust.setNotification(NotificationEnum.Y);
			
			
	
			var custNew = Cust.builder()
					.birthday(cust.getBirthday())
					.cif(cust.getCif())
					.code(code)
					.email(cust.getEmail())
					.fullname(cust.getFullname())
					.idno(cust.getIdno())
					.indentitypapers(cust.getIndentitypapers())
					.nmemonicName(cust.getNmemonicName())
					.notification(cust.getNotification())
					.position(cust.getPosition())
					.sms(cust.getSms())
					.status(cust.getStatus())
					.tel(cust.getTel())
					.password(cust.getPassword())
					.verifyType(cust.getVerifyType())
					.company(cust.getCompany())
					.build();
			//jpaExecute(custNew);
			Set<TransLimitCust> lstTransLimitCust = transLimitCustServiceImpl.createTransLimitCust();
			custNew.setTransLimitCust(lstTransLimitCust);
			jpaExecuteTransLimitCust(custNew);
			baseResponse.setData(custNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public String genCode(Cust currentCust, String ma) {
		String code = "";
		int count = 1;
		
		String oldCode=custRepository.getCode(ma);
		if(oldCode != null ) {
			count = (Integer.parseInt(oldCode.substring((oldCode.length()-2))) + 1);
		}
		if(count<10) {
			code = currentCust.getCompany().getCif() + ma + "0" + count;
		}else code = currentCust.getCompany().getCif() + ma + count;
		
		return code;
	}
	
	@Override
	public BaseResponse update(Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custCur = custRepository.findById(cust.getId()).orElse(null);
			if (custCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			//NamDD: set default value
			cust.setStatus(CustStatusEnum.CHOKICHHOAT);
			
			custCur.setBirthday(cust.getBirthday());
			custCur.setCif(cust.getCif());
			custCur.setCode(cust.getCode());
			custCur.setEmail(cust.getEmail());
			custCur.setFullname(cust.getFullname());
			custCur.setIdno(cust.getIdno());
			custCur.setIndentitypapers(cust.getIndentitypapers());
			custCur.setNmemonicName(cust.getNmemonicName());
			custCur.setNotification(cust.getNotification());
			custCur.setPosition(cust.getPosition());
			custCur.setSms(cust.getSms());
			custCur.setStatus(cust.getStatus());
			custCur.setTel(cust.getTel());
			custCur.setPassword(cust.getPassword());
			custCur.setVerifyType(cust.getVerifyType());
			custCur.setRolesearch(cust.getRolesearch());
			custCur.setRoletrans(cust.getRoletrans());
			custCur.setSAllDd(cust.getSAllDd());
			custCur.setSAllFd(cust.getSAllFd());
			custCur.setSAllLn(cust.getSAllLn());
			custCur.setTAllDd(cust.getTAllDd());
			custCur.setCompany(cust.getCompany());
			jpaExecute(custCur);
			/*
			 * Luu obj lich su neu can
			 */
			custCur.setCustAcc(custAccRepository.findByCust(custCur));
			custCur.setCustProduct(custProductRepository.findByCust(custCur));
			baseResponse.setData(custCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public BaseResponse saveSAll(Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custCur = custRepository.findById(cust.getId()).orElse(null);
			if(custCur != null) {
				custAccServiceImpl.saveSAcc(custCur, cust);
			}
			baseResponse = update(cust);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public BaseResponse saveAllGiaoDich(Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custCur = custRepository.findById(cust.getId()).orElse(null);
			if(custCur != null) {
				custAccServiceImpl.saveTAcc(custCur, cust);
				custProductServiceImpl.saveProduct(custCur, cust);
			}
			baseResponse = update(cust);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public BaseResponse getAllCodes() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
			if(currentCust != null) {
				baseResponse.setData(custRepository.getAllCode(currentCust.getCompany().getId().toString()));
			}else{
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public BaseResponse getAllTels() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
			if(currentCust != null) {
				baseResponse.setData(custRepository.getAllTel(currentCust.getCompany().getId().toString()));
			}else{
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public BaseResponse getAllEmails() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
			if(currentCust != null) {
				baseResponse.setData(custRepository.getAllEmail(currentCust.getCompany().getId().toString()));
			}else{
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	@Override
	public BaseResponse search(CustSearchRequest cust, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		try {
			Specification<Cust> specification = new Specification<Cust>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Cust> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if(currentCust != null) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("company")),
								currentCust.getCompany())));						
					}
					if (!ObjectUtils.isEmpty(cust.getBirthday())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("birthday")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(cust.getBirthday())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(cust.getCif())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cif")),
								"%" + cust.getCif().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + cust.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
								"%" + cust.getEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")),
								"%" + cust.getFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("idno")),
								"%" + cust.getIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("indentitypapers")),
								 cust.getIndentitypapers())));
					}
					if (!ObjectUtils.isEmpty(cust.getNmemonicName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("nmemonicName")),
								"%" + cust.getNmemonicName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getNotification())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("notification")),
								cust.getNotification())));
					}
					if (!ObjectUtils.isEmpty(cust.getPosition())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("position")),
								 cust.getPosition())));
					}
					if (!ObjectUtils.isEmpty(cust.getSms())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("sms")),
								cust.getSms())));
					}
					if (!ObjectUtils.isEmpty(cust.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 cust.getStatus())));
					}
					if (!ObjectUtils.isEmpty(cust.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + cust.getTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getVerifyType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("verifyType")),
								 cust.getVerifyType())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Cust> page = custRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public BaseResponse searchCust(CustSearchRequest cust, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		try {
			Specification<Cust> specification = new Specification<Cust>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Cust> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(criteriaBuilder.upper(root.get("position")),
								 PositionEnum.QUANLY)));
					if(currentCust != null) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("company")),
								currentCust.getCompany())));						
					}
					if (!ObjectUtils.isEmpty(cust.getBirthday())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("birthday")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(cust.getBirthday())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(cust.getCif())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cif")),
								"%" + cust.getCif().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + cust.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
								"%" + cust.getEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")),
								"%" + cust.getFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("idno")),
								"%" + cust.getIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("indentitypapers")),
								 cust.getIndentitypapers())));
					}
					if (!ObjectUtils.isEmpty(cust.getNmemonicName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("nmemonicName")),
								"%" + cust.getNmemonicName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getNotification())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("notification")),
								cust.getNotification())));
					}
					
					if (!ObjectUtils.isEmpty(cust.getSms())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("sms")),
								cust.getSms())));
					}
					if (!ObjectUtils.isEmpty(cust.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 cust.getStatus())));
					}
					if (!ObjectUtils.isEmpty(cust.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + cust.getTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(cust.getVerifyType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("verifyType")),
								 cust.getVerifyType())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Cust> page = custRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	@Override
	public BaseResponse delete(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custCur = custRepository.findById(id).orElse(null);
			if (custCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			jpaDeleteExecute(id);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaExecute(Cust cust) {
		custRepository.save(cust);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Cust jpaExecuteTransLimitCust(Cust cust) {
		Optional.of(custRepository.save(cust))
        .map(res -> {
            if(cust.getTransLimitCust() != null && !cust.getTransLimitCust().isEmpty()){
            	cust.getTransLimitCust().stream().forEach(cu -> cu.setCust(res));
            }
            return cust.getTransLimitCust();
        })
        .ifPresent(res ->{
        	transLimitCustRepository.saveAll(res);
        });
    return cust;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		custRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var custCur = custRepository.findById(id).orElse(null);
		if (custCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(custCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public BaseResponse checkTel(String value, Long id) {
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		Long count;
		if(id != null) {
			count = custRepository.countCustDupTel(value, id);	
		}else {
			count = custRepository.countCustDupTel(value);			
		}
		if(count > 0) baseResponse.setData(true);
		else baseResponse.setData(false);
		return baseResponse;
	}
	
	public BaseResponse checkEmail(String value, Long id) {
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		Long count;
		if(id != null) {
			count = custRepository.countCustDupEmail(value, id);
		}else {
			count = custRepository.countCustDupEmail(value);
		}
		if(count > 0) baseResponse.setData(true);
		else baseResponse.setData(false);
		return baseResponse;
	}
	
	public BaseResponse checkIdno(String value, Long id) {
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		Long count;
		if(id != null) {
			count = custRepository.countCustDupIdno(value, id);
		}else {
			count = custRepository.countCustDupIdno(value);
		}
		if(count > 0) baseResponse.setData(true);
		else baseResponse.setData(false);
		return baseResponse;
	}
	
	//TruongPN: đổi mật khẩu
		public BaseResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setFwError(FwError.THANHCONG);
			try {
				//lấy user đang đăng nhập
				CustDetailsImpl userDetail =  (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//lây thông tin cust từ user đăng nhập
				Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
				
				//check thong tin currentpassword nhap vao 
				if(encoder.matches(updatePasswordRequest.getCurrentPass(),currentCust.getPassword())) {
					PasswordHi passwordHis = new PasswordHi();
					//kiểm tra password nhập có trùng với password cũ trong bảng passwordhis ko
					// TODO: Lấy dữ liệu history order by id desc ; lấy 3 bản ghi gần nhất, số lượng cấu hình vào SYS_PARAMETERS
					// Check trong list có tồn tại pass hay không
					SysParameter sysParam =  sysParameterRepository.findByCode(Constants.QUALITY_PASSWORD);
					Long value = Long.parseLong(sysParam.getValue());
					List<PasswordHi> lstPasswordHi = passwordHiRepository.getTopPasswordHi(value);
					
					List<PasswordHi> filterPass = lstPasswordHi.stream().filter(
							item -> encoder.matches(updatePasswordRequest.getNewPass(), item.getPassword()))
//							item -> item.getPassword().equals(encoder.encode(updatePasswordRequest.getNewPass())))
							.collect(Collectors.toList());
					
					if(filterPass.isEmpty() && filterPass.size() == 0) {
						String passwordOld = currentCust.getPassword();
						
						//kiểm tra password mới có trùng password hiện tại k, nếu không thì save pass mới
//						if(!currentCust.getPassword().equals(encoder.encode(updatePasswordRequest.getNewPass()))) {
						
							currentCust.setPassword(encoder.encode(updatePasswordRequest.getNewPass()));
							currentCust.setLoginfalseNumber(0L);
							jpaExecute(currentCust);
							//save pass cu vao passwordhis
							passwordHis.setCust(currentCust);
							passwordHis.setPassword(passwordOld);
							passwordHiRepository.save(passwordHis);
							/*
							 * Luu obj lich su neu can
							 */
							baseResponse.setData(currentCust);
//						}else {
//							baseResponse.setFwError(FwError.DUPLICATEPASSWORD);
//							baseResponse.setFwError(FwError.KHONGTHANHCONG);
//						}
						
					}else {
						baseResponse.setFwError(FwError.KHONGTHANHCONG);
					}
				}else {
					//nhập sai mật khẩu tăng số lần login sai + 1
					currentCust.setLoginfalseNumber(currentCust.getLoginfalseNumber() + 1 );
					jpaExecute(currentCust);
					baseResponse.setData(currentCust);
					baseResponse.setFwError(FwError.PASSWORDNOTFOUND);
				}
				//kiểm tra số lần đăng nhập sai
				Cust cust = (Cust) baseResponse.getData();
				if (cust.getLoginfalseNumber() > Constants.LIMIT_LOGIN_FALSE) {
					//thoat ra ngoai 
//					refreshTokenIbService.deleteByCustId(cust.getId());
					baseResponse.setFwError(FwError.PASSWORDFAILLIMIT);
				}
			}catch (Exception e) {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
				logger.error("error",e);
			}
			return baseResponse;
		}
	
	//TruongPN: quen mat khau
		public BaseResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setFwError(FwError.THANHCONG);
			try {
				//check thong tin nhap vao
				Cust cust = custRepository.findByCode(forgotPasswordRequest.getUsername()).orElse(null);
				if(cust != null) {
					
					if(cust.getEmail().equals(forgotPasswordRequest.getEmail()) &&
						cust.getTel().equals(forgotPasswordRequest.getTel()) &&
						cust.getCif().equals(forgotPasswordRequest.getCif())) {
						/* check ma capcha */
						String newPass = "123456";
						cust.setPassword(encoder.encode(newPass));
						jpaExecute(cust);
						baseResponse.setData(cust);
						
						//save pass moi passwordhis
						PasswordHi passwordHis = new PasswordHi();
						passwordHis.setCust(cust);
						passwordHiRepository.save(passwordHis);
						/*
						 * gui pass moi qua sms
						 */
					}else {
						baseResponse.setFwError(FwError.DLKHONGTONTAI);
					}
				}else {
					baseResponse.setFwError(FwError.DLKHONGTONTAI);
				}
			}catch (Exception e) {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
				logger.error("error",e);			}
			return baseResponse;
		}
		
		
//		private static Random generator = new Random();
//		private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
//	    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
//	    private static final String digits = "0123456789"; // 0-9
//	    private static final String specials = "~=+%^*/()[]{}/!@#$?|";//ky tu dac biet
//	    private static final String ALL = alpha + alphaUpperCase + digits + specials;
//	    
//		public String randomPassword(int numberOfCharactor) {
//	        List<String> result = new ArrayList<>();
//	        Consumer<String> appendChar = s -> {
//	            int number = randomNumber(0, s.length() - 1);
//	            result.add("" + s.charAt(number));
//	        };
//	        appendChar.accept(digits);
//	        appendChar.accept(specials);
//	        while (result.size() < numberOfCharactor) {
//	            appendChar.accept(ALL);
//	        }
//	        Collections.shuffle(result, generator);
//	        return String.join("", result);
//	    }
//		 public static int randomNumber(int min, int max) {
//		        return generator.nextInt((max - min) + 1) + min;
//		 }
//		 public static void main(String[] args) {
//			CustServiceImpl rand = new CustServiceImpl();
//			String a = rand.randomPassword(8);
//			System.out.println(a);
//		}
}
