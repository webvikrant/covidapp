package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import in.co.itlabs.ui.views.AboutUsView;
import in.co.itlabs.ui.views.ContactUsView;
import in.co.itlabs.ui.views.IndexView;
import in.co.itlabs.ui.views.PlasmaDonorFormView;
import in.co.itlabs.ui.views.PlasmaSeekerFormView;
import in.co.itlabs.ui.views.ResourceFormView;

public class GuestNavBar extends VerticalLayout {

	// ui
	private Button homeButton;
	private Button aboutButton;
	private Button contactButton;

	private Button leadsButton;
	private Button donorsButton;
	private Button seekersButton;

	// non-ui

	public GuestNavBar() {

		addClassName("navbar");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		homeButton = new Button(VaadinIcon.HOME.create());
		aboutButton = new Button("About us", VaadinIcon.INFO_CIRCLE.create());
		contactButton = new Button("Contact us", VaadinIcon.ENVELOPE.create());

		leadsButton = new Button("Leads", VaadinIcon.HAND.create());
		donorsButton = new Button("Donors", VaadinIcon.DROP.create());
		seekersButton = new Button("Seekers", VaadinIcon.DROP.create());

		configureButtons();

		HorizontalLayout topBar = new HorizontalLayout();
		HorizontalLayout bottomBar = new HorizontalLayout();

		topBar.add(homeButton, aboutButton, contactButton);
		bottomBar.add(leadsButton, donorsButton, seekersButton);

		add(topBar, bottomBar);
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

		leadsButton.addClickListener(e -> {
			UI.getCurrent().navigate(ResourceFormView.class);
		});

		donorsButton.addClickListener(e -> {
			UI.getCurrent().navigate(PlasmaDonorFormView.class);
		});

		seekersButton.addClickListener(e -> {
			UI.getCurrent().navigate(PlasmaSeekerFormView.class);
		});
	}
}
