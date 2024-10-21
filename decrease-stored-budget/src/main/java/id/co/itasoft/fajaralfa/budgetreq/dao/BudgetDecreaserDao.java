package id.co.itasoft.fajaralfa.budgetreq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

public class BudgetDecreaserDao {
    public BudgetDecreaserDao() {
    }

    public String getTotalUsedBudget(String parentId) {
        String result = null;
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean("setupDataSource");

        try {
            Connection c = ds.getConnection();

            try {
                String sql = "SELECT SUM(c_price * c_quantity) AS total FROM app_fd_budget_use_items WHERE c_parent_id = ?";
                PreparedStatement stat = c.prepareStatement(sql);
                stat.setString(1, parentId);
                ResultSet resultSet = stat.executeQuery();
                if (resultSet.next()) {
                    result = resultSet.getString("total");
                }
            } catch (Throwable var9) {
                if (c != null) {
                    try {
                        c.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (c != null) {
                c.close();
            }
        } catch (SQLException var10) {
            SQLException e = var10;
            LogUtil.error(this.getClass().getName(), e, e.getMessage());
        }

        return result;
    }

    public void decreaseBudget(String username, String totalUsedBudget) {
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean("setupDataSource");

        try {
            Connection c = ds.getConnection();

            try {
                String sql = "UPDATE app_fd_mst_budget SET c_budget_amount = c_budget_amount - ? WHERE c_username = ?;";
                PreparedStatement stat = c.prepareStatement(sql);
                stat.setString(1, totalUsedBudget);
                stat.setString(2, username);
                stat.executeUpdate();
            } catch (Throwable var8) {
                if (c != null) {
                    try {
                        c.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (c != null) {
                c.close();
            }
        } catch (SQLException var9) {
            SQLException e = var9;
            LogUtil.error(this.getClass().getName(), e, e.getMessage());
        }

    }
}
