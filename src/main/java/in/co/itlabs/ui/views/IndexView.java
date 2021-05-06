package in.co.itlabs.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.GuestResourceFilterForm;
import in.co.itlabs.ui.components.ResourceEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;
import in.co.itlabs.util.ResourceDataProvider;
import in.co.itlabs.util.ResourceFilterParams;

@PageTitle(value = "Ghaziabad Covid Support")
@Route(value = "", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class IndexView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private GuestResourceFilterForm filterForm;
	private ListBox<Resource> listBox;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private ResourceService resourceService;

	private ResourceFilterParams filterParams;
	private ResourceDataProvider dataProvider;

	public IndexView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("75%");

		filterParams = new ResourceFilterParams();

		filterForm = new GuestResourceFilterForm();
		filterForm.setWidth("87%");
		filterForm.addClassName("card");
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(GuestResourceFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
//		recordCount.setWidth("150px");

		dataProvider = new ResourceDataProvider(resourceService);
		dataProvider.setFilterParams(filterParams);

		listBox = new ListBox<Resource>();
		configureListBox();

		add(titleDiv, filterForm, recordCount, listBox);

		reload();
	}

	private void configureListBox() {

		listBox.setWidthFull();
		listBox.setRenderer(new ComponentRenderer<VerticalLayout, Resource>(resource -> {
			VerticalLayout root = new VerticalLayout();
			root.setAlignItems(Alignment.CENTER);
			root.setWidthFull();
			root.addClassName("card");

			Button updatedAtButton = new Button("Verified " + resource.getUpdatedAtString());
			updatedAtButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			TextField nameField = new TextField(resource.getType().toString());
			nameField.setValue(resource.getName());
			nameField.setWidthFull();
			nameField.setReadOnly(true);

			TextField phonesField = new TextField("Phones");
			phonesField.setValue(resource.getPhones());
			phonesField.setWidthFull();
			phonesField.setReadOnly(true);

			TextArea addressField = new TextArea("Address");
			addressField.setValue(resource.getAddress());
			addressField.setWidthFull();
			addressField.setReadOnly(true);

			root.add(updatedAtButton, nameField, phonesField, addressField);
			return root;

		}));

		listBox.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Home");
	}

	public void handleFilterEvent(GuestResourceFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(ResourceEditorForm.SaveEvent event) {
//		List<String> messages = new ArrayList<String>();
//		resource = event.getResource();

//		if (resource.getId() > 0) {
//			// existing resource, hence update it
//
//			resource.setUpdatedBy(authUser.getId());
//			resource.setUpdatedAt(LocalDateTime.now());
//
//			boolean success = resourceService.updateResource(messages, resource);
//			if (success) {
//				Notification.show("Resource updated successfully", 3000, Position.TOP_CENTER);
//				reload();
//				dialog.close();
//			} else {
//				Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
//			}
//		} else {
//			// new resource, hence create it
//
//			LocalDateTime now = LocalDateTime.now();
//
//			resource.setCreatedBy(authUser.getId());
//			resource.setCreatedAt(now);
//
//			resource.setUpdatedBy(authUser.getId());
//			resource.setUpdatedAt(now);
//
//			int resourceId = resourceService.createResource(messages, resource);
//			if (resourceId > 0) {
//				Notification.show("Resource created successfully", 3000, Position.TOP_CENTER);
//				reload();
//				resource = new Resource();
//				editorForm.setResource(resource);
//			} else {
//				Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
//			}
//		}
	}

	public void handleCancelEvent(ResourceEditorForm.CancelEvent event) {
		dialog.close();
	}

	public void reload() {
		dataProvider.refreshAll();
		recordCount.setText("Record(s) found: " + dataProvider.getCount());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
