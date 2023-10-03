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
import llq.fw.cm.models.Roles;
import llq.fw.cm.models.RolesGroup;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.RolesRepository;
import llq.fw.cm.services.BaseService;

@Component
public class RolesServiceImpl implements BaseService<BaseResponse, Roles, Roles, Long> {
	private static final Logger logger = LoggerFactory.getLogger(RolesServiceImpl.class);
	@Autowired
	RolesRepository rolesRepository;

	@Override
	public BaseResponse create(Roles roles) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesCur = rolesRepository.findById(roles.getId()).orElse(null);
			if (rolesCur != null) {
				baseResponse.setFwError(FwError.DLDATONTAI);
			}

			var rolesNew = Roles.builder().description(roles.getDescription())
					.name(roles.getName()).status(roles.getStatus())
					.rolesGroup_Roles(roles.getRolesGroup_Roles()).build();

			jpaExecute(rolesNew);
			baseResponse.setData(rolesNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}

		return baseResponse;
	}

	@Override
	public BaseResponse update(Roles roles) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var rolesCur = rolesRepository.findById(roles.getId()).orElse(null);
			if (rolesCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			rolesCur.setDescription(roles.getDescription());
			rolesCur.setName(roles.getName());
			rolesCur.setStatus(roles.getStatus());
			rolesCur.setRolesGroup_Roles(roles.getRolesGroup_Roles());
			jpaExecute(rolesCur);
			baseResponse.setData(rolesCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(Roles roles, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Roles> specification = new Specification<Roles>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Roles> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(roles.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.equal(criteriaBuilder.upper(root.get("status")), roles.getStatus())));
					}
					if (!ObjectUtils.isEmpty(roles.getName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
								"%" + roles.getName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(roles.getDescription())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
								"%" + roles.getDescription().toUpperCase() + "%")));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Roles> page = rolesRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}
	
	public BaseResponse searchSelect2(Roles roles, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {

			Specification<Roles> specification = new Specification<Roles>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Roles> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Roles> page = rolesRepository.findAll(specification, pageable).map(item -> {
				return Roles.builder().id(item.getId()).description(item.getDescription()).name(item.getName())
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

			Specification<Roles> specification = new Specification<Roles>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Roles> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(id)) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};

			Roles roles = rolesRepository.findAll(specification).stream().findFirst().orElse(null);
			if (roles == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
			}
			baseResponse.setData(roles);

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
			var rolesCur = rolesRepository.findById(id).orElse(null);
			if (rolesCur == null) {
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
	public void jpaExecute(Roles roles) {
		rolesRepository.save(roles);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(Long id) {
		rolesRepository.deleteById(id);
	}

	@Override
	public BaseResponse checkExist(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
