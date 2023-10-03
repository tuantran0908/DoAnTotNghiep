package llq.fw.services.gen;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.CustType;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.CustTypeRepository;
import llq.fw.payload.request.gen.CustTypeSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class CustTypeServiceImpl implements BaseService<BaseResponse, CustType,CustTypeSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustTypeServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CustTypeRepository custTypeRepository;
	@Override
	public BaseResponse create(CustType custType) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custTypeNew = CustType.builder()
					.code(custType.getCode())
					.description(custType.getDescription())
					.name(custType.getName())
					.status(custType.getStatus())
					.companies(custType.getCompanies())
					.companyTmps(custType.getCompanyTmps())
					.packageFees(custType.getPackageFees())
					.packageFeeCts(custType.getPackageFeeCts())
					.build();
			jpaExecute(custTypeNew);
			baseResponse.setData(custTypeNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(CustType custType) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custTypeCur = custTypeRepository.findById(custType.getId()).orElse(null);
			if (custTypeCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			custTypeCur.setCode(custType.getCode());
			custTypeCur.setDescription(custType.getDescription());
			custTypeCur.setName(custType.getName());
			custTypeCur.setStatus(custType.getStatus());
			custTypeCur.setCompanies(custType.getCompanies());
			custTypeCur.setCompanyTmps(custType.getCompanyTmps());
			custTypeCur.setPackageFees(custType.getPackageFees());
			custTypeCur.setPackageFeeCts(custType.getPackageFeeCts());
			jpaExecute(custTypeCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(custTypeCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(CustTypeSearchRequest custType, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CustType> specification = new Specification<CustType>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<CustType> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(custType.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 custType.getId())));
					}
					if (!ObjectUtils.isEmpty(custType.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + custType.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custType.getDescription())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
								"%" + custType.getDescription().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custType.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + custType.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custType.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 custType.getStatus())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CustType> page = custTypeRepository.findAll(specification, pageable);
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
			var custTypeCur = custTypeRepository.findById(id).orElse(null);
			if (custTypeCur == null) {
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
	public void jpaExecute(CustType custType) {
		custTypeRepository.save(custType);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		custTypeRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var custTypeCur = custTypeRepository.findById(id).orElse(null);
		if (custTypeCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(custTypeCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(custTypeRepository.existsById(id));
		return baseResponse;
	}
}
