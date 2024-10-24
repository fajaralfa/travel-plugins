package id.co.itasoft.fajaralfa.budgetreq.validator.dao;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetDao {
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

}
