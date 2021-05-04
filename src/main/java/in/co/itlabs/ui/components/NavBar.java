package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.ui.views.ResourcesView;
import in.co.itlabs.ui.views.UsersView;

public class NavBar extends HorizontalLayout {

	// ui
	private MenuBar menuBar;
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

		menuBar = new MenuBar();
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

		menuBar.setOpenOnHover(true);
		menuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);

		MenuItem mainMenuItem = menuBar.addItem(VaadinIcon.MENU.create());
		mainMenuItem.add("Menu");

		SubMenu subMenu = mainMenuItem.getSubMenu();

		switch (authUser.getRole()) {
		case Admin:
			subMenu.addItem("Users", e -> UI.getCurrent().navigate(UsersView.class));
			subMenu.addItem("Cities", e -> UI.getCurrent().navigate(UsersView.class));

			break;

		case Team_Member:
			subMenu.addItem("Resources", e -> UI.getCurrent().navigate(ResourcesView.class));

			break;

		default:
			break;
		}

	}

}
