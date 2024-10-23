package id.co.itasoft.fajaralfa.budgetreq.tool.dao;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.*;

public class UserLookupDao {
    public String isOnAnotherRequest(String username, String primaryKey) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        try (Connection c = ds.getConnection()) {
            String sql = "INSERT INTO app_fd_user_process_flag (username, primary_key) VALUE (?, ?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, primaryKey);
            statement.execute();
            return "false";
        } catch (SQLIntegrityConstraintViolationException e) {
            LogUtil.info(getClass().getName(), e.getMessage());
        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }

        return "true";
    }

    public void deleteUserFlag(String username) {
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        try (Connection c = ds.getConnection()) {
            String sql = "DELETE FROM app_fd_user_process_flag WHERE username = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }
    }

}
