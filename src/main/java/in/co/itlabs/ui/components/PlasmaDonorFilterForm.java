package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.PlasmaDonorFilterParams;

public class PlasmaDonorFilterForm extends VerticalLayout {

	// ui
	private ComboBox<City> cityCombo;

	private TextField queryField;

	private Button okButton;
	private Button cancelButton;

	// non-ui
	private Binder<PlasmaDonorFilterParams> binder;

	private ResourceService resourceService;

	public PlasmaDonorFilterForm() {

		resourceService = new ResourceService();

		Div titleDiv = new Div();
		titleDiv.setText("Filter");

		cityCombo = new ComboBox<>();
		configureCityCombo();

		queryField = new TextField();
		configureQueryField();

		okButton = new Button("Filter", VaadinIcon.FILTER.create());
		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());

		binder = new Binder<>(PlasmaDonorFilterParams.class);

		binder.forField(cityCombo).bind("city");
		binder.forField(queryField).bind("query");

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(titleDiv, cityCombo, queryField, buttonBar);
	}

	private void buildButtonBar(HorizontalLayout root) {
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

		Span blank = new Span();
		root.add(okButton, blank, cancelButton);
		root.expand(blank);
	}

	private void clearForm() {
		cityCombo.clear();
		queryField.clear();
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
		queryField.setLabel("Provider");
		queryField.setPlaceholder("Type name or address");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(PlasmaDonorFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class PlasmaDonorFilterFormEvent extends ComponentEvent<PlasmaDonorFilterForm> {
		private PlasmaDonorFilterParams filterParams;

		protected PlasmaDonorFilterFormEvent(PlasmaDonorFilterForm source, PlasmaDonorFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public PlasmaDonorFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends PlasmaDonorFilterFormEvent {
		FilterEvent(PlasmaDonorFilterForm source, PlasmaDonorFilterParams filterParams) {
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
