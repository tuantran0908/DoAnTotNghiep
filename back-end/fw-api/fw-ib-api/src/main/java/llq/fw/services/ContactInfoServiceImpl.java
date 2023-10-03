package llq.fw.services;
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
import llq.fw.cm.models.ContactInfo;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.ContactInfoRepository;
import llq.fw.payload.request.gen.ContactInfoSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class ContactInfoServiceImpl implements BaseService<BaseResponse, ContactInfo,ContactInfoSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(ContactInfoServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected ContactInfoRepository contactInfoRepository;
	@Override
	public BaseResponse create(ContactInfo contactInfo) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var contactInfoCur = contactInfoRepository.findById(contactInfo.getCode()).orElse(null);
			if (contactInfoCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var contactInfoNew = ContactInfo.builder()
					.code(contactInfo.getCode().toUpperCase())
					.address(contactInfo.getAddress())
					.email(contactInfo.getEmail())
					.fax(contactInfo.getFax())
					.hotline(contactInfo.getHotline())
					.tel(contactInfo.getTel())
					.build();
			jpaExecute(contactInfoNew);
			baseResponse.setData(contactInfoNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(ContactInfo contactInfo) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var contactInfoCur = contactInfoRepository.findById(contactInfo.getCode()).orElse(null);
			if (contactInfoCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			contactInfoCur.setAddress(contactInfo.getAddress());
			contactInfoCur.setEmail(contactInfo.getEmail());
			contactInfoCur.setFax(contactInfo.getFax());
			contactInfoCur.setHotline(contactInfo.getHotline());
			contactInfoCur.setTel(contactInfo.getTel());
			jpaExecute(contactInfoCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(contactInfoCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(ContactInfoSearchRequest contactInfo, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<ContactInfo> specification = new Specification<ContactInfo>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<ContactInfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(contactInfo.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + contactInfo.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(contactInfo.getAddress())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("address")),
								"%" + contactInfo.getAddress().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(contactInfo.getEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
								"%" + contactInfo.getEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(contactInfo.getFax())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fax")),
								"%" + contactInfo.getFax().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(contactInfo.getHotline())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("hotline")),
								"%" + contactInfo.getHotline().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(contactInfo.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + contactInfo.getTel().toUpperCase() + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<ContactInfo> page = contactInfoRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse delete(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var contactInfoCur = contactInfoRepository.findById(id).orElse(null);
			if (contactInfoCur == null) {
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
	public void jpaExecute(ContactInfo contactInfo) {
		contactInfoRepository.save(contactInfo);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		contactInfoRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var contactInfoCur = contactInfoRepository.findById(id).orElse(null);
		if (contactInfoCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(contactInfoCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(contactInfoRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
	public BaseResponse getContactInfo() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(contactInfoRepository.findAll().stream().findFirst().get());
		return baseResponse;
	}
}
