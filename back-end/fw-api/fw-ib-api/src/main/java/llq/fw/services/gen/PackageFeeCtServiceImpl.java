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
import llq.fw.cm.models.PackageFeeCt;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.PackageFeeCtRepository;
import llq.fw.payload.request.gen.PackageFeeCtSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class PackageFeeCtServiceImpl implements BaseService<BaseResponse, PackageFeeCt,PackageFeeCtSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(PackageFeeCtServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected PackageFeeCtRepository packageFeeCtRepository;
	@Override
	public BaseResponse create(PackageFeeCt packageFeeCt) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var packageFeeCtNew = PackageFeeCt.builder()
					.custType(packageFeeCt.getCustType())
					.packageFee(packageFeeCt.getPackageFee())
					.build();
			jpaExecute(packageFeeCtNew);
			baseResponse.setData(packageFeeCtNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(PackageFeeCt packageFeeCt) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var packageFeeCtCur = packageFeeCtRepository.findById(packageFeeCt.getId()).orElse(null);
			if (packageFeeCtCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			packageFeeCtCur.setCustType(packageFeeCt.getCustType());
			packageFeeCtCur.setPackageFee(packageFeeCt.getPackageFee());
			jpaExecute(packageFeeCtCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(packageFeeCtCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(PackageFeeCtSearchRequest packageFeeCt, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<PackageFeeCt> specification = new Specification<PackageFeeCt>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<PackageFeeCt> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(packageFeeCt.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 packageFeeCt.getId())));
					}
					if (!ObjectUtils.isEmpty(packageFeeCt.getCustType())) {
						String[] ids=packageFeeCt.getCustType().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("custType").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("custType").get("id"),
									 packageFeeCt.getCustType().toUpperCase())));
						}
					}
					if (!ObjectUtils.isEmpty(packageFeeCt.getPackageFee())) {
						String[] ids=packageFeeCt.getPackageFee().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("packageFee").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("packageFee").get("id"),
									 packageFeeCt.getPackageFee().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<PackageFeeCt> page = packageFeeCtRepository.findAll(specification, pageable);
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
			var packageFeeCtCur = packageFeeCtRepository.findById(id).orElse(null);
			if (packageFeeCtCur == null) {
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
	public void jpaExecute(PackageFeeCt packageFeeCt) {
		packageFeeCtRepository.save(packageFeeCt);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		packageFeeCtRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var packageFeeCtCur = packageFeeCtRepository.findById(id).orElse(null);
		if (packageFeeCtCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(packageFeeCtCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(packageFeeCtRepository.existsById(id));
		return baseResponse;
	}
}
