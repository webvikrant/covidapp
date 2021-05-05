package in.co.itlabs.ui.layouts;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.ui.components.AppFooter;
import in.co.itlabs.ui.components.AppHeader;
import in.co.itlabs.ui.components.AppNavBar;
import in.co.itlabs.ui.views.LoginView;

@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@Theme(Lumo.class)
@CssImport("./styles/shared-styles.css")
@Push(PushMode.MANUAL)
public class AppLayout extends VerticalLayout implements RouterLayout, BeforeEnterObserver {

	// ui

	private AppHeader header;
	private AppNavBar navBar;
	private VerticalLayout content;
	private AppFooter footer;

	// non-ui
	private static final Logger logger = LoggerFactory.getLogger(AppLayout.class);

	public AppLayout() {

		logger.info("AppLayout() invoked...");

		addClassName("app-layout");
		
		header = new AppHeader();
		header.setWidthFull();

		navBar = new AppNavBar();
		navBar.setWidthFull();

		content = new VerticalLayout();
		content.setPadding(false);
		content.setWidthFull();
		content.addClassName("card");

		footer = new AppFooter();
		footer.setWidthFull();

		VerticalLayout root = new VerticalLayout();

		root.getStyle().set("margin", "auto");
		root.setPadding(false);
		root.setWidth("1000px");

		root.add(header, navBar, content, footer);

		add(root);
	}

	@Override
	public void removeRouterLayoutContent(HasElement oldContent) {
		content.getElement().removeAllChildren();
	}

	@Override
	public void showRouterLayoutContent(HasElement newContent) {
		if (newContent != null) {
			content.getElement().appendChild(Objects.requireNonNull(newContent.getElement()));
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		AuthenticatedUser authUser = event.getUI().getSession().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			event.forwardTo(LoginView.class);
		}
	}
}
