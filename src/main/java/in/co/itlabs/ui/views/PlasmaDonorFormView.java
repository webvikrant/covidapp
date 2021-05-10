package in.co.itlabs.ui.views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.PlasmaDonor;
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
	private Div infoDiv;
	private PlasmaDonorEditorForm editorForm;

	// non-ui
	private ResourceService resourceService;
	private PlasmaDonor plasmaDonor;

	public PlasmaDonorFormView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		infoDiv = new Div();
		configureConditionsDiv();

		plasmaDonor = new PlasmaDonor();

		editorForm = new PlasmaDonorEditorForm(resourceService);
		editorForm.addClassName("card");

		editorForm.addListener(PlasmaDonorEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(PlasmaDonorEditorForm.CancelEvent.class, this::handleCancelEvent);

		editorForm.setPlasmaDonor(plasmaDonor);

		add(titleDiv, infoDiv, editorForm);
	}

	private void configureConditionsDiv() {
		infoDiv.getStyle().set("fontSize", "10pt");
		infoDiv.getStyle().set("textAlign", "justify");
		infoDiv.getStyle().set("color", "gray");

		infoDiv.add(new Span("Donor INELIGIBLE for convalescent plasma donation:"));
		infoDiv.add(new Paragraph("1. Weight less than 50 kg"));
		infoDiv.add(new Paragraph("2. Females who have ever been pregnant"));
		infoDiv.add(new Paragraph("3. Diabetic on insulin"));
		infoDiv.add(new Paragraph("4. B.P more than 140 and diastolic less than 60 or more than 90"));
		infoDiv.add(
				new Paragraph("5. Uncontrolled diabetes or hypertension with change in medication in last 28 days"));
		infoDiv.add(new Paragraph("6. Cancer Survivor"));
		infoDiv.add(new Paragraph("7. Chronic kidney/heart/lung or liver disease"));
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Donor");
	}

	public void handleSaveEvent(PlasmaDonorEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		plasmaDonor = event.getPlasmaDonor();

		// new resource, hence create it
		LocalDateTime now = LocalDateTime.now();

		plasmaDonor.setCreatedAt(now);
		plasmaDonor.setUpdatedAt(now);

		int plasmaDonorId = resourceService.createPlasmaDonor(messages, plasmaDonor);
		if (plasmaDonorId > 0) {
			Notification.show("Plasma Donor record saved successfully", 3000, Position.TOP_CENTER);
			plasmaDonor = new PlasmaDonor();
			editorForm.setPlasmaDonor(plasmaDonor);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	public void handleCancelEvent(PlasmaDonorEditorForm.CancelEvent event) {
		plasmaDonor = new PlasmaDonor();
		editorForm.setPlasmaDonor(plasmaDonor);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
