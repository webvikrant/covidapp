package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import in.co.itlabs.ui.views.IndexView;
import in.co.itlabs.ui.views.LoginView;
import in.co.itlabs.ui.views.PlasmaDonorFormView;
import in.co.itlabs.ui.views.PlasmaSeekerFormView;

public class GuestNavBar extends VerticalLayout {

	// ui
	private HorizontalLayout menuBar;
	private Button loginButton;
	// non-ui

	public GuestNavBar() {

		addClassName("navbar");
		setAlignItems(Alignment.CENTER);

		menuBar = new HorizontalLayout();
		configureMenuBar();
		add(menuBar);

		loginButton = new Button("Login", VaadinIcon.SIGN_IN.create());
		configureButtons();

		Span blank = new Span();

		add(loginButton, menuBar);
		expand(blank);
	}

	private void configureButtons() {
		loginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		loginButton.addClickListener(e -> {
			UI.getCurrent().navigate(LoginView.class);
		});
	}

	private void configureMenuBar() {

		Button indexButton = new Button(VaadinIcon.HOME.create());
		indexButton.addClickListener(e -> {
			UI.getCurrent().navigate(IndexView.class);
		});

		Button plasmaSeekersButton = new Button("Need Plasma?");
		plasmaSeekersButton.addClickListener(e -> {
			UI.getCurrent().navigate(PlasmaSeekerFormView.class);
		});

		Button plasmaDonorsButton = new Button("Donate Plasma");
		plasmaDonorsButton.addClickListener(e -> {
			UI.getCurrent().navigate(PlasmaDonorFormView.class);
		});

		menuBar.add(indexButton, plasmaSeekersButton, plasmaDonorsButton);

	}

}
