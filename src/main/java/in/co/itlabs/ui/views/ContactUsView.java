package in.co.itlabs.ui.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.Enquiry;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Contact us - Ghaziabad Covid Support")
@Route(value = "contact-us", layout = GuestLayout.class)
public class ContactUsView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(ContactUsView.class);

	// ui
	private Div titleDiv;
	private TextField nameField;;
	private TextField phoneField;
	private TextField emailIdField;
	private TextField messageField;

	private Button submitButton = new Button("Send", VaadinIcon.PAPERPLANE.create());

	private Binder<Enquiry> binder;

	// non-ui

	private final List<String> messages = new ArrayList<String>();

	public ContactUsView() {

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		nameField = new TextField("Your name");
		configureNameField();

		phoneField = new TextField("Your phone");
		configurePhoneField();

		emailIdField = new TextField("Your email");
		configureEmailField();

		messageField = new TextField("Your message");
		configureMessageField();

		submitButton = new Button("Send", VaadinIcon.PAPERPLANE.create());
		configureSubmitButton();

		binder = new Binder<>(Enquiry.class);

		binder.forField(nameField).asRequired("Name can not be blank").bind("name");
		binder.forField(phoneField).asRequired("Phone can not be blank").bind("phone");
		binder.forField(emailIdField).bind("emailId");
		binder.forField(messageField).asRequired("Message can not be blank").bind("message");

		binder.setBean(new Enquiry());

		// right id form
		VerticalLayout main = new VerticalLayout();
		main.addClassName("card");
		main.setWidthFull();

		main.add(nameField, phoneField, emailIdField, messageField, submitButton);

		add(titleDiv, main);
	}

	private void configureNameField() {
		nameField.setWidthFull();
	}

	private void configurePhoneField() {
		phoneField.setWidthFull();
	}

	private void configureEmailField() {
		emailIdField.setWidthFull();
	}

	private void configureMessageField() {
		messageField.setWidthFull();
	}

	private void configureSubmitButton() {

		submitButton.setWidthFull();
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		submitButton.addClickListener(e -> {

			if (binder.validate().isOk()) {
				messages.clear();
//				authUser = authService.authenticate(messages, binder.getBean());
			}
		});
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Contact us");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
