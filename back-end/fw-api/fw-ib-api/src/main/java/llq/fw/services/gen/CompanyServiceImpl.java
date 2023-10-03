package llq.fw.services.gen;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
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
import llq.fw.cm.models.Company;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.CompanyRepository;
import llq.fw.payload.request.gen.CompanySearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class CompanyServiceImpl implements BaseService<BaseResponse, Company,CompanySearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CompanyRepository companyRepository;
	@Override
	public BaseResponse create(Company company) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var companyNew = Company.builder()
					.address(company.getAddress())
					.approveType(company.getApproveType())
					.branchCode(company.getBranchCode())
					.businessCode(company.getBusinessCode())
					.businessIssuedon(company.getBusinessIssuedon())
					.businessPlaceofissue(company.getBusinessPlaceofissue())
					.businesslineCode(company.getBusinesslineCode())
					.businesslineName(company.getBusinesslineName())
					.cif(company.getCif())
					.cifCreatedDate(company.getCifCreatedDate())
					.cifStatus(company.getCifStatus())
					.country(company.getCountry())
					.custBirthday(company.getCustBirthday())
					.custCode(company.getCustCode())
					.custEmail(company.getCustEmail())
					.custFullname(company.getCustFullname())
					.custIdno(company.getCustIdno())
					.custIndentitypapers(company.getCustIndentitypapers())
					.custNmemonicName(company.getCustNmemonicName())
					.custPosition(company.getCustPosition())
					.custTel(company.getCustTel())
					.email(company.getEmail())
					.fax(company.getFax())
					.feeAccountNo(company.getFeeAccountNo())
					.feeBranchCode(company.getFeeBranchCode())
					.fullname(company.getFullname())
					.fullnameEn(company.getFullnameEn())
					.lrAuthorized(company.getLrAuthorized())
					.lrAuthorizedEnddate(company.getLrAuthorizedEnddate())
					.lrAuthorizedStartdate(company.getLrAuthorizedStartdate())
					.lrDate(company.getLrDate())
					.lrDefault(company.getLrDefault())
					.lrFullname(company.getLrFullname())
					.lrIdno(company.getLrIdno())
					.lrIndentitypapers(company.getLrIndentitypapers())
					.lrOccupations(company.getLrOccupations())
					.lrPlaceofissue(company.getLrPlaceofissue())
					.lrPosition(company.getLrPosition())
					.payAccountNo(company.getPayAccountNo())
					.payBranchCode(company.getPayBranchCode())
					.servicesPackage(company.getServicesPackage())
					.sortname(company.getSortname())
					.sortnameEn(company.getSortnameEn())
					.status(company.getStatus())
					.taxcode(company.getTaxcode())
					.tel(company.getTel())
					.approveAt(company.getApproveAt())
					.approveBy(company.getApproveBy())
					.note(company.getNote())
					.businessForm(company.getBusinessForm())
					.custType(company.getCustType())
					.companyTmps(company.getCompanyTmps())
					.custs(company.getCusts())
					.build();
			jpaExecute(companyNew);
			baseResponse.setData(companyNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Company company) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var companyCur = companyRepository.findById(company.getId()).orElse(null);
			if (companyCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			companyCur.setAddress(company.getAddress());
			companyCur.setApproveType(company.getApproveType());
			companyCur.setBranchCode(company.getBranchCode());
			companyCur.setBusinessCode(company.getBusinessCode());
			companyCur.setBusinessIssuedon(company.getBusinessIssuedon());
			companyCur.setBusinessPlaceofissue(company.getBusinessPlaceofissue());
			companyCur.setBusinesslineCode(company.getBusinesslineCode());
			companyCur.setBusinesslineName(company.getBusinesslineName());
			companyCur.setCif(company.getCif());
			companyCur.setCifCreatedDate(company.getCifCreatedDate());
			companyCur.setCifStatus(company.getCifStatus());
			companyCur.setCountry(company.getCountry());
			companyCur.setCustBirthday(company.getCustBirthday());
			companyCur.setCustCode(company.getCustCode());
			companyCur.setCustEmail(company.getCustEmail());
			companyCur.setCustFullname(company.getCustFullname());
			companyCur.setCustIdno(company.getCustIdno());
			companyCur.setCustIndentitypapers(company.getCustIndentitypapers());
			companyCur.setCustNmemonicName(company.getCustNmemonicName());
			companyCur.setCustPosition(company.getCustPosition());
			companyCur.setCustTel(company.getCustTel());
			companyCur.setEmail(company.getEmail());
			companyCur.setFax(company.getFax());
			companyCur.setFeeAccountNo(company.getFeeAccountNo());
			companyCur.setFeeBranchCode(company.getFeeBranchCode());
			companyCur.setFullname(company.getFullname());
			companyCur.setFullnameEn(company.getFullnameEn());
			companyCur.setLrAuthorized(company.getLrAuthorized());
			companyCur.setLrAuthorizedEnddate(company.getLrAuthorizedEnddate());
			companyCur.setLrAuthorizedStartdate(company.getLrAuthorizedStartdate());
			companyCur.setLrDate(company.getLrDate());
			companyCur.setLrDefault(company.getLrDefault());
			companyCur.setLrFullname(company.getLrFullname());
			companyCur.setLrIdno(company.getLrIdno());
			companyCur.setLrIndentitypapers(company.getLrIndentitypapers());
			companyCur.setLrOccupations(company.getLrOccupations());
			companyCur.setLrPlaceofissue(company.getLrPlaceofissue());
			companyCur.setLrPosition(company.getLrPosition());
			companyCur.setPayAccountNo(company.getPayAccountNo());
			companyCur.setPayBranchCode(company.getPayBranchCode());
			companyCur.setServicesPackage(company.getServicesPackage());
			companyCur.setSortname(company.getSortname());
			companyCur.setSortnameEn(company.getSortnameEn());
			companyCur.setStatus(company.getStatus());
			companyCur.setTaxcode(company.getTaxcode());
			companyCur.setTel(company.getTel());
			companyCur.setApproveAt(company.getApproveAt());
			companyCur.setApproveBy(company.getApproveBy());
			companyCur.setNote(company.getNote());
			companyCur.setBusinessForm(company.getBusinessForm());
			companyCur.setCustType(company.getCustType());
			companyCur.setCompanyTmps(company.getCompanyTmps());
			companyCur.setCusts(company.getCusts());
			jpaExecute(companyCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(companyCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(CompanySearchRequest company, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Company> specification = new Specification<Company>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(company.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 company.getId())));
					}
					if (!ObjectUtils.isEmpty(company.getAddress())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("address")),
								"%" + company.getAddress().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getApproveType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("approveType")),
								"%" + company.getApproveType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("branchCode")),
								"%" + company.getBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBusinessCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businessCode")),
								"%" + company.getBusinessCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBusinessIssuedon())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("businessIssuedon")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getBusinessIssuedon())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getBusinessPlaceofissue())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businessPlaceofissue")),
								"%" + company.getBusinessPlaceofissue().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBusinesslineCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businesslineCode")),
								"%" + company.getBusinesslineCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBusinesslineName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businesslineName")),
								"%" + company.getBusinesslineName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCif())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cif")),
								"%" + company.getCif().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCifCreatedDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("cifCreatedDate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getCifCreatedDate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getCifStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cifStatus")),
								"%" + company.getCifStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCountry())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("country")),
								"%" + company.getCountry().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustBirthday())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("custBirthday")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getCustBirthday())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getCustCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custCode")),
								"%" + company.getCustCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custEmail")),
								"%" + company.getCustEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custFullname")),
								"%" + company.getCustFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custIdno")),
								"%" + company.getCustIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custIndentitypapers")),
								"%" + company.getCustIndentitypapers().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustNmemonicName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custNmemonicName")),
								"%" + company.getCustNmemonicName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustPosition())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custPosition")),
								"%" + company.getCustPosition().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getCustTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custTel")),
								"%" + company.getCustTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
								"%" + company.getEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getFax())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fax")),
								"%" + company.getFax().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getFeeAccountNo())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("feeAccountNo")),
								"%" + company.getFeeAccountNo().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getFeeBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("feeBranchCode")),
								"%" + company.getFeeBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")),
								"%" + company.getFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getFullnameEn())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullnameEn")),
								"%" + company.getFullnameEn().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrAuthorized())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("lrAuthorized")),
								 company.getLrAuthorized())));
					}
					if (!ObjectUtils.isEmpty(company.getLrAuthorizedEnddate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrAuthorizedEnddate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getLrAuthorizedEnddate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getLrAuthorizedStartdate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrAuthorizedStartdate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getLrAuthorizedStartdate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getLrDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrDate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getLrDate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getLrDefault())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrDefault")),
								"%" + company.getLrDefault().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrFullname")),
								"%" + company.getLrFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrIdno")),
								"%" + company.getLrIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrIndentitypapers")),
								"%" + company.getLrIndentitypapers().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrOccupations())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrOccupations")),
								"%" + company.getLrOccupations().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrPlaceofissue())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrPlaceofissue")),
								"%" + company.getLrPlaceofissue().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getLrPosition())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrPosition")),
								"%" + company.getLrPosition().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getPayAccountNo())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("payAccountNo")),
								"%" + company.getPayAccountNo().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getPayBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("payBranchCode")),
								"%" + company.getPayBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getServicesPackage())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("servicesPackage")),
								 company.getServicesPackage())));
					}
					if (!ObjectUtils.isEmpty(company.getSortname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
								"%" + company.getSortname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getSortnameEn())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortnameEn")),
								"%" + company.getSortnameEn().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 company.getStatus())));
					}
					if (!ObjectUtils.isEmpty(company.getTaxcode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("taxcode")),
								"%" + company.getTaxcode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + company.getTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getApproveAt())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("approveAt")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(company.getApproveAt())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(company.getApproveBy())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("approveBy")),
								 company.getApproveBy())));
					}
					if (!ObjectUtils.isEmpty(company.getNote())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("note")),
								"%" + company.getNote().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(company.getBusinessForm())) {
						String[] ids=company.getBusinessForm().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("businessForm").get("code"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("businessForm").get("code"),
									 company.getBusinessForm().toUpperCase())));
						}
					}
					if (!ObjectUtils.isEmpty(company.getCustType())) {
						String[] ids=company.getCustType().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("custType").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("custType").get("id"),
									 company.getCustType().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Company> page = companyRepository.findAll(specification, pageable);
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
			var companyCur = companyRepository.findById(id).orElse(null);
			if (companyCur == null) {
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
	public void jpaExecute(Company company) {
		companyRepository.save(company);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		companyRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var companyCur = companyRepository.findById(id).orElse(null);
		if (companyCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(companyCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(companyRepository.existsById(id));
		return baseResponse;
	}
}
