package in.co.itlabs.ui.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
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
	private Div bodyDiv;

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

		Paragraph para1 = new Paragraph(
				"World is passing through pandemic and everyone is facing a different challenge of life and struggling for survival.");
		Paragraph para2 = new Paragraph(
				"We, TEAM100-Ghaziabad, is trying to support citizens with very vital and required details after due verification.");
		Paragraph para3 = new Paragraph(
				"TEAM-100, having members from 100 wards of Ghaziabad, has been constituted by following the directions of the Nagar Ayukt, Ghaziabad Nagar Nigam (Shri Mahender Singh Tanwar, IAS) for other purposes (Clean and Green Ghaziabad) but resources are now diverted towards fight against Corona also.");

		bodyDiv.add(para1, para2, para3);

		VerticalLayout main = new VerticalLayout();
		main.addClassName("card");
		main.add(bodyDiv);

		add(titleDiv, main);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("About us");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
