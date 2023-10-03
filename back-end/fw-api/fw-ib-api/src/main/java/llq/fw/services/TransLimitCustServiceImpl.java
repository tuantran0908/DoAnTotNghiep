package llq.fw.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.models.Company;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustType;
import llq.fw.cm.models.TransLimit;
import llq.fw.cm.models.TransLimitCust;
import llq.fw.cm.models.TransLimitDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.CompanyRepository;
import llq.fw.cm.repository.CustTypeRepository;
import llq.fw.cm.repository.TransLimitRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.repository.TransLimitCustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.gen.TransLimitCustSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TransLimitCustServiceImpl implements BaseService<BaseResponse, TransLimitCust,TransLimitCustSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(TransLimitCustServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransLimitCustRepository transLimitCustRepository;
	@Autowired
	private CustRepository custRepository;
	@Autowired
	private TransLimitRepository transLimitRepository;
	@Override
	public BaseResponse create(TransLimitCust transLimitCust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLimitCustNew = TransLimitCust.builder()
					.maxdaily(transLimitCust.getMaxdaily())
					.maxtrans(transLimitCust.getMaxtrans())
					.product(transLimitCust.getProduct())
					.productName(transLimitCust.getProductName())
					.cust(transLimitCust.getCust())
					.build();	
			jpaExecute(transLimitCustNew);
			baseResponse.setData(transLimitCustNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TransLimitCust transLimitCust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLimitCustCur = transLimitCustRepository.findById(transLimitCust.getId()).orElse(null);
			if (transLimitCustCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transLimitCustCur.setMaxdaily(transLimitCust.getMaxdaily());
			transLimitCustCur.setMaxtrans(transLimitCust.getMaxtrans());
			transLimitCustCur.setProduct(transLimitCust.getProduct());
			transLimitCustCur.setProductName(transLimitCust.getProductName());
			transLimitCustCur.setCust(transLimitCust.getCust());
			jpaExecute(transLimitCustCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transLimitCustCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	public BaseResponse updateAll(List<TransLimitCust> transLimitCust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			if(checkListTransLimit(transLimitCust) == true) {
				jpaUpdateExecute(transLimitCust);
				baseResponse.setData(transLimitCust);
			}else {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
			}
			
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	
	@Override
	public BaseResponse search(TransLimitCustSearchRequest transLimitCust, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<TransLimitCust> specification = new Specification<TransLimitCust>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TransLimitCust> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(transLimitCust.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 transLimitCust.getId())));
					}
					if (!ObjectUtils.isEmpty(transLimitCust.getMaxdaily())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("maxdaily")),
								 transLimitCust.getMaxdaily())));
					}
					if (!ObjectUtils.isEmpty(transLimitCust.getMaxtrans())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("maxtrans")),
								 transLimitCust.getMaxtrans())));
					}
					if (!ObjectUtils.isEmpty(transLimitCust.getProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("product")),
								"%" + transLimitCust.getProduct().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLimitCust.getProductName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("productName")),
								"%" + transLimitCust.getProductName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLimitCust.getCust())) {
						String[] ids=transLimitCust.getCust().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("cust").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("cust").get("id"),
									 transLimitCust.getCust().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransLimitCust> page = transLimitCustRepository.findAll(specification, pageable);
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
			var transLimitCustCur = transLimitCustRepository.findById(id).orElse(null);
			if (transLimitCustCur == null) {
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
	public void jpaExecute(TransLimitCust transLimitCust) {
		transLimitCustRepository.save(transLimitCust);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		transLimitCustRepository.deleteById(id);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaUpdateExecute(List<TransLimitCust> translimitCust) {
		transLimitCustRepository.saveAll(translimitCust);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transLimitCustCur = transLimitCustRepository.findById(id).orElse(null);
		if (transLimitCustCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transLimitCustCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transLimitCustRepository.existsById(id));
		return baseResponse;
	}
	
	public BaseResponse getCust(Long position){
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		//lấy user đang đăng nhập
		CustDetailsImpl userDetail =  (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//lây thông tin cust từ user đăng nhập
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		List<Cust> lstCust = custRepository.getCustByPosition(currentCust.getCompany().getId().toString(),position);
		baseResponse.setData(lstCust);
		return baseResponse;
	}
	
	public Cust getCustByCode(String code){
		return custRepository.findByCode(code).orElse(null);
	}
	
	public BaseResponse getDetailByCust(Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		Set<TransLimitCust> transLimitCustCur = transLimitCustRepository.findByCust(cust);
		if (transLimitCustCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transLimitCustCur);
		return baseResponse;
	}

	
	//set info translimitcust de tao cust
	public Set<TransLimitCust> createTransLimitCust(){
		//lấy user đang đăng nhập
		CustDetailsImpl userDetail =  (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//lây thông tin cust từ user đăng nhập
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		CustType custT = currentCust.getCompany().getCustType();
		TransLimit trans = transLimitRepository.findByCustTypeId(custT);
		Set<TransLimitDetail> transDetails = trans.getTransLimitDetail();
		Set<TransLimitCust> lstTransCust = new HashSet<>();
		for (TransLimitDetail transLimitDetail : transDetails) {
			TransLimitCust transLimitCust = new TransLimitCust();
			transLimitCust.setMaxdaily(transLimitDetail.getMaxdaily().longValue());
			transLimitCust.setMaxtrans(transLimitDetail.getMaxtrans());
			transLimitCust.setProduct(transLimitDetail.getProduct());
			transLimitCust.setProductName(transLimitDetail.getProductName());
			lstTransCust.add(transLimitCust);
		}
		return lstTransCust;
	}
	
	//check han muc khi update
	public Boolean checkListTransLimit(List<TransLimitCust> transLimitCust) {
		//lấy user đang đăng nhập
		CustDetailsImpl userDetail =  (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//lây thông tin cust từ user đăng nhập
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		CustType custT = currentCust.getCompany().getCustType();
		TransLimit trans = transLimitRepository.findByCustTypeId(custT);
		Set<TransLimitDetail> transDetails = trans.getTransLimitDetail();
		Set<TransLimitCust> lstTransCust = new HashSet<>();
		for (TransLimitDetail transLimitDetail : transDetails) {
			TransLimitCust transLimitCustNew = new TransLimitCust();
			transLimitCustNew.setMaxdaily(transLimitDetail.getMaxdaily().longValue());
			transLimitCustNew.setMaxtrans(transLimitDetail.getMaxtrans());
			transLimitCustNew.setProduct(transLimitDetail.getProduct());
			transLimitCustNew.setProductName(transLimitDetail.getProductName());
			lstTransCust.add(transLimitCustNew);
		}
		if(transLimitCust.get(0).getCust().getPosition().equals(PositionEnum.LAPLENH)) {
			for (TransLimitCust transLimitCustDetail : lstTransCust) {
				for (TransLimitCust transLimitCustUpdate : transLimitCust) {	
					if(transLimitCustUpdate.getMaxtrans() > transLimitCustDetail.getMaxtrans() &&
					   transLimitCustUpdate.getMaxdaily() > transLimitCustDetail.getMaxdaily()) {
						return false;
					}
				}
			}
		}
		if(transLimitCust.get(0).getCust().getPosition().equals(PositionEnum.DUYETLENH)) {
			for (TransLimitCust transLimitCustDetail : lstTransCust) {
				for (TransLimitCust transLimitCustUpdate : transLimitCust) {	
					if(transLimitCustUpdate.getMaxtrans() > transLimitCustDetail.getMaxtrans()) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
