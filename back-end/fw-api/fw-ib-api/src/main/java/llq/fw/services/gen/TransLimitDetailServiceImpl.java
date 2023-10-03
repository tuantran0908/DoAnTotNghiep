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
import llq.fw.cm.models.TransLimitDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.TransLimitDetailRepository;
import llq.fw.payload.request.gen.TransLimitDetailSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TransLimitDetailServiceImpl implements BaseService<BaseResponse, TransLimitDetail,TransLimitDetailSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(TransLimitDetailServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransLimitDetailRepository transLimitDetailRepository;
	@Override
	public BaseResponse create(TransLimitDetail transLimitDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLimitDetailNew = TransLimitDetail.builder()
					.maxdaily(transLimitDetail.getMaxdaily())
					.maxmonth(transLimitDetail.getMaxmonth())
					.maxtrans(transLimitDetail.getMaxtrans())
					.product(transLimitDetail.getProduct())
					.productName(transLimitDetail.getProductName())
					.totaldaily(transLimitDetail.getTotaldaily())
					.transLimit(transLimitDetail.getTransLimit())
					.build();
			jpaExecute(transLimitDetailNew);
			baseResponse.setData(transLimitDetailNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TransLimitDetail transLimitDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLimitDetailCur = transLimitDetailRepository.findById(transLimitDetail.getId()).orElse(null);
			if (transLimitDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transLimitDetailCur.setMaxdaily(transLimitDetail.getMaxdaily());
			transLimitDetailCur.setMaxmonth(transLimitDetail.getMaxmonth());
			transLimitDetailCur.setMaxtrans(transLimitDetail.getMaxtrans());
			transLimitDetailCur.setProduct(transLimitDetail.getProduct());
			transLimitDetailCur.setProductName(transLimitDetail.getProductName());
			transLimitDetailCur.setTotaldaily(transLimitDetail.getTotaldaily());
			transLimitDetailCur.setTransLimit(transLimitDetail.getTransLimit());
			jpaExecute(transLimitDetailCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transLimitDetailCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(TransLimitDetailSearchRequest transLimitDetail, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<TransLimitDetail> specification = new Specification<TransLimitDetail>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TransLimitDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(transLimitDetail.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 transLimitDetail.getId())));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getMaxdaily())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("maxdaily")),
								 transLimitDetail.getMaxdaily())));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getMaxmonth())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("maxmonth")),
								 transLimitDetail.getMaxmonth())));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getMaxtrans())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("maxtrans")),
								 transLimitDetail.getMaxtrans())));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("product")),
								"%" + transLimitDetail.getProduct().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getProductName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("productName")),
								"%" + transLimitDetail.getProductName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getTotaldaily())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("totaldaily")),
								 transLimitDetail.getTotaldaily())));
					}
					if (!ObjectUtils.isEmpty(transLimitDetail.getTransLimit())) {
						String[] ids=transLimitDetail.getTransLimit().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("transLimit").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("transLimit").get("id"),
									 transLimitDetail.getTransLimit().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransLimitDetail> page = transLimitDetailRepository.findAll(specification, pageable);
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
			var transLimitDetailCur = transLimitDetailRepository.findById(id).orElse(null);
			if (transLimitDetailCur == null) {
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
	public void jpaExecute(TransLimitDetail transLimitDetail) {
		transLimitDetailRepository.save(transLimitDetail);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		transLimitDetailRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transLimitDetailCur = transLimitDetailRepository.findById(id).orElse(null);
		if (transLimitDetailCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transLimitDetailCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transLimitDetailRepository.existsById(id));
		return baseResponse;
	}
}
