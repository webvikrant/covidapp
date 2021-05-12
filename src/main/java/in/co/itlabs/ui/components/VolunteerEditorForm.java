package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.Volunteer;
import in.co.itlabs.business.entities.Volunteer.Service;

public class VolunteerEditorForm extends VerticalLayout {

	// ui
	private TextField nameField;
	private IntegerField ageField;
	private TextField phoneField;
	private EmailField emailIdField;
	private IntegerField hoursField;
	private RadioButtonGroup<Service> serviceRadio;
	private TextField otherServiceField;

	private Button saveButton;
	private Button cancelButton;

	private Binder<Volunteer> binder;

	// non-ui

	public VolunteerEditorForm() {

		setAlignItems(Alignment.START);

		nameField = new TextField("Name");
		configureNameField();

		ageField = new IntegerField("Age");
		configureAgeField();

		phoneField = new TextField("Phone");
		phoneField.setWidthFull();

		emailIdField = new EmailField("Email");
		emailIdField.setWidthFull();

		hoursField = new IntegerField("How many hours can you devote per day?");
		hoursField.setWidthFull();

		serviceRadio = new RadioButtonGroup<Volunteer.Service>();
		configureServiceRadio();

		otherServiceField = new TextField("Other service");
		configureOtherServiceField();

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CHECK.create());

		binder = new Binder<>(Volunteer.class);

		binder.forField(nameField).asRequired("Name can not be blank").bind("name");

		binder.forField(ageField).asRequired("Age can not be blank")
				.withValidator(age -> (age >= 18 && age <= 60), "Min age is 18 and max age is 60").bind("age");

		binder.forField(phoneField).asRequired("Mobile can not be blank")
				.withValidator(phone -> phone.length() == 10, "Mobile number must have 10 digits")
				.withValidator(new RegexpValidator("Only 0-9 allowed", "^\\d{10}$")).bind("phone");

		binder.forField(emailIdField).asRequired("Email can not be blank")
				.withValidator(new RegexpValidator("Only valid email allowed", "^$|^[A-Za-z0-9+_.-]+@(.+)$"))
				.bind("emailId");

		binder.forField(hoursField).asRequired("Hours can not be blank")
				.withValidator(hours -> (hours >= 1 && hours <= 8), "Min hours is 1 and max hours are 8").bind("hours");

		binder.forField(serviceRadio).asRequired("Service can not be blank").bind("service");
		binder.forField(otherServiceField).bind("otherService");

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		HorizontalLayout agePhoneBar = new HorizontalLayout();
		agePhoneBar.setWidthFull();
		agePhoneBar.add(ageField, phoneField);

		add(nameField, agePhoneBar, emailIdField, hoursField, serviceRadio, otherServiceField, buttonBar);
	}

	private void configureOtherServiceField() {
		otherServiceField.setWidthFull();
	}

	private void configureAgeField() {
		ageField.setWidth("80px");
	}

	private void configureServiceRadio() {
		serviceRadio.setLabel("Service");
		serviceRadio.setItems(Service.values());
		serviceRadio.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		serviceRadio.addValueChangeListener(e -> {
			if (e.getValue() == Service.Other) {
				otherServiceField.setEnabled(true);
			} else {
				otherServiceField.setEnabled(false);
			}
		});
	}

	private void configureNameField() {
		nameField.setWidthFull();
		nameField.setLabel("Name");
		nameField.setPlaceholder("Type name");
	}

	public void setVolunteer(Volunteer volunteer) {
		binder.setBean(volunteer);
	}

	private void buildButtonBar(HorizontalLayout root) {

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
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

	public void setReadOnly() {

		nameField.setReadOnly(true);
		ageField.setReadOnly(true);
		phoneField.setReadOnly(true);
		emailIdField.setReadOnly(true);
		hoursField.setReadOnly(true);
		serviceRadio.setReadOnly(true);
		otherServiceField.setReadOnly(true);

		saveButton.setVisible(false);
	}

	public static abstract class VolunteerEditorFormEvent extends ComponentEvent<VolunteerEditorForm> {
		private Volunteer volunteer;

		protected VolunteerEditorFormEvent(VolunteerEditorForm source, Volunteer volunteer) {

			super(source, false);
			this.volunteer = volunteer;
		}

		public Volunteer getVolunteer() {
			return volunteer;
		}
	}

	public static class SaveEvent extends VolunteerEditorFormEvent {
		SaveEvent(VolunteerEditorForm source, Volunteer volunteer) {
			super(source, volunteer);
		}
	}

	public static class CancelEvent extends VolunteerEditorFormEvent {
		CancelEvent(VolunteerEditorForm source, Volunteer volunteer) {
			super(source, volunteer);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
