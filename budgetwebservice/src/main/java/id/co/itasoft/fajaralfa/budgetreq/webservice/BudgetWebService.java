package id.co.itasoft.fajaralfa.budgetreq.webservice;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.plugin.base.PluginWebSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import mjson.Json;

public class BudgetWebService extends DefaultApplicationPlugin implements PluginWebSupport {
    public String getName() {
        return "Itasoft - FajarAlfa - Budget Checker Web Service";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Check User Budget on Database";
    }

    @Override
    public Object execute(Map map) {
        return null;
    }

    @Override
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String username = request.getParameter("username");
        HashMap<String, Object> responseMap = new HashMap<>();

        if (username != null) {
            String budget = getBudgetByUsername(username);
            if (budget != null) {
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("budget", budget);
                responseMap.put("data", dataMap);

                String responseString = Json.make(responseMap).toString();
                response.setHeader("content-type", "application/json");
                writer.write(responseString);
            } else {
                response.sendError(
                        response.SC_NOT_FOUND,
                        "This user doesn't have budget reserved by admin"
                );
            }
        } else {
            response.sendError(
                    response.SC_NOT_FOUND,
                    "Username not provided"
            );
        }
    }

    public String getBudgetByUsername(String username) {
        String result = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        try (Connection c = ds.getConnection()) {
            String sql = "SELECT c_budget_amount FROM app_fd_mst_budget WHERE c_username = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("c_budget_amount");
            }
        } catch (SQLException e) {
            LogUtil.error(getClassName(), e, e.getMessage());
        }

        return result;
    }

    @Override
    public String getLabel() {
        return "Itasoft - FajarAlfa - Budget Checker Web Service";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }
}
