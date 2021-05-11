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
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.util.DateUtil;

public class ResourceEditorForm extends VerticalLayout {

	// ui

	private Div updatedDiv;

	private ComboBox<Resource.Type> typeCombo;
	private ComboBox<City> cityCombo;

	private TextField nameField;
	private TextArea addressField;

	private TextField phone1Field;
	private TextField phone2Field;
	private TextField phone3Field;

	private TextArea remarkField;

	private TextField guestNameField;
	private TextField guestPhoneField;

	private ComboBox<Resource.Status> statusCombo;

	private Button saveButton;
	private Button cancelButton;

	private Binder<Resource> binder;

	// non-ui

	private ResourceService resourceService;

	public ResourceEditorForm(ResourceService resourceService) {

		this.resourceService = resourceService;

		AuthenticatedUser authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);

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

		phone1Field = new TextField("Mobile1");
		phone1Field.setWidth("140px");

		phone2Field = new TextField("Mobile2 (Optional)");
		phone2Field.setWidth("140px");

		phone3Field = new TextField("Mobile3 (Optional)");
		phone3Field.setWidth("140px");

		remarkField = new TextArea();
		configureRemarkField();

		guestNameField = new TextField();
		configureGuestNameField();

		guestPhoneField = new TextField();
		configureGuestPhoneField();

		statusCombo = new ComboBox<>();
		configureStatusCombo();

		binder = new Binder<>(Resource.class);

		binder.forField(cityCombo).asRequired("City can not be blank").bind("city");

		binder.forField(typeCombo).asRequired("Type can not be blank").bind("type");

		binder.forField(nameField).asRequired("Name can not be blank").withValidator(name -> name.length() < 250, "")
				.bind("name");

		binder.forField(addressField).asRequired("Address can not be blank")
				.withValidator(address -> address.length() < 1000, "").bind("address");

		binder.forField(phone1Field).asRequired("Mobile1 can not be blank")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^\\d{10}$"))
				.withValidator(phone -> phone.length() == 10, "Mobile number must have 10 digits").bind("phone1");

		binder.forField(phone2Field)
				.withValidator(phone -> phone.length() == 10 || phone.length() == 0,
						"Mobile number must have 10 digits or none")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^$|^\\d{10}$")).bind("phone2");

		binder.forField(phone3Field)
				.withValidator(phone -> phone.length() == 10 || phone.length() == 0,
						"Mobile number must have 10 digits or none")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^$|^\\d{10}$")).bind("phone3");

		binder.forField(remarkField).bind("remark");

		binder.forField(guestNameField).bind("guestName");

		binder.forField(guestPhoneField)
				.withValidator(phone -> phone.length() == 10 || phone.length() == 0,
						"Mobile number must have 10 digits or none")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^$|^\\d{10}$")).bind("guestPhone");

		if (authUser != null) {
			binder.forField(statusCombo).asRequired("Status can not be blank").bind("status");
		}

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		if (authUser == null) {
			// smartphone ui

			phone1Field.setWidthFull();
			phone2Field.setWidthFull();
			phone3Field.setWidthFull();

			add(typeCombo, cityCombo, nameField, addressField, remarkField, phone1Field, phone2Field, phone3Field,
					guestNameField, guestPhoneField, buttonBar);
		} else {
			// desktop ui
			HorizontalLayout cityTypeBar = new HorizontalLayout();
			cityTypeBar.setWidthFull();
			cityTypeBar.add(cityCombo, typeCombo);

			FlexLayout phoneBar = new FlexLayout();
			configurePhoneBar(phoneBar);
			phoneBar.add(phone1Field, phone2Field, phone3Field);

			add(updatedDiv, cityTypeBar, nameField, addressField, remarkField, phoneBar, statusCombo, guestNameField,
					guestPhoneField, buttonBar);
			guestNameField.setReadOnly(true);
			guestPhoneField.setReadOnly(true);
			guestNameField.setLabel("Guest submitter's name");
			guestPhoneField.setLabel("Guest submitter's phone");
		}
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
		nameField.setLabel("Resource provider organization or person's name");
		nameField.setPlaceholder("Type name");
	}

	private void configureAddressField() {
		addressField.setWidthFull();
		addressField.setHeight("100px");
		addressField.setLabel("Address");
		addressField.setPlaceholder("Type address");
	}

	private void configurePhoneBar(FlexLayout root) {
		root.setFlexWrap(FlexWrap.WRAP);
		root.getStyle().set("gap", "8px");
	}

	private void configureRemarkField() {
		remarkField.setWidthFull();
		remarkField.setHeight("100px");
		remarkField.setLabel("Remark (Optional)");
		remarkField.setPlaceholder("Type remark");
	}

	private void configureGuestNameField() {
		guestNameField.setWidthFull();
		guestNameField.setLabel("Your name (optional)");
		guestNameField.setPlaceholder("Type name");
	}

	private void configureGuestPhoneField() {
		guestPhoneField.setWidthFull();
		guestPhoneField.setLabel("Your phone (optional)");
		guestPhoneField.setPlaceholder("Type mobile");
	}

	public void setResource(Resource resource) {
		City city = resourceService.getCityById(resource.getCityId());
		resource.setCity(city);

		if (resource.getId() > 0) {
			updatedDiv.setVisible(true);
			updatedDiv.setText("Last updated " + DateUtil.humanize(resource.getUpdatedAt()));
		} else {
			updatedDiv.setVisible(false);
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
