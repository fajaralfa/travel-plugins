package id.co.itasoft.fajaralfa.budgetreq.tool.dao;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetRequestDao {
    public String getUsernameByPrimaryKey(String primaryKey) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        try (Connection c = ds.getConnection()) {
            String sql = "SELECT createdBy FROM app_fd_pengajuan_dana WHERE id = ? LIMIT 1";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, primaryKey);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("createdBy");
            }
        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }

        return null;
    }

    public void unstore(String primaryKey) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        try (Connection c = ds.getConnection()) {
            String sql = "DELETE FROM app_fd_pengajuan_dana WHERE id = ? LIMIT 1";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, primaryKey);
            statement.execute();
        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }
    }
}
