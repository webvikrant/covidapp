package in.co.itlabs.ui.views;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.entities.User;
import in.co.itlabs.business.services.AuthService;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.AuthService.Role;
import in.co.itlabs.ui.components.UserEditorForm;
import in.co.itlabs.ui.components.UserFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;

@PageTitle(value = "Users")
@Route(value = "users", layout = AppLayout.class)
public class UsersView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	// ui

	private UserEditorForm userEditor;
	private UserFilterForm userFilterForm;
	private Grid<User> grid;
	private Div resultCount;
	private Dialog dialog;

	// non-ui
	private AuthenticatedUser authUser;
	private AuthService authService;

	private String queryString;

	@PostConstruct
	public void init() {

		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			logger.info("User not logged in.");
			return;
		}
		
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		Div titleDiv = new Div();
		buildTitle(titleDiv);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);

		userEditor = new UserEditorForm();
		userEditor.setUser(new User());
		userEditor.addListener(UserEditorForm.SaveEvent.class, this::handleSaveEvent);
		userEditor.addListener(UserEditorForm.CancelEvent.class, this::handleCancelEvent);

		queryString = null;

		userFilterForm = new UserFilterForm();
		userFilterForm.setPadding(false);
		userFilterForm.addListener(UserFilterForm.FilterEvent.class, this::handleFilterEvent);

		resultCount = new Div();
		resultCount.addClassName("small-text");
		resultCount.setWidth("150px");

		grid = new Grid<>(User.class);
		configureGrid();

		HorizontalLayout toolBar = new HorizontalLayout();
		buildToolBar(toolBar);
		toolBar.setWidthFull();

		VerticalLayout main = new VerticalLayout();
		main.add(toolBar, grid);

		add(titleDiv, main);

		reload();
	}

	private void configureGrid() {
		grid.removeAllColumns();

		grid.addColumn("name").setHeader("name").setWidth("100px");
		grid.addColumn("username").setHeader("Username").setWidth("100px");

		grid.addComponentColumn(user -> {
			Button button = new Button("More", VaadinIcon.ARROW_FORWARD.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
			});

			return button;
		}).setHeader("More");

	}

	private void buildToolBar(HorizontalLayout root) {
//		root.setAlignItems(Alignment.END);

		Button createButton = new Button("New", VaadinIcon.PLUS.create());
		createButton.setWidth("100px");
		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		createButton.addClickListener(e -> {
			dialog.setWidth("400px");
			dialog.removeAll();
			dialog.open();
			dialog.add(userEditor);
		});

		Span blank = new Span();

		root.add(resultCount, userFilterForm, blank, createButton);
		root.setAlignItems(Alignment.CENTER);
		root.expand(blank);

	}

	private void buildTitle(Div root) {
		root.addClassName("view-title");
		root.add("Users");
	}

	public void handleFilterEvent(UserFilterForm.FilterEvent event) {
		queryString = event.getQueryString();
		reload();
	}

	public void handleSaveEvent(UserEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		User user = event.getUser();

		int userId = authService.createUser(messages, user);
		if (userId > 0) {
			Notification.show("Student created successfully", 3000, Position.TOP_CENTER);
			reload();
//			user.clear();
			userEditor.setUser(user);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	public void handleCancelEvent(UserEditorForm.CancelEvent event) {
		dialog.close();
	}

	public void reload() {
		List<User> users = authService.getAllUsers(queryString);
		resultCount.setText("Record(s) found: " + users.size());
		grid.setItems(users);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		AuthenticatedUser authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser.getRole() != Role.Admin) {
			VaadinSession.getCurrent().setAttribute("error-message", "Access denied.");
			event.forwardTo(ErrorView.class);
		}
	}
}
