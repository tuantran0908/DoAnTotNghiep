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
import llq.fw.cm.models.SysParameter;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.SysParameterRepository;
import llq.fw.payload.request.gen.SysParameterSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class SysParameterServiceImpl implements BaseService<BaseResponse, SysParameter,SysParameterSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(SysParameterServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected SysParameterRepository sysParameterRepository;
	@Override
	public BaseResponse create(SysParameter sysParameter) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var sysParameterCur = sysParameterRepository.findById(sysParameter.getCode()).orElse(null);
			if (sysParameterCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var sysParameterNew = SysParameter.builder()
					.code(sysParameter.getCode().toUpperCase())
					.name(sysParameter.getName())
					.type(sysParameter.getType())
					.value(sysParameter.getValue())
					.build();
			jpaExecute(sysParameterNew);
			baseResponse.setData(sysParameterNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(SysParameter sysParameter) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var sysParameterCur = sysParameterRepository.findById(sysParameter.getCode()).orElse(null);
			if (sysParameterCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			sysParameterCur.setName(sysParameter.getName());
			sysParameterCur.setType(sysParameter.getType());
			sysParameterCur.setValue(sysParameter.getValue());
			jpaExecute(sysParameterCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(sysParameterCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(SysParameterSearchRequest sysParameter, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<SysParameter> specification = new Specification<SysParameter>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<SysParameter> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(sysParameter.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + sysParameter.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(sysParameter.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + sysParameter.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(sysParameter.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("type")),
								"%" + sysParameter.getType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(sysParameter.getValue())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("value")),
								"%" + sysParameter.getValue().toUpperCase() + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<SysParameter> page = sysParameterRepository.findAll(specification, pageable);
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
			var sysParameterCur = sysParameterRepository.findById(id).orElse(null);
			if (sysParameterCur == null) {
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
	public void jpaExecute(SysParameter sysParameter) {
		sysParameterRepository.save(sysParameter);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		sysParameterRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var sysParameterCur = sysParameterRepository.findById(id).orElse(null);
		if (sysParameterCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(sysParameterCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(sysParameterRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
