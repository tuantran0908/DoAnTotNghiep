package llq.fw.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.util.ObjectUtils;

import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.Bill;
import llq.fw.cm.models.CartDetail;
import llq.fw.cm.models.Category;
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.models.Review;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.BillRepository;
import llq.fw.cm.security.repository.CategoryRepository;
import llq.fw.cm.security.repository.GiaoTrinhRepository;
import llq.fw.cm.security.repository.ReviewRepository;
import llq.fw.payload.request.CartDetailSearchRequest;
import llq.fw.payload.request.ReportSearchRequest;

@Component
public class ReportServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
	@Autowired
	GiaoTrinhRepository giaoTrinhRepository;

	@Autowired
	ReviewRepository reviewRepository;

	@Autowired
	BillRepository billRepository;

	@Autowired
	CategoryRepository categoryRepository;

	public BaseResponse getTotalQuantity() {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			HashMap<String, Long> result = new HashMap<String, Long>();
			result.put("giaoTrinh", giaoTrinhRepository.count());
			result.put("review", reviewRepository.count());
			result.put("bill", billRepository.count());
			result.put("totalPayment", billRepository.sumTotal());
			baseResponse.setData(result);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse getTotalCategory() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			HashMap<String, Long> result = new HashMap<String, Long>();
			List<Category> categories = categoryRepository.findAll();
			for (Category category : categories) {
				result.put(category.getName(), giaoTrinhRepository.countByCategory(category));
			}
			baseResponse.setData(result);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchGiaoTrinh(ReportSearchRequest reportSearchRequest, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<GiaoTrinh> specification = new Specification<GiaoTrinh>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<GiaoTrinh> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateFrom())) {
						try {
							Date publicDateFrom = formatDate.parse(reportSearchRequest.getDateFrom());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), publicDateFrom)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateTo())) {
						try {
							Date publicDateTo = formatDate.parse(reportSearchRequest.getDateTo());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), publicDateTo)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Object> page = giaoTrinhRepository.findAll(specification, pageable).map(item -> {
				GiaoTrinh giaoTrinh = GiaoTrinh.builder().id(item.getId()).name(item.getName()).author(item.getAuthor())
						.image(item.getImage()).build();
				if (item.getCategory() != null) {
					Category category = item.getCategory();
					Category categoryNew = Category.builder().name(category.getName()).build();
					giaoTrinh.setCategory(categoryNew);
				}
				return giaoTrinh;
			});
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchReviews(ReportSearchRequest reportSearchRequest, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Review> specification = new Specification<Review>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateFrom())) {
						try {
							Date publicDateFrom = formatDate.parse(reportSearchRequest.getDateFrom());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), publicDateFrom)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateTo())) {
						try {
							Date publicDateTo = formatDate.parse(reportSearchRequest.getDateTo());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), publicDateTo)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Object> page = reviewRepository.findAll(specification, pageable).map(item -> {
				Review review = Review.builder().id(item.getId()).build();
				if (item.getCreatedAt() != null) {
					review.setCreatedAt(item.getCreatedAt());
				}
				if (item.getUser() != null) {
					User userNew = User.builder().fullname(item.getUser().getFullname())
							.avatar(item.getUser().getAvatar()).build();
					review.setUser(userNew);
				}
				if (item.getGiaoTrinh() != null) {
					GiaoTrinh giaoTrinhNew = GiaoTrinh.builder().name(item.getGiaoTrinh().getName()).build();
					review.setGiaoTrinh(giaoTrinhNew);
				}
				return review;
			});
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchBills(ReportSearchRequest reportSearchRequest, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Bill> specification = new Specification<Bill>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateFrom())) {
						try {
							Date publicDateFrom = formatDate.parse(reportSearchRequest.getDateFrom());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), publicDateFrom)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(reportSearchRequest.getDateTo())) {
						try {
							Date publicDateTo = formatDate.parse(reportSearchRequest.getDateTo());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), publicDateTo)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Object> page = billRepository.findAll(specification, pageable).map(item -> {
				Bill review = Bill.builder().id(item.getId()).phoneNumber(item.getPhoneNumber())
						.payment(item.getPayment()).address(item.getAddress()).name(item.getName())
						.total(item.getTotal()).build();
				if (item.getCreatedAt() != null) {
					review.setCreatedAt(item.getCreatedAt());
				}
				return review;
			});
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

}
