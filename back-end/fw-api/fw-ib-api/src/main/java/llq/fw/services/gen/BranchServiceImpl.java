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
import llq.fw.cm.models.Branch;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.BranchRepository;
import llq.fw.payload.request.gen.BranchSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class BranchServiceImpl implements BaseService<BaseResponse, Branch,BranchSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected BranchRepository branchRepository;
	@Override
	public BaseResponse create(Branch branch) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var branchCur = branchRepository.findById(branch.getCode()).orElse(null);
			if (branchCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var branchNew = Branch.builder()
					.code(branch.getCode().toUpperCase())
					.address(branch.getAddress())
					.fax(branch.getFax())
					.location(branch.getLocation())
					.name(branch.getName())
					.parentCode(branch.getParentCode())
					.status(branch.getStatus())
					.tel(branch.getTel())
					.type(branch.getType())
					.province(branch.getProvince())
					.build();
			jpaExecute(branchNew);
			baseResponse.setData(branchNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Branch branch) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var branchCur = branchRepository.findById(branch.getCode()).orElse(null);
			if (branchCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			branchCur.setAddress(branch.getAddress());
			branchCur.setFax(branch.getFax());
			branchCur.setLocation(branch.getLocation());
			branchCur.setName(branch.getName());
			branchCur.setParentCode(branch.getParentCode());
			branchCur.setStatus(branch.getStatus());
			branchCur.setTel(branch.getTel());
			branchCur.setType(branch.getType());
			branchCur.setProvince(branch.getProvince());
			jpaExecute(branchCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(branchCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(BranchSearchRequest branch, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Branch> specification = new Specification<Branch>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Branch> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(branch.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + branch.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getAddress())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("address")),
								"%" + branch.getAddress().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getFax())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fax")),
								"%" + branch.getFax().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getLocation())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("location")),
								"%" + branch.getLocation().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + branch.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getParentCode())) {
						String[] ids=branch.getParentCode().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("parentCode").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("parentCode").get("code"),
									 branch.getParentCode().toUpperCase())));
						}
					}
					if (!ObjectUtils.isEmpty(branch.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 branch.getStatus())));
					}
					if (!ObjectUtils.isEmpty(branch.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + branch.getTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(branch.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("type")),
								 branch.getType())));
					}
					if (!ObjectUtils.isEmpty(branch.getProvince())) {
						String[] ids=branch.getProvince().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("province").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("province").get("code"),
									 branch.getProvince().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Branch> page = branchRepository.findAll(specification, pageable);
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
			var branchCur = branchRepository.findById(id).orElse(null);
			if (branchCur == null) {
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
	public void jpaExecute(Branch branch) {
		branchRepository.save(branch);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		branchRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var branchCur = branchRepository.findById(id).orElse(null);
		if (branchCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(branchCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(branchRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
