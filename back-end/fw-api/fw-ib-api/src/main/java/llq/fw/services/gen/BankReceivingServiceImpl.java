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
import llq.fw.cm.models.BankReceiving;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.BankReceivingRepository;
import llq.fw.payload.request.gen.BankReceivingSearchRequest;
import llq.fw.cm.services.BaseService;

@Component
public class BankReceivingServiceImpl
		implements BaseService<BaseResponse, BankReceiving, BankReceivingSearchRequest, java.lang.String> {
	private static final Logger logger = LoggerFactory.getLogger(BankReceivingServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected BankReceivingRepository bankReceivingRepository;

	@Override
	public BaseResponse create(BankReceiving bankReceiving) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var bankReceivingCur = bankReceivingRepository.findById(bankReceiving.getCode()).orElse(null);
			if (bankReceivingCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
				return baseResponse;
			}

			var bankReceivingNew = BankReceiving.builder()
					.code(bankReceiving.getCode().toUpperCase())
					.citad(bankReceiving.getCitad())
					.isttt(bankReceiving.getIsttt())
					.name(bankReceiving.getName())
					.nameEn(bankReceiving.getNameEn())
					.napas(bankReceiving.getNapas())
					.sortname(bankReceiving.getSortname())
					.status(bankReceiving.getStatus())
					.swiftCode(bankReceiving.getSwiftCode())
					.build();
			jpaExecute(bankReceivingNew);
			baseResponse.setData(bankReceivingNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse update(BankReceiving bankReceiving) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var bankReceivingCur = bankReceivingRepository.findById(bankReceiving.getCode()).orElse(null);
			if (bankReceivingCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			bankReceivingCur.setCitad(bankReceiving.getCitad());
			bankReceivingCur.setIsttt(bankReceiving.getIsttt());
			bankReceivingCur.setName(bankReceiving.getName());
			bankReceivingCur.setNameEn(bankReceiving.getNameEn());
			bankReceivingCur.setNapas(bankReceiving.getNapas());
			bankReceivingCur.setSortname(bankReceiving.getSortname());
			bankReceivingCur.setStatus(bankReceiving.getStatus());
			bankReceivingCur.setSwiftCode(bankReceiving.getSwiftCode());
			jpaExecute(bankReceivingCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(bankReceivingCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(BankReceivingSearchRequest bankReceiving, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<BankReceiving> specification = new Specification<BankReceiving>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<BankReceiving> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(bankReceiving.getCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
								"%" + bankReceiving.getCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getCitad())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("citad")),
										"%" + bankReceiving.getCitad().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getIsttt())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("isttt")),
										"%" + bankReceiving.getIsttt().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + bankReceiving.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getNameEn())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("nameEn")),
										"%" + bankReceiving.getNameEn().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getNapas())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("napas")),
										"%" + bankReceiving.getNapas().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getSortname())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
										"%" + bankReceiving.getSortname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getStatus())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("status")),
										"%" + bankReceiving.getStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bankReceiving.getSwiftCode())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("swiftCode")),
										"%" + bankReceiving.getSwiftCode().toUpperCase() + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<BankReceiving> page = bankReceivingRepository.findAll(specification, pageable);
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
			var bankReceivingCur = bankReceivingRepository.findById(id).orElse(null);
			if (bankReceivingCur == null) {
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
	public void jpaExecute(BankReceiving bankReceiving) {
		bankReceivingRepository.save(bankReceiving);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.String id) {
		bankReceivingRepository.deleteById(id);
	}

	@Override
	public BaseResponse getDetail(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var bankReceivingCur = bankReceivingRepository.findById(id).orElse(null);
		if (bankReceivingCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(bankReceivingCur);
		return baseResponse;
	}

	@Override
	public BaseResponse checkExist(java.lang.String id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(bankReceivingRepository.existsById(id.toUpperCase()));
		return baseResponse;
	}

	public BaseResponse getAllBank() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(bankReceivingRepository.findAll());
		return baseResponse;
	}
}
