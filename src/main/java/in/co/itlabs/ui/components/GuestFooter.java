package in.co.itlabs.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GuestFooter extends HorizontalLayout {

	private Icon icon;
	private Span copyright = new Span("Team100");
	private Span email = new Span("team100tech@gmail.com");

	public GuestFooter() {

		setSpacing(false);

		setAlignItems(Alignment.CENTER);

		icon = VaadinIcon.COPYRIGHT.create();
		icon.setSize("11px");

		icon.getStyle().set("color", "gray");

		copyright.getStyle().set("fontSize", "small");
		copyright.getStyle().set("color", "gray");

		email.getStyle().set("fontSize", "small");
		email.getStyle().set("color", "gray");

		Span blank = new Span();
		add(icon, copyright, blank, email);
		expand(blank);
	}
}
