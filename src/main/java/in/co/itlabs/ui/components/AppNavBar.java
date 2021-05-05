package in.co.itlabs.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.entities.User;
import in.co.itlabs.business.services.AuthService;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.ui.components.PasswordEditorForm.SaveEvent;
import in.co.itlabs.ui.components.PasswordEditorForm.CancelEvent;
import in.co.itlabs.ui.views.ResourcesView;
import in.co.itlabs.ui.views.UsersView;

public class AppNavBar extends HorizontalLayout {

	// ui
	private HorizontalLayout menuBar;
	private Button passwordButton;
	private Button userButton;
	private Button logoutButton;

	private PasswordEditorForm passwordEditorForm;
	private Dialog dialog;

	// non-ui
	private AuthService authService;
	private AuthenticatedUser authUser;
	private final List<String> messages = new ArrayList<>();

	public AppNavBar() {

		authService = new AuthService();
		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			return;
		}

		setAlignItems(Alignment.CENTER);

		addClassName("navbar");

		menuBar = new HorizontalLayout();
		configureMenuBar();

		User user = new User();
		user.setName(authUser.getName());
		
		passwordEditorForm = new PasswordEditorForm();
		passwordEditorForm.setUser(user);
		passwordEditorForm.addListener(SaveEvent.class, this::handleSaveEvent);
		passwordEditorForm.addListener(CancelEvent.class, this::handleCancelEvent);
		
		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("300px");
		dialog.add(passwordEditorForm);

		passwordButton = new Button("Change password", VaadinIcon.PASSWORD.create());
		userButton = new Button("", VaadinIcon.USER.create());
		logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());

		configureButtons();

		Span blank = new Span();

		add(menuBar, blank, passwordButton, userButton, logoutButton);
		expand(blank);
	}

	private void configureButtons() {

		userButton.setText(authUser.getName());

		passwordButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		passwordButton.addClickListener(e -> {
			dialog.open();
		});

		logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		logoutButton.addClickListener(e -> {
			VaadinSession.getCurrent().getSession().invalidate();
//			UI.getCurrent().navigate(LoginView.class);
		});
	}

	private void configureMenuBar() {

		switch (authUser.getRole()) {
		case Admin:
			Button usersButton = new Button("Users", VaadinIcon.USERS.create());
			usersButton.addClickListener(e -> {
				UI.getCurrent().navigate(UsersView.class);
			});

			Button citiesButton = new Button("Cities", VaadinIcon.MAP_MARKER.create());
			citiesButton.addClickListener(e -> {
//				UI.getCurrent().navigate(UsersView.class);
			});

			menuBar.add(usersButton, citiesButton);

			break;

		case Verifier:
			Button resourcesButton = new Button("Resources", VaadinIcon.AMBULANCE.create());
			resourcesButton.addClickListener(e -> {
				UI.getCurrent().navigate(ResourcesView.class);
			});

			Button plasmaSeekersButton = new Button("Plasma-seekers", VaadinIcon.AMBULANCE.create());
			plasmaSeekersButton.addClickListener(e -> {
//				UI.getCurrent().navigate(ResourcesView.class);
			});

			menuBar.add(resourcesButton, plasmaSeekersButton);

			break;

		default:
			break;
		}

	}

	private void handleSaveEvent(PasswordEditorForm.SaveEvent event) {
		User user = event.getUser();
		messages.clear();
		boolean success = authService.updateUserPassword(messages, authUser, user.getPassword());
		if (success) {
			Notification.show("Password updated successfully", 3000, Position.TOP_CENTER);
			dialog.close();
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	private void handleCancelEvent(PasswordEditorForm.CancelEvent event) {
		dialog.close();
	}
}
