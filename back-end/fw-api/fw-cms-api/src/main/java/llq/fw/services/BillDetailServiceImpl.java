package llq.fw.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import llq.fw.cm.models.BillDetail;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.BillDetailRepository;
import llq.fw.cm.services.BaseService;

@Component
public class BillDetailServiceImpl implements BaseService<BaseResponse, BillDetail, BillDetail, Long> {
	private static final Logger logger = LoggerFactory.getLogger(BillDetailServiceImpl.class);
	@Autowired
	BillDetailRepository billDetailRepository;

	@Override
	public BaseResponse create(BillDetail billDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var billDetailCur = billDetailRepository.findById(billDetail.getId()).orElse(null);
			if (billDetailCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var billDetailNew = BillDetail.builder().quantity(billDetail.getQuantity()).bill(billDetail.getBill())
					.giaoTrinh(billDetail.getGiaoTrinh()).build();

			jpaExecute(billDetailNew);
			baseResponse.setData(billDetailNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(BillDetail billDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var billDetailCur = billDetailRepository.findById(billDetail.getId()).orElse(null);
			if (billDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			billDetailCur.setQuantity(billDetail.getQuantity());
			billDetailCur.setBill(billDetail.getBill());
			billDetailCur.setGiaoTrinh(billDetail.getGiaoTrinh());
			jpaExecute(billDetailCur);
			baseResponse.setData(billDetailCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(BillDetail billDetail, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<BillDetail> specification = new Specification<BillDetail>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<BillDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(billDetail.getGiaoTrinh())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
								criteriaBuilder.upper(root.get("giaoTrinh")), billDetail.getGiaoTrinh().getId())));
					}
					if (!ObjectUtils.isEmpty(billDetail.getBill())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.equal(criteriaBuilder.upper(root.get("bill")), billDetail.getBill().getId())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<BillDetail> page = billDetailRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse getDetail(Long id) {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<BillDetail> specification = new Specification<BillDetail>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<BillDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			BillDetail billDetail = billDetailRepository.findAll(specification).stream().findFirst().orElse(null);
			if (billDetail == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(billDetail);

		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse delete(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			var billDetailCur = billDetailRepository.findById(id).orElse(null);
			if (billDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			jpaDeleteExecute(id);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaExecute(BillDetail billDetail) {
		billDetailRepository.save(billDetail);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		billDetailRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
