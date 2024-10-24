package id.co.itasoft.fajaralfa.budgetreq.webservice;

import id.co.itasoft.fajaralfa.budgetreq.webservice.dao.BudgetDao;
import mjson.Json;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.plugin.base.PluginWebSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BudgetChecker extends DefaultApplicationPlugin implements PluginWebSupport {

    @Override
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
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BudgetDao budgetDao = new BudgetDao();

        String username = request.getParameter("username");
        HashMap<String, Object> responseMap = new HashMap<>();
        int status = 200;

        if (username != null) {
            String budget = budgetDao.getBudgetByUsername(username);
            if (budget != null) {
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("budget", budget);
                responseMap.put("data", dataMap);
            } else {
                status = response.SC_NOT_FOUND;
                responseMap.put("data", null);
                responseMap.put("messages", "This user doesn't have budget reserved by admin");
            }
        } else {
            status = response.SC_BAD_REQUEST;
            responseMap.put("data", null);
            responseMap.put("messages", "Username not provided");
        }

        responseMap.put("status", status);

        String responseString = Json.make(responseMap).toString();

        response.setStatus(status);
        response.getWriter().write(responseString);
    }

    @Override
    public Object execute(Map map) {
        return null;
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
