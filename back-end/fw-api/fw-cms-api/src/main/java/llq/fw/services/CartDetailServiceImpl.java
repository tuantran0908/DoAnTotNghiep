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
import llq.fw.cm.models.CartDetail;
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CartDetailRepository;
import llq.fw.cm.security.repository.UserRepository;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.CartDetailSearchRequest;

@Component
public class CartDetailServiceImpl implements BaseService<BaseResponse, CartDetail, CartDetailSearchRequest, Long> {
	private static final Logger logger = LoggerFactory.getLogger(CartDetailServiceImpl.class);
	@Autowired
	CartDetailRepository cartDetailRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public BaseResponse create(CartDetail cartDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var cartDetailCur = cartDetailRepository.findById(cartDetail.getId()).orElse(null);
			if (cartDetailCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var cartDetailNew = CartDetail.builder().quantity(cartDetail.getQuantity()).giaoTrinh(cartDetail.getGiaoTrinh())
					.user(cartDetail.getUser()).build();

			jpaExecute(cartDetailNew);
			baseResponse.setData(cartDetailNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(CartDetail cartDetail) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var cartDetailCur = cartDetailRepository.findById(cartDetail.getId()).orElse(null);
			if (cartDetailCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			cartDetailCur.setQuantity(cartDetail.getQuantity());
			cartDetailCur.setGiaoTrinh(cartDetail.getGiaoTrinh());
			jpaExecute(cartDetailCur);
			baseResponse.setData(cartDetailCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(CartDetailSearchRequest cartDetail, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<CartDetail> specification = new Specification<CartDetail>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CartDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(cartDetail.getUserId())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("user").get("id"),
										cartDetail.getUserId())));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CartDetail> page = cartDetailRepository.findAll(specification, pageable);
//			Page<Object> page = cartDetailRepository.findAll(specification, pageable).map(item -> {
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

	@Override
	public BaseResponse getDetail(Long id) {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<CartDetail> specification = new Specification<CartDetail>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CartDetail> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			CartDetail cartDetail = cartDetailRepository.findAll(specification).stream().findFirst().orElse(null);
			if (cartDetail == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(cartDetail);

		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}
	
	public BaseResponse getCountCartDetail(Long id) {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			User user=userRepository.findById(id).orElse(null);
			if(user!=null) {
				baseResponse.setData(cartDetailRepository.countByUser(user));
			}
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
			var cartDetailCur = cartDetailRepository.findById(id).orElse(null);
			if (cartDetailCur == null) {
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
	public void jpaExecute(CartDetail cartDetail) {
		cartDetailRepository.save(cartDetail);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		cartDetailRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
