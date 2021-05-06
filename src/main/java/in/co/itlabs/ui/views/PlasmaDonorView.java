package in.co.itlabs.ui.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Ghaziabad Covid Support")
@Route(value = "plasma-donor", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class PlasmaDonorView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;

	// non-ui
	private ResourceService resourceService;

	public PlasmaDonorView() {

//		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
//		if (authUser == null) {
//			logger.info("User not logged in.");
//			return;
//		}

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		add(titleDiv);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Donor Form");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
