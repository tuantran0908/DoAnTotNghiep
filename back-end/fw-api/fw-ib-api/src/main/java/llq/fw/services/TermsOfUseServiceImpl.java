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
import llq.fw.cm.models.TermsOfUse;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.TermsOfUseRepository;
import llq.fw.payload.request.gen.TermsOfUseSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TermsOfUseServiceImpl implements BaseService<BaseResponse, TermsOfUse,TermsOfUseSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(TermsOfUseServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TermsOfUseRepository termsOfUseRepository;
	@Override
	public BaseResponse create(TermsOfUse termsOfUse) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var termsOfUseNew = TermsOfUse.builder()
					.code(termsOfUse.getCode())
					.content(termsOfUse.getContent())
					.name(termsOfUse.getName())
					.status(termsOfUse.getStatus())
					.build();
			jpaExecute(termsOfUseNew);
			baseResponse.setData(termsOfUseNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TermsOfUse termsOfUse) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var termsOfUseCur = termsOfUseRepository.findById(termsOfUse.getId()).orElse(null);
			if (termsOfUseCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			termsOfUseCur.setCode(termsOfUse.getCode());
			termsOfUseCur.setContent(termsOfUse.getContent());
			termsOfUseCur.setName(termsOfUse.getName());
			termsOfUseCur.setStatus(termsOfUse.getStatus());
			jpaExecute(termsOfUseCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(termsOfUseCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(TermsOfUseSearchRequest termsOfUse, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<TermsOfUse> specification = new Specification<TermsOfUse>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TermsOfUse> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(termsOfUse.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 termsOfUse.getId())));
					}
					if (!ObjectUtils.isEmpty(termsOfUse.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + termsOfUse.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(termsOfUse.getContent())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("content")),
								"%" + termsOfUse.getContent().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(termsOfUse.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + termsOfUse.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(termsOfUse.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 termsOfUse.getStatus())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TermsOfUse> page = termsOfUseRepository.findAll(specification, pageable);
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
			var termsOfUseCur = termsOfUseRepository.findById(id).orElse(null);
			if (termsOfUseCur == null) {
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
	public void jpaExecute(TermsOfUse termsOfUse) {
		termsOfUseRepository.save(termsOfUse);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		termsOfUseRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var termsOfUseCur = termsOfUseRepository.findById(id).orElse(null);
		if (termsOfUseCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(termsOfUseCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(termsOfUseRepository.existsById(id));
		return baseResponse;
	}
	public BaseResponse getAll() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		List<TermsOfUse> lstTerm = termsOfUseRepository.findAll();
		if(lstTerm.size() > 0) {
			baseResponse.setData(lstTerm);
			baseResponse.setFwError(FwError.THANHCONG);
		}else {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
}
