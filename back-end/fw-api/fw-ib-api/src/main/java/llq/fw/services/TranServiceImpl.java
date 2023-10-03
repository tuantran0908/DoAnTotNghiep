package llq.fw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.Company;
import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.Tran;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.TranRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.TranSearchRequest;
import llq.fw.payload.request.TransRequest;
import llq.fw.cm.services.BaseService;

@Component
public class TranServiceImpl implements BaseService<BaseResponse, Tran, TranSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(TranServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TranRepository tranRepository;
	@Autowired
	protected CustRepository custRepository;

	@Override
	public BaseResponse create(Tran tran) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			if (!ObjectUtils.isEmpty(tran.getCode())) {
				var tranCur = tranRepository.findById(tran.getCode()).orElse(null);
				if (tranCur != null) {
					baseResponse.setFwError(FwError.DLDATONTAI);
					return baseResponse;
				}
			}

			var tranNew = Tran.builder()
					.amount(tran.getAmount())
					.approvedBy(tran.getApprovedBy())
					.approvedDate(tran.getApprovedDate())
					.branch(tran.getBranch())
					.ccy(tran.getCcy())
					.content(tran.getContent())
					.custId(tran.getCustId())
					.fee(tran.getFee())
					.feeType(tran.getFeeType())
					.paymentStatus(tran.getPaymentStatus())
					.receiveAccount(tran.getReceiveAccount())
					.receiveBank(tran.getReceiveBank())
					.receiveName(tran.getReceiveName())
					.reqId(tran.getReqId())
					.resCode(tran.getResCode())
					.resDes(tran.getResDes())
					.resId(tran.getResId())
					.revertCode(tran.getRevertCode())
					.revertDes(tran.getRevertDes())
					.revertFeeCode(tran.getRevertFeeCode())
					.revertFeeDes(tran.getRevertFeeDes())
					.sendAccount(tran.getSendAccount())
					.sendName(tran.getSendName())
					.status(tran.getStatus())
					.transDate(tran.getTransDate())
					.transDatetime(tran.getTransDatetime())
					.transTime(tran.getTransTime())
					.txnum(tran.getTxnum())
					.type(tran.getType())
					.username(tran.getUsername())
					.vat(tran.getVat())
					.build();

			// Check OPT

			//

			jpaExecute(tranNew);
			baseResponse.setData(tranNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse createTrans(TransRequest tran, Cust cust) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			// Check quyen lap lenh hay duyet lenh
			if (cust.getPosition().getValue() != 1 && cust.getPosition().getValue() != 2) {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
				return baseResponse;
			}
			// end Check quyen lap lenh hay duyet lenh

			// Check OPT
			// end Check OPT

			if (!ObjectUtils.isEmpty(tran.getCode())) {
				var tranCur = tranRepository.findById(tran.getCode()).orElse(null);
				if (tranCur != null) {
					baseResponse.setFwError(FwError.DLDATONTAI);
					return baseResponse;
				}
			}

			var tranNew = Tran.builder()
					.amount(tran.getAmount())
					.approvedBy(tran.getApprovedBy())
					.approvedDate(tran.getApprovedDate())
					.username(cust.getCode())
					.sendName(cust.getFullname())
					.branch(tran.getBranch())
					.ccy(tran.getCcy())
					.content(tran.getContent())
					.custId(tran.getCustId())
					.fee(tran.getFee())
					.feeType(tran.getFeeType())
					.paymentStatus(tran.getPaymentStatus())
					.receiveAccount(tran.getReceiveAccount())
					.receiveBank(tran.getReceiveBank())
					.receiveName(tran.getReceiveName())
					.reqId(tran.getReqId())
					.resCode(tran.getResCode())
					.resDes(tran.getResDes())
					.resId(tran.getResId())
					.revertCode(tran.getRevertCode())
					.revertDes(tran.getRevertDes())
					.revertFeeCode(tran.getRevertFeeCode())
					.revertFeeDes(tran.getRevertFeeDes())
					.sendAccount(tran.getSendAccount())
					.status(tran.getStatus())
					.transDate(tran.getTransDate())
					.transDatetime(tran.getTransDatetime())
					.transTime(tran.getTransTime())
					.txnum(tran.getTxnum())
					.type(tran.getType())
					.vat(tran.getVat())
					.build();

			jpaExecute(tranNew);
			baseResponse.setData(tranNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse update(Tran tran) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var tranCur = tranRepository.findById(tran.getCode()).orElse(null);
			if (tranCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			tranCur.setAmount(tran.getAmount());
			tranCur.setApprovedBy(tran.getApprovedBy());
			tranCur.setApprovedDate(tran.getApprovedDate());
			tranCur.setBranch(tran.getBranch());
			tranCur.setCcy(tran.getCcy());
			tranCur.setContent(tran.getContent());
			tranCur.setCustId(tran.getCustId());
			tranCur.setFee(tran.getFee());
			tranCur.setPaymentStatus(tran.getPaymentStatus());
			tranCur.setReceiveAccount(tran.getReceiveAccount());
			tranCur.setReceiveBank(tran.getReceiveBank());
			tranCur.setReceiveName(tran.getReceiveName());
			tranCur.setReqId(tran.getReqId());
			tranCur.setResCode(tran.getResCode());
			tranCur.setResDes(tran.getResDes());
			tranCur.setResId(tran.getResId());
			tranCur.setRevertCode(tran.getRevertCode());
			tranCur.setRevertDes(tran.getRevertDes());
			tranCur.setRevertFeeCode(tran.getRevertFeeCode());
			tranCur.setRevertFeeDes(tran.getRevertFeeDes());
			tranCur.setSendAccount(tran.getSendAccount());
			tranCur.setSendName(tran.getSendName());
			tranCur.setStatus(tran.getStatus());
			tranCur.setTransDate(tran.getTransDate());
			tranCur.setTransDatetime(tran.getTransDatetime());
			tranCur.setTransTime(tran.getTransTime());
			tranCur.setTxnum(tran.getTxnum());
			tranCur.setType(tran.getType());
			tranCur.setUsername(tran.getUsername());
			tranCur.setVat(tran.getVat());
			tranCur.setReason(tran.getReason());
			jpaExecute(tranCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(tranCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(TranSearchRequest tran, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		CustDetailsImpl userDetail = (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);

		// check cust
		if (currentCust != null) {
			if (currentCust.getPosition().equals(PositionEnum.LAPLENH)) {
				tran.setUserLapLenh(currentCust.getCode());
			} else {
				if (currentCust.getPosition().equals(PositionEnum.DUYETLENH)) {
					tran.setUserDuyetLenh(currentCust.getCode());
				}
			}
		}

		try {
			Specification<Tran> specification = new Specification<Tran>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Tran> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(tran.getMaGiaoDich())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + tran.getMaGiaoDich().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tran.getKhoangTienTu())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								((replacedValue).as(Double.class)), Double.parseDouble(tran.getKhoangTienTu()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"),tran.getKhoangTienTu())));
					}

					if (!ObjectUtils.isEmpty(tran.getKhoangTienDen())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								((replacedValue).as(Double.class)), Double.parseDouble(tran.getKhoangTienDen()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("amount"),
						// tran.getKhoangTienDen())));
					}
					if (!ObjectUtils.isEmpty(tran.getUserDuyetLenh())) {
						predicates.add(
								criteriaBuilder.and(
										criteriaBuilder.equal(criteriaBuilder.upper(root.get("approvedBy").get("code")),
												tran.getUserDuyetLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tran.getUserLapLenh())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(
										criteriaBuilder.upper(root.get("createdByCust").get("code")),
										tran.getUserLapLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tran.getThoiGianDuyetLenhTu())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tran.getThoiGianDuyetLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tran.getThoiGianDuyetLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tran.getThoiGianDuyetLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}

					if (!ObjectUtils.isEmpty(tran.getTaiKhoanThuHuong())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
										"%" + tran.getTaiKhoanThuHuong().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(tran.getTenNguoiThuHuong())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveName")),
										"%" + tran.getTenNguoiThuHuong().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tran.getTaiKhoanNguon())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sendAccount")),
										"%" + tran.getTaiKhoanNguon().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tran.getStatus())) {
						List<String> lstStatus = new ArrayList<>();
						for(String str : tran.getStatus().split(",")) {
							lstStatus.add(str);
						}
						predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("status")).value(lstStatus)));
						
//						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
//										"%" + tranSearch.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tran.getThoiGianLapLenhTu())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tran.getThoiGianLapLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tran.getThoiGianLapLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tran.getThoiGianLapLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tran.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("type")),
								"%" + tran.getType().toUpperCase() + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Tran> page = tranRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse delete(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var tranCur = tranRepository.findById(id).orElse(null);
			if (tranCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			jpaDeleteExecute(id);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaExecute(Tran tran) {
		tranRepository.save(tran);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		tranRepository.deleteById(id);
	}

	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var tranCur = tranRepository.findById(id).orElse(null);
		if (tranCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(tranCur);
		return baseResponse;
	}

	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(tranRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}
}
