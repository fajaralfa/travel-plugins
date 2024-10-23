package id.co.itasoft.fajaralfa.budgetreq.tool;

import id.co.itasoft.fajaralfa.budgetreq.tool.dao.BudgetRequestDao;
import id.co.itasoft.fajaralfa.budgetreq.tool.dao.UserLookupDao;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.workflow.model.WorkflowAssignment;

import java.util.Map;

public class UserLookupClearTool extends DefaultApplicationPlugin {
    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - User Lookup Clear Tool";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Used to clear user lookup";
    }

    @Override
    public Object execute(Map map) {
        AppService appService = (AppService) AppUtil.getApplicationContext().getBean("appService");
        WorkflowAssignment workflowAssignment = (WorkflowAssignment) map.get("workflowAssignment");
        UserLookupDao userLookupDao = new UserLookupDao();
        BudgetRequestDao budgetRequestDao = new BudgetRequestDao();

        String processId = workflowAssignment.getProcessId();
        String primaryKey = appService.getOriginProcessId(processId);
        // get username
        String username = budgetRequestDao.getUsernameByPrimaryKey(primaryKey);
        // delete data on flag table based on username
        userLookupDao.deleteUserFlag(username);
        return null;
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - User Lookup Clear Tool";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }
}
