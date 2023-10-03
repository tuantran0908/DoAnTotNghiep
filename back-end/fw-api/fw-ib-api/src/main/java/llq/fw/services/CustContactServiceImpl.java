package llq.fw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.Normalizer;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.enums.custcontact.ProductEnum;
import llq.fw.cm.models.BankReceiving;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustContact;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.BankReceivingRepository;
import llq.fw.cm.repository.gen.CustContactRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.gen.CustContactSearchRequest;
import llq.fw.cm.services.BaseService;

@Component
public class CustContactServiceImpl
		implements BaseService<BaseResponse, CustContact, CustContactSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustContactServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CustContactRepository custContactRepository;
	@Autowired
	private BankReceivingRepository bankReceivingRepository;
	@Autowired
	private CustRepository custRepository;

	@Override
	public BaseResponse create(CustContact custContact) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		CustDetailsImpl userDetail = (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		try {
			var custContactNew = CustContact.builder()
					.custId(currentCust)
					.product(custContact.getProduct())
					.receiveAccount(custContact.getReceiveAccount())
					.bankReceiving(custContact.getBankReceiving())
					.sortname(custContact.getSortname())
					.receiveName(convertToString(custContact.getReceiveName()))
					.build();
			// if(ProductEnum.CORE.equals(custContactNew.getProduct())) {
			// //call corebanking check receive account
			// }
			if (checkSortName(custContactNew.getSortname())) {
				jpaExecute(custContactNew);
				baseResponse.setData(custContactNew);
			} else {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
			}
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse update(CustContact custContact) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custContactCur = custContactRepository.findById(custContact.getId()).orElse(null);
			if (custContactCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			boolean checkName = false;
			if (!custContactCur.getSortname().equals(custContact.getSortname())) {
				checkName = checkSortName(custContact.getSortname());
			} else {
				checkName = true;
			}
			custContactCur.setCustId(custContact.getCustId());
			custContactCur.setProduct(custContact.getProduct());
			custContactCur.setReceiveAccount(custContact.getReceiveAccount());
			custContactCur.setBankReceiving(custContact.getBankReceiving());
			custContactCur.setSortname(custContact.getSortname());
			custContactCur.setReceiveName(convertToString(custContact.getReceiveName()));
			// check ton tai sortname
			if (checkName) {
				jpaExecute(custContactCur);
				baseResponse.setData(custContactCur);
				baseResponse.setFwError(FwError.THANHCONG);
			} else {
				baseResponse.setFwError(FwError.KHONGTHANHCONG);
			}
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse search(CustContactSearchRequest custContact, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CustContact> specification = new Specification<CustContact>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CustContact> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(custContact.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"),
								custContact.getId())));
					}
					if (!ObjectUtils.isEmpty(custContact.getCustId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("custId"),
								custContact.getCustId())));
					}
					if (!ObjectUtils.isEmpty(custContact.getProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("product"),
								custContact.getProduct())));
					}
					if (!ObjectUtils.isEmpty(custContact.getReceiveAccount())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
										"%" + custContact.getReceiveAccount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custContact.getSortname())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
										"%" + custContact.getSortname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custContact.getKeyWord())) {
						predicates.add(criteriaBuilder
								.or(criteriaBuilder
										.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
												"%" + custContact.getKeyWord().trim().toUpperCase() + "%")),
										criteriaBuilder.and(
												criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveName")),
														"%" + custContact.getKeyWord().trim().toUpperCase() + "%")),
										criteriaBuilder.and(
												criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
														"%" + custContact.getKeyWord().trim().toUpperCase() + "%"))));
					}
					if (!ObjectUtils.isEmpty(custContact.getBankReceiving())) {
						String[] ids = custContact.getBankReceiving().split(",");
						if (ids.length > 1) {
							In<String> inClause = criteriaBuilder.in(root.get("bankReceiving").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(
									criteriaBuilder.and(criteriaBuilder.equal(root.get("bankReceiving").get("code"),
											custContact.getBankReceiving().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CustContact> page = custContactRepository.findAll(specification, pageable);
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
			var custContactCur = custContactRepository.findById(id).orElse(null);
			if (custContactCur == null) {
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
	public void jpaExecute(CustContact custContact) {
		custContactRepository.save(custContact);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		custContactRepository.deleteById(id);
	}

	public boolean checkSortName(String value) {
		String[] lstSortName = custContactRepository.getSortName();
		for (String sName : lstSortName) {
			if (sName.equals(value)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var custContactCur = custContactRepository.findById(id).orElse(null);
		if (custContactCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(custContactCur);
		return baseResponse;
	}

	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(custContactRepository.existsById(id));
		return baseResponse;
	}

	public List<BankReceiving> getDsBankReceiving() {
		List<BankReceiving> lstBank = bankReceivingRepository.findAll();
		return lstBank;
	}

	// convert string to uppercase
	public static String convertToString(String value) {
		try {
			String value1 = value.toUpperCase();
			String temp = Normalizer.normalize(value1, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			return pattern.matcher(temp).replaceAll("");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// quandev
	public BaseResponse getCutsContact(CustContactSearchRequest custContact) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CustContact> specification = new Specification<CustContact>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CustContact> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(custContact.getCustId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("custId"),
								custContact.getCustId())));
					}
					if (!ObjectUtils.isEmpty(custContact.getProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("product"),
								custContact.getProduct())));
					}
					if (!ObjectUtils.isEmpty(custContact.getReceiveAccount())) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
										"%" + custContact.getReceiveAccount().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custContact.getSortname())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
										"%" + custContact.getSortname().toUpperCase() + "%")));
					}

					if (!ObjectUtils.isEmpty(custContact.getProductValue())) {
						predicates.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("product"),
										ProductEnum.forValue(custContact.getProductValue()))));
					}

					if (!ObjectUtils.isEmpty(custContact.getKeyWord())) {
						predicates.add(criteriaBuilder
								.or(criteriaBuilder
										.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
												"%" + custContact.getKeyWord().trim().toUpperCase() + "%")),
										criteriaBuilder.and(
												criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveName")),
														"%" + custContact.getKeyWord().trim().toUpperCase() + "%")),
										criteriaBuilder.and(
												criteriaBuilder.like(criteriaBuilder.upper(root.get("receiveAccount")),
														"%" + custContact.getKeyWord().trim().toUpperCase() + "%"))));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			List<CustContact> listCustContact = custContactRepository.findAll(specification);
			baseResponse.setData(listCustContact);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error", e);
		}
		return baseResponse;
	}

	// end quandev
}
