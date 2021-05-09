package in.co.itlabs.ui.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.PlasmaDonorEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Plasma Donor Form - Ghaziabad Covid Support")
@Route(value = "plasma-donor-form", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class PlasmaDonorFormView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private PlasmaDonorEditorForm editorForm;

	// non-ui
	private ResourceService resourceService;

	public PlasmaDonorFormView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		editorForm = new PlasmaDonorEditorForm();
		editorForm.addClassName("card");

		add(titleDiv, editorForm);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Donor");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
