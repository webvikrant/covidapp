package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.User;

public class PasswordEditorForm extends VerticalLayout {

	// ui
	private Div nameDiv;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;

	private Button saveButton;
	private Button cancelButton;

	private Binder<User> userBinder;

	// non-ui

	public PasswordEditorForm() {

		setAlignItems(Alignment.CENTER);

		nameDiv = new Div();

		passwordField = new PasswordField();
		configurePasswordField();

		confirmPasswordField = new PasswordField();
		configureConfirmPasswordField();

		userBinder = new Binder<>(User.class);

		userBinder.forField(passwordField).asRequired("Password can not be blank").bind("password");
		userBinder.forField(confirmPasswordField).asRequired("Confirm password can not be blank")
				.withValidator(value -> value.equals(passwordField.getValue()), "Passwords do not match")
				.bind("confirmPassword");

		saveButton = new Button("OK", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buildButtonBar(buttonBar);

		buttonBar.setWidthFull();

		add(nameDiv, passwordField, confirmPasswordField, buttonBar);

	}

	private void configurePasswordField() {
		passwordField.setWidthFull();
		passwordField.setLabel("New password");
	}

	private void configureConfirmPasswordField() {
		confirmPasswordField.setWidthFull();
		confirmPasswordField.setLabel("Confrm password");
	}

	public void setUser(User user) {
		nameDiv.setText(user.getName());
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

	public static abstract class PasswordEditorFormEvent extends ComponentEvent<PasswordEditorForm> {
		private User user;

		protected PasswordEditorFormEvent(PasswordEditorForm source, User user) {

			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class SaveEvent extends PasswordEditorFormEvent {
		SaveEvent(PasswordEditorForm source, User user) {
			super(source, user);
		}
	}

	public static class CancelEvent extends PasswordEditorFormEvent {
		CancelEvent(PasswordEditorForm source, User user) {
			super(source, user);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
