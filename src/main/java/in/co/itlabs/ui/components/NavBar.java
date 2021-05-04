package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.ui.views.ResourcesView;
import in.co.itlabs.ui.views.UsersView;

public class NavBar extends HorizontalLayout {

	// ui
	private HorizontalLayout menuBar;
	private Button userButton;
	private Button logoutButton;

	// non-ui
	private final AuthenticatedUser authUser;

	public NavBar() {

		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			return;
		}

		setAlignItems(Alignment.CENTER);

		addClassName("navbar");

		menuBar = new HorizontalLayout();
		configureMenuBar();

		userButton = new Button("", VaadinIcon.USER.create());
		logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());

		configureButtons();

		Span blank = new Span();

		add(menuBar, blank, userButton, logoutButton);
		expand(blank);
	}

	private void configureButtons() {

		userButton.setText(authUser.getName());

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

			menuBar.add(resourcesButton);

			break;

		default:
			break;
		}

	}

}
