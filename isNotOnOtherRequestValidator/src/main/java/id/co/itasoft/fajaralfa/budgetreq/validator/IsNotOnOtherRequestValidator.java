package id.co.itasoft.fajaralfa.budgetreq.validator;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormValidator;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;
import org.joget.workflow.model.service.WorkflowUserManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IsNotOnOtherRequestValidator extends FormValidator {
    @Override
    public boolean validate(Element element, FormData formData, String[] strings) {
        WorkflowUserManager workflowUserManager = (WorkflowUserManager) AppUtil.getApplicationContext().getBean("workflowUserManager");
        String id = FormUtil.getElementParameterName(element);

        boolean result = true;

        String username = workflowUserManager.getCurrentUsername();
        if (isOnOtherRequest(username)) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-message"));
        }

        return result;
    }

    public boolean isOnOtherRequest(String username) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        boolean result = false;
        try (Connection c = ds.getConnection()) {
            String sql = "SELECT createdBy FROM app_fd_pengajuan_dana WHERE createdBy = ? AND c_on_request IS NOT NULL LIMIT 1";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();
        } catch (SQLException e) {
            LogUtil.error(getClassName(), e, e.getMessage());
        }

        return result;
    }

    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - BudgetReq - Is Not On Other Request Validator";
    }

    @Override
    public String getVersion() {
        return "1.0.1-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Itasoft - FajarAlfa - BudgetReq - Is Not On Other Request Validator";
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - BudgetReq - Is Not On Other Request Validator";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/is-not-on-other-request-validator.json");
    }
}
