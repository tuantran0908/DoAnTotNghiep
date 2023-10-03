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
import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.models.Category;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CategoryRepository;
import llq.fw.cm.services.BaseService;

@Component
public class CategoryServiceImpl implements BaseService<BaseResponse, Category, Category, Long> {
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public BaseResponse create(Category category) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var categoryCur = categoryRepository.findById(category.getId()).orElse(null);
			if (categoryCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var categoryNew = Category.builder().description(category.getDescription()).name(category.getName())
					.status(category.getStatus()).giaoTrinhs(category.getGiaoTrinhs()).build();

			jpaExecute(categoryNew);
			baseResponse.setData(categoryNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(Category category) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var categoryCur = categoryRepository.findById(category.getId()).orElse(null);
			if (categoryCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			categoryCur.setDescription(category.getDescription());
			categoryCur.setName(category.getName());
			categoryCur.setStatus(category.getStatus());
			categoryCur.setGiaoTrinhs(category.getGiaoTrinhs());
			jpaExecute(categoryCur);
			baseResponse.setData(categoryCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(Category category, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Category> specification = new Specification<Category>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(category.getStatus())) {
						predicates.add(criteriaBuilder.and(
								criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")), category.getStatus())));
					}
					if (!ObjectUtils.isEmpty(category.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + category.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(category.getDescription())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
										"%" + category.getDescription().toUpperCase() + "%")));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Category> page = categoryRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchSelect2(Category category, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Category> specification = new Specification<Category>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Category> page = categoryRepository.findAll(specification, pageable).map(item -> {
				return Category.builder().id(item.getId()).description(item.getDescription()).name(item.getName())
						.status(item.getStatus()).build();
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

			Specification<Category> specification = new Specification<Category>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			Category category = categoryRepository.findAll(specification).stream().findFirst().orElse(null);
			if (category == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(category);

		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}
	
	public BaseResponse getAllCategory() {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			List<Category> category = categoryRepository.findAllByStatus(IBStatusEnum.Y);
			baseResponse.setData(category);

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
			var categoryCur = categoryRepository.findById(id).orElse(null);
			if (categoryCur == null) {
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
	public void jpaExecute(Category category) {
		categoryRepository.save(category);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
