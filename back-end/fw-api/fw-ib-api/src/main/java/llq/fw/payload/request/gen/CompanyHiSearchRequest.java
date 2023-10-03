package llq.fw.payload.request.gen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHiSearchRequest {
	private java.lang.Long id;
	private java.lang.String address;
	private java.lang.String approveType;
	private java.lang.String branchCode;
	private java.lang.String businessCode;
	private java.lang.String businessFormCode;
	private java.util.Date businessIssuedon;
	private java.lang.String businessPlaceofissue;
	private java.lang.String businesslineCode;
	private java.lang.String businesslineName;
	private java.lang.String cif;
	private java.util.Date cifCreatedDate;
	private java.lang.String cifStatus;
	private java.lang.String country;
	private java.lang.String creatatedAt;
	private java.lang.String creatatedBy;
	private java.util.Date custBirthday;
	private java.lang.String custCode;
	private java.lang.String custEmail;
	private java.lang.String custFullname;
	private java.lang.String custIdno;
	private java.lang.String custIndentitypapers;
	private java.lang.String custNmemonicName;
	private java.lang.String custPosition;
	private java.lang.String custTel;
	private java.lang.Long custTypeId;
	private java.lang.String email;
	private java.lang.String fax;
	private java.lang.String feeAccountNo;
	private java.lang.String feeBranchCode;
	private java.lang.String fullname;
	private java.lang.String fullnameEn;
	private llq.fw.cm.enums.company.AuthorizedEnum lrAuthorized;
	private java.util.Date lrAuthorizedEnddate;
	private java.util.Date lrAuthorizedStartdate;
	private java.util.Date lrDate;
	private java.lang.String lrDefault;
	private java.lang.String lrFullname;
	private java.lang.String lrIdno;
	private java.lang.String lrIndentitypapers;
	private java.lang.String lrOccupations;
	private java.lang.String lrPlaceofissue;
	private java.lang.String lrPosition;
	private java.lang.String payAccountNo;
	private java.lang.String payBranchCode;
	private llq.fw.cm.enums.company.ServicePackageEnum servicesPackage;
	private java.lang.String sortname;
	private java.lang.String sortnameEn;
	private llq.fw.cm.enums.company.CompanyStatusEnum status;
	private java.lang.String taxcode;
	private java.lang.String tel;
	private java.util.Date approveAt;
	private java.lang.Long approveBy;
	private java.lang.Long companyId;
	private java.lang.Long companyTmpId;
}
