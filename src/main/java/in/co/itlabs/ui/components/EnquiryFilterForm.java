package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.util.EnquiryFilterParams;

public class EnquiryFilterForm extends HorizontalLayout {

	// ui
	private TextField queryField;

	private Button okButton;
	private Button cancelButton;

	// non-ui
	private Binder<EnquiryFilterParams> binder;

	public EnquiryFilterForm() {

		setAlignItems(Alignment.END);

//		Div titleDiv = new Div();
//		titleDiv.setText("Filter");

		queryField = new TextField();
		configureQueryField();

		okButton = new Button("Filter", VaadinIcon.FILTER.create());
		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());
		configureButtons();

		binder = new Binder<>(EnquiryFilterParams.class);

		binder.forField(queryField).bind("query");

		add(queryField, okButton, cancelButton);
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
	}

	private void configureQueryField() {
		queryField.setLabel("Name or Mobile");
		queryField.setPlaceholder("Type name or mobile");
		queryField.setWidth("150px");
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(EnquiryFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class EnquiryFilterFormEvent extends ComponentEvent<EnquiryFilterForm> {
		private EnquiryFilterParams filterParams;

		protected EnquiryFilterFormEvent(EnquiryFilterForm source, EnquiryFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public EnquiryFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends EnquiryFilterFormEvent {
		FilterEvent(EnquiryFilterForm source, EnquiryFilterParams filterParams) {
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
