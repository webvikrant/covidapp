package in.co.itlabs.ui.views;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

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
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.entities.Circular;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.CircularService;
import in.co.itlabs.ui.components.CircularEditorForm;
import in.co.itlabs.ui.components.CircularFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.CircularDataProvider;
import in.co.itlabs.util.CircularFilterParams;
import in.co.itlabs.util.DateUtil;

@PageTitle(value = "Circulars - Ghaziabad Covid Support")
@Route(value = "circulars", layout = AppLayout.class)
public class CircularsView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(CircularsView.class);

	// ui
	private Div titleDiv;
	private Button createButton;
	private CircularEditorForm editorForm;
	private CircularFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<Circular> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private AuthenticatedUser authUser;
	private CircularService circularService;

	private CircularFilterParams filterParams;
	private CircularDataProvider dataProvider;
	private Circular circular;

	public CircularsView() {
		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			logger.info("User not logged in.");
			return;
		}

		circularService = new CircularService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		circular = new Circular();

		editorForm = new CircularEditorForm();
		editorForm.addListener(CircularEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(CircularEditorForm.CancelEvent.class, this::handleCancelEvent);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("450px");
		dialog.add(editorForm);

		filterParams = new CircularFilterParams();

		filterForm = new CircularFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(CircularFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
		recordCount.setWidth("150px");

		createButton = new Button("New", VaadinIcon.PLUS.create());
		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		createButton.addClickListener(e -> {
			dialog.open();
			editorForm.setCircular(circular);
		});

		toolBar = new HorizontalLayout();
		toolBar.setWidthFull();
		toolBar.setAlignItems(Alignment.END);

		Span blank = new Span();
		toolBar.add(filterForm, blank, createButton);
		toolBar.expand(blank);

		dataProvider = new CircularDataProvider(circularService);
		dataProvider.setFilterParams(filterParams);

		grid = new Grid<>(Circular.class);
		configureGrid();

		add(titleDiv, toolBar, grid, recordCount);

		setAlignSelf(Alignment.START, recordCount);

		reload();
	}

	private void configureGrid() {
		grid.removeAllColumns();

		grid.addColumn("subject").setHeader("Subject").setWidth("300px");

		grid.addColumn(resource -> {
			return DateUtil.humanize(resource.getCreatedAt());
		}).setHeader("Uploaded");

		grid.addComponentColumn(circular -> {
			Button button = new Button("More", VaadinIcon.ELLIPSIS_DOTS_H.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
				dialog.open();
				editorForm.setCircular(circular);
			});

			return button;
		}).setHeader("More");

		grid.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Circulars");
	}

	public void handleFilterEvent(CircularFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(CircularEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		circular = event.getCircular();

		if (circular.getId() > 0) {
			// existing resource, hence update it

//			boolean success = circularService.updateResource(messages, circular);
//			if (success) {
//				Notification.show("Resource updated successfully", 5000, Position.TOP_CENTER)
//						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//				reload();
//				circular = new Resource();
//				dialog.close();
//			} else {
//				Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
//						.addThemeVariants(NotificationVariant.LUMO_ERROR);
//			}
		} else {
			// new resource, hence create it

			LocalDateTime now = LocalDateTime.now();

			circular.setCreatedBy(authUser.getId());
			circular.setCreatedAt(now);

			int resourceId = circularService.createCircular(messages, circular);
			if (resourceId > 0) {
				Notification.show("Circular created successfully", 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				reload();
				circular = new Circular();
				editorForm.setCircular(circular);
			} else {
				Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		}
	}

	public void handleCancelEvent(CircularEditorForm.CancelEvent event) {
		dialog.close();
	}

	public void reload() {
//		List<Resource> resources = resourceService.getResources();
// 		recordCount.setText("Record(s) found: " + resources.size());
//		grid.setItems(resources);

		dataProvider.refreshAll();
		recordCount.setText("Record(s) found: " + dataProvider.getCount());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
