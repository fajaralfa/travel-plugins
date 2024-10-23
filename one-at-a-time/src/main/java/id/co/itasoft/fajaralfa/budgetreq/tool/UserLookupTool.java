package id.co.itasoft.fajaralfa.budgetreq.tool;

import id.co.itasoft.fajaralfa.budgetreq.tool.dao.BudgetRequestDao;
import id.co.itasoft.fajaralfa.budgetreq.tool.dao.UserLookupDao;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.WorkflowManager;

import java.util.Map;

public class UserLookupTool extends DefaultApplicationPlugin {
    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - User on a Process";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Check wether user is already start the process or not, designed to use with route";
    }

    @Override
    public Object execute(Map map) {
        WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");
        AppService appService = (AppService) AppUtil.getApplicationContext().getBean("appService");
        WorkflowAssignment workflowAssignment = (WorkflowAssignment) map.get("workflowAssignment");
        UserLookupDao userLookupDao = new UserLookupDao();
        BudgetRequestDao budgetRequestDao = new BudgetRequestDao();

        String activityId = workflowAssignment.getActivityId();
        String processId = workflowAssignment.getProcessId();
        String flagVariable = (String) getProperty("flagVariable");

        // get processId
        String primaryKey = appService.getOriginProcessId(processId);
        // get username
        String username = budgetRequestDao.getUsernameByPrimaryKey(primaryKey);
        // check if username not exist in flag table & store it.
        // if not then set flag variable (workflow variable) to true & store username on a table
        // else set flag to false
        String flag = userLookupDao.isOnAnotherRequest(username, primaryKey);
        if (flag.equals("true")) {
            budgetRequestDao.unstore(primaryKey);
        }
        workflowManager.activityVariable(activityId, flagVariable, flag);

        return null;
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - User on a Process";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(this.getClassName(), "/properties/user-lookup-tool.json");
    }
}
