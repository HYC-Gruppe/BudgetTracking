package capgemini.hyc.BudgetTracking.jira.reports;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.bc.issue.worklog.TimeTrackingConfiguration;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.util.ParameterUtils;
import com.atlassian.jira.web.action.ProjectActionSupport;
/**
 * 
 * @author HYC (Nadine Heinz, Jennifer Pritzl, Sarah Jordan, 
 *Alexandra Dos Santos, Maja Stach)
 */

public class BudgetTracking extends AbstractReport {
	final IssueManager manager;
	final IssueLinkManager linkManager;
	/**
	 * Konstruktor
	 * @param manager
	 * 
	 */
	public BudgetTracking(final IssueManager manager, final IssueLinkManager linkManager){
		this.manager = manager;
		this.linkManager = linkManager;
	}

	/**
	 * Method that creates html
	 */
	public String generateReportHtml(ProjectActionSupport action, Map map) throws Exception {

		return descriptor.getHtml("Html-view", putInMap(action, map));
	}

	/**
	 * 
	 * @param action
	 * @param map
	 * @return The Method looks for the Issue which the user chose
	 */
	public Issue getParams(ProjectActionSupport action, Map map){
		String erID = map.get("erID").toString();
		Issue er = manager.getIssueObject(erID);
		return er;
	}	

	/**
	 * 
	 * @param action
	 * @param map
	 * @return list of features belonging to the chosen Issue
	 */
	public List<Issue> getFeatures(ProjectActionSupport action, Map map){
		List<Issue> list = (List<Issue>) getParams(action, map).getSubTaskObjects();
		return list;
	}


	/**
	 * The next block of Methods calculates the sum of all the important numbers  
	 */
	public BigDecimal getCbbSum(ProjectActionSupport action, Map map){
		List<IssueEx> list = convertToIssueEx(action, map);
		BigDecimal cbbSum = new BigDecimal(0);
		for(IssueEx issue : list){
			cbbSum = cbbSum.add(issue.getCbb());
		}
		return cbbSum;
	}
	public BigDecimal getPtdSum(ProjectActionSupport action, Map map){
		List<IssueEx> list = convertToIssueEx(action, map);
		BigDecimal ptdSum = new BigDecimal(0);
		for(IssueEx issue : list){
			ptdSum = ptdSum.add(issue.getPtd());
		}
		return ptdSum;
	}
	public BigDecimal getEtcSum(ProjectActionSupport action, Map map){
		List<IssueEx> list = convertToIssueEx(action, map);
		BigDecimal etcSum = new BigDecimal(0);
		for(IssueEx issue : list){
			etcSum = etcSum.add(issue.getEtc());
		}
		return etcSum;
	}
	public BigDecimal getEacSum(ProjectActionSupport action, Map map){
		List<IssueEx> list = convertToIssueEx(action, map);
		BigDecimal eacSum = new BigDecimal(0);
		for(IssueEx issue : list){
			eacSum = eacSum.add(issue.getEac());
		}
		return eacSum;
	}
	public BigDecimal getDviSum(ProjectActionSupport action, Map map){
		List<IssueEx> list = convertToIssueEx(action, map);
		BigDecimal dviSum = new BigDecimal(0);
		for(IssueEx issue : list){
			dviSum = dviSum.add(issue.getDvi());
		}
		return dviSum;
	}
	/**
	 * End of sum-calculation block
	 */



	/**
	 * 
	 * @param action
	 * @param map
	 * @return List<IssueEx> of Features converted into custom class "IssueEx"
	 */
	public List<IssueEx> convertToIssueEx(ProjectActionSupport action, Map map){

		List<Issue> list = getFeatures(action, map);
		List<IssueEx> issues = new ArrayList();
		for (Issue issue : list){
			IssueEx ex = new IssueEx(issue, map);
			issues.add(ex);
		}
		return issues;
	}

	/**
	 * 
	 * @param action
	 * @param map
	 * @return Map<String, Object> to use for html
	 */
	public Map<String, Object> putInMap(ProjectActionSupport action, Map map){
		Map<String,Object> finalMap = new HashMap();
		finalMap.put("cbbSum", getCbbSum(action, map));
		finalMap.put("ptdSum", getPtdSum(action, map));
		finalMap.put("etcSum", getEtcSum(action, map));
		finalMap.put("eacSum", getEacSum(action, map));
		finalMap.put("dviSum", getDviSum(action, map));
		finalMap.put("Issues", convertToIssueEx(action, map));
		finalMap.put("ER", getParams(action, map));
		finalMap.put("Features", getFeatures(action, map));
		return finalMap;
	}
	/**
	 * Method to prevent error prompts
	 */
	@Override
	public void validate(ProjectActionSupport action, Map map){

		super.validate(action, map);
		if(manager.getIssueObject(ParameterUtils.getStringParam(map, "erID")) == null)
			action.addError("notCorrect", "n");
		
	}
}


