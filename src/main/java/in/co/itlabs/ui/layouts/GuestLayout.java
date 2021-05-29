package in.co.itlabs.ui.layouts;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.googleanalytics.tracking.EnableGoogleAnalytics;
import org.vaadin.googleanalytics.tracking.TrackerConfigurator;
import org.vaadin.googleanalytics.tracking.EnableGoogleAnalytics.SendMode;
import org.vaadin.googleanalytics.tracking.TrackerConfiguration;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
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
@EnableGoogleAnalytics(value = "UA-197046056-1", sendMode = SendMode.ALWAYS)
public class GuestLayout extends VerticalLayout implements RouterLayout, BeforeEnterObserver, TrackerConfigurator {

	// ui

	private GuestHeader header;
	private GuestNavBar navBar;
	private VerticalLayout content;
	private GuestFooter footer;
	private Div disclaimerDiv;

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

		disclaimerDiv = new Div();
		disclaimerDiv.setText(
				"This is to notify that Team100-Ghaziabad is providing free service and there is no financial involvement either from service seeker or service provider. It is solely and wholely the responsibility of service seekers and service providers for any mutual transaction for exchange of any service. - Team100 Ghaziabad");
		disclaimerDiv.getStyle().set("fontSize", "10pt");
		disclaimerDiv.getStyle().set("fontWeight", "300");
		disclaimerDiv.getStyle().set("color", "red");
		disclaimerDiv.getStyle().set("textJustify", "auto");

		Details disclaimer = new Details("Disclaimer", disclaimerDiv);
		disclaimer.addThemeVariants(DetailsVariant.SMALL);
		disclaimer.setOpened(true);
		add(disclaimer);

		VerticalLayout root = new VerticalLayout();

		root.getStyle().set("margin", "auto");
		root.setMaxWidth("350px");

		root.add(header, navBar, content, disclaimer, footer);
		root.setAlignSelf(Alignment.START, disclaimer);

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

	@Override
	public void configureTracker(TrackerConfiguration configuration) {
		configuration.setCreateField("allowAnchor", Boolean.FALSE);
		configuration.setInitialValue("transport", "beacon");
	}
}
