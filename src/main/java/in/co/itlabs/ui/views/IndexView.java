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

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.Resource.Status;
import in.co.itlabs.business.entities.Resource.Type;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.GuestResourceFilterForm;
import in.co.itlabs.ui.components.ResourceEditorForm;
import in.co.itlabs.ui.layouts.GuestLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.GuestResourceDataProvider;
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
	private GuestResourceDataProvider dataProvider;

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

		City city = resourceService.getCityById(1);
		Type type = Type.Oxygen;

		filterParams.setCity(city);
		filterParams.setType(type);
		filterParams.setStatus(Status.Verified);

		filterForm = new GuestResourceFilterForm();
//		filterForm.setWidth("87%");
		filterForm.addClassName("card");
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(GuestResourceFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
//		recordCount.setWidth("150px");

		dataProvider = new GuestResourceDataProvider(resourceService);
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

			root.setMargin(false);
			root.setPadding(true);
			root.setSpacing(false);

			root.setAlignItems(Alignment.CENTER);
			root.setWidthFull();
			root.addClassName("card");

			Button verifiedButton = new Button();

			if (resource.getStatus() == Status.Verified) {
				verifiedButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
				verifiedButton.setText("Verified");
			} else if (resource.getStatus() == Status.Pending) {
				verifiedButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
				verifiedButton.setText("Verification awaited");
			}

//			Button updatedAtButton = new Button();
//			updatedAtButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
//			updatedAtButton.setText("Updated " + DateUtil.humanize(resource.getUpdatedAt()));
			Div updatedDiv = new Div();
			updatedDiv.getStyle().set("fontSize", "small");
			updatedDiv.getStyle().set("color", "gray");
			updatedDiv.setText("Updated " + DateUtil.humanize(resource.getUpdatedAt()));

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

			TextArea remarkField = new TextArea("Remark");
			remarkField.setValue(resource.getRemark());
			remarkField.setWidthFull();
			remarkField.setReadOnly(true);

			root.add(verifiedButton, updatedDiv, nameField, phonesField, addressField, remarkField);
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
