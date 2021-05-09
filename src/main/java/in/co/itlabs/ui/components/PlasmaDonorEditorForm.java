package in.co.itlabs.ui.components;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.util.BloodGroup;
import in.co.itlabs.util.Gender;

public class PlasmaDonorEditorForm extends VerticalLayout {

	// ui
	private ComboBox<City> cityCombo;
	private RadioButtonGroup<BloodGroup> bloodGroupRadio;
	private RadioButtonGroup<Gender> genderRadio;

	private IntegerField ageField;

	private TextField nameField;

	private TextField phoneField;
	private TextField pincodeField;
	private TextArea addressField;

	private DatePicker infectionDatePicker;
	private DatePicker recoveryDatePicker;

	private Checkbox availableCheck;
	private Checkbox verifiedCheck;

	private Button saveButton;
	private Button cancelButton;

	private Binder<PlasmaDonor> binder;

	// non-ui
	private ResourceService resourceService;

	public PlasmaDonorEditorForm(ResourceService resourceService) {

		this.resourceService = resourceService;

		AuthenticatedUser authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);

		setAlignItems(Alignment.START);

		cityCombo = new ComboBox<>();
		configureCityCombo();

		bloodGroupRadio = new RadioButtonGroup<BloodGroup>();
		configureBloodGroupRadio();

		genderRadio = new RadioButtonGroup<Gender>();
		configureGenderRadio();

		nameField = new TextField("Name");
		configureNameField();

		ageField = new IntegerField("Age");
		configureAgeField();

		phoneField = new TextField("Phone");
		phoneField.setWidth("100px");

		pincodeField = new TextField("Pincode");
		pincodeField.setWidth("100px");

		addressField = new TextArea("Address");
		configureAddressField();

		infectionDatePicker = new DatePicker("Infected on");
		configureInfectionDatePicker();

		recoveryDatePicker = new DatePicker("Recovery confirmed on");
		configureRecoveryDatePicker();

		availableCheck = new Checkbox("Available");
		verifiedCheck = new Checkbox("Verified");

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CHECK.create());

		binder = new Binder<>(PlasmaDonor.class);

		binder.forField(bloodGroupRadio).asRequired("Blood group can not be blank").bind("bloodGroup");

		binder.forField(genderRadio).asRequired("Gender can not be blank").bind("gender");

		binder.forField(nameField).asRequired("Name can not be blank").bind("name");

		binder.forField(ageField).asRequired("Age can not be blank")
				.withValidator(age -> (age >= 18 && age <= 60), "Min age is 18 and max age is 60").bind("age");

		binder.forField(phoneField).asRequired("Phone can not be blank")
				.withValidator(phone -> phone.length() == 10, "Phone number must have 10 digits")
				.withValidator(new RegexpValidator("Only 1-9 allowed", "^\\d{10}$")).bind("phone");

		binder.forField(cityCombo).asRequired("City can not be blank").bind("city");

		binder.forField(pincodeField).asRequired("Pincode can not be blank")
				.withValidator(pincode -> pincode.length() == 6, "Pincode must have 6 digits")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^\\d{6}$")).bind("pincode");

		binder.forField(addressField).asRequired("Address can not be blank").bind("address");

		binder.forField(infectionDatePicker).asRequired("Infection date can not be blank")
				.withValidator(date -> date.isBefore(LocalDate.now()), "Infection date can not be in future")
				.withValidator(date -> ChronoUnit.MONTHS.between(date, LocalDate.now()) <= 6,
						"Infection date can not be more than 6 months old")
				.bind("infectionDate");

		binder.forField(recoveryDatePicker).asRequired("Recovery date can not be blank")
				.withValidator(date -> date.isBefore(LocalDate.now()), "Recovery date can not be in future")
				.withValidator(date -> ChronoUnit.DAYS.between(infectionDatePicker.getValue(), date) >= 15,
						"Recovery date must be 15 days or more than infection date")
				.bind("recoveryDate");

		if (authUser != null) {
			binder.forField(availableCheck).bind("available");
			binder.forField(availableCheck).bind("verified");
		}

		HorizontalLayout namePhoneBar = new HorizontalLayout();
		namePhoneBar.setWidthFull();
		Span blank = new Span();
		namePhoneBar.add(nameField, blank, phoneField);
		namePhoneBar.expand(blank);

		HorizontalLayout datesBar = new HorizontalLayout();
		datesBar.setWidthFull();
		Span blank2 = new Span();
		datesBar.add(infectionDatePicker, blank2, recoveryDatePicker);
		datesBar.expand(blank2);

		HorizontalLayout cityBar = new HorizontalLayout();
		cityBar.setWidthFull();
		Span blank3 = new Span();
		cityBar.add(cityCombo, blank3, pincodeField);
		cityBar.expand(blank3);

		HorizontalLayout checksBar = new HorizontalLayout();
		checksBar.setWidthFull();
		Span blank4 = new Span();
		checksBar.add(verifiedCheck, blank4, availableCheck);
		checksBar.expand(blank4);

		HorizontalLayout genderAgeBar = new HorizontalLayout();
		genderAgeBar.setWidthFull();
		Span blank5 = new Span();
		genderAgeBar.add(genderRadio, blank5, ageField);
		genderAgeBar.expand(blank5);

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		if (authUser != null) {
			add(genderAgeBar, bloodGroupRadio, namePhoneBar, addressField, cityBar, datesBar, checksBar, buttonBar);
		} else {
			add(genderAgeBar, bloodGroupRadio, namePhoneBar, addressField, cityBar, datesBar, buttonBar);
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

	private void configureInfectionDatePicker() {
		infectionDatePicker.setWidth("130px");
		infectionDatePicker.setLocale(new Locale("in"));
	}

	private void configureRecoveryDatePicker() {
		recoveryDatePicker.setWidth("150px");
		recoveryDatePicker.setLocale(new Locale("in"));
	}

	private void configureAgeField() {
		ageField.setWidth("80px");
	}

	private void configureGenderRadio() {
		genderRadio.setLabel("Gender");
		genderRadio.setItems(Gender.values());
	}

	private void configureBloodGroupRadio() {
		bloodGroupRadio.setLabel("Blood group");
		bloodGroupRadio.setItems(BloodGroup.values());
	}

	private void configureNameField() {
		nameField.setWidthFull();
		nameField.setLabel("Name");
		nameField.setPlaceholder("Type name");
	}

	private void configureAddressField() {
		addressField.setWidthFull();
		addressField.setLabel("Address");
		addressField.setPlaceholder("Type address");
		addressField.setHeight("100px");
	}

	public void setPlasmaDonor(PlasmaDonor plasmaDonor) {
		binder.setBean(plasmaDonor);
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

	public static abstract class PlasmaDonorEditorFormFormEvent extends ComponentEvent<PlasmaDonorEditorForm> {
		private PlasmaDonor plasmaDonor;

		protected PlasmaDonorEditorFormFormEvent(PlasmaDonorEditorForm source, PlasmaDonor plasmaDonor) {

			super(source, false);
			this.plasmaDonor = plasmaDonor;
		}

		public PlasmaDonor getPlasmaDonor() {
			return plasmaDonor;
		}
	}

	public static class SaveEvent extends PlasmaDonorEditorFormFormEvent {
		SaveEvent(PlasmaDonorEditorForm source, PlasmaDonor plasmaDonor) {
			super(source, plasmaDonor);
		}
	}

	public static class CancelEvent extends PlasmaDonorEditorFormFormEvent {
		CancelEvent(PlasmaDonorEditorForm source, PlasmaDonor plasmaDonor) {
			super(source, plasmaDonor);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
