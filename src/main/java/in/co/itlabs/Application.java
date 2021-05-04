package in.co.itlabs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.VaadinServlet;

import in.co.itlabs.business.services.DatabaseService;

@WebListener
@WebServlet(urlPatterns = "/*", asyncSupported = true)
public class Application extends VaadinServlet
		implements ServletContextListener, SessionInitListener, SessionDestroyListener {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static DatabaseService databaseService = null;

	public static DatabaseService getDatabaseService() {
		return databaseService;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			databaseService = new DatabaseService();
			logger.info("Context initialized, Application up.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		databaseService = null;
		logger.info("Context destoyed, Application down.");
	}

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(this);
		getService().addSessionDestroyListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		logger.info("Session initialized, id: " + event.getSession().getSession().getId());
	}

	@Override
	public void sessionDestroy(SessionDestroyEvent event) {
		logger.info("Session destroyed, id: " + event.getSession().getSession().getId());
	}

}
