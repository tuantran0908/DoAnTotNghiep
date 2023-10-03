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
import llq.fw.cm.models.TransLot;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.TransLotRepository;
import llq.fw.payload.request.gen.TransLotSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TransLotServiceImpl implements BaseService<BaseResponse, TransLot,TransLotSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(TransLotServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransLotRepository transLotRepository;
	@Override
	public BaseResponse create(TransLot transLot) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLotCur = transLotRepository.findById(transLot.getCode()).orElse(null);
			if (transLotCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}
	
			var transLotNew = TransLot.builder()
					.code(transLot.getCode().toUpperCase())
					.amount(transLot.getAmount())
					.approvedBy(transLot.getApprovedBy())
					.approvedDate(transLot.getApprovedDate())
					.branch(transLot.getBranch())
					.content(transLot.getContent())
					.custId(transLot.getCustId())
					.fee(transLot.getFee())
					.fileData(transLot.getFileData())
					.sendAccount(transLot.getSendAccount())
					.status(transLot.getStatus())
					.total(transLot.getTotal())
					.transDate(transLot.getTransDate())
					.transDatetime(transLot.getTransDatetime())
					.transTime(transLot.getTransTime())
					.username(transLot.getUsername())
					.vat(transLot.getVat())
					.transLotDetails(transLot.getTransLotDetails())
					.build();
			jpaExecute(transLotNew);
			baseResponse.setData(transLotNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TransLot transLot) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLotCur = transLotRepository.findById(transLot.getCode()).orElse(null);
			if (transLotCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transLotCur.setAmount(transLot.getAmount());
			transLotCur.setApprovedBy(transLot.getApprovedBy());
			transLotCur.setApprovedDate(transLot.getApprovedDate());
			transLotCur.setBranch(transLot.getBranch());
			transLotCur.setContent(transLot.getContent());
			transLotCur.setCustId(transLot.getCustId());
			transLotCur.setFee(transLot.getFee());
			transLotCur.setFileData(transLot.getFileData());
			transLotCur.setSendAccount(transLot.getSendAccount());
			transLotCur.setStatus(transLot.getStatus());
			transLotCur.setTotal(transLot.getTotal());
			transLotCur.setTransDate(transLot.getTransDate());
			transLotCur.setTransDatetime(transLot.getTransDatetime());
			transLotCur.setTransTime(transLot.getTransTime());
			transLotCur.setUsername(transLot.getUsername());
			transLotCur.setVat(transLot.getVat());
			transLotCur.setTransLotDetails(transLot.getTransLotDetails());
			jpaExecute(transLotCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transLotCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(TransLotSearchRequest transLot, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<TransLot> specification = new Specification<TransLot>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TransLot> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(transLot.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + transLot.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getAmount())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("amount")),
								"%" + transLot.getAmount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getApprovedBy())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("approvedBy")),
								"%" + transLot.getApprovedBy().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getApprovedDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(transLot.getApprovedDate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(transLot.getBranch())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("branch")),
								"%" + transLot.getBranch().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getContent())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("content")),
								"%" + transLot.getContent().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getCustId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custId")),
								"%" + transLot.getCustId().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getFee())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fee")),
								"%" + transLot.getFee().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getSendAccount())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sendAccount")),
								"%" + transLot.getSendAccount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
								"%" + transLot.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getTotal())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("total")),
								"%" + transLot.getTotal().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getTransDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transDate")),
								"%" + transLot.getTransDate().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getTransDatetime())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(transLot.getTransDatetime())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(transLot.getTransTime())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transTime")),
								"%" + transLot.getTransTime().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getUsername())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("username")),
								"%" + transLot.getUsername().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLot.getVat())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("vat")),
								"%" + transLot.getVat().toUpperCase() + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransLot> page = transLotRepository.findAll(specification, pageable);
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
			var transLotCur = transLotRepository.findById(id).orElse(null);
			if (transLotCur == null) {
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
	public void jpaExecute(TransLot transLot) {
		transLotRepository.save(transLot);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		transLotRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transLotCur = transLotRepository.findById(id).orElse(null);
		if (transLotCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transLotCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transLotRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
