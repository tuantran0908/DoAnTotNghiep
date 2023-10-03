package llq.fw.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.BlobProxy;
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
import llq.fw.cm.models.Category;
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.GiaoTrinhRepository;
import llq.fw.cm.security.repository.ReviewRepository;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.GiaoTrinhSearchRequest;
import llq.fw.payload.response.GiaoTrinhRepsonse;
import llq.fw.payload.response.UserResponse;

@Component
public class GiaoTrinhServiceImpl implements BaseService<BaseResponse, GiaoTrinh, GiaoTrinhSearchRequest, Long> {
	private static final Logger logger = LoggerFactory.getLogger(GiaoTrinhServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
	@Autowired
	GiaoTrinhRepository giaoTrinhRepository;
	@Autowired
	ReviewRepository reviewRepository;

	@Override
	public BaseResponse create(GiaoTrinh giaoTrinh) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var giaoTrinhCur = giaoTrinhRepository.findById(giaoTrinh.getId()).orElse(null);
			if (giaoTrinhCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var giaoTrinhNew = GiaoTrinh.builder().description(giaoTrinh.getDescription()).name(giaoTrinh.getName())
					.author(giaoTrinh.getAuthor()).price(giaoTrinh.getPrice())
					.quantityRemaining(giaoTrinh.getQuantityRemaining()).quantitySell(giaoTrinh.getQuantitySell())
					.width(giaoTrinh.getWidth()).height(giaoTrinh.getHeight()).length(giaoTrinh.getLength())
					.weight(giaoTrinh.getWeight()).image(giaoTrinh.getImage()).publicDate(giaoTrinh.getPublicDate())
					.status(giaoTrinh.getStatus()).billDetails(giaoTrinh.getBillDetails())
					.category(giaoTrinh.getCategory()).cartDetails(giaoTrinh.getCartDetails())
					.sales(giaoTrinh.getSales()).reviews(giaoTrinh.getReviews()).isNew(giaoTrinh.getIsNew()).build();

			jpaExecute(giaoTrinhNew);
			baseResponse.setData(giaoTrinhNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(GiaoTrinh giaoTrinh) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var giaoTrinhCur = giaoTrinhRepository.findById(giaoTrinh.getId()).orElse(null);
			if (giaoTrinhCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			giaoTrinhCur.setDescription(giaoTrinh.getDescription());
			giaoTrinhCur.setIsNew(giaoTrinh.getIsNew());
			giaoTrinhCur.setName(giaoTrinh.getName());
			giaoTrinhCur.setAuthor(giaoTrinh.getAuthor());
			giaoTrinhCur.setSales(giaoTrinh.getSales());
			giaoTrinhCur.setPrice(giaoTrinh.getPrice());
			giaoTrinhCur.setQuantityRemaining(giaoTrinh.getQuantityRemaining());
			giaoTrinhCur.setQuantitySell(giaoTrinh.getQuantitySell());
			giaoTrinhCur.setWidth(giaoTrinh.getWidth());
			giaoTrinhCur.setHeight(giaoTrinh.getHeight());
			giaoTrinhCur.setLength(giaoTrinh.getLength());
			giaoTrinhCur.setLength(giaoTrinh.getLength());
			giaoTrinhCur.setWeight(giaoTrinh.getWeight());
			if (!ObjectUtils.isEmpty(giaoTrinh.getImage()))
				giaoTrinhCur.setImage(giaoTrinh.getImage());
			giaoTrinhCur.setPublicDate(giaoTrinh.getPublicDate());
			giaoTrinhCur.setStatus(giaoTrinh.getStatus());
			giaoTrinhCur.setBillDetails(giaoTrinh.getBillDetails());
			giaoTrinhCur.setCategory(giaoTrinh.getCategory());
			giaoTrinhCur.setCartDetails(giaoTrinh.getCartDetails());
			giaoTrinhCur.setReviews(giaoTrinh.getReviews());
			jpaExecute(giaoTrinhCur);
			baseResponse.setData(toProductResponse(giaoTrinhCur));
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(GiaoTrinhSearchRequest giaoTrinh, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<GiaoTrinh> specification = new Specification<GiaoTrinh>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<GiaoTrinh> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(giaoTrinh.getStatus())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), giaoTrinh.getStatus())));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + giaoTrinh.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getDescription())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
										"%" + giaoTrinh.getDescription().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getAuthor())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("author")),
										"%" + giaoTrinh.getAuthor().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(giaoTrinh.getPublicDateFrom())) {
						try {
							Date publicDateFrom = formatDate.parse(giaoTrinh.getPublicDateFrom());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.greaterThanOrEqualTo(root.get("publicDate"), publicDateFrom)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getPublicDateTo())) {
						try {
							Date publicDateTo = formatDate.parse(giaoTrinh.getPublicDateTo());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.lessThanOrEqualTo(root.get("publicDate"), publicDateTo)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getPriceFrom())) {
						predicates.add(criteriaBuilder.and(
								criteriaBuilder.greaterThanOrEqualTo(root.get("price"), giaoTrinh.getPriceFrom())));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getPriceTo())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.lessThanOrEqualTo(root.get("price"), giaoTrinh.getPriceTo())));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getCategoryIds())) {
						String[] categoryIdsString = giaoTrinh.getCategoryIds().split(",");
						List<Long> categoryIds = Stream.of(categoryIdsString).map(Long::valueOf)
								.collect(Collectors.toList());
						if (categoryIds.size() > 1) {
							In<Long> inClause = criteriaBuilder.in(root.get("category").get("id"));
							for (Long categoryId : categoryIds) {
								inClause.value(categoryId);
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(criteriaBuilder.and(
									criteriaBuilder.equal(root.get("category").get("id"), giaoTrinh.getCategoryIds())));
						}
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getSaleProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("sales"))));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getIsNew())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("isNew"), giaoTrinh.getIsNew())));
					}
					if (!ObjectUtils.isEmpty(giaoTrinh.getFavoriteProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("sales"))));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
//			Page<GiaoTrinh> page = giaoTrinhRepository.findAll(specification, pageable);
			Page<Object> page = giaoTrinhRepository.findAll(specification, pageable).map(item -> {
				byte[] image = null;
				if (!ObjectUtils.isEmpty(item.getImage())) {
					try (InputStream inputStream = item.getImage().getBinaryStream()) {
						image = IOUtils.toByteArray(inputStream);
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				GiaoTrinhRepsonse giaoTrinhRepsonse = GiaoTrinhRepsonse.builder().id(item.getId())
						.description(item.getDescription()).name(item.getName()).author(item.getAuthor())
						.price(item.getPrice()).quantityRemaining(item.getQuantityRemaining())
						.quantitySell(item.getQuantitySell()).width(item.getWidth()).height(item.getHeight())
						.length(item.getLength()).weight(item.getWeight()).image(image).publicDate(item.getPublicDate())
						.status(item.getStatus()).category(item.getCategory()).sales(item.getSales())
						.isNew(item.getIsNew()).build(); // tiện ích
				giaoTrinhRepsonse.setAvgStar(reviewRepository.getAvgStar(giaoTrinhRepsonse.getId()));
				return giaoTrinhRepsonse;
			});
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchSelect2(GiaoTrinh giaoTrinh, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<GiaoTrinh> specification = new Specification<GiaoTrinh>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<GiaoTrinh> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<GiaoTrinh> page = giaoTrinhRepository.findAll(specification, pageable).map(item -> {
				return GiaoTrinh.builder().id(item.getId()).description(item.getDescription()).name(item.getName())
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

			Specification<GiaoTrinh> specification = new Specification<GiaoTrinh>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<GiaoTrinh> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			GiaoTrinh giaoTrinh = giaoTrinhRepository.findAll(specification).stream().findFirst().orElse(null);
			if (giaoTrinh == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(toProductResponse(giaoTrinh));

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
			var giaoTrinhCur = giaoTrinhRepository.findById(id).orElse(null);
			if (giaoTrinhCur == null) {
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
	public void jpaExecute(GiaoTrinh giaoTrinh) {
		giaoTrinhRepository.save(giaoTrinh);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		giaoTrinhRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	private GiaoTrinhRepsonse toProductResponse(GiaoTrinh giaoTrinh) throws SQLException, IOException {
		byte[] image = null;
		if (!ObjectUtils.isEmpty(giaoTrinh.getImage())) {
			try (InputStream inputStream = giaoTrinh.getImage().getBinaryStream()) {
				image = IOUtils.toByteArray(inputStream);
			}
		}
		GiaoTrinhRepsonse giaoTrinhRepsonse = GiaoTrinhRepsonse.builder().id(giaoTrinh.getId())
				.description(giaoTrinh.getDescription()).name(giaoTrinh.getName()).author(giaoTrinh.getAuthor())
				.price(giaoTrinh.getPrice()).quantityRemaining(giaoTrinh.getQuantityRemaining())
				.quantitySell(giaoTrinh.getQuantitySell()).width(giaoTrinh.getWidth()).height(giaoTrinh.getHeight())
				.length(giaoTrinh.getLength()).weight(giaoTrinh.getWeight()).image(image)
				.publicDate(giaoTrinh.getPublicDate()).status(giaoTrinh.getStatus()).category(giaoTrinh.getCategory())
				.sales(giaoTrinh.getSales()).isNew(giaoTrinh.getIsNew()).build(); // tiện ích
		giaoTrinhRepsonse.setAvgStar(reviewRepository.getAvgStar(giaoTrinhRepsonse.getId()));
		if (giaoTrinhRepsonse.getCreatedAt() != null) {
			giaoTrinhRepsonse.setCreatedAt(giaoTrinhRepsonse.getCreatedAt());
		}
		if (giaoTrinhRepsonse.getUpdatedAt() != null) {
			giaoTrinhRepsonse.setUpdatedAt(giaoTrinhRepsonse.getUpdatedAt());
		}

		if (giaoTrinhRepsonse.getUpdatedBy() != null) {
			UserResponse updatedByNew = UserResponse.builder().username(giaoTrinhRepsonse.getUpdatedBy().getUsername())
					.build();
			giaoTrinhRepsonse.setUpdatedBy(updatedByNew);
		}

		if (giaoTrinhRepsonse.getCreatedBy() != null) {
			UserResponse createdByNew = UserResponse.builder().username(giaoTrinhRepsonse.getCreatedBy().getUsername())
					.build();
			giaoTrinhRepsonse.setCreatedBy(createdByNew);
		}
		return giaoTrinhRepsonse;
	}

	public GiaoTrinh byteToBlob(GiaoTrinhRepsonse giaoTrinh) throws SQLException, IOException {
		Blob blob = null;
		if (giaoTrinh.getImage() != null && giaoTrinh.getId() != null) {
			blob = BlobProxy.generateProxy(giaoTrinh.getImage());

		}
		return GiaoTrinh.builder().id(giaoTrinh.getId()).description(giaoTrinh.getDescription())
				.name(giaoTrinh.getName()).author(giaoTrinh.getAuthor()).price(giaoTrinh.getPrice())
				.quantityRemaining(giaoTrinh.getQuantityRemaining()).quantitySell(giaoTrinh.getQuantitySell())
				.width(giaoTrinh.getWidth()).height(giaoTrinh.getHeight()).length(giaoTrinh.getLength())
				.weight(giaoTrinh.getWeight()).image(blob).publicDate(giaoTrinh.getPublicDate())
				.status(giaoTrinh.getStatus()).category(giaoTrinh.getCategory()).sales(giaoTrinh.getSales())
				.isNew(giaoTrinh.getIsNew()).build();
	}

}
