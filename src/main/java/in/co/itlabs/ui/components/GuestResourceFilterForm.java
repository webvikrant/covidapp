package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.Resource.Type;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.ResourceFilterParams;

public class GuestResourceFilterForm extends VerticalLayout {

	// ui
	private ComboBox<City> cityCombo;
	private ComboBox<Resource.Type> typeCombo;

	private TextField queryField;
	private Button filterButton;

	// non-ui
	private Binder<ResourceFilterParams> binder;

	private ResourceService resourceService;

	public GuestResourceFilterForm() {

		resourceService = new ResourceService();

		setAlignItems(Alignment.CENTER);

		cityCombo = new ComboBox<>();
		configureCityCombo();

		typeCombo = new ComboBox<>();
		configureTypeCombo();

		queryField = new TextField();
		configureQueryField();

		filterButton = new Button("Search", VaadinIcon.SEARCH.create());
		configureFilterButton();

		binder = new Binder<>(ResourceFilterParams.class);

		binder.forField(cityCombo).bind("city");
		binder.forField(typeCombo).bind("type");
		binder.forField(queryField).bind("query");

		add(typeCombo, cityCombo, queryField, filterButton);
	}

	private void configureFilterButton() {
		filterButton.setWidthFull();
		filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		filterButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				fireEvent(new FilterEvent(this, binder.getBean()));
			}
		});
	}

//	private void clearForm() {
//		cityCombo.clear();
//		typeCombo.clear();
//		queryField.clear();
//	}

	private void configureCityCombo() {
		cityCombo.setLabel("City (optional)");
		cityCombo.setPlaceholder("Select a city");
		cityCombo.setWidthFull();
		cityCombo.setClearButtonVisible(true);
		cityCombo.setItemLabelGenerator(city -> {
			return city.getName();
		});
		cityCombo.setItems(resourceService.getAllCities());
	}

	private void configureTypeCombo() {
		typeCombo.setLabel("Resource (optional)");
		typeCombo.setPlaceholder("Select a resource");
		typeCombo.setClearButtonVisible(true);
		typeCombo.setWidthFull();
		typeCombo.setItems(Type.values());
	}

	private void configureQueryField() {
		queryField.setLabel("Specific service provider's details (optional)");
		queryField.setPlaceholder("Type provider's name or address or phone");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(ResourceFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class AdvancedResourceFilterFormEvent extends ComponentEvent<GuestResourceFilterForm> {
		private ResourceFilterParams filterParams;

		protected AdvancedResourceFilterFormEvent(GuestResourceFilterForm source, ResourceFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public ResourceFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends AdvancedResourceFilterFormEvent {
		FilterEvent(GuestResourceFilterForm source, ResourceFilterParams filterParams) {
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
