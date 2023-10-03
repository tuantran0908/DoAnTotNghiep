package llq.fw.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;
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
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustAcc;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CustAccRepository;
import llq.fw.payload.request.gen.CustAccSearchRequest;
import llq.fw.cm.services.BaseService;

@Component
public class CustAccServiceImpl implements BaseService<BaseResponse, CustAcc, CustAccSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustAccServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CustAccRepository custAccRepository;

	@Override
	public BaseResponse create(CustAcc custAcc) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custAccNew = CustAcc.builder()
					.acc(custAcc.getAcc())
					.type(custAcc.getType())
					.accType(custAcc.getAccType())
					.cust(custAcc.getCust())
					.build();
			jpaExecute(custAccNew);
			baseResponse.setData(custAccNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse update(CustAcc custAcc) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custAccCur = custAccRepository.findById(custAcc.getId()).orElse(null);
			if (custAccCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			custAccCur.setAcc(custAcc.getAcc());
			custAccCur.setType(custAcc.getType());
			custAccCur.setAccType(custAcc.getAccType());
			custAccCur.setCust(custAcc.getCust());
			jpaExecute(custAccCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(custAccCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(CustAccSearchRequest custAcc, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CustAcc> specification = new Specification<CustAcc>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CustAcc> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(custAcc.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								custAcc.getId())));
					}
					if (!ObjectUtils.isEmpty(custAcc.getAcc())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("acc")),
								"%" + custAcc.getAcc().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custAcc.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("type")),
								"%" + custAcc.getType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custAcc.getAccType())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("accType")),
										"%" + custAcc.getAccType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custAcc.getCust())) {
						String[] ids = custAcc.getCust().split(",");
						if (ids.length > 1) {
							In<String> inClause = criteriaBuilder.in(root.get("cust").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("cust").get("id"),
									custAcc.getCust().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CustAcc> page = custAccRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse delete(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custAccCur = custAccRepository.findById(id).orElse(null);
			if (custAccCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			jpaDeleteExecute(id);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaExecute(CustAcc custAcc) {
		custAccRepository.save(custAcc);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		custAccRepository.deleteById(id);
	}

	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var custAccCur = custAccRepository.findById(id).orElse(null);
		if (custAccCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(custAccCur);
		return baseResponse;
	}

	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(custAccRepository.existsById(id));
		return baseResponse;
	}

	public boolean checkAccS(CustAcc accOld, Cust custNew) {
		for (CustAcc accNew : custNew.getCustAcc()) {
			if (accNew.getId().equals(accOld.getId()) && "1".equals(accNew.getType())) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAccT(CustAcc accOld, Cust custNew) {
		for (CustAcc accNew : custNew.getCustAcc()) {
			if (accNew.getId().equals(accOld.getId()) && "2".equals(accNew.getType())) {
				return true;
			}
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveSAcc(Cust custOld, Cust custNew) {
		Set<CustAcc> custAcc = new HashSet<CustAcc>(); // danh sach can them
		Set<Long> lstId = new HashSet<Long>();
		for (CustAcc accOld : custOld.getCustAcc()) {
			if (accOld.getType().equals("1")) {
				if (!checkAccS(accOld, custNew)) {
					lstId.add(accOld.getId());
				}
			}
		}
		for (CustAcc accNew : custNew.getCustAcc()) {
			if (accNew.getId() == 0) {
				custAcc.add(accNew);
			}
		}
		for (Long id : lstId) {
			jpaDeleteExecute(id);
		}
		for (CustAcc accNew : custAcc) {
			accNew.setCust(custNew);
		}

		custAccRepository.saveAll(custAcc);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveTAcc(Cust custOld, Cust custNew) {
		Set<CustAcc> custAcc = new HashSet<CustAcc>(); // danh sach can them
		Set<Long> lstId = new HashSet<Long>();
		for (CustAcc accOld : custOld.getCustAcc()) {
			if (accOld.getType().equals("2")) {
				if (!checkAccT(accOld, custNew)) {
					lstId.add(accOld.getId());
				}
			}
		}
		for (CustAcc accNew : custNew.getCustAcc()) {
			if (accNew.getId() == 0) {
				custAcc.add(accNew);
			}
		}
		for (Long id : lstId) {
			jpaDeleteExecute(id);
		}
		for (CustAcc accNew : custAcc) {
			accNew.setCust(custNew);
		}

		custAccRepository.saveAll(custAcc);
	}

	// quandev
	public BaseResponse getCusAccById(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			List<CustAcc> listCus = custAccRepository.getCustTAccById(id);
			baseResponse.setData(listCus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}

	public BaseResponse getCusAccByAcc(String acc) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			// Lay nguoi thu huong tu core
			// tam thoi fix cung

			Cust custCore = new Cust();
			custCore.setFullname("Nguyen Van A");

			if (!ObjectUtils.isEmpty(acc) && !acc.trim().equals(""))
				baseResponse.setData(custCore);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	// end quandev
}
