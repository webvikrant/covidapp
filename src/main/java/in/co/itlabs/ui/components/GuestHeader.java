package in.co.itlabs.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GuestHeader extends HorizontalLayout {

	private Icon icon;
	private Span appName;

	public GuestHeader() {
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		icon = VaadinIcon.AMBULANCE.create();
		icon.setSize("18px");

		appName = new Span();
		appName.setText("Ghaziabad Covid Support");
		appName.getStyle().set("fontSize", "16pt");

		add(icon, appName);
	}
}
