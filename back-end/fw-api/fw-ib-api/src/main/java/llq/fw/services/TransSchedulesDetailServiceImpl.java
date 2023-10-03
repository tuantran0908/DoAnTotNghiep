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
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.enums.cust.PositionEnum;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.TransSchedule;
import llq.fw.cm.models.TransSchedulesDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.TransSchedulesDetailRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.TranSearchRequest;
import llq.fw.payload.request.gen.TransSchedulesDetailSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class TransSchedulesDetailServiceImpl implements BaseService<BaseResponse, TransSchedulesDetail,TranSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(TransSchedulesDetailServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected TransSchedulesDetailRepository transSchedulesDetailRepository;
	@Autowired
	protected CustRepository custRepository;
	
	@Override
	public BaseResponse create(TransSchedulesDetail transSchedulesDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transSchedulesDetailNew = TransSchedulesDetail.builder()
					.code(transSchedulesDetail.getCode())
					.paymentStatus(transSchedulesDetail.getPaymentStatus())
					.reqId(transSchedulesDetail.getReqId())
					.resCode(transSchedulesDetail.getResCode())
					.resDes(transSchedulesDetail.getResDes())
					.resId(transSchedulesDetail.getResId())
					.revertCode(transSchedulesDetail.getRevertCode())
					.revertDes(transSchedulesDetail.getRevertDes())
					.revertFeeCode(transSchedulesDetail.getRevertFeeCode())
					.revertFeeDes(transSchedulesDetail.getRevertFeeDes())
					.scheduledDate(transSchedulesDetail.getScheduledDate())
					.transDate(transSchedulesDetail.getTransDate())
					.transSchedule(transSchedulesDetail.getTransSchedule())
					.build();
			jpaExecute(transSchedulesDetailNew);
			baseResponse.setData(transSchedulesDetailNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(TransSchedulesDetail transSchedulesDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var transSchedulesDetailCur = transSchedulesDetailRepository.findById(transSchedulesDetail.getId()).orElse(null);
			if (transSchedulesDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			transSchedulesDetailCur.setCode(transSchedulesDetail.getCode());
			transSchedulesDetailCur.setPaymentStatus(transSchedulesDetail.getPaymentStatus());
			transSchedulesDetailCur.setReqId(transSchedulesDetail.getReqId());
			transSchedulesDetailCur.setResCode(transSchedulesDetail.getResCode());
			transSchedulesDetailCur.setResDes(transSchedulesDetail.getResDes());
			transSchedulesDetailCur.setResId(transSchedulesDetail.getResId());
			transSchedulesDetailCur.setRevertCode(transSchedulesDetail.getRevertCode());
			transSchedulesDetailCur.setRevertDes(transSchedulesDetail.getRevertDes());
			transSchedulesDetailCur.setRevertFeeCode(transSchedulesDetail.getRevertFeeCode());
			transSchedulesDetailCur.setRevertFeeDes(transSchedulesDetail.getRevertFeeDes());
			transSchedulesDetailCur.setScheduledDate(transSchedulesDetail.getScheduledDate());
			transSchedulesDetailCur.setTransDate(transSchedulesDetail.getTransDate());
			transSchedulesDetailCur.setTransSchedule(transSchedulesDetail.getTransSchedule());
			jpaExecute(transSchedulesDetailCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(transSchedulesDetailCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
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
			Specification<TransSchedulesDetail> specification = new Specification<TransSchedulesDetail>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<TransSchedulesDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(tranSearch.getMaGiaoDich())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transSchedule").get("code")),
								"%" + tranSearch.getMaGiaoDich().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getKhoangTienTu())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("transSchedule").get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								((replacedValue).as(Double.class)), Double.parseDouble(tranSearch.getKhoangTienTu()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"),tranSearch.getKhoangTienTu())));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getKhoangTienDen())) {
						Expression<String> replacedValue = criteriaBuilder.function("replace", String.class,
								root.get("transSchedule").get("amount"), criteriaBuilder.literal(","), criteriaBuilder.literal(""));
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.lessThanOrEqualTo(((replacedValue).as(Double.class)),
										Double.parseDouble(tranSearch.getKhoangTienDen()))));
						// predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("amount"),
						// tranSearch.getKhoangTienDen())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getUserDuyetLenh())) {
						predicates.add(
								criteriaBuilder.and(
										criteriaBuilder.equal(criteriaBuilder.upper(root.get("transSchedule").get("approvedBy").get("code")),
												tranSearch.getUserDuyetLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getUserLapLenh())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(
										criteriaBuilder.upper(root.get("transSchedule").get("createdByCust").get("code")),
										tranSearch.getUserLapLenh().toUpperCase())));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianDuyetLenhTu())) {

						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianDuyetLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianDuyetLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("approvedDate")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder
												.literal(formatDate.format(tranSearch.getThoiGianDuyetLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getTaiKhoanThuHuong())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transSchedule").get("receiveAccount")),
										"%" + tranSearch.getTaiKhoanThuHuong().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(tranSearch.getTenNguoiThuHuong())) {
						criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transSchedule").get("receiveName")),
								"%" + tranSearch.getTenNguoiThuHuong().toUpperCase() + "%"));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getTaiKhoanNguon())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transSchedule").get("sendAccount")),
										"%" + tranSearch.getTaiKhoanNguon().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getStatus())) {
						List<String> lstStatus = new ArrayList<>();
						for (String str : tranSearch.getStatus().split(",")) {
							lstStatus.add(str);
						}
						predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("transSchedule").get("status")).value(lstStatus)));

						// predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
						// "%" + tranSearch.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getPaymentStatus())) {
						List<String> lstStatus = new ArrayList<>();
						for (String str : tranSearch.getPaymentStatus().split(",")) {
							lstStatus.add(str);
						}
						predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("paymentStatus")).value(lstStatus)));

						// predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
						// "%" + tranSearch.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhTu())) {

						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("transDatetime")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));

						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhTu())),
										criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getThoiGianLapLenhDen())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
								criteriaBuilder.function("TRUNC", Date.class, root.get("transSchedule").get("createdAt")),
								criteriaBuilder.function("TO_DATE", Date.class,
										criteriaBuilder.literal(formatDate.format(tranSearch.getThoiGianLapLenhDen())),
										criteriaBuilder.literal("DD-MM-YYYY")))));

					}
					if (!ObjectUtils.isEmpty(tranSearch.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("transSchedule").get("type")),
								"%" + tranSearch.getType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(tranSearch.getSchedule())) {

						// predicates.add(
						// criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("schedule")),
						// tranSearch.getSchedule())));

						predicates.add(criteriaBuilder
								.and(criteriaBuilder.equal(root.get("transSchedule").get("schedules"), tranSearch.getSchedule())));

					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<TransSchedulesDetail> page = transSchedulesDetailRepository.findAll(specification, pageable);
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
			var transSchedulesDetailCur = transSchedulesDetailRepository.findById(id).orElse(null);
			if (transSchedulesDetailCur == null) {
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
	public void jpaExecute(TransSchedulesDetail transSchedulesDetail) {
		transSchedulesDetailRepository.save(transSchedulesDetail);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		transSchedulesDetailRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transSchedulesDetailCur = transSchedulesDetailRepository.findById(id).orElse(null);
		if (transSchedulesDetailCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transSchedulesDetailCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(transSchedulesDetailRepository.existsById(id));
		return baseResponse;
	}
	
	public BaseResponse getDetailByCode(String code) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var transSchedulesDetailCur = transSchedulesDetailRepository.findByCode(code).orElse(null);
		if (transSchedulesDetailCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(transSchedulesDetailCur);
		return baseResponse;
	}
}
