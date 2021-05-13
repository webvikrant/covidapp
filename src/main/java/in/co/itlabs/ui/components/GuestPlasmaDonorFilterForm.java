package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.BloodGroup;
import in.co.itlabs.util.Gender;
import in.co.itlabs.util.PlasmaDonorFilterParams;

public class GuestPlasmaDonorFilterForm extends VerticalLayout {

	// ui
	private RadioButtonGroup<BloodGroup> bloodGroupRadio;
	private RadioButtonGroup<Gender> genderRadio;
	private ComboBox<City> cityCombo;

	private TextField queryField;

	private Button okButton;
//	private Button cancelButton;

	// non-ui
	private Binder<PlasmaDonorFilterParams> binder;

	private ResourceService resourceService;

	public GuestPlasmaDonorFilterForm() {

		resourceService = new ResourceService();

		bloodGroupRadio = new RadioButtonGroup<>();
		configureBloodGroupRadio();

		genderRadio = new RadioButtonGroup<>();
		configureGenderRadio();

		cityCombo = new ComboBox<>();
		configureCityCombo();

		queryField = new TextField();
		configureQueryField();

		okButton = new Button("Search", VaadinIcon.SEARCH.create());
//		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());
		configureButtons();

		binder = new Binder<>(PlasmaDonorFilterParams.class);

		binder.forField(bloodGroupRadio).asRequired("Please select a blood group").bind("bloodGroup");
		binder.forField(genderRadio).asRequired("Please select a gender").bind("gender");
		binder.forField(cityCombo).bind("city");
		binder.forField(queryField).bind("query");

		add(bloodGroupRadio, genderRadio, cityCombo, queryField, okButton);
	}

	private void configureButtons() {
		okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		okButton.setWidthFull();
		okButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				fireEvent(new FilterEvent(this, binder.getBean()));
			}
		});

//		cancelButton.setWidthFull();
//		cancelButton.addClickListener(e -> {
//			clearForm();
//			fireEvent(new FilterEvent(this, binder.getBean()));
//		});
	}

	private void configureGenderRadio() {
		genderRadio.setLabel("Gender");
		genderRadio.setItems(Gender.values());
	}

	private void configureBloodGroupRadio() {
		bloodGroupRadio.setLabel("Blood group");
		bloodGroupRadio.setItems(BloodGroup.values());
	}

	private void configureCityCombo() {
		cityCombo.setLabel("City");
		cityCombo.setPlaceholder("Select a city");
		cityCombo.setWidthFull();
		cityCombo.setClearButtonVisible(true);
		cityCombo.setItemLabelGenerator(city -> {
			return city.getName();
		});
		cityCombo.setItems(resourceService.getAllCities());
	}

	private void configureQueryField() {
		queryField.setLabel("Specific donor");
		queryField.setPlaceholder("Type name or address");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
	}

//	private void clearForm() {
//		bloodGroupRadio.clear();
//		genderRadio.clear();
//		cityCombo.clear();
//		queryField.clear();
//	}

	public void setFilterParams(PlasmaDonorFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class PlasmaDonorFilterFormEvent extends ComponentEvent<GuestPlasmaDonorFilterForm> {
		private PlasmaDonorFilterParams filterParams;

		protected PlasmaDonorFilterFormEvent(GuestPlasmaDonorFilterForm source, PlasmaDonorFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public PlasmaDonorFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends PlasmaDonorFilterFormEvent {
		FilterEvent(GuestPlasmaDonorFilterForm source, PlasmaDonorFilterParams filterParams) {
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
