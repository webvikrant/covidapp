package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.User;

public class ResourceEditorForm extends VerticalLayout {

	// ui

	private TextField usernameField;
	private PasswordField passwordField;
	private TextField nameField;
	private EmailField emailIdField;

	private Button saveButton;
	private Button cancelButton;

	private Binder<User> userBinder;

	// non-ui

	public ResourceEditorForm() {

		setAlignItems(Alignment.CENTER);

		nameField = new TextField();
		configureNameField();

		emailIdField = new EmailField();
		configureEmailIdField();

		usernameField = new TextField();
		configureUsernameField();

		passwordField = new PasswordField();
		configurePasswordField();

		userBinder = new Binder<>(User.class);

		userBinder.forField(nameField).asRequired("Name can not be blank").bind("name");
		userBinder.forField(emailIdField).asRequired("Email id can not be blank").bind("emailId");
		userBinder.forField(usernameField).asRequired("Username can not be blank").bind("username");
		userBinder.forField(passwordField).asRequired("Password can not be blank").bind("password");

		saveButton = new Button("OK", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buildButtonBar(buttonBar);

		buttonBar.setWidthFull();

		add(nameField, emailIdField, usernameField, passwordField, buttonBar);

	}

	private void configureUsernameField() {
		usernameField.setWidthFull();
		usernameField.setLabel("Username");
		usernameField.setPlaceholder("Type username");
	}

	private void configurePasswordField() {
		passwordField.setWidthFull();
		passwordField.setLabel("Password");
	}

	private void configureNameField() {
		nameField.setWidthFull();
		nameField.setLabel("Name");
		nameField.setPlaceholder("Type name");
	}

	private void configureEmailIdField() {
		emailIdField.setWidthFull();
		emailIdField.setLabel("Email Id");
		emailIdField.setPlaceholder("Type email id");
	}

	public void setUser(User user) {
		userBinder.setBean(user);
	}

	private void buildButtonBar(HorizontalLayout root) {

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> {
			if (userBinder.validate().isOk()) {
				fireEvent(new SaveEvent(this, userBinder.getBean()));
			}
		});

		cancelButton.addClickListener(e -> {
			fireEvent(new CancelEvent(this, userBinder.getBean()));
		});

		Span blank = new Span();

		root.add(saveButton, blank, cancelButton);
		root.expand(blank);
	}

	public static abstract class NewUserFormEvent extends ComponentEvent<ResourceEditorForm> {
		private User user;

		protected NewUserFormEvent(ResourceEditorForm source, User user) {

			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class SaveEvent extends NewUserFormEvent {
		SaveEvent(ResourceEditorForm source, User user) {
			super(source, user);
		}
	}

	public static class CancelEvent extends NewUserFormEvent {
		CancelEvent(ResourceEditorForm source, User user) {
			super(source, user);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
