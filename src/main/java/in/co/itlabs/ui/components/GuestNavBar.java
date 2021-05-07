package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import in.co.itlabs.ui.views.AboutUsView;
import in.co.itlabs.ui.views.ContactUsView;
import in.co.itlabs.ui.views.IndexView;

public class GuestNavBar extends HorizontalLayout {

	// ui
	private Button homeButton;
	private Button aboutButton;
	private Button contactButton;

	// non-ui

	public GuestNavBar() {

		addClassName("navbar");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		homeButton = new Button(VaadinIcon.HOME.create());
		aboutButton = new Button("About us", VaadinIcon.INFO_CIRCLE.create());
		contactButton = new Button("Conatact us", VaadinIcon.ENVELOPE.create());
		
		configureButtons();

		add(homeButton, aboutButton, contactButton);
	}

	private void configureButtons() {

		homeButton.addClickListener(e -> {
			UI.getCurrent().navigate(IndexView.class);
		});

		aboutButton.addClickListener(e -> {
			UI.getCurrent().navigate(AboutUsView.class);
		});

		contactButton.addClickListener(e -> {
			UI.getCurrent().navigate(ContactUsView.class);
		});
	}
}
