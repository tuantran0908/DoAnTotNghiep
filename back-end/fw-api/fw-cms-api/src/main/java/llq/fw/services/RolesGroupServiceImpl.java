package llq.fw.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
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
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.models.RolesGroup_Roles;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.RolesGroupRepository;
import llq.fw.cm.security.repository.RolesGroup_RolesRepository;
import llq.fw.cm.services.BaseService;
import llq.fw.payload.request.RolesGroupSearchRequest;

@Component
public class RolesGroupServiceImpl implements BaseService<BaseResponse, RolesGroup, RolesGroupSearchRequest, Long> {
	private static final Logger logger = LoggerFactory.getLogger(RolesGroupServiceImpl.class);
	@Autowired
	RolesGroupRepository rolesGroupRepository;
	@Autowired
	RolesGroup_RolesRepository rolesGroup_RolesRepository;

	@Override
	public BaseResponse create(RolesGroup rolesGroup) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesGroupCur = rolesGroupRepository.findById(rolesGroup.getId()).orElse(null);
			if (rolesGroupCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var rolesGroupNew = RolesGroup.builder().description(rolesGroup.getDescription()).name(rolesGroup.getName())
					.status(rolesGroup.getStatus()).users(rolesGroup.getUsers()).build();

			jpaExecute(rolesGroupNew);
			baseResponse.setData(rolesGroupNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(RolesGroup rolesGroup) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesGroupCur = rolesGroupRepository.findById(rolesGroup.getId()).orElse(null);
			if (rolesGroupCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			rolesGroupCur.setDescription(rolesGroup.getDescription());
			rolesGroupCur.setName(rolesGroup.getName());
			rolesGroupCur.setStatus(rolesGroup.getStatus());
			rolesGroupCur.setUsers(rolesGroup.getUsers());

			Set<RolesGroup_Roles> rolesGroup_Roles = rolesGroupCur.getRolesGroup_Roles();
			List<RolesGroup_Roles> rolesGroup_RolesDelete = rolesGroup_Roles.stream().filter(item -> {
				return !rolesGroup.getRolesGroup_Roles().stream().filter(l -> {
					if (l.getId() != null) {
						return l.getId() == item.getId();
					}
					return false;

				}).findAny().isPresent();
			}).toList();
			rolesGroup_Roles.removeAll(rolesGroup_RolesDelete);

			for (RolesGroup_Roles rolesGroup_RolesBody : rolesGroup.getRolesGroup_Roles()) {
				if (rolesGroup_RolesBody.getId() == null) {
					rolesGroup_RolesBody.setRolesGroup(rolesGroup);
					rolesGroup_Roles.add(rolesGroup_RolesBody);
				}
			}
			jpaExecute(rolesGroupCur);
			baseResponse.setData(rolesGroupCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(RolesGroupSearchRequest rolesGroup, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<RolesGroup> specification = new Specification<RolesGroup>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<RolesGroup> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(rolesGroup.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.equal(criteriaBuilder.upper(root.get("status")), rolesGroup.getStatus())));
					}
					if (!ObjectUtils.isEmpty(rolesGroup.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + rolesGroup.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(rolesGroup.getDescription())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
										"%" + rolesGroup.getDescription().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(rolesGroup.getRolesIds())) {
						String[] rolesGroupIdsString = rolesGroup.getRolesIds().split(",");
						List<Long> rolesGroupIds = Stream.of(rolesGroupIdsString).map(Long::valueOf)
								.collect(Collectors.toList());
						if (rolesGroupIds.size() > 1) {
							In<Long> inClause = criteriaBuilder.in(root.get("roles").get("id"));
							for (Long rolesGroupId : rolesGroupIds) {
								inClause.value(rolesGroupId);
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.equal(root.get("roles").get("id"), rolesGroup.getRolesIds())));
						}
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<RolesGroup> page = rolesGroupRepository.findAll(specification, pageable);

			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	public BaseResponse searchSelect2(RolesGroup rolesGroup, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<RolesGroup> specification = new Specification<RolesGroup>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<RolesGroup> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(rolesGroup.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.equal(criteriaBuilder.upper(root.get("status")), rolesGroup.getStatus())));
					}
					if (!ObjectUtils.isEmpty(rolesGroup.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + rolesGroup.getName().toUpperCase() + "%")));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<RolesGroup> page = rolesGroupRepository.findAll(specification, pageable).map(item -> {
				return RolesGroup.builder().id(item.getId()).description(item.getDescription()).name(item.getName())
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

			Specification<RolesGroup> specification = new Specification<RolesGroup>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<RolesGroup> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			RolesGroup rolesGroup = rolesGroupRepository.findAll(specification).stream().findFirst().orElse(null);
			if (rolesGroup == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(rolesGroup);

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
			var rolesGroupCur = rolesGroupRepository.findById(id).orElse(null);
			if (rolesGroupCur == null) {
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
	public void jpaExecute(RolesGroup rolesGroup) {
		rolesGroupRepository.save(rolesGroup);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		rolesGroupRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
