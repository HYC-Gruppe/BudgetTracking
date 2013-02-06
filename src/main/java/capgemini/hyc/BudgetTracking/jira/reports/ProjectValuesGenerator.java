package capgemini.hyc.BudgetTracking.jira.reports;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.configurable.ValuesGenerator;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.link.IssueLinkType;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
/**
 * @author Thomas Renner
 *
 */
public class ProjectValuesGenerator implements ValuesGenerator {
	@Override
	public Map getValues(Map userParams) {
		/**
		 * IssueLinkTypes
		 */

		// Gibt eine Collection mit allen bekannten IssueLinkTypes zurück.
		Collection<IssueLinkType> allIssueLinkTypes = ComponentAccessor
				.getComponentOfType(IssueLinkTypeManager.class)
				.getIssueLinkTypes(false);
		 
		Map<Long, String> issueLinkTypeMap = new HashMap<Long, String>();
		for (IssueLinkType issueLinkType : allIssueLinkTypes) {
			issueLinkTypeMap
					.put(issueLinkType.getId(), issueLinkType.getName());
		}

		return issueLinkTypeMap;

		//
		// /**
		// * Issue Types
		// */
		//
		// // Gibt eine Collection mit allen bekannten Issue Typen zurück
		// Collection<IssueType> allIssueTypes = ComponentAccessor
		// .getConstantsManager().getAllIssueTypeObjects();
		//
		// Map<String, String> issueTypesMap = new HashMap<String, String>();
		// for (IssueType issueType : allIssueTypes) {
		// issueTypesMap.put(issueType.getId(), issueType.getName());
		// }
		//
		// /**
		// * CustomFields
		// */
		//
		// // Gibt eine Collection mit allen bekannten CustomFields zurück
		// Collection<CustomField> allCustomFields = ComponentAccessor
		// .getCustomFieldManager().getCustomFieldObjects();
		//
		// Map<String, String> customFieldsMap = new HashMap<String, String>();
		// for (CustomField customField : allCustomFields) {
		// customFieldsMap.put(customField.getId(), customField.getName());
		//
		// }

	}
}
