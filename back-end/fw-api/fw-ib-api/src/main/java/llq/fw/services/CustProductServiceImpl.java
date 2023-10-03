package llq.fw.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
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
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustAcc;
import llq.fw.cm.models.CustProduct;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CustProductRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.services.CustDetailsImpl;
import llq.fw.payload.request.gen.CustProductSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class CustProductServiceImpl implements BaseService<BaseResponse, CustProduct,CustProductSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CustProductServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CustProductRepository custProductRepository;
	
	@Autowired
	protected CustRepository custRepository;
	
	@Override
	public BaseResponse create(CustProduct custProduct) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custProductNew = CustProduct.builder()
					.product(custProduct.getProduct())
					.custCode(custProduct.getCustCode())
					.cust(custProduct.getCust())
					.build();
			jpaExecute(custProductNew);
			baseResponse.setData(custProductNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(CustProduct custProduct) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custProductCur = custProductRepository.findById(custProduct.getId()).orElse(null);
			if (custProductCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			custProductCur.setProduct(custProduct.getProduct());
			custProductCur.setCustCode(custProduct.getCustCode());
			custProductCur.setCust(custProduct.getCust());
			jpaExecute(custProductCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(custProductCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(CustProductSearchRequest custProduct, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		CustDetailsImpl userDetail =   (CustDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Cust currentCust = custRepository.findById(userDetail.getId()).orElse(null);
		try {
			Specification<CustProduct> specification = new Specification<CustProduct>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<CustProduct> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if(currentCust != null) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("company")),
								currentCust.getCompany())));						
					}
					if (!ObjectUtils.isEmpty(custProduct.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 custProduct.getId())));
					}
					if (!ObjectUtils.isEmpty(custProduct.getProduct())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("product")),
								"%" + custProduct.getProduct().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custProduct.getCustCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custCode")),
								"%" + custProduct.getCustCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(custProduct.getCust())) {
						String[] ids=custProduct.getCust().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("cust").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("cust").get("id"),
									 custProduct.getCust().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CustProduct> page = custProductRepository.findAll(specification, pageable);
			baseResponse.setData(page);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse delete(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var custProductCur = custProductRepository.findById(id).orElse(null);
			if (custProductCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			jpaDeleteExecute(id);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaExecute(CustProduct custProduct) {
		custProductRepository.save(custProduct);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		custProductRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var custProductCur = custProductRepository.findById(id).orElse(null);
		if (custProductCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(custProductCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(custProductRepository.existsById(id));
		return baseResponse;
	}
	
	public boolean checkProduct(CustProduct productOld, Cust custNew) {
		for(CustProduct productNew: custNew.getCustProduct()) {
			if(productOld.getId().equals(productNew.getId())) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveProduct(Cust custOld, Cust custNew) {
		Set<CustProduct> custProduct = new HashSet<CustProduct>(); //danh sach can them
		Set<CustProduct> product = new HashSet<CustProduct>(); //danh sach can update
		Set<Long> lstId  = new HashSet<Long>();
		for(CustProduct productOld: custOld.getCustProduct()) {
			if(!checkProduct(productOld, custNew)) {
				lstId.add(productOld.getId());
			}else {
				product.add(productOld);
			}
		}
		for(CustProduct productNew: custNew.getCustProduct()) {
			if(productNew.getId() == null) {
				custProduct.add(productNew);
			}else {
				for(CustProduct pro: product) {
					if(pro.getId().equals(productNew.getId())) {
						pro.setCustCode(productNew.getCustCode());
					}
				}	
			}
		}
		for(Long id: lstId) {
			jpaDeleteExecute(id);
		}
		for(CustProduct productNew: custProduct) {
			productNew.setCust(custNew);
		}
		
		for(CustProduct pro: product) {
			pro.setCust(custNew);
			update(pro);
		}
		custProductRepository.saveAll(custProduct);
	}
}
