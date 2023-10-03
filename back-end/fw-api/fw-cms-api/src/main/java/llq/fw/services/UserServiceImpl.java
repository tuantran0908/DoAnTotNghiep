package llq.fw.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
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
import llq.fw.cm.models.GiaoTrinh;
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.UserRepository;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.UserSearchRequest;
import llq.fw.payload.response.GiaoTrinhRepsonse;
import llq.fw.payload.response.UserResponse;

@Component
public class UserServiceImpl implements BaseService<BaseResponse, User, UserSearchRequest, Long> {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
	@Autowired
	UserRepository userRepository;

	@Override
	public BaseResponse create(User user) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var userCur = userRepository.findById(user.getId()).orElse(null);
			if (userCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var userNew = User.builder().email(user.getEmail()).fullname(user.getFullname())
					.password(user.getPassword()).phoneNumber(user.getPhoneNumber()).birthday(user.getBirthday())
					.gender(user.getGender()).status(user.getStatus()).username(user.getUsername()).avatar(user.getAvatar())
					.refreshtokens(user.getRefreshtokens()).bills(user.getBills()).reviews(user.getReviews())
					.rolesGroup(user.getRolesGroup()).build();
			jpaExecute(userNew);
			baseResponse.setData(userNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(User user) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var userCur = userRepository.findById(user.getId()).orElse(null);
			if (userCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			userCur.setEmail(user.getEmail());
			userCur.setFullname(user.getFullname());
			userCur.setPhoneNumber(user.getPhoneNumber());
			userCur.setBirthday(user.getBirthday());
			userCur.setGender(user.getGender());
			userCur.setStatus(user.getStatus());
			userCur.setUsername(user.getUsername());
			if (!ObjectUtils.isEmpty(user.getAvatar()))
				userCur.setAvatar(user.getAvatar());
			userCur.setRefreshtokens(user.getRefreshtokens());
			userCur.setRolesGroup(user.getRolesGroup());
			jpaExecute(userCur);
			baseResponse.setData(toProductResponse(userCur));
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(UserSearchRequest user, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<User> specification = new Specification<User>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					root.fetch("rolesGroup", JoinType.LEFT);
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(user.getName())) {
						predicates.add(criteriaBuilder.or(
								criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")),
										"%" + user.getName().toUpperCase() + "%"),
								criteriaBuilder.like(criteriaBuilder.upper(root.get("username")),
										"%" + user.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(user.getStatus())) {
						predicates
								.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), user.getStatus())));
					}
					if (!ObjectUtils.isEmpty(user.getRolesGroupIds())) {
						String[] rolesGroupIdsString = user.getRolesGroupIds().split(",");
						List<Long> rolesGroupIds = Stream.of(rolesGroupIdsString).map(Long::valueOf)
								.collect(Collectors.toList());
						if (rolesGroupIds.size() > 1) {
							In<Long> inClause = criteriaBuilder.in(root.get("rolesGroup").get("id"));
							for (Long rolesGroupId : rolesGroupIds) {
								inClause.value(rolesGroupId);
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(criteriaBuilder.and(
									criteriaBuilder.equal(root.get("rolesGroup").get("id"), user.getRolesGroupIds())));
						}
					}
					if (!ObjectUtils.isEmpty(user.getCreatedAtFrom())) {
						try {
							Date createdAtFrom = formatDate.parse(user.getCreatedAtFrom());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!ObjectUtils.isEmpty(user.getCreatedAtTo())) {
						try {
							Date createdAtTo = formatDate.parse(user.getCreatedAtTo());
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtTo)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
//			Page<User> page = userRepository.findAll(specification, pageable);
			Page<Object> page = userRepository.findAll(specification, pageable).map(item -> {
				if (item.getRolesGroup() != null) {
					RolesGroup rolesGroup = item.getRolesGroup();
					RolesGroup rolesGroupNew = RolesGroup.builder().id(rolesGroup.getId())
							.description(rolesGroup.getDescription()).name(rolesGroup.getName())
							.status(rolesGroup.getStatus()).build();
					item.setRolesGroup(rolesGroupNew);
				}
				if (item.getUpdatedBy() != null) {
					User updatedBy = item.getUpdatedBy();
					User updatedByNew = User.builder().id(updatedBy.getId()).email(updatedBy.getEmail())
							.fullname(updatedBy.getFullname()).status(updatedBy.getStatus())
							.username(updatedBy.getUsername()).build();
					item.setUpdatedBy(updatedByNew);
				}

				if (item.getCreatedBy() != null) {
					User createdBy = item.getCreatedBy();
					User createdByNew = User.builder().id(createdBy.getId()).email(createdBy.getEmail())
							.fullname(createdBy.getFullname()).status(createdBy.getStatus())
							.username(createdBy.getUsername()).build();
					item.setCreatedBy(createdByNew);
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

			Specification<User> specification = new Specification<User>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					root.fetch("rolesGroup", JoinType.LEFT);
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			User user = userRepository.findAll(specification).stream().findFirst().orElse(null);
			if (user == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}

			else {
				if (user.getUpdatedBy() != null) {
					User updatedBy = user.getUpdatedBy();
					User updatedByNew = User.builder().email(updatedBy.getEmail()).fullname(updatedBy.getFullname())
							.status(updatedBy.getStatus()).username(updatedBy.getUsername()).build();
					user.setUpdatedBy(updatedByNew);
				}

				if (user.getCreatedBy() != null) {
					User createdBy = user.getCreatedBy();
					User createdByNew = User.builder().email(createdBy.getEmail()).fullname(createdBy.getFullname())
							.status(createdBy.getStatus()).username(createdBy.getUsername()).build();
					user.setCreatedBy(createdByNew);
				}
			}
			baseResponse.setData(toProductResponse(user));
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
			var userCur = userRepository.findById(id).orElse(null);
			if (userCur == null) {
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
	public void jpaExecute(User user) {
		userRepository.save(user);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	private UserResponse toProductResponse(User user) throws SQLException, IOException {
		byte[] image = null;
		if (!ObjectUtils.isEmpty(user.getAvatar())) {
			try (InputStream inputStream = user.getAvatar().getBinaryStream()) {
				image = IOUtils.toByteArray(inputStream);
			}
		}
		UserResponse userRepsonse = UserResponse.builder().id(user.getId()).email(user.getEmail()).fullname(user.getFullname())
				.phoneNumber(user.getPhoneNumber()).birthday(user.getBirthday())
				.gender(user.getGender()).status(user.getStatus()).username(user.getUsername()).avatar(image).rolesGroup(user.getRolesGroup())
				.build();
		if (userRepsonse.getCreatedAt() != null) {
			userRepsonse.setCreatedAt(userRepsonse.getCreatedAt());
		}
		if (userRepsonse.getUpdatedAt() != null) {
			userRepsonse.setUpdatedAt(userRepsonse.getUpdatedAt());
		}

		if (userRepsonse.getUpdatedBy() != null) {
			UserResponse updatedByNew = UserResponse.builder().username(userRepsonse.getUpdatedBy().getUsername())
					.build();
			userRepsonse.setUpdatedBy(updatedByNew);
		}

		if (userRepsonse.getCreatedBy() != null) {
			UserResponse createdByNew = UserResponse.builder().username(userRepsonse.getCreatedBy().getUsername())
					.build();
			userRepsonse.setCreatedBy(createdByNew);
		}
		return userRepsonse;
	}

	public User byteToBlob(UserResponse user) throws SQLException, IOException {
		Blob blob = null;
		if (user.getAvatar() != null && user.getId() != null) {
			blob = BlobProxy.generateProxy(user.getAvatar());
		}
		return User.builder().id(user.getId()).email(user.getEmail()).fullname(user.getFullname())
				.phoneNumber(user.getPhoneNumber()).birthday(user.getBirthday())
				.gender(user.getGender()).status(user.getStatus()).username(user.getUsername()).avatar(blob).rolesGroup(user.getRolesGroup())
				.build();
	}
}
