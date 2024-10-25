package id.co.itasoft.fajaralfa.budgetreq.validator;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormValidator;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IsNotOnOtherRequestValidator extends FormValidator {
    @Override
    public boolean validate(Element element, FormData formData, String[] strings) {
        boolean result = true;

        String id = FormUtil.getElementParameterName(element);
        if (isOnOtherRequest(id)) {
            result = false;
            formData.addFormError(id, (String) getProperty("error-message"));
        }

        return result;
    }

    public boolean isOnOtherRequest(String id) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        int count = 0;

        try (Connection c = ds.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM app_fd_pengajuan_dana WHERE c_on_request IS NOT NULL AND createdBy = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LogUtil.error(getClassName(), e, e.getMessage());
        }

        return count >= 1;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }
}
