package id.co.itasoft.fajaralfa.budgetreq.validator;

import id.co.itasoft.fajaralfa.budgetreq.validator.dao.BudgetDao;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetNotExceedGridValidator extends FormMultiRowValidator {
    @Override
    public boolean validate(Element element, FormData formData, FormRowSet formRowSet) {
        boolean result = true;
        BudgetDao budgetDao = new BudgetDao();

        int totalUsedBudget = 0;
        for (FormRow row : formRowSet) {
            int price = Integer.parseInt(row.getProperty("price"));
            int quantity = Integer.parseInt(row.getProperty("quantity"));
            int subtotal = price * quantity;
            totalUsedBudget += subtotal;
        }

        String id = FormUtil.getElementParameterName(element);

        Integer storedBudget = budgetDao.getStoredBudget((String) getProperty("requester-username"));
        if (storedBudget == null) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-message-value-not-exist"));
        } else if (totalUsedBudget > storedBudget) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-message-value-exceeded"));
        }

        return result;
    }

    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Grid Validator";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Grid Validator";
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Grid Validator";
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
