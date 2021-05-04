package in.co.itlabs.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.ui.layouts.AppLayout;

@PageTitle(value = "Error")
@Route(value = "error", layout = AppLayout.class)
public class ErrorView extends VerticalLayout implements BeforeEnterObserver {

	private Div div;

	public ErrorView() {
		div = new Div();
		add(div);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		String message = (String) VaadinSession.getCurrent().getAttribute("error-message");
		if (message != null) {
			div.setText(message);
		}
	}
}
