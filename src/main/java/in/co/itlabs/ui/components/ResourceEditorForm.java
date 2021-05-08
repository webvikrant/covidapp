package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.DateUtil;

public class ResourceEditorForm extends VerticalLayout {

	// ui

	private Div updatedDiv;

	private ComboBox<City> cityCombo;
	private ComboBox<Resource.Type> typeCombo;
	private TextField nameField;
	private TextArea addressField;

	private TextField phone1Field;
	private TextField phone2Field;
	private TextField phone3Field;

	private TextArea remarkField;

//	private Checkbox verifiedCheck;
	private ComboBox<Resource.Status> statusCombo;

	private Button saveButton;
	private Button cancelButton;

	private Binder<Resource> binder;

	// non-ui

	private ResourceService resourceService;

	public ResourceEditorForm(ResourceService resourceService) {

		this.resourceService = resourceService;

		setAlignItems(Alignment.START);

		updatedDiv = new Div();

		cityCombo = new ComboBox<>();
		configureCityCombo();

		typeCombo = new ComboBox<>();
		configureTypeCombo();

		nameField = new TextField();
		configureNameField();

		addressField = new TextArea();
		configureAddressField();

		phone1Field = new TextField("Phone1");
		phone1Field.setWidth("140px");

		phone2Field = new TextField("Phone2");
		phone2Field.setWidth("140px");

		phone3Field = new TextField("Phone3");
		phone3Field.setWidth("140px");

		FlexLayout phoneBar = new FlexLayout();
		configurePhoneBar(phoneBar);
		phoneBar.add(phone1Field, phone2Field, phone3Field);

		remarkField = new TextArea();
		configureRemarkField();

//		verifiedCheck = new Checkbox("Verified");
		statusCombo = new ComboBox<>();
		configureStatusCombo();

		binder = new Binder<>(Resource.class);

		binder.forField(cityCombo).asRequired("City can not be blank").bind("city");
		binder.forField(typeCombo).asRequired("Type can not be blank").bind("type");
		binder.forField(nameField).asRequired("Name can not be blank").bind("name");
		binder.forField(addressField).asRequired("Address can not be blank").bind("address");
		binder.forField(phone1Field).asRequired("Phone1 can not be blank").bind("phone1");
		binder.forField(phone2Field).bind("phone2");
		binder.forField(phone3Field).bind("phone3");
		binder.forField(remarkField).bind("remark");
//		binder.forField(verifiedCheck).bind("verified");
		binder.forField(statusCombo).asRequired("Status can not be blank").bind("status");

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout cityTypeBar = new HorizontalLayout();
		cityTypeBar.setWidthFull();
		cityTypeBar.add(cityCombo, typeCombo);

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(updatedDiv, cityTypeBar, nameField, addressField, phoneBar, remarkField, statusCombo, buttonBar);

	}

	private void configureCityCombo() {
		cityCombo.setLabel("City");
		cityCombo.setPlaceholder("Select a city");
		cityCombo.setWidthFull();
		cityCombo.setItemLabelGenerator(city -> {
			return city.getName();
		});
		cityCombo.setItems(resourceService.getAllCities());
	}

	private void configureTypeCombo() {
		typeCombo.setLabel("Resource-type");
		typeCombo.setPlaceholder("Select a resource-type");
		typeCombo.setWidthFull();
		typeCombo.setItems(Resource.Type.values());
	}

	private void configureStatusCombo() {
		statusCombo.setLabel("Status");
		statusCombo.setPlaceholder("Select a status");
		statusCombo.setWidthFull();
		statusCombo.setItems(Resource.Status.values());
	}

	private void configureNameField() {
		nameField.setWidthFull();
		nameField.setLabel("Provider");
		nameField.setPlaceholder("Type name");
	}

	private void configureAddressField() {
		addressField.setWidthFull();
		addressField.setLabel("Address");
		addressField.setPlaceholder("Type address");
	}

	private void configurePhoneBar(FlexLayout root) {
		root.setFlexWrap(FlexWrap.WRAP);
		root.getStyle().set("gap", "8px");
	}

	private void configureRemarkField() {
		remarkField.setWidthFull();
		remarkField.setLabel("Remark");
		remarkField.setPlaceholder("Type remark");
	}

	public void setResource(Resource resource) {
		City city = resourceService.getCityById(resource.getCityId());
		resource.setCity(city);

		if (resource.getId() > 0) {
			updatedDiv.setText("Last updated " + DateUtil.humanize(resource.getUpdatedAt()));
		}
		binder.setBean(resource);
	}

	private void buildButtonBar(HorizontalLayout root) {

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				binder.getBean().setCityId(binder.getBean().getCity().getId());
				fireEvent(new SaveEvent(this, binder.getBean()));
			}
		});

		cancelButton.addClickListener(e -> {
			fireEvent(new CancelEvent(this, binder.getBean()));
		});

		Span blank = new Span();

		root.add(saveButton, blank, cancelButton);
		root.expand(blank);
	}

	public static abstract class ResourceEditorFormFormEvent extends ComponentEvent<ResourceEditorForm> {
		private Resource resource;

		protected ResourceEditorFormFormEvent(ResourceEditorForm source, Resource resource) {

			super(source, false);
			this.resource = resource;
		}

		public Resource getResource() {
			return resource;
		}
	}

	public static class SaveEvent extends ResourceEditorFormFormEvent {
		SaveEvent(ResourceEditorForm source, Resource resource) {
			super(source, resource);
		}
	}

	public static class CancelEvent extends ResourceEditorFormFormEvent {
		CancelEvent(ResourceEditorForm source, Resource resource) {
			super(source, resource);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
