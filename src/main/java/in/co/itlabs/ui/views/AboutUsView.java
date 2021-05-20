package in.co.itlabs.ui.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "About us - Ghaziabad Covid Support")
@Route(value = "about-us", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class AboutUsView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
//	private Image image;
//	private Span name;
//	private Span post;
	private Div bodyDiv;
	private FlexLayout techTeam;
//	private FlexLayout nonTechTeam;
	// non-ui

	public AboutUsView() {

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		bodyDiv = new Div();

		bodyDiv.getStyle().set("fontSize", "10pt");
		bodyDiv.getStyle().set("textAlign", "justify");
		bodyDiv.getStyle().set("color", "gray");

//		image = new Image("images/nagar-ayukt-shri-mahender-singh-tanwar.jpeg",
//				"Nagar Ayukt, Shri Mahender Singh Tanwar, IAS");
//		image.setHeight("100px");
//		image.addClassName("photo");
//
//		name = new Span("Shri Mahender Singh Tanwar, IAS");
//		name.getStyle().set("fontSize", "small");
//		name.getStyle().set("color", "dark-gray");
//
//		post = new Span("(Nagar Ayukt, Ghaziabad Nagar Nigam)");
//		post.getStyle().set("fontSize", "small");
//		post.getStyle().set("color", "gray");

		Paragraph para1 = new Paragraph(
				"World is passing through pandemic and everyone is facing a different challenge of life and struggling for survival.");
		Paragraph para2 = new Paragraph(
				"We, TEAM100-Ghaziabad, is trying to support citizens with very vital and required details after due verification.");
		Paragraph para3 = new Paragraph(
				"TEAM-100, having members from 100 wards of Ghaziabad, has been constituted by following the directions of the Nagar Ayukt, Ghaziabad Nagar Nigam (Shri Mahender Singh Tanwar, IAS) for other purposes (Clean and Green Ghaziabad) but resources are now diverted towards fight against Corona also.");

		bodyDiv.add(para1, para2, para3);

		techTeam = new FlexLayout();
		techTeam.setWidthFull();
		techTeam.setFlexWrap(FlexWrap.WRAP);

		techTeam.add(buildCard("images/team/piyush-goyal.jpeg", "Piyush G."));
		techTeam.add(buildCard("images/team/vikrant-thakur.jpeg", "Vikrant T."));
		techTeam.add(buildCard("images/team/gaurav-makkar.jpeg", "Gaurav M."));
		
		techTeam.add(buildCard("images/team/puneet.jpeg", "Puneet"));
		techTeam.add(buildCard("images/team/pankaj-munjal.jpeg", "Pankaj M."));
		techTeam.add(buildCard("images/team/avishek.jpeg", "Avishek"));
		
		techTeam.add(buildCard("images/team/avinash.jpeg", "Avinash"));
		techTeam.add(buildCard("images/team/gaurav-singhal.jpeg", "Gaurav S."));
		techTeam.add(buildCard("images/team/sanjay-gupta.jpeg", "Sanjay G."));
		
		techTeam.add(buildCard("images/team/santosh-kaushik.jpeg", "Santosh K."));

		VerticalLayout main = new VerticalLayout();
		main.setAlignItems(Alignment.CENTER);
		main.addClassName("card");
		main.add(bodyDiv);
		main.add(techTeam);

		add(titleDiv, main);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("About Us");
	}

	private VerticalLayout buildCard(String imageUrl, String name) {
		VerticalLayout root = new VerticalLayout();
		
		root.setMargin(false);
		root.setPadding(true);
		root.setSpacing(false);
		
		root.setWidth("100px");
		root.setAlignItems(Alignment.CENTER);

		Image image = new Image(imageUrl, name);
		image.setWidthFull();
		image.addClassName("photo");
		image.getStyle().set("objectFit", "contain");

		Span nameSpan = new Span(name);

		nameSpan.getStyle().set("fontSize", "small");
		nameSpan.getStyle().set("color", "gray");

		root.add(image, nameSpan);

		return root;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
