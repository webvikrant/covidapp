package in.co.itlabs.ui.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GuestHeader extends HorizontalLayout {

//	private Icon icon;
	private Span appName;

	public GuestHeader() {
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

//		icon = VaadinIcon.AMBULANCE.create();
//		icon.setSize("18px");

		Image image = new Image("icons/icon.png", "Ghaziabad Covid Support");
		image.setWidth("28px");
		image.setHeight("28px");
//		image.addClassName("photo");
		image.getStyle().set("objectFit", "contain");
		
		appName = new Span();
		appName.setText("Ghaziabad Covid Support");
		appName.getStyle().set("fontSize", "16pt");
		appName.getStyle().set("fontWeight", "300");
		appName.getStyle().set("color", "gray");
		
		add(image, appName);
	}
}
