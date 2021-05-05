package in.co.itlabs.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppFooter extends HorizontalLayout {

	private Icon copyrightIcon;
	private Anchor link;
	private Button button;
	private Span email = new Span("team100@gmail.com");

	public AppFooter() {
		setMargin(false);
		setPadding(false);
		setSpacing(false);

		setJustifyContentMode(JustifyContentMode.BETWEEN);
		setAlignItems(Alignment.CENTER);

		copyrightIcon = VaadinIcon.COPYRIGHT.create();
		copyrightIcon.setSize("10px");

		button = new Button("Team 100", VaadinIcon.COPYRIGHT.create());
		button.addThemeVariants(ButtonVariant.LUMO_SMALL);
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		link = new Anchor("https://itlabs.co.in", button);

		email.addClassName("small-text");

		add(link, email);
	}
}
