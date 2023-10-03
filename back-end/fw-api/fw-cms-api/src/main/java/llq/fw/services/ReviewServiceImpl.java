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
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.models.Review;
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.ReviewRepository;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.ReviewSearchRequest;

@Component
public class ReviewServiceImpl implements BaseService<BaseResponse, Review, ReviewSearchRequest, Long> {
	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
	@Autowired
	ReviewRepository reviewRepository;

	@Override
	public BaseResponse create(Review review) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var reviewCur = reviewRepository.findById(review.getId()).orElse(null);
			if (reviewCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var reviewNew = Review.builder().star(review.getStar()).message(review.getMessage())
					.giaoTrinh(review.getGiaoTrinh()).user(review.getUser()).build();

			jpaExecute(reviewNew);
			baseResponse.setData(reviewNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(Review review) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var reviewCur = reviewRepository.findById(review.getId()).orElse(null);
			if (reviewCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			reviewCur.setStar(review.getStar());
			reviewCur.setMessage(review.getMessage());
			jpaExecute(reviewCur);
			baseResponse.setData(reviewCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(ReviewSearchRequest review, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Review> specification = new Specification<Review>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(review.getStar())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("star")), review.getStar())));
					}
					if (!ObjectUtils.isEmpty(review.getMessage())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("message")),
										"%" + review.getMessage().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(review.getUserId())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("user").get("id"),
										review.getUserId())));
					}
					if (!ObjectUtils.isEmpty(review.getGiaoTrinhId())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("giaoTrinh").get("id"),
										review.getGiaoTrinhId())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
//			Page<Review> page = reviewRepository.findAll(specification, pageable);
			Page<Object> page = reviewRepository.findAll(specification, pageable).map(item -> {
				if (item.getUser() != null) {
					User user = item.getUser();
					User userNew = User.builder().id(user.getId())
							.avatar(user.getAvatar()).fullname(user.getFullname()).build();
					item.setUser(userNew);
				}
				if (item.getGiaoTrinh() != null) {
					GiaoTrinh giaoTrinh = item.getGiaoTrinh();
					GiaoTrinh giaoTrinhNew = GiaoTrinh.builder().id(giaoTrinh.getId()).name(giaoTrinh.getName()).image(giaoTrinh.getImage())
							.build();
					item.setGiaoTrinh(giaoTrinhNew);
				}

				return item;
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

			Specification<Review> specification = new Specification<Review>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			Review review = reviewRepository.findAll(specification).stream().findFirst().orElse(null);
			if (review == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(review);

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
			var reviewCur = reviewRepository.findById(id).orElse(null);
			if (reviewCur == null) {
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
	public void jpaExecute(Review review) {
		reviewRepository.save(review);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		reviewRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
