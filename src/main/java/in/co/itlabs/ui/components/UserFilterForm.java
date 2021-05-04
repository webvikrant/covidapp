package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class UserFilterForm extends VerticalLayout {

	private TextField queryField;

	public UserFilterForm() {

		queryField = new TextField();
		queryField.setPlaceholder("Filter by name/username");
		queryField.setWidth("200px");
		queryField.setClearButtonVisible(true);
		queryField.addValueChangeListener(e -> {
			fireEvent(new FilterEvent(this, e.getValue()));
		});

		add(queryField);
	}

	public static abstract class UserFilterFormEvent extends ComponentEvent<UserFilterForm> {
		private String queryString;

		protected UserFilterFormEvent(UserFilterForm source, String queryString) {
			super(source, false);
			this.queryString = queryString;
		}

		public String getQueryString() {
			return queryString;
		}
	}

	public static class FilterEvent extends UserFilterFormEvent {
		FilterEvent(UserFilterForm source, String queryQuery) {
			super(source, queryQuery);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
