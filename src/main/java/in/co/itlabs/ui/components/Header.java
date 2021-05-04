package in.co.itlabs.ui.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Header extends HorizontalLayout {

	private Icon icon;
	private Span appName;
	private Span blank;
	private Span date;

	public Header() {
		setJustifyContentMode(JustifyContentMode.BETWEEN);
		setAlignItems(Alignment.CENTER);

		icon = VaadinIcon.DATABASE.create();
		icon.setSize("16px");

		appName = new Span();
		appName.setText("mini ERP");

		blank = new Span();

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, YYYY");
		LocalDate today = LocalDate.now();

		date = new Span();
		date.setText(dateFormatter.format(today));

		add(icon, appName, blank, date);

		expand(blank);
	}
}
