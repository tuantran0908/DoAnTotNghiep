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
import llq.fw.cm.models.TransLotDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.TransLotDetailRepository;
import llq.fw.payload.request.gen.TransLotDetailSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TransLotDetailServiceImpl implements BaseService<BaseResponse, TransLotDetail,TransLotDetailSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(TransLotDetailServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransLotDetailRepository transLotDetailRepository;
	@Override
	public BaseResponse create(TransLotDetail transLotDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLotDetailNew = TransLotDetail.builder()
					.code(transLotDetail.getCode())
					.amount(transLotDetail.getAmount())
					.approvedBy(transLotDetail.getApprovedBy())
					.approvedDat(transLotDetail.getApprovedDat())
					.ccy(transLotDetail.getCcy())
					.fee(transLotDetail.getFee())
					.paymentStatus(transLotDetail.getPaymentStatus())
					.receiveAccount(transLotDetail.getReceiveAccount())
					.receiveBank(transLotDetail.getReceiveBank())
					.receiveName(transLotDetail.getReceiveName())
					.reqId(transLotDetail.getReqId())
					.resCode(transLotDetail.getResCode())
					.resDes(transLotDetail.getResDes())
					.resId(transLotDetail.getResId())
					.revertCode(transLotDetail.getRevertCode())
					.revertDes(transLotDetail.getRevertDes())
					.revertFeeCode(transLotDetail.getRevertFeeCode())
					.revertFeeDes(transLotDetail.getRevertFeeDes())
					.transDate(transLotDetail.getTransDate())
					.transDatetime(transLotDetail.getTransDatetime())
					.transTime(transLotDetail.getTransTime())
					.txnum(transLotDetail.getTxnum())
					.type(transLotDetail.getType())
					.vat(transLotDetail.getVat())
					.transLot(transLotDetail.getTransLot())
					.build();
			jpaExecute(transLotDetailNew);
			baseResponse.setData(transLotDetailNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TransLotDetail transLotDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transLotDetailCur = transLotDetailRepository.findById(transLotDetail.getId()).orElse(null);
			if (transLotDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transLotDetailCur.setCode(transLotDetail.getCode());
			transLotDetailCur.setAmount(transLotDetail.getAmount());
			transLotDetailCur.setApprovedBy(transLotDetail.getApprovedBy());
			transLotDetailCur.setApprovedDat(transLotDetail.getApprovedDat());
			transLotDetailCur.setCcy(transLotDetail.getCcy());
			transLotDetailCur.setFee(transLotDetail.getFee());
			transLotDetailCur.setPaymentStatus(transLotDetail.getPaymentStatus());
			transLotDetailCur.setReceiveAccount(transLotDetail.getReceiveAccount());
			transLotDetailCur.setReceiveBank(transLotDetail.getReceiveBank());
			transLotDetailCur.setReceiveName(transLotDetail.getReceiveName());
			transLotDetailCur.setReqId(transLotDetail.getReqId());
			transLotDetailCur.setResCode(transLotDetail.getResCode());
			transLotDetailCur.setResDes(transLotDetail.getResDes());
			transLotDetailCur.setResId(transLotDetail.getResId());
			transLotDetailCur.setRevertCode(transLotDetail.getRevertCode());
			transLotDetailCur.setRevertDes(transLotDetail.getRevertDes());
			transLotDetailCur.setRevertFeeCode(transLotDetail.getRevertFeeCode());
			transLotDetailCur.setRevertFeeDes(transLotDetail.getRevertFeeDes());
			transLotDetailCur.setTransDate(transLotDetail.getTransDate());
			transLotDetailCur.setTransDatetime(transLotDetail.getTransDatetime());
			transLotDetailCur.setTransTime(transLotDetail.getTransTime());
			transLotDetailCur.setTxnum(transLotDetail.getTxnum());
			transLotDetailCur.setType(transLotDetail.getType());
			transLotDetailCur.setVat(transLotDetail.getVat());
			transLotDetailCur.setTransLot(transLotDetail.getTransLot());
			jpaExecute(transLotDetailCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transLotDetailCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(TransLotDetailSearchRequest transLotDetail, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<TransLotDetail> specification = new Specification<TransLotDetail>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TransLotDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(transLotDetail.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 transLotDetail.getId())));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + transLotDetail.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getAmount())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("amount")),
								"%" + transLotDetail.getAmount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getApprovedBy())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("approvedBy")),
								"%" + transLotDetail.getApprovedBy().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getApprovedDat())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDat")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(transLotDetail.getApprovedDat())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getCcy())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("ccy")),
								"%" + transLotDetail.getCcy().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getFee())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fee")),
								"%" + transLotDetail.getFee().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getPaymentStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("paymentStatus")),
								"%" + transLotDetail.getPaymentStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getReceiveAccount())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
								"%" + transLotDetail.getReceiveAccount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getReceiveBank())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveBank")),
								"%" + transLotDetail.getReceiveBank().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getReceiveName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveName")),
								"%" + transLotDetail.getReceiveName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getReqId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("reqId")),
								"%" + transLotDetail.getReqId().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getResCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("resCode")),
								"%" + transLotDetail.getResCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getResDes())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("resDes")),
								"%" + transLotDetail.getResDes().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getResId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("resId")),
								"%" + transLotDetail.getResId().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getRevertCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("revertCode")),
								"%" + transLotDetail.getRevertCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getRevertDes())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("revertDes")),
								"%" + transLotDetail.getRevertDes().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getRevertFeeCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("revertFeeCode")),
								"%" + transLotDetail.getRevertFeeCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getRevertFeeDes())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("revertFeeDes")),
								"%" + transLotDetail.getRevertFeeDes().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getTransDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transDate")),
								"%" + transLotDetail.getTransDate().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getTransDatetime())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(transLotDetail.getTransDatetime())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getTransTime())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transTime")),
								"%" + transLotDetail.getTransTime().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getTxnum())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("txnum")),
								"%" + transLotDetail.getTxnum().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("type")),
								"%" + transLotDetail.getType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getVat())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("vat")),
								"%" + transLotDetail.getVat().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(transLotDetail.getTransLot())) {
						String[] ids=transLotDetail.getTransLot().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("transLot").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("transLot").get("code"),
									 transLotDetail.getTransLot().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransLotDetail> page = transLotDetailRepository.findAll(specification, pageable);
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
			var transLotDetailCur = transLotDetailRepository.findById(id).orElse(null);
			if (transLotDetailCur == null) {
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
	public void jpaExecute(TransLotDetail transLotDetail) {
		transLotDetailRepository.save(transLotDetail);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		transLotDetailRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transLotDetailCur = transLotDetailRepository.findById(id).orElse(null);
		if (transLotDetailCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transLotDetailCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transLotDetailRepository.existsById(id));
		return baseResponse;
	}
}
