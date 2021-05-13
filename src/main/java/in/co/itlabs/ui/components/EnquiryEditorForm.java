package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.Enquiry;

public class EnquiryEditorForm extends VerticalLayout {

	// ui

	private TextField nameField;;
	private TextField phoneField;
	private EmailField emailIdField;
	private TextArea messageField;
	private Checkbox actionTakenCheck;

	private Button saveButton;
	private Button cancelButton;

	private Binder<Enquiry> binder;

	// non-ui

	public EnquiryEditorForm() {

		setAlignItems(Alignment.START);

		nameField = new TextField("Your name");
		configureNameField();

		phoneField = new TextField("Your phone");
		configurePhoneField();

		emailIdField = new EmailField("Your email");
		configureEmailField();

		messageField = new TextArea("Your message");
		configureMessageField();

		actionTakenCheck = new Checkbox("Action taken");

		binder = new Binder<>(Enquiry.class);

		binder.forField(nameField).asRequired("Name can not be blank").bind("name");

		binder.forField(phoneField).asRequired("Phone can not be blank")
				.withValidator(phone -> phone.length() == 10, "Phone number must have 10 digits")
				.withValidator(new RegexpValidator("Only 1-9 allowed", "^\\d{10}$")).bind("phone");

		binder.forField(emailIdField)
				.withValidator(new RegexpValidator("Only valid email allowed", "^$|^[A-Za-z0-9+_.-]+@(.+)$"))
				.bind("emailId");

		binder.forField(messageField).asRequired("Message can not be blank").bind("message");
		binder.forField(actionTakenCheck).bind("actionTaken");

		binder.setBean(new Enquiry());

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(nameField, phoneField, emailIdField, messageField, actionTakenCheck, buttonBar);
	}

	private void configureNameField() {
		nameField.setWidthFull();
	}

	private void configurePhoneField() {
		phoneField.setWidthFull();
	}

	private void configureEmailField() {
		emailIdField.setWidthFull();
	}

	private void configureMessageField() {
		messageField.setWidthFull();
		messageField.setHeight("100px");
	}

	public void setEnquiry(Enquiry enquiry) {
		binder.setBean(enquiry);
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
		phoneField.setReadOnly(true);
		emailIdField.setReadOnly(true);
		messageField.setReadOnly(true);
	}

	public static abstract class EnquiryEditorFormEvent extends ComponentEvent<EnquiryEditorForm> {
		private Enquiry enquiry;

		protected EnquiryEditorFormEvent(EnquiryEditorForm source, Enquiry enquiry) {

			super(source, false);
			this.enquiry = enquiry;
		}

		public Enquiry getEnquiry() {
			return enquiry;
		}
	}

	public static class SaveEvent extends EnquiryEditorFormEvent {
		SaveEvent(EnquiryEditorForm source, Enquiry enquiry) {
			super(source, enquiry);
		}
	}

	public static class CancelEvent extends EnquiryEditorFormEvent {
		CancelEvent(EnquiryEditorForm source, Enquiry enquiry) {
			super(source, enquiry);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
