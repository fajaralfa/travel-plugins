package id.co.itasoft.fajaralfa.budgetreq.webservice.dao;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetDao {
    public String getBudgetByUsername(String username) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        try (Connection c = ds.getConnection()) {
            String sql = "SELECT c_budget_amount FROM app_fd_mst_budget WHERE c_username = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("c_budget_amount");
            }
        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }

        return null;
    }
}
