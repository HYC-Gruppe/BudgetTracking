package capgemini.hyc.BudgetTracking.jira.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.bc.issue.worklog.TimeTrackingConfiguration;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.LinkCollection;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.util.ParameterUtils;
/**
 * @author HYC (Nadine Heinz, Jennifer Pritzl, Sarah Jordan, 
 *Alexandra Dos Santos, Maja Stach)
 *Assistant class to get all necessary information from the ER
 */
public class IssueEx {

	final Issue issue;
	final BigDecimal cbb, ptd, etc,eac, dvi;
	String Sta = "Open";
	private final Map map;


	public IssueEx(Issue pIssue, Map map) {
		this.issue = pIssue;
		cbb = calcCbb(pIssue);
		ptd = calcPtd(pIssue, map);
		etc = calcEtc(pIssue, map);
		eac = calcEac();
		dvi = calcDvi();
		this.map = map;

	}

	public final static BigDecimal BT = ComponentAccessor
			.getComponent(TimeTrackingConfiguration.class).getHoursPerDay()
			.multiply(new BigDecimal(60 * 60));

	/**
	 * 
	 * @param pIssue
	 * @param map
	 * @return List of linked issues to this.issue
	 */
	public List<Issue> linkedIssues(Issue pIssue, Map map){
		IssueLinkManager linkManager = ComponentAccessor.getIssueLinkManager();
		LinkCollection linkedIssues = linkManager.getLinkCollection(pIssue, ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser());
		List<IssueLink> list = linkManager.getInwardLinks(pIssue.getId());
		List<Issue> types = new ArrayList();
		for(IssueLink link : list){
			if(link.getIssueLinkType().getId().compareTo(ParameterUtils.getLongParam(map, "linkType")) == 0)
				types.add(link.getSourceObject());
		}
		return types;
	}

	
	/**
	 * 
	 * @param pIssue
	 * @param map
	 * @return List of subtasks of the linked issues
	 */
	public List<Issue> subTasks(Issue pIssue, Map map){
		List<Issue> list = linkedIssues(pIssue, map);
		List<Issue> subTasks = new ArrayList();
		for(Issue issue : list){
			for(Issue i : issue.getSubTaskObjects()){
				if(i.getStatusObject().getId().compareTo("5")!=0)
					subTasks.add(i);
			}
		}
		return subTasks;
	}
	
	/**
	 * 
	 * @param aIssue
	 * @return cbb value as BigDecimal
	 */
	public BigDecimal calcCbb(Issue aIssue) {

		if (aIssue.getOriginalEstimate() == null) {
			return new BigDecimal(0.0);
		} else {

			BigDecimal cbbBig = new BigDecimal(aIssue.getOriginalEstimate());

			cbbBig = cbbBig.divide(BT, 1, BigDecimal.ROUND_HALF_UP);

			return cbbBig;
		}
	}


/**
 * 
 * @param aIssue
 * @param map
 * @return ptd value as BigDecimal
 */
	private BigDecimal calcPtd(Issue aIssue, Map map) {
		List<Issue> list1 = linkedIssues(aIssue, map);
		BigDecimal ptd = new BigDecimal(0.0);
		for (Issue issue : list1) {
			if (issue.getTimeSpent() != null)
				ptd = ptd.add(new BigDecimal(issue.getTimeSpent()));
		}

		return ptd.divide(BT, 1, BigDecimal.ROUND_HALF_UP);
	}
/**
 * 
 * @param pIssue
 * @param map
 * @return etc value as BigDecimal
 */
	public BigDecimal calcEtc(Issue pIssue, Map map){
		List<Issue> tasks = subTasks(pIssue, map);
		BigDecimal etc = new BigDecimal(0.0);
		for(Issue issue : tasks){
			if(issue.getOriginalEstimate() != null)
				etc = etc.add(new BigDecimal(issue.getOriginalEstimate()));
		}
		if(tasks != null)
			return etc.divide(BT, 1, BigDecimal.ROUND_HALF_UP);
		else return new BigDecimal(0.0);
	}


	public BigDecimal calcEac(){
		BigDecimal eac= ptd.add(etc);
		return eac;
	}

	public BigDecimal calcDvi(){
		BigDecimal dvi =cbb.subtract(eac);
		return dvi;
	}

	/**
	 * The Method gets the Type of the Issue, later diplayed as an icon
	 */
	public Issue getIssue() {
		return issue;
	}
	
	public String getKey() {
		return issue.getKey();
	}

	public String getSummary() {
		return issue.getSummary();
	}
	/**
	 * 
	 * @return Gets Status Object, later displayed as an icon
	 */
	public Status getStatus() {
		return issue.getStatusObject();
	}

	public BigDecimal getCbb() {
		return cbb;
	}

	public BigDecimal getPtd(){
		return ptd;
	}

	public BigDecimal getEtc(){
		return etc;

	}
	public BigDecimal getEac(){
		return eac;
	}

	public BigDecimal getDvi(){
		return dvi;
	}

	public List<Issue> getLinkedIssues(){
		return linkedIssues(issue, map);
	}
	public List<Issue> getSubTasks(){
		return subTasks(issue, map);
	}

}
