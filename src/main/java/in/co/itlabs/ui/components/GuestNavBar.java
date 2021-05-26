package in.co.itlabs.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
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

	private Div disclaimerDiv;

	// non-ui

	public GuestNavBar() {

		addClassName("navbar");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);
		
		setPadding(false);
		setMargin(false);
		setSpacing(false);

		homeButton = new Button(VaadinIcon.HOME.create());
		aboutButton = new Button("About Us", VaadinIcon.INFO_CIRCLE.create());
		enquiryButton = new Button("Contact Us", VaadinIcon.ENVELOPE.create());

		leadsButton = new Button("Submit Resource Info", VaadinIcon.PAPERPLANE.create());
//		donorsButton = new Button("Donors", VaadinIcon.DROP.create());
//		seekersButton = new Button("Seekers", VaadinIcon.DROP.create());

		volunteersButton = new Button("Volunteer", VaadinIcon.HANDS_UP.create());

		circularsButton = new Button("Circulars / Notifications", VaadinIcon.DIPLOMA.create());

		configureButtons();

		HorizontalLayout topBar = new HorizontalLayout();
		HorizontalLayout middleBar = new HorizontalLayout();
		HorizontalLayout bottomBar = new HorizontalLayout();

		topBar.setMargin(false);
		topBar.setPadding(false);
		
		middleBar.setMargin(false);
		middleBar.setPadding(false);
		
		bottomBar.setMargin(false);
		bottomBar.setPadding(false);
		
		topBar.add(homeButton, aboutButton, enquiryButton);
		middleBar.add(leadsButton, volunteersButton);
		bottomBar.add(circularsButton);

		disclaimerDiv = new Div();
		disclaimerDiv.setText(
				"This is to notify that Team100-Ghaziabad is providing free service and there is no financial involvement either from service seeker or service provider. It is solely and wholely the responsibility of service seekers and service providers for any mutual transaction for exchange of any service. - Team100 Ghaziabad");
		disclaimerDiv.getStyle().set("fontSize", "10pt");
		disclaimerDiv.getStyle().set("fontWeight", "300");
		disclaimerDiv.getStyle().set("color", "red");
		disclaimerDiv.getStyle().set("textJustify", "auto");
		
		Details disclaimer = new Details("Disclaimer",
		        disclaimerDiv);
		disclaimer.addThemeVariants(DetailsVariant.SMALL);
		disclaimer.setOpened(true);
		add(disclaimer);
		
		add(topBar, middleBar, bottomBar, disclaimer);
		setAlignSelf(Alignment.START, disclaimer);
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
