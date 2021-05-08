package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.ResourceFilterParams;

public class ResourceFilterForm extends HorizontalLayout {

	// ui
	private ComboBox<City> cityCombo;
	private ComboBox<Resource.Type> typeCombo;
	private ComboBox<Resource.Status> statusCombo;

	private TextField queryField;

	private Button okButton;
	private Button cancelButton;

	// non-ui
	private Binder<ResourceFilterParams> binder;

	private ResourceService resourceService;

	public ResourceFilterForm() {

		resourceService = new ResourceService();

		setAlignItems(Alignment.END);

//		Div titleDiv = new Div();
//		titleDiv.setText("Filter");

		cityCombo = new ComboBox<>();
		configureCityCombo();

		typeCombo = new ComboBox<>();
		configureTypeCombo();

		statusCombo = new ComboBox<>();
		configureStatusCombo();

		queryField = new TextField();
		configureQueryField();

		okButton = new Button("Filter", VaadinIcon.FILTER.create());
		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());
		configureButtons();

		binder = new Binder<>(ResourceFilterParams.class);

		binder.forField(cityCombo).bind("city");
		binder.forField(typeCombo).bind("type");
		binder.forField(statusCombo).bind("status");
		binder.forField(queryField).bind("query");

		add(typeCombo, cityCombo, queryField, statusCombo, okButton, cancelButton);
	}

	private void configureButtons() {
		okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		okButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				fireEvent(new FilterEvent(this, binder.getBean()));
			}
		});

		cancelButton.addClickListener(e -> {
			clearForm();
			fireEvent(new FilterEvent(this, binder.getBean()));
		});
	}

	private void clearForm() {
		cityCombo.clear();
		typeCombo.clear();
		statusCombo.clear();
		queryField.clear();
	}

	private void configureCityCombo() {
		cityCombo.setLabel("City");
		cityCombo.setPlaceholder("Select a city");
		cityCombo.setWidth("130px");
		cityCombo.setClearButtonVisible(true);
		cityCombo.setItemLabelGenerator(city -> {
			return city.getName();
		});
		cityCombo.setItems(resourceService.getAllCities());
	}

	private void configureTypeCombo() {
		typeCombo.setLabel("Resource-type");
		typeCombo.setPlaceholder("Select a resource-type");
		typeCombo.setClearButtonVisible(true);
		typeCombo.setWidth("190px");
		typeCombo.setItems(Resource.Type.values());
	}

	private void configureStatusCombo() {
		statusCombo.setLabel("Status");
		statusCombo.setPlaceholder("Select a status");
		statusCombo.setClearButtonVisible(true);
		statusCombo.setWidth("150px");
		statusCombo.setItems(Resource.Status.values());
	}

	private void configureQueryField() {
		queryField.setLabel("Provider");
		queryField.setPlaceholder("Type name or address");
		queryField.setWidth("150px");
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(ResourceFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class AdvancedResourceFilterFormEvent extends ComponentEvent<ResourceFilterForm> {
		private ResourceFilterParams filterParams;

		protected AdvancedResourceFilterFormEvent(ResourceFilterForm source, ResourceFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public ResourceFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends AdvancedResourceFilterFormEvent {
		FilterEvent(ResourceFilterForm source, ResourceFilterParams filterParams) {
			super(source, filterParams);
		}
	}

//	public static class CancelEvent extends AdvancedResourceFilterFormEvent {
//		CancelEvent(AdvancedResourceFilterForm source, ResourceFilterParams filterParams) {
//			super(source, filterParams);
//		}
//	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
