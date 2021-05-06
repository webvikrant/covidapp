package in.co.itlabs.ui.layouts;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import in.co.itlabs.ui.components.GuestFooter;
import in.co.itlabs.ui.components.GuestHeader;
import in.co.itlabs.ui.components.GuestNavBar;

//@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@Theme(Lumo.class)
@CssImport("./styles/shared-styles.css")
@Push(PushMode.MANUAL)
@PWA(name = "Covid Support App", shortName = "CovidApp", enableInstallPrompt = true)
public class GuestLayout extends VerticalLayout implements RouterLayout, BeforeEnterObserver {

	// ui

	private GuestHeader header;
	private GuestNavBar navBar;
	private VerticalLayout content;
	private GuestFooter footer;

	// non-ui
	private static final Logger logger = LoggerFactory.getLogger(GuestLayout.class);

	public GuestLayout() {

		logger.info("GuestLayout() invoked...");
		
		addClassName("guest-layout");
		
		setMargin(false);
		setPadding(true);

		header = new GuestHeader();
		header.setWidthFull();

		navBar = new GuestNavBar();
		navBar.setWidthFull();

		content = new VerticalLayout();
		content.setWidthFull();
		content.setMargin(false);
		content.setPadding(false);

		footer = new GuestFooter();
		footer.setWidthFull();

		VerticalLayout root = new VerticalLayout();

		root.getStyle().set("margin", "auto");
		root.setMaxWidth("350px");

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
	}
}
