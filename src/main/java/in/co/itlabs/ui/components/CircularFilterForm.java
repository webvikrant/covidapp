package in.co.itlabs.ui.components;

import java.util.Locale;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.util.CircularFilterParams;

public class CircularFilterForm extends HorizontalLayout {

	// ui

	private TextField queryField;
	private DatePicker fromDatePicker;
	private DatePicker toDatePicker;

	private Button okButton;
	private Button cancelButton;

	// non-ui
	private Binder<CircularFilterParams> binder;

	public CircularFilterForm() {

		setAlignItems(Alignment.END);

		queryField = new TextField();
		configureQueryField();

		fromDatePicker = new DatePicker();
		configureFromDatePicker();

		toDatePicker = new DatePicker();
		configureFromToPicker();

		okButton = new Button("Filter", VaadinIcon.FILTER.create());
		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());
		configureButtons();

		binder = new Binder<>(CircularFilterParams.class);

		binder.forField(queryField).bind("query");
		binder.forField(fromDatePicker).bind("fromDate");
		binder.forField(toDatePicker).bind("toDate");

		add(queryField, fromDatePicker, toDatePicker, okButton, cancelButton);
	}

	private void configureFromDatePicker() {
		fromDatePicker.setLabel("From");
		fromDatePicker.setWidth("120px");
		fromDatePicker.setLocale(new Locale("in"));
	}

	private void configureFromToPicker() {
		toDatePicker.setLabel("To");
		toDatePicker.setWidth("120px");
		toDatePicker.setLocale(new Locale("in"));
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
		queryField.clear();
		fromDatePicker.clear();
		toDatePicker.clear();
	}

	private void configureQueryField() {
		queryField.setLabel("Specific circular");
		queryField.setPlaceholder("Type subject");
		queryField.setWidth("300px");
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(CircularFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class CircularFilterFormEvent extends ComponentEvent<CircularFilterForm> {
		private CircularFilterParams filterParams;

		protected CircularFilterFormEvent(CircularFilterForm source, CircularFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public CircularFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends CircularFilterFormEvent {
		FilterEvent(CircularFilterForm source, CircularFilterParams filterParams) {
			super(source, filterParams);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
