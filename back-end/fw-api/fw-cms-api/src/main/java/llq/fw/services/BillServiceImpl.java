package llq.fw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import llq.fw.cm.models.Bill;
import llq.fw.cm.models.BillDetail;
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.BillRepository;
import llq.fw.cm.security.repository.GiaoTrinhRepository;
import llq.fw.cm.services.BaseService;

@Component
public class BillServiceImpl implements BaseService<BaseResponse, Bill, Bill, Long> {
	private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);
	@Autowired
	BillRepository billRepository;
	@Autowired
	GiaoTrinhRepository giaoTrinhRepository;

	@Override
	public BaseResponse create(Bill bill) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			var billNew = Bill.builder().address(bill.getAddress()).name(bill.getName()).total(bill.getTotal())
					.payment(bill.getPayment()).phoneNumber(bill.getPhoneNumber()).status(bill.getStatus())
					.user(bill.getUser()).build();
			jpaExecute(billNew);
			baseResponse.setData(billNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(Bill bill) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var billCur = billRepository.findById(bill.getId()).orElse(null);
			if (billCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			billCur.setName(bill.getName());
			billCur.setAddress(bill.getAddress());
			billCur.setTotal(bill.getTotal());
			billCur.setPayment(bill.getPayment());
			billCur.setPhoneNumber(bill.getPhoneNumber());
			billCur.setStatus(bill.getStatus());

			Set<BillDetail> billDetail = billCur.getBillDetails();
			List<BillDetail> billDetailDelete = billDetail.stream().filter(item -> {
				return !bill.getBillDetails().stream().filter(l -> {
					if (l.getId() != null) {
						return l.getId() == item.getId();
					}
					return false;

				}).findAny().isPresent();
			}).toList();
			billDetail.removeAll(billDetailDelete);
			if(bill.getBillDetails()!=null) {
				for (BillDetail billDetailBody : bill.getBillDetails()) {
//					if (billDetailBody.getId() == null) {
						billDetailBody.setBill(bill);
						billDetail.add(billDetailBody);
//					}
				}
//				for (BillDetail billDetailBody : bill.getBillDetails()) {
//					GiaoTrinh giaoTrinh = giaoTrinhRepository.getReferenceById(billDetailBody.getGiaoTrinh().getId());
//					giaoTrinh.setQuantityRemaining(giaoTrinh.getQuantityRemaining()-billDetailBody.getQuantity());
//					giaoTrinh.setQuantitySell(giaoTrinh.getQuantitySell()+billDetailBody.getQuantity());
//					giaoTrinhRepository.save(giaoTrinh);
//				}
			}
			jpaExecute(billCur);
			baseResponse.setData(billCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(Bill bill, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Bill> specification = new Specification<Bill>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(bill.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + bill.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bill.getPhoneNumber())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("phoneNumber")),
										"%" + bill.getPhoneNumber().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(bill.getStatus())) {
						predicates.add(criteriaBuilder.and(
								criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")), bill.getStatus())));
					}
					if (!ObjectUtils.isEmpty(bill.getPayment())) {
						predicates.add(criteriaBuilder.and(
								criteriaBuilder.equal(criteriaBuilder.upper(root.get("payment")), bill.getPayment())));
					}
					if (!ObjectUtils.isEmpty(bill.getPhoneNumber())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("phoneNumber")),
										"%" + bill.getPhoneNumber().toUpperCase() + "%")));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Bill> page = billRepository.findAll(specification, pageable);
//			Page<Object> page = billRepository.findAll(specification, pageable).map(item -> {
//				if (item.getUpdatedBy() != null) {
//					User updatedBy = item.getUpdatedBy();
//					User updatedByNew = User.builder().id(updatedBy.getId()).email(updatedBy.getEmail())
//							.fullname(updatedBy.getFullname()).status(updatedBy.getStatus())
//							.username(updatedBy.getUsername()).build();
//					item.setUpdatedBy(updatedByNew);
//				}
//
//				if (item.getCreatedBy() != null) {
//					User createdBy = item.getCreatedBy();
//					User createdByNew = User.builder().id(createdBy.getId()).email(createdBy.getEmail())
//							.fullname(createdBy.getFullname()).status(createdBy.getStatus())
//							.username(createdBy.getUsername()).build();
//					item.setCreatedBy(createdByNew);
//				}
//				if (item.getUser() != null) {
//					User user = item.getUser();
//					User userNew = User.builder().id(user.getId()).build();
//					item.setUser(userNew);
//				}
//
//				return item;
//			});
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchSelect2(Bill bill, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Bill> specification = new Specification<Bill>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Bill> page = billRepository.findAll(specification, pageable).map(item -> {
				return Bill.builder().id(item.getId()).address(bill.getAddress()).name(bill.getName())
						.total(bill.getTotal()).payment(bill.getPayment()).phoneNumber(bill.getPhoneNumber())
						.status(bill.getStatus()).build();
			});
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

			Specification<Bill> specification = new Specification<Bill>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			Bill bill = billRepository.findAll(specification).stream().findFirst().orElse(null);
			if (bill == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(bill);

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
			var billCur = billRepository.findById(id).orElse(null);
			if (billCur == null) {
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
	public void jpaExecute(Bill bill) {
		billRepository.save(bill);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		billRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
