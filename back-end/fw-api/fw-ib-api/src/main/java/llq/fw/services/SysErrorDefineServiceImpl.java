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
import llq.fw.cm.models.SysErrorDefine;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.SysErrorDefineRepository;
import llq.fw.payload.request.gen.SysErrorDefineSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class SysErrorDefineServiceImpl implements BaseService<BaseResponse, SysErrorDefine,SysErrorDefineSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(SysErrorDefineServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected SysErrorDefineRepository sysErrorDefineRepository;
	@Override
	public BaseResponse create(SysErrorDefine sysErrorDefine) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var sysErrorDefineCur = sysErrorDefineRepository.findById(sysErrorDefine.getCode()).orElse(null);
			if (sysErrorDefineCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var sysErrorDefineNew = SysErrorDefine.builder()
					.code(sysErrorDefine.getCode().toUpperCase())
					.content(sysErrorDefine.getContent())
					.contentEn(sysErrorDefine.getContentEn())
					.description(sysErrorDefine.getDescription())
					.function(sysErrorDefine.getFunction())
					.name(sysErrorDefine.getName())
					.build();
			jpaExecute(sysErrorDefineNew);
			baseResponse.setData(sysErrorDefineNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(SysErrorDefine sysErrorDefine) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var sysErrorDefineCur = sysErrorDefineRepository.findById(sysErrorDefine.getCode()).orElse(null);
			if (sysErrorDefineCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			sysErrorDefineCur.setContent(sysErrorDefine.getContent());
			sysErrorDefineCur.setContentEn(sysErrorDefine.getContentEn());
			sysErrorDefineCur.setDescription(sysErrorDefine.getDescription());
			sysErrorDefineCur.setFunction(sysErrorDefine.getFunction());
			sysErrorDefineCur.setName(sysErrorDefine.getName());
			jpaExecute(sysErrorDefineCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(sysErrorDefineCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(SysErrorDefineSearchRequest sysErrorDefine, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<SysErrorDefine> specification = new Specification<SysErrorDefine>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<SysErrorDefine> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(sysErrorDefine.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + sysErrorDefine.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(sysErrorDefine.getFunction())) {
						List<String> functionSysErrorSearch = new ArrayList<>();
						for(String str : sysErrorDefine.getFunction().split(",")) {
							functionSysErrorSearch.add(str);
						}
						predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("function")).value(functionSysErrorSearch)));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<SysErrorDefine> page = sysErrorDefineRepository.findAll(specification, pageable);
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
			var sysErrorDefineCur = sysErrorDefineRepository.findById(id).orElse(null);
			if (sysErrorDefineCur == null) {
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
	public void jpaExecute(SysErrorDefine sysErrorDefine) {
		sysErrorDefineRepository.save(sysErrorDefine);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		sysErrorDefineRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var sysErrorDefineCur = sysErrorDefineRepository.findById(id).orElse(null);
		if (sysErrorDefineCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(sysErrorDefineCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(sysErrorDefineRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
