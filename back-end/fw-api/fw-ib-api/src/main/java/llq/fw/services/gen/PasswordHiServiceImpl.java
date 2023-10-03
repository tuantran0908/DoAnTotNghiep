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
import llq.fw.cm.models.PasswordHi;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.PasswordHiRepository;
import llq.fw.payload.request.gen.PasswordHiSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class PasswordHiServiceImpl implements BaseService<BaseResponse, PasswordHi,PasswordHiSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(PasswordHiServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected PasswordHiRepository passwordHiRepository;
	@Override
	public BaseResponse create(PasswordHi passwordHi) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var passwordHiNew = PasswordHi.builder()
					.password(passwordHi.getPassword())
					.cust(passwordHi.getCust())
					.build();
			jpaExecute(passwordHiNew);
			baseResponse.setData(passwordHiNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(PasswordHi passwordHi) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var passwordHiCur = passwordHiRepository.findById(passwordHi.getId()).orElse(null);
			if (passwordHiCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			passwordHiCur.setPassword(passwordHi.getPassword());
			passwordHiCur.setCust(passwordHi.getCust());
			jpaExecute(passwordHiCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(passwordHiCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(PasswordHiSearchRequest passwordHi, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<PasswordHi> specification = new Specification<PasswordHi>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<PasswordHi> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(passwordHi.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 passwordHi.getId())));
					}
					if (!ObjectUtils.isEmpty(passwordHi.getPassword())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("password")),
								"%" + passwordHi.getPassword().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(passwordHi.getCust())) {
						String[] ids=passwordHi.getCust().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("cust").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("cust").get("id"),
									 passwordHi.getCust().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<PasswordHi> page = passwordHiRepository.findAll(specification, pageable);
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
			var passwordHiCur = passwordHiRepository.findById(id).orElse(null);
			if (passwordHiCur == null) {
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
	public void jpaExecute(PasswordHi passwordHi) {
		passwordHiRepository.save(passwordHi);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		passwordHiRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var passwordHiCur = passwordHiRepository.findById(id).orElse(null);
		if (passwordHiCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(passwordHiCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(passwordHiRepository.existsById(id));
		return baseResponse;
	}
}
