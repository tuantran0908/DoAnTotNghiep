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
import llq.fw.cm.models.RefreshtokenIb;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.RefreshtokenIbRepository;
import llq.fw.payload.request.gen.RefreshtokenIbSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class RefreshtokenIbServiceImpl implements BaseService<BaseResponse, RefreshtokenIb,RefreshtokenIbSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(RefreshtokenIbServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected RefreshtokenIbRepository refreshtokenIbRepository;
	@Override
	public BaseResponse create(RefreshtokenIb refreshtokenIb) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var refreshtokenIbNew = RefreshtokenIb.builder()
					.expiryDate(refreshtokenIb.getExpiryDate())
					.token(refreshtokenIb.getToken())
					.cust(refreshtokenIb.getCust())
					.build();
			jpaExecute(refreshtokenIbNew);
			baseResponse.setData(refreshtokenIbNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(RefreshtokenIb refreshtokenIb) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var refreshtokenIbCur = refreshtokenIbRepository.findById(refreshtokenIb.getId()).orElse(null);
			if (refreshtokenIbCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			refreshtokenIbCur.setExpiryDate(refreshtokenIb.getExpiryDate());
			refreshtokenIbCur.setToken(refreshtokenIb.getToken());
			refreshtokenIbCur.setCust(refreshtokenIb.getCust());
			jpaExecute(refreshtokenIbCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(refreshtokenIbCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(RefreshtokenIbSearchRequest refreshtokenIb, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<RefreshtokenIb> specification = new Specification<RefreshtokenIb>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<RefreshtokenIb> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(refreshtokenIb.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 refreshtokenIb.getId())));
					}
					if (!ObjectUtils.isEmpty(refreshtokenIb.getExpiryDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("expiryDate")),
								 refreshtokenIb.getExpiryDate())));
					}
					if (!ObjectUtils.isEmpty(refreshtokenIb.getToken())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("token")),
								"%" + refreshtokenIb.getToken().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(refreshtokenIb.getCust())) {
						String[] ids=refreshtokenIb.getCust().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("cust").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("cust").get("id"),
									 refreshtokenIb.getCust().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<RefreshtokenIb> page = refreshtokenIbRepository.findAll(specification, pageable);
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
			var refreshtokenIbCur = refreshtokenIbRepository.findById(id).orElse(null);
			if (refreshtokenIbCur == null) {
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
	public void jpaExecute(RefreshtokenIb refreshtokenIb) {
		refreshtokenIbRepository.save(refreshtokenIb);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		refreshtokenIbRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var refreshtokenIbCur = refreshtokenIbRepository.findById(id).orElse(null);
		if (refreshtokenIbCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(refreshtokenIbCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(refreshtokenIbRepository.existsById(id));
		return baseResponse;
	}
}
