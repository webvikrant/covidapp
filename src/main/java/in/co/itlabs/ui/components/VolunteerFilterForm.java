package in.co.itlabs.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.util.VolunteerFilterParams;

public class VolunteerFilterForm extends VerticalLayout {

	// ui
	private TextField queryField;

	private Button okButton;
	private Button cancelButton;

	// non-ui
	private Binder<VolunteerFilterParams> binder;

	private ResourceService resourceService;

	public VolunteerFilterForm() {

		resourceService = new ResourceService();

		Div titleDiv = new Div();
		titleDiv.setText("Filter");

		queryField = new TextField();
		configureQueryField();

		okButton = new Button("Filter", VaadinIcon.FILTER.create());
		cancelButton = new Button("Clear", VaadinIcon.CLOSE.create());

		binder = new Binder<>(VolunteerFilterParams.class);

		binder.forField(queryField).bind("query");

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(titleDiv, queryField, buttonBar);
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
		queryField.clear();
	}

	private void configureQueryField() {
		queryField.setLabel("Provider");
		queryField.setPlaceholder("Type name or phone");
		queryField.setWidthFull();
		queryField.setClearButtonVisible(true);
	}

	public void setFilterParams(VolunteerFilterParams filterParams) {
		binder.setBean(filterParams);
	}

	public static abstract class VolunteerFilterFormEvent extends ComponentEvent<VolunteerFilterForm> {
		private VolunteerFilterParams filterParams;

		protected VolunteerFilterFormEvent(VolunteerFilterForm source, VolunteerFilterParams filterParams) {
			super(source, false);
			this.filterParams = filterParams;
		}

		public VolunteerFilterParams getFilterParams() {
			return filterParams;
		}
	}

	public static class FilterEvent extends VolunteerFilterFormEvent {
		FilterEvent(VolunteerFilterForm source, VolunteerFilterParams filterParams) {
			super(source, filterParams);
		}
	}

	public static class CancelEvent extends VolunteerFilterFormEvent {
		CancelEvent(VolunteerFilterForm source, VolunteerFilterParams filterParams) {
			super(source, filterParams);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}
}
