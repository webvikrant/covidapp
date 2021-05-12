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

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.Resource.Status;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.ResourceEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Submit Lead Form - Ghaziabad Covid Support")
@Route(value = "resource-form", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class ResourceFormView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private ResourceEditorForm editorForm;

	// non-ui
	private ResourceService resourceService;
	private Resource resource;

	public ResourceFormView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		resource = new Resource();

		editorForm = new ResourceEditorForm(resourceService);
		editorForm.setResource(resource);

		editorForm.addListener(ResourceEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(ResourceEditorForm.CancelEvent.class, this::handleCancelEvent);
		editorForm.addClassName("card");

		add(titleDiv, editorForm);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Submit Lead");
	}

	public void handleSaveEvent(ResourceEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		resource = event.getResource();

		// new resource, hence create it

		LocalDateTime now = LocalDateTime.now();

		resource.setCreatedBy(0);
		resource.setCreatedAt(now);

		resource.setUpdatedBy(0);
		resource.setUpdatedAt(now);

		resource.setStatus(Status.Pending);

		int resourceId = resourceService.createResource(messages, resource);
		if (resourceId > 0) {
			Notification.show("Lead submitted successfully", 3000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			resource = new Resource();
			editorForm.setResource(resource);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	public void handleCancelEvent(ResourceEditorForm.CancelEvent event) {
		resource = new Resource();
		editorForm.setResource(resource);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
