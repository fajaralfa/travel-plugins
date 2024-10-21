package id.co.itasoft.fajaralfa.budgetreq.validator;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetNotExceed extends FormMultiRowValidator {
    @Override
    public boolean validate(Element element, FormData formData, FormRowSet formRowSet) {
        boolean result = true;

        int totalUsedBudget = 0;
        for (FormRow row : formRowSet) {
            int price = Integer.parseInt(row.getProperty("price"));
            int quantity = Integer.parseInt(row.getProperty("quantity"));
            int subtotal = price * quantity;
            totalUsedBudget += subtotal;
        }

        String id = FormUtil.getElementParameterName(element);

        Integer storedBudget = getStoredBudget((String) getProperty("requester-username"));
        if (totalUsedBudget > storedBudget) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-message"));
        }

        return result;
    }

    public Integer getStoredBudget(String username) {
        Integer result = null;
        DataSource dataSource = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        Connection c = null;
        try {
            c = dataSource.getConnection();
            String sql = "SELECT c_budget_amount FROM app_fd_mst_budget WHERE c_username = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String budgetAmount = resultSet.getString("c_budget_amount");
                result = Integer.parseInt(budgetAmount);
            }
        } catch (SQLException e) {
            LogUtil.error("SQLException", e, e.getMessage());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                LogUtil.error("SQLException", e, e.getMessage());
            }
        }

        return result;
    }

    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - BudgetNotExceed Validator";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Itasoft - FajarAlfa - BudgetNotExceed Validator";
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - BudgetNotExceed Validator";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/validator.json");
    }
}
