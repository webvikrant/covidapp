package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;

public class AdvancedResourceFilterForm extends VerticalLayout {

	public enum status {
		Not_Verified, Verified_12_Hrs_Ago, Verified_24_Hrs_Ago
	}

	private ComboBox<City> cityCombo;
	private ComboBox<Resource> resourceCombo;
	private ComboBox<status> statusCombo;

	private TextField queryField;

	public AdvancedResourceFilterForm() {

		cityCombo = new ComboBox<>();
		configureCityCombo();

		resourceCombo = new ComboBox<>();
		configureResourceCombo();

		statusCombo = new ComboBox<>();
		configureStatusCombo();

		queryField = new TextField();
		configureQueryField();

		add(cityCombo, resourceCombo, statusCombo, queryField);
	}

	private void configureCityCombo() {
		cityCombo.setLabel("City");
		cityCombo.setPlaceholder("Select a city");
		cityCombo.setWidthFull();
	}

	private void configureResourceCombo() {
		resourceCombo.setLabel("Resource-type");
		resourceCombo.setPlaceholder("Select a resource-type");
		resourceCombo.setWidthFull();
	}

	private void configureStatusCombo() {
		statusCombo.setLabel("Status");
		statusCombo.setPlaceholder("Select a status");
		statusCombo.setWidthFull();
	}

	private void configureQueryField() {
		queryField.setLabel("Provider");
		queryField.setPlaceholder("Type name or address");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
		queryField.addValueChangeListener(e -> {
			fireEvent(new FilterEvent(this, e.getValue()));
		});
	}

	public static abstract class UserFilterFormEvent extends ComponentEvent<AdvancedResourceFilterForm> {
		private String queryString;

		protected UserFilterFormEvent(AdvancedResourceFilterForm source, String queryString) {
			super(source, false);
			this.queryString = queryString;
		}

		public String getQueryString() {
			return queryString;
		}
	}

	public static class FilterEvent extends UserFilterFormEvent {
		FilterEvent(AdvancedResourceFilterForm source, String queryQuery) {
			super(source, queryQuery);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
