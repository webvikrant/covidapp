package in.co.itlabs.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.layouts.GuestLayout;
import in.co.itlabs.util.BloodGroup;

@PageTitle(value = "Ghaziabad Covid Support")
@Route(value = "plasma-donor", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class PlasmaDonorView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;

	private ComboBox<BloodGroup> bloodGroupCombo;

	private TextField nameField;
	private IntegerField ageField;
	private TextField phoneField;
	private TextField pincodeField;
	private TextArea addressField;

	private DatePicker infectionDatePicker;
	private DatePicker recoveryDatePicker;
	private DatePicker donationDatePicker;

	private Checkbox availableCheck;

	private Button submitButton;

	private Binder<PlasmaDonor> binder;

	// non-ui
	private ResourceService resourceService;

	public PlasmaDonorView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		bloodGroupCombo = new ComboBox<>("Blood group");
		bloodGroupCombo.setWidthFull();

		nameField = new TextField("Name");
		nameField.setWidthFull();

		ageField = new IntegerField("Age");
		ageField.setWidth("150px");

		phoneField = new TextField("Phone");
		phoneField.setWidthFull();

		pincodeField = new TextField("Pincode");
		pincodeField.setWidthFull();

		addressField = new TextArea("Address");
		addressField.setWidthFull();

		infectionDatePicker = new DatePicker("Infected on");
		infectionDatePicker.setWidthFull();

		recoveryDatePicker = new DatePicker("Recovery confirmed on");
		recoveryDatePicker.setWidthFull();

		donationDatePicker = new DatePicker("Expected date of donation");
		donationDatePicker.setWidthFull();

		availableCheck = new Checkbox("Available for donation in hospital");

		submitButton = new Button("Submit", VaadinIcon.CHECK.create());
		configureSubmitButton();

		binder = new Binder<>(PlasmaDonor.class);

		binder.forField(bloodGroupCombo).asRequired("Blood group can not be blank").bind("bloodGroup");
		binder.forField(nameField).asRequired("Name can not be blank").bind("name");
		binder.forField(ageField).asRequired("Age can not be blank").bind("age");
		binder.forField(phoneField).asRequired("Phone can not be blank").bind("phone");
		binder.forField(pincodeField).asRequired("Pincode can not be blank").bind("pincode");
		binder.forField(addressField).asRequired("Address can not be blank").bind("address");
		binder.forField(infectionDatePicker).asRequired("Infection date can not be blank").bind("infectionDate");
		binder.forField(recoveryDatePicker).asRequired("Recovery date can not be blank").bind("recoveryDate");
		binder.forField(donationDatePicker).asRequired("Expected donation date can not be blank").bind("donationDate");
		binder.forField(availableCheck).asRequired("Availability for hospital can not be blank").bind("availability");

		VerticalLayout main = new VerticalLayout();
		main.addClassName("card");
		main.add(bloodGroupCombo, nameField, ageField, phoneField, pincodeField, addressField, infectionDatePicker,
				recoveryDatePicker, donationDatePicker, availableCheck, submitButton);

		add(titleDiv, main);
	}

	private void configureSubmitButton() {
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitButton.setWidthFull();
		submitButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				
			}
		});
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Donor");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
