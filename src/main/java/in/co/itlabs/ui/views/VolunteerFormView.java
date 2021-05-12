package in.co.itlabs.ui.views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.Volunteer;
import in.co.itlabs.business.entities.Volunteer.Service;
import in.co.itlabs.business.services.VolunteerService;
import in.co.itlabs.ui.components.VolunteerEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Volunteer Form - Ghaziabad Covid Support")
@Route(value = "volunteer-form", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class VolunteerFormView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private VolunteerEditorForm editorForm;

	// non-ui
	private VolunteerService volunteerService;
	private Volunteer volunteer;

	public VolunteerFormView() {

		volunteerService = new VolunteerService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		volunteer = new Volunteer();
		volunteer.setService(Service.TeleCalling_DataVerification);

		editorForm = new VolunteerEditorForm();
		editorForm.addClassName("card");

		editorForm.addListener(VolunteerEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(VolunteerEditorForm.CancelEvent.class, this::handleCancelEvent);

		editorForm.setVolunteer(volunteer);

		add(titleDiv, editorForm);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Volunteer Form");
	}

	public void handleSaveEvent(VolunteerEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		volunteer = event.getVolunteer();

		// new resource, hence create it
		LocalDateTime now = LocalDateTime.now();

		volunteer.setCreatedAt(now);

		int volunteerId = volunteerService.createVolunteer(messages, volunteer);
		if (volunteerId > 0) {
			Notification.show("Volunteer record saved successfully", 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			volunteer = new Volunteer();
			editorForm.setVolunteer(volunteer);
		} else {
			Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	public void handleCancelEvent(VolunteerEditorForm.CancelEvent event) {
		volunteer = new Volunteer();
		editorForm.setVolunteer(volunteer);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
