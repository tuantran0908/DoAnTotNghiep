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
import llq.fw.cm.models.Issue;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.repository.gen.IssueRepository;
import llq.fw.payload.request.gen.IssueSearchRequest;
import llq.fw.cm.services.BaseService;
@Component
public class IssueServiceImpl implements BaseService<BaseResponse, Issue,IssueSearchRequest, java.lang.Long> {
	private static final Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);
	private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	protected IssueRepository issueRepository;
	@Override
	public BaseResponse create(Issue issue) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var issueNew = Issue.builder()
					.answer(issue.getAnswer())
					.iindex(issue.getIindex())
					.question(issue.getQuestion())
					.status(issue.getStatus())
					.issueCategory(issue.getIssueCategory())
					.build();
			jpaExecute(issueNew);
			baseResponse.setData(issueNew);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse update(Issue issue) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			var issueCur = issueRepository.findById(issue.getId()).orElse(null);
			if (issueCur == null) {
				baseResponse.setFwError(FwError.DLKHONGTONTAI);
				return baseResponse;
			}
			issueCur.setAnswer(issue.getAnswer());
			issueCur.setIindex(issue.getIindex());
			issueCur.setQuestion(issue.getQuestion());
			issueCur.setStatus(issue.getStatus());
			issueCur.setIssueCategory(issue.getIssueCategory());
			jpaExecute(issueCur);
			/*
			 * Luu obj lich su neu can
			 */
			baseResponse.setData(issueCur);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
			logger.error("error",e);
		}
		return baseResponse;
	}
	@Override
	public BaseResponse search(IssueSearchRequest issue, Pageable pageable) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG); // TODO : Lay tham so xu ly song ngu
		try {
			Specification<Issue> specification = new Specification<Issue>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public Predicate toPredicate(Root<Issue> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (!ObjectUtils.isEmpty(issue.getId())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("id")),
								 issue.getId())));
					}
					if (!ObjectUtils.isEmpty(issue.getAnswer())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("answer")),
								"%" + issue.getAnswer().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(issue.getIindex())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("iindex")),
								"%" + issue.getIindex().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(issue.getQuestion())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(root.get("question")),
								"%" + issue.getQuestion().toUpperCase() + "%")));
					}
					if (!ObjectUtils.isEmpty(issue.getStatus())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
								 issue.getStatus())));
					}
					if (!ObjectUtils.isEmpty(issue.getIssueCategory())) {
						String[] ids=issue.getIssueCategory().split(",");
						if(ids.length >1) {
							In<String> inClause = criteriaBuilder.in(root.get("issueCategory").get("id"));
							for (String id : ids) {
								inClause.value(id.toUpperCase());
							}
							predicates.add(criteriaBuilder.and(inClause));
						}else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("issueCategory").get("id"),
									 issue.getIssueCategory().toUpperCase())));
						}
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			Page<Issue> page = issueRepository.findAll(specification, pageable);
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
			var issueCur = issueRepository.findById(id).orElse(null);
			if (issueCur == null) {
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
	public void jpaExecute(Issue issue) {
		issueRepository.save(issue);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void jpaDeleteExecute(java.lang.Long id) {
		issueRepository.deleteById(id);
	}
	@Override
	public BaseResponse getDetail(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		var issueCur = issueRepository.findById(id).orElse(null);
		if (issueCur == null) {
			baseResponse.setFwError(FwError.DLKHONGTONTAI);
			return baseResponse;
		}
		baseResponse.setData(issueCur);
		return baseResponse;
	}
	@Override
	public BaseResponse checkExist(java.lang.Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		baseResponse.setData(issueRepository.existsById(id));
		return baseResponse;
	}
}
