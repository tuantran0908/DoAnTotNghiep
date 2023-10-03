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
import llq.fw.cm.models.Fdtype;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.FdtypeRepository;
import llq.fw.payload.request.gen.FdtypeSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class FdtypeServiceImpl implements BaseService<BaseResponse, Fdtype,FdtypeSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(FdtypeServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected FdtypeRepository fdtypeRepository;
	@Override
	public BaseResponse create(Fdtype fdtype) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var fdtypeCur = fdtypeRepository.findById(fdtype.getCode()).orElse(null);
			if (fdtypeCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var fdtypeNew = Fdtype.builder()
					.code(fdtype.getCode().toUpperCase())
					.ib(fdtype.getIb())
					.name(fdtype.getName())
					.status(fdtype.getStatus())
					.build();
			jpaExecute(fdtypeNew);
			baseResponse.setData(fdtypeNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Fdtype fdtype) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var fdtypeCur = fdtypeRepository.findById(fdtype.getCode()).orElse(null);
			if (fdtypeCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			fdtypeCur.setIb(fdtype.getIb());
			fdtypeCur.setName(fdtype.getName());
			fdtypeCur.setStatus(fdtype.getStatus());
			jpaExecute(fdtypeCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(fdtypeCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(FdtypeSearchRequest fdtype, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Fdtype> specification = new Specification<Fdtype>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Fdtype> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(fdtype.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + fdtype.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(fdtype.getIb())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("ib")),
								 fdtype.getIb())));
					}
					if (!ObjectUtils.isEmpty(fdtype.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + fdtype.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(fdtype.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 fdtype.getStatus())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Fdtype> page = fdtypeRepository.findAll(specification, pageable);
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
			var fdtypeCur = fdtypeRepository.findById(id).orElse(null);
			if (fdtypeCur == null) {
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
	public void jpaExecute(Fdtype fdtype) {
		fdtypeRepository.save(fdtype);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		fdtypeRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var fdtypeCur = fdtypeRepository.findById(id).orElse(null);
		if (fdtypeCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(fdtypeCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(fdtypeRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
