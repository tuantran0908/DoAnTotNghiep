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
import llq.fw.cm.models.PaymentDesDefine;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.PaymentDesDefineRepository;
import llq.fw.payload.request.gen.PaymentDesDefineSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class PaymentDesDefineServiceImpl implements BaseService<BaseResponse, PaymentDesDefine,PaymentDesDefineSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(PaymentDesDefineServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected PaymentDesDefineRepository paymentDesDefineRepository;
	@Override
	public BaseResponse create(PaymentDesDefine paymentDesDefine) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var paymentDesDefineNew = PaymentDesDefine.builder()
					.code(paymentDesDefine.getCode())
					.content(paymentDesDefine.getContent())
					.status(paymentDesDefine.getStatus())
					.build();
			jpaExecute(paymentDesDefineNew);
			baseResponse.setData(paymentDesDefineNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(PaymentDesDefine paymentDesDefine) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var paymentDesDefineCur = paymentDesDefineRepository.findById(paymentDesDefine.getId()).orElse(null);
			if (paymentDesDefineCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			paymentDesDefineCur.setCode(paymentDesDefine.getCode());
			paymentDesDefineCur.setContent(paymentDesDefine.getContent());
			paymentDesDefineCur.setStatus(paymentDesDefine.getStatus());
			jpaExecute(paymentDesDefineCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(paymentDesDefineCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(PaymentDesDefineSearchRequest paymentDesDefine, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<PaymentDesDefine> specification = new Specification<PaymentDesDefine>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<PaymentDesDefine> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(paymentDesDefine.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 paymentDesDefine.getId())));
					}
					if (!ObjectUtils.isEmpty(paymentDesDefine.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + paymentDesDefine.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(paymentDesDefine.getContent())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("content")),
								"%" + paymentDesDefine.getContent().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(paymentDesDefine.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 paymentDesDefine.getStatus())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<PaymentDesDefine> page = paymentDesDefineRepository.findAll(specification, pageable);
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
			var paymentDesDefineCur = paymentDesDefineRepository.findById(id).orElse(null);
			if (paymentDesDefineCur == null) {
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
	public void jpaExecute(PaymentDesDefine paymentDesDefine) {
		paymentDesDefineRepository.save(paymentDesDefine);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		paymentDesDefineRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var paymentDesDefineCur = paymentDesDefineRepository.findById(id).orElse(null);
		if (paymentDesDefineCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(paymentDesDefineCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(paymentDesDefineRepository.existsById(id));
		return baseResponse;
	}
}
