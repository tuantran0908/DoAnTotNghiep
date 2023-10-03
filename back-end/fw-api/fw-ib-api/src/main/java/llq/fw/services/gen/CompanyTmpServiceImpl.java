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
import llq.fw.cm.models.CompanyTmp;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.CompanyTmpRepository;
import llq.fw.payload.request.gen.CompanyTmpSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class CompanyTmpServiceImpl implements BaseService<BaseResponse, CompanyTmp,CompanyTmpSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(CompanyTmpServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected CompanyTmpRepository companyTmpRepository;
	@Override
	public BaseResponse create(CompanyTmp companyTmp) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var companyTmpNew = CompanyTmp.builder()
					.address(companyTmp.getAddress())
					.approveType(companyTmp.getApproveType())
					.branchCode(companyTmp.getBranchCode())
					.businessCode(companyTmp.getBusinessCode())
					.businessFormCode(companyTmp.getBusinessFormCode())
					.businessIssuedon(companyTmp.getBusinessIssuedon())
					.businessPlaceofissue(companyTmp.getBusinessPlaceofissue())
					.businesslineCode(companyTmp.getBusinesslineCode())
					.businesslineName(companyTmp.getBusinesslineName())
					.cif(companyTmp.getCif())
					.cifCreatedDate(companyTmp.getCifCreatedDate())
					.cifStatus(companyTmp.getCifStatus())
					.country(companyTmp.getCountry())
					.custBirthday(companyTmp.getCustBirthday())
					.custCode(companyTmp.getCustCode())
					.custEmail(companyTmp.getCustEmail())
					.custFullname(companyTmp.getCustFullname())
					.custIdno(companyTmp.getCustIdno())
					.custIndentitypapers(companyTmp.getCustIndentitypapers())
					.custNmemonicName(companyTmp.getCustNmemonicName())
					.custPosition(companyTmp.getCustPosition())
					.custTel(companyTmp.getCustTel())
					.email(companyTmp.getEmail())
					.fax(companyTmp.getFax())
					.feeAccountNo(companyTmp.getFeeAccountNo())
					.feeBranchCode(companyTmp.getFeeBranchCode())
					.fullname(companyTmp.getFullname())
					.fullnameEn(companyTmp.getFullnameEn())
					.lrAuthorized(companyTmp.getLrAuthorized())
					.lrAuthorizedEnddate(companyTmp.getLrAuthorizedEnddate())
					.lrAuthorizedStartdate(companyTmp.getLrAuthorizedStartdate())
					.lrDate(companyTmp.getLrDate())
					.lrDefault(companyTmp.getLrDefault())
					.lrFullname(companyTmp.getLrFullname())
					.lrIdno(companyTmp.getLrIdno())
					.lrIndentitypapers(companyTmp.getLrIndentitypapers())
					.lrOccupations(companyTmp.getLrOccupations())
					.lrPlaceofissue(companyTmp.getLrPlaceofissue())
					.lrPosition(companyTmp.getLrPosition())
					.payAccountNo(companyTmp.getPayAccountNo())
					.payBranchCode(companyTmp.getPayBranchCode())
					.servicesPackage(companyTmp.getServicesPackage())
					.sortname(companyTmp.getSortname())
					.sortnameEn(companyTmp.getSortnameEn())
					.status(companyTmp.getStatus())
					.taxcode(companyTmp.getTaxcode())
					.tel(companyTmp.getTel())
					.approveAt(companyTmp.getApproveAt())
					.approveBy(companyTmp.getApproveBy())
					.note(companyTmp.getNote())
					.company(companyTmp.getCompany())
					.custType(companyTmp.getCustType())
					.build();
			jpaExecute(companyTmpNew);
			baseResponse.setData(companyTmpNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(CompanyTmp companyTmp) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var companyTmpCur = companyTmpRepository.findById(companyTmp.getId()).orElse(null);
			if (companyTmpCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			companyTmpCur.setAddress(companyTmp.getAddress());
			companyTmpCur.setApproveType(companyTmp.getApproveType());
			companyTmpCur.setBranchCode(companyTmp.getBranchCode());
			companyTmpCur.setBusinessCode(companyTmp.getBusinessCode());
			companyTmpCur.setBusinessFormCode(companyTmp.getBusinessFormCode());
			companyTmpCur.setBusinessIssuedon(companyTmp.getBusinessIssuedon());
			companyTmpCur.setBusinessPlaceofissue(companyTmp.getBusinessPlaceofissue());
			companyTmpCur.setBusinesslineCode(companyTmp.getBusinesslineCode());
			companyTmpCur.setBusinesslineName(companyTmp.getBusinesslineName());
			companyTmpCur.setCif(companyTmp.getCif());
			companyTmpCur.setCifCreatedDate(companyTmp.getCifCreatedDate());
			companyTmpCur.setCifStatus(companyTmp.getCifStatus());
			companyTmpCur.setCountry(companyTmp.getCountry());
			companyTmpCur.setCustBirthday(companyTmp.getCustBirthday());
			companyTmpCur.setCustCode(companyTmp.getCustCode());
			companyTmpCur.setCustEmail(companyTmp.getCustEmail());
			companyTmpCur.setCustFullname(companyTmp.getCustFullname());
			companyTmpCur.setCustIdno(companyTmp.getCustIdno());
			companyTmpCur.setCustIndentitypapers(companyTmp.getCustIndentitypapers());
			companyTmpCur.setCustNmemonicName(companyTmp.getCustNmemonicName());
			companyTmpCur.setCustPosition(companyTmp.getCustPosition());
			companyTmpCur.setCustTel(companyTmp.getCustTel());
			companyTmpCur.setEmail(companyTmp.getEmail());
			companyTmpCur.setFax(companyTmp.getFax());
			companyTmpCur.setFeeAccountNo(companyTmp.getFeeAccountNo());
			companyTmpCur.setFeeBranchCode(companyTmp.getFeeBranchCode());
			companyTmpCur.setFullname(companyTmp.getFullname());
			companyTmpCur.setFullnameEn(companyTmp.getFullnameEn());
			companyTmpCur.setLrAuthorized(companyTmp.getLrAuthorized());
			companyTmpCur.setLrAuthorizedEnddate(companyTmp.getLrAuthorizedEnddate());
			companyTmpCur.setLrAuthorizedStartdate(companyTmp.getLrAuthorizedStartdate());
			companyTmpCur.setLrDate(companyTmp.getLrDate());
			companyTmpCur.setLrDefault(companyTmp.getLrDefault());
			companyTmpCur.setLrFullname(companyTmp.getLrFullname());
			companyTmpCur.setLrIdno(companyTmp.getLrIdno());
			companyTmpCur.setLrIndentitypapers(companyTmp.getLrIndentitypapers());
			companyTmpCur.setLrOccupations(companyTmp.getLrOccupations());
			companyTmpCur.setLrPlaceofissue(companyTmp.getLrPlaceofissue());
			companyTmpCur.setLrPosition(companyTmp.getLrPosition());
			companyTmpCur.setPayAccountNo(companyTmp.getPayAccountNo());
			companyTmpCur.setPayBranchCode(companyTmp.getPayBranchCode());
			companyTmpCur.setServicesPackage(companyTmp.getServicesPackage());
			companyTmpCur.setSortname(companyTmp.getSortname());
			companyTmpCur.setSortnameEn(companyTmp.getSortnameEn());
			companyTmpCur.setStatus(companyTmp.getStatus());
			companyTmpCur.setTaxcode(companyTmp.getTaxcode());
			companyTmpCur.setTel(companyTmp.getTel());
			companyTmpCur.setApproveAt(companyTmp.getApproveAt());
			companyTmpCur.setApproveBy(companyTmp.getApproveBy());
			companyTmpCur.setNote(companyTmp.getNote());
			companyTmpCur.setCompany(companyTmp.getCompany());
			companyTmpCur.setCustType(companyTmp.getCustType());
			jpaExecute(companyTmpCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(companyTmpCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(CompanyTmpSearchRequest companyTmp, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<CompanyTmp> specification = new Specification<CompanyTmp>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<CompanyTmp> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(companyTmp.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 companyTmp.getId())));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getAddress())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("address")),
								"%" + companyTmp.getAddress().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getApproveType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("approveType")),
								"%" + companyTmp.getApproveType().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("branchCode")),
								"%" + companyTmp.getBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinessCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businessCode")),
								"%" + companyTmp.getBusinessCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinessFormCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businessFormCode")),
								"%" + companyTmp.getBusinessFormCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinessIssuedon())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("businessIssuedon")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getBusinessIssuedon())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinessPlaceofissue())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businessPlaceofissue")),
								"%" + companyTmp.getBusinessPlaceofissue().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinesslineCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businesslineCode")),
								"%" + companyTmp.getBusinesslineCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getBusinesslineName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("businesslineName")),
								"%" + companyTmp.getBusinesslineName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCif())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cif")),
								"%" + companyTmp.getCif().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCifCreatedDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("cifCreatedDate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getCifCreatedDate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCifStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("cifStatus")),
								"%" + companyTmp.getCifStatus().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCountry())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("country")),
								"%" + companyTmp.getCountry().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustBirthday())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("custBirthday")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getCustBirthday())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custCode")),
								"%" + companyTmp.getCustCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custEmail")),
								"%" + companyTmp.getCustEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custFullname")),
								"%" + companyTmp.getCustFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custIdno")),
								"%" + companyTmp.getCustIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custIndentitypapers")),
								"%" + companyTmp.getCustIndentitypapers().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustNmemonicName())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custNmemonicName")),
								"%" + companyTmp.getCustNmemonicName().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustPosition())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custPosition")),
								"%" + companyTmp.getCustPosition().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("custTel")),
								"%" + companyTmp.getCustTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getEmail())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
								"%" + companyTmp.getEmail().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getFax())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fax")),
								"%" + companyTmp.getFax().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getFeeAccountNo())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("feeAccountNo")),
								"%" + companyTmp.getFeeAccountNo().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getFeeBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("feeBranchCode")),
								"%" + companyTmp.getFeeBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")),
								"%" + companyTmp.getFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getFullnameEn())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("fullnameEn")),
								"%" + companyTmp.getFullnameEn().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrAuthorized())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("lrAuthorized")),
								 companyTmp.getLrAuthorized())));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrAuthorizedEnddate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrAuthorizedEnddate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getLrAuthorizedEnddate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrAuthorizedStartdate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrAuthorizedStartdate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getLrAuthorizedStartdate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrDate())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("lrDate")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getLrDate())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrDefault())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrDefault")),
								"%" + companyTmp.getLrDefault().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrFullname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrFullname")),
								"%" + companyTmp.getLrFullname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrIdno())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrIdno")),
								"%" + companyTmp.getLrIdno().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrIndentitypapers())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrIndentitypapers")),
								"%" + companyTmp.getLrIndentitypapers().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrOccupations())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrOccupations")),
								"%" + companyTmp.getLrOccupations().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrPlaceofissue())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrPlaceofissue")),
								"%" + companyTmp.getLrPlaceofissue().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getLrPosition())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("lrPosition")),
								"%" + companyTmp.getLrPosition().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getPayAccountNo())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("payAccountNo")),
								"%" + companyTmp.getPayAccountNo().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getPayBranchCode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("payBranchCode")),
								"%" + companyTmp.getPayBranchCode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getServicesPackage())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("servicesPackage")),
								 companyTmp.getServicesPackage())));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getSortname())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortname")),
								"%" + companyTmp.getSortname().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getSortnameEn())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("sortnameEn")),
								"%" + companyTmp.getSortnameEn().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 companyTmp.getStatus())));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getTaxcode())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("taxcode")),
								"%" + companyTmp.getTaxcode().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getTel())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("tel")),
								"%" + companyTmp.getTel().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getApproveAt())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.function("TRUNC", Date.class, root.get("approveAt")),
								criteriaBuilder.function("TO_DATE", Date.class, criteriaBuilder.literal(formatDate.format(companyTmp.getApproveAt())), criteriaBuilder.literal("DD-MM-YYYY")))));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getApproveBy())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("approveBy")),
								 companyTmp.getApproveBy())));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getNote())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("note")),
								"%" + companyTmp.getNote().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCompany())) {
						String[] ids=companyTmp.getCompany().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("company").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("company").get("id"),
									 companyTmp.getCompany().toUpperCase())));
						}
					}
					if (!ObjectUtils.isEmpty(companyTmp.getCustType())) {
						String[] ids=companyTmp.getCustType().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("custType").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("custType").get("id"),
									 companyTmp.getCustType().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<CompanyTmp> page = companyTmpRepository.findAll(specification, pageable);
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
			var companyTmpCur = companyTmpRepository.findById(id).orElse(null);
			if (companyTmpCur == null) {
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
	public void jpaExecute(CompanyTmp companyTmp) {
		companyTmpRepository.save(companyTmp);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		companyTmpRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var companyTmpCur = companyTmpRepository.findById(id).orElse(null);
		if (companyTmpCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(companyTmpCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(companyTmpRepository.existsById(id));
		return baseResponse;
	}
}
