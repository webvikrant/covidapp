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

import in.co.itlabs.business.entities.Enquiry;
import in.co.itlabs.business.services.EnquiryService;
import in.co.itlabs.ui.components.EnquiryEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Contact us - Ghaziabad Covid Support")
@Route(value = "enquiry", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class EnquiryFormView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private EnquiryEditorForm editorForm;

	// non-ui
	private EnquiryService enquiryService;
	private Enquiry enquiry;

	public EnquiryFormView() {

		enquiryService = new EnquiryService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		enquiry = new Enquiry();

		editorForm = new EnquiryEditorForm();
		editorForm.addClassName("card");

		editorForm.addListener(EnquiryEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(EnquiryEditorForm.CancelEvent.class, this::handleCancelEvent);

		editorForm.setEnquiry(enquiry);

		add(titleDiv, editorForm);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Contact us");
	}

	public void handleSaveEvent(EnquiryEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		enquiry = event.getEnquiry();

		// new resource, hence create it
		LocalDateTime now = LocalDateTime.now();

		enquiry.setCreatedAt(now);

		int enquiryId = enquiryService.createEnquiry(messages, enquiry);
		if (enquiryId > 0) {
			Notification.show("Message sent successfully", 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			;
			enquiry = new Enquiry();
			editorForm.setEnquiry(enquiry);
		} else {
			Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	public void handleCancelEvent(EnquiryEditorForm.CancelEvent event) {
		enquiry = new Enquiry();
		editorForm.setEnquiry(enquiry);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
