package in.co.itlabs.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.services.AuthService;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.AuthService.Credentials;
import in.co.itlabs.business.services.EmailService;
import in.co.itlabs.ui.layouts.GuestLayout;

@PageTitle(value = "Login")
@Route(value = "login", layout = GuestLayout.class)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

	// ui
	private Div titleDiv;
	private TextField userNameField = new TextField("User name");
	private PasswordField passwordField = new PasswordField("Password");
	private Button loginButton = new Button("Login", VaadinIcon.SIGN_IN.create());
	private Button forgotPasswordButton = new Button("Forgot your password?");

	private Binder<Credentials> binder;

	// non-ui
	private AuthenticatedUser authUser;
	private AuthService authService;
	private EmailService emailService;

	private final List<String> messages = new ArrayList<String>();

	public LoginView() {
		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser != null) {
			logger.info("User already logged in.");
			return;
		}

		authService = new AuthService();
		emailService = new EmailService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		// left is graphic
//		Image image = new Image("images/login-view-image.jpeg", "miniERP");
//		image.getStyle().set("objectFit", "contain");
//		image.addClassName("card-photo");
//		image.setWidth("650px");

		userNameField = new TextField("Username");
		userNameField.setWidthFull();

		passwordField = new PasswordField("Password");
		passwordField.setWidthFull();

		binder = new Binder<>(Credentials.class);

		binder.forField(userNameField).asRequired("Username can not be blank").bind("username");
		binder.forField(passwordField).bind("password");

		binder.setBean(new Credentials());

		// right id form
		VerticalLayout loginForm = new VerticalLayout();
//		loginForm.setWidth("350px");
		loginForm.setWidthFull();
		buildLogin(loginForm);

		HorizontalLayout main = new HorizontalLayout();
		main.setWidthFull();
		main.addClassName("card");
		main.setSpacing(false);
		main.add(loginForm);

		add(titleDiv, main);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Login");
	}

	private void buildLogin(VerticalLayout root) {
		root.setMargin(true);
		root.setAlignItems(Alignment.CENTER);

		loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		loginButton.addClickShortcut(Key.ENTER);
		loginButton.addClickListener(e -> {
			// store the user and his/her privileges in session
			// check if any colleges exist.

			if (binder.validate().isOk()) {
				messages.clear();

				AuthenticatedUser authUser = null;

				authUser = authService.authenticate(messages, binder.getBean());

				if (authUser == null) {
					Notification.show(messages.toString(), 5000, Position.TOP_CENTER);
				} else {
					VaadinSession.getCurrent().setAttribute(AuthenticatedUser.class, authUser);
					switch (authUser.getRole()) {
					case Admin:
						UI.getCurrent().navigate(UsersView.class);

						break;

					case Verifier:
						UI.getCurrent().navigate(ResourcesView.class);

						break;

					default:
						break;
					}
				}
			}
		});

		forgotPasswordButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		forgotPasswordButton.addClickListener(e -> {
			// CHANGE THIS
			// send test email
			String password = "testing 123";
			Email message = EmailBuilder.startingBlank().from("miniERP", "miniERP@itlabs.co.in")
					.to("Vikrant Thakur", "webvikrant@gmail.com").withSubject("Test email")
					.withPlainText("Your password is: " + password).buildEmail();

			Mailer mailer = emailService.getMailer();
			mailer.sendMail(message);
			Notification.show("Email sent", 5000, Position.TOP_CENTER);
		});

		HorizontalLayout buttonBar = new HorizontalLayout();
		Span blank = new Span();

		buttonBar.add(loginButton, blank, forgotPasswordButton);
		buttonBar.setWidthFull();
		buttonBar.expand(blank);

		root.add(userNameField, passwordField, buttonBar);

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		if (authUser != null) {
			// user already logged in
			switch (authUser.getRole()) {
			case Admin:
				event.forwardTo(UsersView.class);
				break;

			case Verifier:
				event.forwardTo(ResourcesView.class);
				break;

			default:
				break;
			}

		}
	}
}
