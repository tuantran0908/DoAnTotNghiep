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
import llq.fw.cm.models.CustomerGuide;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.CustomerGuideRepository;
import llq.fw.payload.request.gen.CustomerGuideSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class CustomerGuideServiceImpl implements BaseService<BaseResponse, CustomerGuide,CustomerGuideSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustomerGuideServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CustomerGuideRepository customerGuideRepository;
	@Override
	public BaseResponse create(CustomerGuide customerGuide) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var customerGuideNew = CustomerGuide.builder()
					.data(customerGuide.getData())
					.filename(customerGuide.getFilename())
					.findex(customerGuide.getFindex())
					.name(customerGuide.getName())
					.status(customerGuide.getStatus())
					.build();
			jpaExecute(customerGuideNew);
			baseResponse.setData(customerGuideNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(CustomerGuide customerGuide) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var customerGuideCur = customerGuideRepository.findById(customerGuide.getId()).orElse(null);
			if (customerGuideCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			customerGuideCur.setData(customerGuide.getData());
			customerGuideCur.setFilename(customerGuide.getFilename());
			customerGuideCur.setFindex(customerGuide.getFindex());
			customerGuideCur.setName(customerGuide.getName());
			customerGuideCur.setStatus(customerGuide.getStatus());
			jpaExecute(customerGuideCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(customerGuideCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(CustomerGuideSearchRequest customerGuide, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CustomerGuide> specification = new Specification<CustomerGuide>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<CustomerGuide> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(customerGuide.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 customerGuide.getId())));
					}
					if (!ObjectUtils.isEmpty(customerGuide.getFilename())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("filename")),
								"%" + customerGuide.getFilename().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(customerGuide.getFindex())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("findex")),
								 customerGuide.getFindex())));
					}
					if (!ObjectUtils.isEmpty(customerGuide.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + customerGuide.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(customerGuide.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 customerGuide.getStatus())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CustomerGuide> page = customerGuideRepository.findAll(specification, pageable);
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
			var customerGuideCur = customerGuideRepository.findById(id).orElse(null);
			if (customerGuideCur == null) {
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
	public void jpaExecute(CustomerGuide customerGuide) {
		customerGuideRepository.save(customerGuide);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		customerGuideRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var customerGuideCur = customerGuideRepository.findById(id).orElse(null);
		if (customerGuideCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(customerGuideCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(customerGuideRepository.existsById(id));
		return baseResponse;
	}
}
