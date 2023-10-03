package llq.fw.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
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
import llq.fw.cm.models.RolesGroup_Roles;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.RolesGroup_RolesRepository;
import llq.fw.cm.services.BaseService;

@Component
public class RolesGroup_RolesServiceImpl
		implements BaseService<BaseResponse, RolesGroup_Roles, RolesGroup_Roles, Long> {
	private static final Logger logger = LoggerFactory.getLogger(RolesGroup_RolesServiceImpl.class);
	@Autowired
	RolesGroup_RolesRepository rolesGroup_RolesRepository;

	@Override
	public BaseResponse create(RolesGroup_Roles rolesGroup_Roles) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesGroup_RolesCur = rolesGroup_RolesRepository.findById(rolesGroup_Roles.getId()).orElse(null);
			if (rolesGroup_RolesCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var rolesGroup_RolesNew = RolesGroup_Roles.builder().roles(rolesGroup_Roles.getRoles())
					.rolesGroup(rolesGroup_Roles.getRolesGroup()).build();

			jpaExecute(rolesGroup_RolesNew);
			baseResponse.setData(rolesGroup_RolesNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(RolesGroup_Roles rolesGroup_Roles) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesGroup_RolesCur = rolesGroup_RolesRepository.findById(rolesGroup_Roles.getId()).orElse(null);
			if (rolesGroup_RolesCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			rolesGroup_RolesCur.setRoles(rolesGroup_Roles.getRoles());
			rolesGroup_RolesCur.setRolesGroup(rolesGroup_Roles.getRolesGroup());
			jpaExecute(rolesGroup_RolesCur);
			baseResponse.setData(rolesGroup_RolesCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(RolesGroup_Roles rolesGroup_Roles, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<RolesGroup_Roles> specification = new Specification<RolesGroup_Roles>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<RolesGroup_Roles> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(rolesGroup_Roles.getRoles())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.equal(criteriaBuilder.upper(root.get("roles")), rolesGroup_Roles.getRoles().getId())));
					}
					if (!ObjectUtils.isEmpty(rolesGroup_Roles.getRolesGroup())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("rolesGroup")),
										rolesGroup_Roles.getRolesGroup().getId())));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<RolesGroup_Roles> page = rolesGroup_RolesRepository.findAll(specification, pageable);
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

			Specification<RolesGroup_Roles> specification = new Specification<RolesGroup_Roles>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<RolesGroup_Roles> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			RolesGroup_Roles rolesGroup_Roles = rolesGroup_RolesRepository.findAll(specification).stream().findFirst()
					.orElse(null);
			if (rolesGroup_Roles == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(rolesGroup_Roles);

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
			var rolesGroup_RolesCur = rolesGroup_RolesRepository.findById(id).orElse(null);
			if (rolesGroup_RolesCur == null) {
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
	public void jpaExecute(RolesGroup_Roles rolesGroup_Roles) {
		rolesGroup_RolesRepository.save(rolesGroup_Roles);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		rolesGroup_RolesRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
