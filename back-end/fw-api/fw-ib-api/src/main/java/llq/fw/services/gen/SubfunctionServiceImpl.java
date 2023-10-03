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
import llq.fw.cm.models.Subfunction;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.SubfunctionRepository;
import llq.fw.payload.request.gen.SubfunctionSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class SubfunctionServiceImpl implements BaseService<BaseResponse, Subfunction,SubfunctionSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(SubfunctionServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected SubfunctionRepository subfunctionRepository;
	@Override
	public BaseResponse create(Subfunction subfunction) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var subfunctionCur = subfunctionRepository.findById(subfunction.getCode()).orElse(null);
			if (subfunctionCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var subfunctionNew = Subfunction.builder()
					.code(subfunction.getCode().toUpperCase())
					.name(subfunction.getName())
					.status(subfunction.getStatus())
					.rolesGroupSubs(subfunction.getRolesGroupSubs())
					.function(subfunction.getFunction())
					.build();
			jpaExecute(subfunctionNew);
			baseResponse.setData(subfunctionNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Subfunction subfunction) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var subfunctionCur = subfunctionRepository.findById(subfunction.getCode()).orElse(null);
			if (subfunctionCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			subfunctionCur.setName(subfunction.getName());
			subfunctionCur.setStatus(subfunction.getStatus());
			subfunctionCur.setRolesGroupSubs(subfunction.getRolesGroupSubs());
			subfunctionCur.setFunction(subfunction.getFunction());
			jpaExecute(subfunctionCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(subfunctionCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(SubfunctionSearchRequest subfunction, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Subfunction> specification = new Specification<Subfunction>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Subfunction> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(subfunction.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + subfunction.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(subfunction.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + subfunction.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(subfunction.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 subfunction.getStatus())));
					}
					if (!ObjectUtils.isEmpty(subfunction.getFunction())) {
						String[] ids=subfunction.getFunction().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("function").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("function").get("code"),
									 subfunction.getFunction().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Subfunction> page = subfunctionRepository.findAll(specification, pageable);
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
			var subfunctionCur = subfunctionRepository.findById(id).orElse(null);
			if (subfunctionCur == null) {
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
	public void jpaExecute(Subfunction subfunction) {
		subfunctionRepository.save(subfunction);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		subfunctionRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var subfunctionCur = subfunctionRepository.findById(id).orElse(null);
		if (subfunctionCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(subfunctionCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(subfunctionRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
