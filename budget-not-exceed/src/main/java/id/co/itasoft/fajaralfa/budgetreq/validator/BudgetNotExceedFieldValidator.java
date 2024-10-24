package id.co.itasoft.fajaralfa.budgetreq.validator;

import id.co.itasoft.fajaralfa.budgetreq.validator.dao.BudgetDao;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.FormUtil;

public class BudgetNotExceedFieldValidator extends FormValidator {
    @Override
    public boolean validate(Element element, FormData formData, String[] strings) {
        boolean result = true;
        BudgetDao budgetDao = new BudgetDao();
        String id = FormUtil.getElementParameterName(element);

        int usedBudget = 0;
        if (strings.length > 0) {
            usedBudget = Integer.parseInt(strings[0]);
        }

        Integer storedBudget = budgetDao.getStoredBudget((String) getProperty("requester-username"));

        if (storedBudget == null) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-value-not-exist"));
        } else if (usedBudget > storedBudget) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-value-exceeded"));
        }

        return result;
    }

    @Override
    public String getName() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Field Validator";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Field Validator";
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - Budget Not Exceed Field Validator";
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
