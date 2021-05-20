package in.co.itlabs.ui.components;

import java.util.Locale;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.util.CircularFilterParams;

public class GuestCircularFilterForm extends VerticalLayout {

	// ui

	private TextField queryField;
	private DatePicker fromDatePicker;
	private DatePicker toDatePicker;

	private Button okButton;

	// non-ui
	private Binder<CircularFilterParams> binder;

	public GuestCircularFilterForm() {

		setAlignItems(Alignment.END);

		queryField = new TextField();
		configureQueryField();

		fromDatePicker = new DatePicker();
		configureFromDatePicker();

		toDatePicker = new DatePicker();
		configureFromToPicker();

		okButton = new Button("Search", VaadinIcon.SEARCH.create());
		configureButtons();

		binder = new Binder<>(CircularFilterParams.class);

		binder.forField(queryField).bind("query");
		binder.forField(fromDatePicker).bind("fromDate");
		binder.forField(toDatePicker).bind("toDate");

		Span blank = new Span();

		HorizontalLayout datesBar = new HorizontalLayout();
		datesBar.setWidthFull();
		datesBar.add(fromDatePicker, blank, toDatePicker);
		datesBar.expand(blank);

		add(datesBar, queryField, okButton);
	}

	private void configureQueryField() {
		queryField.setLabel("Specific circular (optional)");
		queryField.setPlaceholder("Type subject");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
	}

	private void configureFromDatePicker() {
		fromDatePicker.setLabel("From date (optional)");
		fromDatePicker.setWidth("145px");
		fromDatePicker.setLocale(new Locale("in"));
		fromDatePicker.setClearButtonVisible(true);
	}

	private void configureFromToPicker() {
		toDatePicker.setLabel("To date (optional)");
		toDatePicker.setWidth("145px");
		toDatePicker.setLocale(new Locale("in"));
		toDatePicker.setClearButtonVisible(true);
	}

	private void configureButtons() {
		okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		okButton.setWidthFull();
		okButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				fireEvent(new FilterEvent(this, binder.getBean()));
			}
		});
	}

//	private void clearForm() {
//		queryField.clear();
//		fromDatePicker.clear();
//		toDatePicker.clear();
//	}

	public void setFilterParams(CircularFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class GuestCircularFilterFormEvent extends ComponentEvent<GuestCircularFilterForm> {
		private CircularFilterParams filterParams;

		protected GuestCircularFilterFormEvent(GuestCircularFilterForm source, CircularFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public CircularFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends GuestCircularFilterFormEvent {
		FilterEvent(GuestCircularFilterForm source, CircularFilterParams filterParams) {
			super(source, filterParams);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
