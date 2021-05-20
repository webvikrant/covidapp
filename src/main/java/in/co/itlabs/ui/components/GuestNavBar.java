package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import in.co.itlabs.ui.views.AboutUsView;
import in.co.itlabs.ui.views.EnquiryFormView;
import in.co.itlabs.ui.views.GuestCircularsView;
import in.co.itlabs.ui.views.IndexView;
import in.co.itlabs.ui.views.ResourceFormView;
import in.co.itlabs.ui.views.VolunteerFormView;

public class GuestNavBar extends VerticalLayout implements AfterNavigationObserver {

	// ui
	private Button homeButton;
	private Button aboutButton;
	private Button enquiryButton;

	private Button leadsButton;
//	private Button donorsButton;
//	private Button seekersButton;

	private Button volunteersButton;
	private Button circularsButton;

	// non-ui

	public GuestNavBar() {

		addClassName("navbar");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		homeButton = new Button(VaadinIcon.HOME.create());
		aboutButton = new Button("About us", VaadinIcon.INFO_CIRCLE.create());
		enquiryButton = new Button("Contact us", VaadinIcon.ENVELOPE.create());

		leadsButton = new Button("Submit resource info", VaadinIcon.PAPERPLANE.create());
//		donorsButton = new Button("Donors", VaadinIcon.DROP.create());
//		seekersButton = new Button("Seekers", VaadinIcon.DROP.create());

		volunteersButton = new Button("Volunteer", VaadinIcon.HANDS_UP.create());
		
		circularsButton = new Button("Govt. circulars", VaadinIcon.DIPLOMA.create());
		
		configureButtons();

		HorizontalLayout topBar = new HorizontalLayout();
		HorizontalLayout middleBar = new HorizontalLayout();
		HorizontalLayout bottomBar = new HorizontalLayout();

		topBar.add(homeButton, aboutButton, enquiryButton);
		middleBar.add(leadsButton, volunteersButton);
		bottomBar.add(circularsButton);

		add(topBar, middleBar, bottomBar);
	}

	private void configureButtons() {

		homeButton.addClickListener(e -> {
			UI.getCurrent().navigate(IndexView.class);
		});

		aboutButton.addClickListener(e -> {
			UI.getCurrent().navigate(AboutUsView.class);
		});

		enquiryButton.addClickListener(e -> {
			UI.getCurrent().navigate(EnquiryFormView.class);
		});

		leadsButton.addClickListener(e -> {
			UI.getCurrent().navigate(ResourceFormView.class);
		});

//		donorsButton.addClickListener(e -> {
//			UI.getCurrent().navigate(PlasmaDonorFormView.class);
//		});

//		seekersButton.addClickListener(e -> {
//			UI.getCurrent().navigate(PlasmaSeekerIndexView.class);
//		});

		volunteersButton.addClickListener(e -> {
			UI.getCurrent().navigate(VolunteerFormView.class);
		});

		circularsButton.addClickListener(e -> {
			UI.getCurrent().navigate(GuestCircularsView.class);
		});

	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		homeButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
		aboutButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
		enquiryButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
		leadsButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
		volunteersButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
		circularsButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);

		String location = event.getLocation().getFirstSegment();

		switch (location) {
		case "":
			homeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		case "about-us":
			aboutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		case "enquiry":
			enquiryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		case "resource-form":
			leadsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		case "volunteer-form":
			volunteersButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		case "public-circulars":
			circularsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			break;

		default:
			break;
		}
	}
}
