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
import javax.persistence.criteria.CriteriaBuilder.In;
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

import llq.fw.cm.common.AddDate;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.enums.trans.ScheduleFrequencyEnum;
import llq.fw.cm.enums.trans.SchedulesEnum;
import llq.fw.cm.models.Company;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.Tran;
import llq.fw.cm.models.TransSchedule;
import llq.fw.cm.models.TransSchedulesDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.TransScheduleRepository;
import llq.fw.cm.repository.TransSchedulesDetailRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.TranSearchRequest;
import llq.fw.payload.request.TransRequest;
import llq.fw.payload.request.gen.TransScheduleSearchRequest;
import llq.fw.cm.services.BaseService;

@Component
public class TransScheduleServiceImpl
		implements BaseService<BaseResponse, TransSchedule, TranSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(TransScheduleServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransScheduleRepository transScheduleRepository;

	@Autowired
	protected TransSchedulesDetailRepository tranSchedulesDetailRepository;

	@Autowired
	protected TransSchedulesDetailServiceImpl transSchedulesDetailServiceImpl;

	@Autowired
	protected CustRepository custRepository;

	@Override
	public BaseResponse create(TransSchedule transSchedule) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transScheduleCur = transScheduleRepository.findById(transSchedule.getCode()).orElse(null);
			if (transScheduleCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}

			var transScheduleNew = TransSchedule.builder()
					.code(transSchedule.getCode().toUpperCase())
					.amount(transSchedule.getAmount())
					.approvedBy(transSchedule.getApprovedBy())
					.approvedDate(transSchedule.getApprovedDate())
					.branch(transSchedule.getBranch())
					.ccy(transSchedule.getCcy())
					.content(transSchedule.getContent())
					.custId(transSchedule.getCustId())
					.fee(transSchedule.getFee())
					.receiveAccount(transSchedule.getReceiveAccount())
					.receiveBank(transSchedule.getReceiveBank())
					.receiveName(transSchedule.getReceiveName())
					.sendAccount(transSchedule.getSendAccount())
					.sendName(transSchedule.getSendName())
					.status(transSchedule.getStatus())
					.transDate(transSchedule.getTransDate())
					.transDatetime(transSchedule.getTransDatetime())
					.transTime(transSchedule.getTransTime())
					.txnum(transSchedule.getTxnum())
					.type(transSchedule.getType())
					.username(transSchedule.getUsername())
					.vat(transSchedule.getVat())
					.schedules(transSchedule.getSchedules())
					.transSchedulesDetails(transSchedule.getTransSchedulesDetails())
					.build();
			jpaExecute(transScheduleNew);
			baseResponse.setData(transScheduleNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse createTranSchedules(TransRequest transSchedule, Cust cust) {
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

			if (!ObjectUtils.isEmpty(transSchedule.getCode())) {
				var transScheduleCur = transScheduleRepository.findById(transSchedule.getCode()).orElse(null);
				if (transScheduleCur != null) {
					baseResponse.setFwError(FwError.DLDATONTAI);
					return baseResponse;
				}
			}

			// transSchedule.getSchedules 0 la tuong lai , 1 la dinh ki
			var transScheduleNew = createTransSchedule(transSchedule, cust, transSchedule.getSchedules());
			if (transScheduleNew != null)
				baseResponse.setData(transScheduleNew);

			// Call API sang core de lap lenh chuyen tien

			//

		} catch (

		Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse update(TransSchedule transSchedule) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transScheduleCur = transScheduleRepository.findById(transSchedule.getCode()).orElse(null);
			if (transScheduleCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transScheduleCur.setAmount(transSchedule.getAmount());
			transScheduleCur.setApprovedBy(transSchedule.getApprovedBy());
			transScheduleCur.setApprovedDate(transSchedule.getApprovedDate());
			transScheduleCur.setBranch(transSchedule.getBranch());
			transScheduleCur.setCcy(transSchedule.getCcy());
			transScheduleCur.setContent(transSchedule.getContent());
			transScheduleCur.setCustId(transSchedule.getCustId());
			transScheduleCur.setFee(transSchedule.getFee());
			transScheduleCur.setReceiveAccount(transSchedule.getReceiveAccount());
			transScheduleCur.setReceiveBank(transSchedule.getReceiveBank());
			transScheduleCur.setReceiveName(transSchedule.getReceiveName());
			transScheduleCur.setSendAccount(transSchedule.getSendAccount());
			transScheduleCur.setSendName(transSchedule.getSendName());
			transScheduleCur.setStatus(transSchedule.getStatus());
			transScheduleCur.setTransDate(transSchedule.getTransDate());
			transScheduleCur.setTransDatetime(transSchedule.getTransDatetime());
			transScheduleCur.setTransTime(transSchedule.getTransTime());
			transScheduleCur.setTxnum(transSchedule.getTxnum());
			transScheduleCur.setType(transSchedule.getType());
			transScheduleCur.setUsername(transSchedule.getUsername());
			transScheduleCur.setVat(transSchedule.getVat());
			transScheduleCur.setReason(transSchedule.getReason());
			transScheduleCur.setTransSchedulesDetails(transSchedule.getTransSchedulesDetails());
			jpaExecute(transScheduleCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transScheduleCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(TranSearchRequest tranSearch, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu

		CustDetailsImpl userDetail = (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);

		// check cust
		if (currentCust != null) {
			if (currentCust.getPosition().equals(PositionEnum.LAPLENH)) {
				tranSearch.setUserLapLenh(currentCust.getCode());
			} else {
				if (currentCust.getPosition().equals(PositionEnum.DUYETLENH)) {
					tranSearch.setUserDuyetLenh(currentCust.getCode());
				}
			}
		}

		try {
			Specification<TransSchedule> specification = new Specification<TransSchedule>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<TransSchedule> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(tranSearch.getMaGiaoDich())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + tranSearch.getMaGiaoDich().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getKhoangTienTu())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								((replacedValue).as(Double.class)), Double.parseDouble(tranSearch.getKhoangTienTu()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"),tranSearch.getKhoangTienTu())));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getKhoangTienDen())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.lessThanOrEqualTo(((replacedValue).as(Double.class)),
										Double.parseDouble(tranSearch.getKhoangTienDen()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("amount"),
						// tranSearch.getKhoangTienDen())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getUserDuyetLenh())) {
						predicates.add(
								criteriaBuilder.and(
										criteriaBuilder.equal(criteriaBuilder.upper(root.get("approvedBy").get("code")),
												tranSearch.getUserDuyetLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getUserLapLenh())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(
										criteriaBuilder.upper(root.get("createdByCust").get("code")),
										tranSearch.getUserLapLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianDuyetLenhTu())) {

						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianDuyetLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianDuyetLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder
												.literal(formatDate.format(tranSearch.getThoiGianDuyetLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getTaiKhoanThuHuong())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
										"%" + tranSearch.getTaiKhoanThuHuong().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getTenNguoiThuHuong())) {
						criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveName")),
								"%" + tranSearch.getTenNguoiThuHuong().toUpperCase() + "%"));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getTaiKhoanNguon())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sendAccount")),
										"%" + tranSearch.getTaiKhoanNguon().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getStatus())) {
						List<String> lstStatus = new ArrayList<>();
						for (String str : tranSearch.getStatus().split(",")) {
							lstStatus.add(str);
						}
						predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("status")).value(lstStatus)));

						// predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
						// "%" + tranSearch.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhTu())) {

						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));

						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));

					}
					if (!ObjectUtils.isEmpty(tranSearch.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("type")),
								"%" + tranSearch.getType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getSchedule())) {

						// predicates.add(
						// criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("schedule")),
						// tranSearch.getSchedule())));

						predicates.add(criteriaBuilder
								.and(criteriaBuilder.equal(root.get("schedules"), tranSearch.getSchedule())));

					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransSchedule> page = transScheduleRepository.findAll(specification, pageable);
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
			var transScheduleCur = transScheduleRepository.findById(id).orElse(null);
			if (transScheduleCur == null) {
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
	public void jpaExecute(TransSchedule transSchedule) {
		transScheduleRepository.save(transSchedule);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		transScheduleRepository.deleteById(id);
	}

	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transScheduleCur = transScheduleRepository.findById(id).orElse(null);
		if (transScheduleCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transScheduleCur);
		return baseResponse;
	}

	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transScheduleRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}

	public Object createTransSchedule(TransRequest transSchedule, Cust cust, String type) {

		try {

			// Create TransSchedule
			if (type.equals("0")) {
				var transScheduleNew = TransSchedule.builder()
						.amount(transSchedule.getAmount())
						.approvedBy(transSchedule.getApprovedBy())
						.approvedDate(transSchedule.getApprovedDate())
						.username(cust.getCode())
						.sendName(cust.getFullname())
						.branch(transSchedule.getBranch())
						.ccy(transSchedule.getCcy())
						.content(transSchedule.getContent())
						.custId(cust.getId().toString())
						.fee(transSchedule.getFee())
						.feeType(transSchedule.getFeeType())
						.receiveAccount(transSchedule.getReceiveAccount())
						.receiveBank(transSchedule.getReceiveBank())
						.receiveName(transSchedule.getReceiveName())
						.sendAccount(transSchedule.getSendAccount())
						.status(transSchedule.getStatus())
						.transDate(transSchedule.getTransDate())
						.transDatetime(transSchedule.getTransDatetime())
						.transTime(transSchedule.getTransTime())
						.txnum(transSchedule.getTxnum())
						.type(transSchedule.getType())
						.vat(transSchedule.getVat())
						.schedules(SchedulesEnum.forValue(transSchedule.getSchedules()))
						.build();

				jpaExecute(transScheduleNew);

				// END Create TransSchedule

				// Create TransScheduleDetail
				var transSchedulesDetailNew = TransSchedulesDetail.builder()
						.code(transSchedule.getCode())
						.paymentStatus("0")
						.reqId(transSchedule.getReqId())
						.resCode(transSchedule.getResCode())
						.resDes(transSchedule.getResDes())
						.resId(transSchedule.getResId())
						.revertCode(transSchedule.getRevertCode())
						.revertDes(transSchedule.getRevertDes())
						.revertFeeCode(transSchedule.getRevertFeeCode())
						.revertFeeDes(transSchedule.getRevertFeeDes())
						.scheduledDate(transSchedule.getScheduleFuture())
						.transSchedule(transScheduleNew)
						.build();

				transSchedulesDetailServiceImpl.create(transSchedulesDetailNew);
				// End Create TransSchedule Detail

				return transScheduleNew;
			} else {

				// Create TransSchedule
				var transScheduleNew = TransSchedule.builder()
						.amount(transSchedule.getAmount())
						.approvedBy(transSchedule.getApprovedBy())
						.approvedDate(transSchedule.getApprovedDate())
						.username(cust.getCode())
						.sendName(cust.getFullname())
						.branch(transSchedule.getBranch())
						.ccy(transSchedule.getCcy())
						.content(transSchedule.getContent())
						.custId(cust.getId().toString())
						.fee(transSchedule.getFee())
						.feeType(transSchedule.getFeeType())
						.receiveAccount(transSchedule.getReceiveAccount())
						.receiveBank(transSchedule.getReceiveBank())
						.receiveName(transSchedule.getReceiveName())
						.sendAccount(transSchedule.getSendAccount())
						.status(transSchedule.getStatus())
						.transDate(transSchedule.getTransDate())
						.transDatetime(transSchedule.getTransDatetime())
						.transTime(transSchedule.getTransTime())
						.txnum(transSchedule.getTxnum())
						.type(transSchedule.getType())
						.vat(transSchedule.getVat())
						.schedules(SchedulesEnum.forValue(transSchedule.getSchedules()))
						.schedulesFromDate(transSchedule.getSchedulesFromDate())
						.schedulesToDate(transSchedule.getSchedulesToDate())
						.schedulesFrequency(ScheduleFrequencyEnum.forValue(transSchedule.getSchedulesFrequency()))
						.schedulesTimes(transSchedule.getSchedulesTimes())
						.build();

				jpaExecute(transScheduleNew);

				// END Create TransSchedule

				for (int i = 0; i <= Long.valueOf(transSchedule.getSchedulesTimesFrequency()) - 1; i++) {

					// Create TransScheduleDetail
					var transSchedulesDetailNew = TransSchedulesDetail.builder()
							.code(transSchedule.getCode())
							.paymentStatus("0")
							.reqId(transSchedule.getReqId())
							.resCode(transSchedule.getResCode())
							.resDes(transSchedule.getResDes())
							.resId(transSchedule.getResId())
							.revertCode(transSchedule.getRevertCode())
							.revertDes(transSchedule.getRevertDes())
							.revertFeeCode(transSchedule.getRevertFeeCode())
							.revertFeeDes(transSchedule.getRevertFeeDes())
							.scheduledDate(AddDate.addTime(transSchedule.getSchedulesFromDate(), i,
									transSchedule.getSchedulesFrequency()))
							.transSchedule(transScheduleNew)
							.build();

					transSchedulesDetailServiceImpl.jpaExecute(transSchedulesDetailNew);
					// End Create TransSchedule Detail

				}
				return transScheduleNew;
			}

		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}

	}
}
