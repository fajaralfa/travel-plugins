package id.co.itasoft.fajaralfa.budgetreq;

import id.co.itasoft.fajaralfa.budgetreq.dao.BudgetDecreaserDao;
import java.util.Map;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.workflow.model.WorkflowAssignment;

public class BudgetDecreaser extends DefaultApplicationPlugin {
    public BudgetDecreaser() {
    }

    public Object execute(Map map) {
        AppService appService = (AppService)AppUtil.getApplicationContext().getBean("appService");
        BudgetDecreaserDao dao = new BudgetDecreaserDao();
        WorkflowAssignment workflowAssignment = (WorkflowAssignment)map.get("workflowAssignment");
        String processId = workflowAssignment.getProcessId();
        String primaryKey = appService.getOriginProcessId(processId);
        String username = (String)this.getProperty("username");
        String totalUsedBudget = dao.getTotalUsedBudget(primaryKey);
        dao.decreaseBudget(username, totalUsedBudget);
        return null;
    }

    public String getName() {
        return "FajarAlfa - BudgetReq - Budget Decreaser";
    }

    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    public String getDescription() {
        return "FajarAlfa - BudgetReq - Budget Decreaser";
    }

    public String getLabel() {
        return "FajarAlfa - BudgetReq - Budget Decreaser";
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getPropertyOptions() {
        return AppUtil.readPluginResource(this.getClassName(), "/properties/budgetDecreaser.json", (Object[])null, true);
    }
}
