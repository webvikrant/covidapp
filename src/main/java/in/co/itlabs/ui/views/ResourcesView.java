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

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.Resource.Status;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.ResourceEditorForm;
import in.co.itlabs.ui.components.ResourceFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.ResourceDataProvider;
import in.co.itlabs.util.ResourceFilterParams;

@PageTitle(value = "Resources")
@Route(value = "resources", layout = AppLayout.class)
public class ResourcesView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(ResourcesView.class);

	// ui
	private Div titleDiv;
	private Button createButton;
	private ResourceEditorForm editorForm;
	private ResourceFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<Resource> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private AuthenticatedUser authUser;
	private ResourceService resourceService;

	private ResourceFilterParams filterParams;
	private ResourceDataProvider dataProvider;
	private Resource resource;

	public ResourcesView() {
		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			logger.info("User not logged in.");
			return;
		}

		resourceService = new ResourceService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		resource = new Resource();

		editorForm = new ResourceEditorForm(resourceService);
		editorForm.addListener(ResourceEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(ResourceEditorForm.CancelEvent.class, this::handleCancelEvent);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("450px");
		dialog.add(editorForm);

		filterParams = new ResourceFilterParams();

		filterForm = new ResourceFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(ResourceFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
		recordCount.setWidth("150px");

		createButton = new Button("New", VaadinIcon.PLUS.create());
		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		createButton.addClickListener(e -> {
			dialog.open();
			editorForm.setResource(resource);
		});

		toolBar = new HorizontalLayout();
		toolBar.setWidthFull();
		toolBar.setAlignItems(Alignment.END);

		Span blank = new Span();
		toolBar.add(filterForm, blank, createButton);
		toolBar.expand(blank);

		dataProvider = new ResourceDataProvider(resourceService);
		dataProvider.setFilterParams(filterParams);

		grid = new Grid<>(Resource.class);
		configureGrid();

		add(titleDiv, toolBar, grid, recordCount);

		setAlignSelf(Alignment.START, recordCount);

		reload();
	}

	private void configureGrid() {
		grid.removeAllColumns();

		grid.addColumn("type").setHeader("Type").setWidth("120px");
		grid.addColumn(resource -> {
			return resource.getCity().getName();
		}).setHeader("City").setWidth("80px");

		grid.addColumn("name").setHeader("Provider name").setWidth("100px");
		grid.addColumn("address").setHeader("Provider address").setWidth("120px");

		grid.addComponentColumn(resource -> {
			Div div = new Div();
			div.getStyle().set("fontWeight", "600");

			switch (resource.getStatus()) {
			case Pending:
				div.setText("Pending");
				div.getStyle().set("color", "gray");
				break;

			case Verified:
				div.setText("Verified");
				div.getStyle().set("color", "green");
				break;

			case Unreachable:
				div.setText("Unreachable");
				div.getStyle().set("color", "orange");
				break;

			case Scam:
				div.setText("Scam");
				div.getStyle().set("color", "red");
				break;

			default:
				break;
			}

			return div;
		}).setHeader("Status").setWidth("90px");

		grid.addColumn(resource -> {
			if (resource.getUpdatedByUser() != null) {
				return resource.getUpdatedByUser().getName();
			} else {
				return "Guest";
			}
		}).setHeader("User").setWidth("80px");

		grid.addColumn(resource -> {
			return DateUtil.humanize(resource.getUpdatedAt());
		}).setHeader("Last updated");

		grid.addComponentColumn(resource -> {
			Button button = new Button("More", VaadinIcon.ELLIPSIS_DOTS_H.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
				dialog.open();
				editorForm.setResource(resource);
			});

			return button;
		}).setHeader("More");

		grid.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Resources");
	}

	public void handleFilterEvent(ResourceFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(ResourceEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		resource = event.getResource();

		if (resource.getId() > 0) {
			// existing resource, hence update it

			resource.setUpdatedBy(authUser.getId());
			resource.setUpdatedAt(LocalDateTime.now());

			boolean success = resourceService.updateResource(messages, resource);
			if (success) {
				Notification.show("Resource updated successfully", 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				reload();
				resource = new Resource();
				dialog.close();
			} else {
				Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} else {
			// new resource, hence create it

			LocalDateTime now = LocalDateTime.now();

			resource.setCreatedBy(authUser.getId());
			resource.setCreatedAt(now);

			resource.setUpdatedBy(authUser.getId());
			resource.setUpdatedAt(now);

			if (resource.getStatus() == null) {
				resource.setStatus(Status.Pending);
			}

			int resourceId = resourceService.createResource(messages, resource);
			if (resourceId > 0) {
				Notification.show("Resource created successfully", 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				reload();
				resource = new Resource();
				editorForm.setResource(resource);
			} else {
				Notification.show(messages.toString(), 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		}
	}

	public void handleCancelEvent(ResourceEditorForm.CancelEvent event) {
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
