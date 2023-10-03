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
import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.RefreshtokenRepository;
import llq.fw.payload.request.gen.RefreshtokenSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class RefreshtokenServiceImpl implements BaseService<BaseResponse, Refreshtoken,RefreshtokenSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(RefreshtokenServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected RefreshtokenRepository refreshtokenRepository;
	@Override
	public BaseResponse create(Refreshtoken refreshtoken) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var refreshtokenNew = Refreshtoken.builder()
					.expiryDate(refreshtoken.getExpiryDate())
					.token(refreshtoken.getToken())
					.user(refreshtoken.getUser())
					.build();
			jpaExecute(refreshtokenNew);
			baseResponse.setData(refreshtokenNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Refreshtoken refreshtoken) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var refreshtokenCur = refreshtokenRepository.findById(refreshtoken.getId()).orElse(null);
			if (refreshtokenCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			refreshtokenCur.setExpiryDate(refreshtoken.getExpiryDate());
			refreshtokenCur.setToken(refreshtoken.getToken());
			refreshtokenCur.setUser(refreshtoken.getUser());
			jpaExecute(refreshtokenCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(refreshtokenCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(RefreshtokenSearchRequest refreshtoken, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Refreshtoken> specification = new Specification<Refreshtoken>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Refreshtoken> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(refreshtoken.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 refreshtoken.getId())));
					}
					if (!ObjectUtils.isEmpty(refreshtoken.getExpiryDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("expiryDate")),
								 refreshtoken.getExpiryDate())));
					}
					if (!ObjectUtils.isEmpty(refreshtoken.getToken())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("token")),
								"%" + refreshtoken.getToken().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(refreshtoken.getUser())) {
						String[] ids=refreshtoken.getUser().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("user").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("user").get("id"),
									 refreshtoken.getUser().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Refreshtoken> page = refreshtokenRepository.findAll(specification, pageable);
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
			var refreshtokenCur = refreshtokenRepository.findById(id).orElse(null);
			if (refreshtokenCur == null) {
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
	public void jpaExecute(Refreshtoken refreshtoken) {
		refreshtokenRepository.save(refreshtoken);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		refreshtokenRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var refreshtokenCur = refreshtokenRepository.findById(id).orElse(null);
		if (refreshtokenCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(refreshtokenCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(refreshtokenRepository.existsById(id));
		return baseResponse;
	}
}
