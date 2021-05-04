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
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.ResourceEditorForm;
import in.co.itlabs.ui.components.AdvancedResourceFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.AdvancedResourceFilterParams;

@PageTitle(value = "Resources")
@Route(value = "resources", layout = AppLayout.class)
public class ResourcesView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(ResourcesView.class);

	// ui
	private Div titleDiv;
	private ResourceEditorForm editorForm;
	private AdvancedResourceFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<Resource> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private AuthenticatedUser authUser;
	private ResourceService resourceService;

	private AdvancedResourceFilterParams filterParams;
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

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);

		resource = new Resource();
		
		editorForm = new ResourceEditorForm(resourceService);
		editorForm.addListener(ResourceEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(ResourceEditorForm.CancelEvent.class, this::handleCancelEvent);

		filterParams = new AdvancedResourceFilterParams();

		filterForm = new AdvancedResourceFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(AdvancedResourceFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
		recordCount.setWidth("150px");

		toolBar = new HorizontalLayout();
		toolBar.setWidthFull();
		buildToolBar();

		grid = new Grid<>(Resource.class);
		configureGrid();

		VerticalLayout main = new VerticalLayout();
		main.add(toolBar, grid);

		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setWidthFull();
		splitLayout.setSplitterPosition(25);
		splitLayout.addToPrimary(filterForm);
		splitLayout.addToSecondary(main);

		add(titleDiv, splitLayout);

		reload();
	}

	private void configureGrid() {
		grid.removeAllColumns();

		grid.addColumn("name").setHeader("name").setWidth("100px");
		grid.addColumn("address").setHeader("address").setWidth("100px");

		grid.addComponentColumn(user -> {
			Button button = new Button("More", VaadinIcon.ARROW_FORWARD.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
			});

			return button;
		}).setHeader("More");

	}

	private void buildToolBar() {
//		root.setAlignItems(Alignment.END);

		Button createButton = new Button("New", VaadinIcon.PLUS.create());
		createButton.setWidth("100px");
		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		createButton.addClickListener(e -> {
			dialog.setWidth("500px");
			dialog.removeAll();
			dialog.open();
			dialog.add(editorForm);
			editorForm.setResource(resource);
		});

		Span blank = new Span();

		toolBar.add(recordCount, blank, createButton);
		toolBar.setAlignItems(Alignment.CENTER);
		toolBar.expand(blank);

	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Resources");
	}

	public void handleFilterEvent(AdvancedResourceFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		reload();
	}

	public void handleSaveEvent(ResourceEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		resource = event.getResource();

		resource.setCreatedBy(authUser.getId());
		resource.setCreatedAt(LocalDateTime.now());
		
		int resourceId = resourceService.createResource(messages, resource);
		if (resourceId > 0) {
			Notification.show("Resource created successfully", 3000, Position.TOP_CENTER);
			reload();
//			user.clear();
			resource = new Resource();
			editorForm.setResource(resource);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	public void handleCancelEvent(ResourceEditorForm.CancelEvent event) {
		dialog.close();
	}

	public void reload() {
		List<Resource> resources = resourceService.getResources();
		recordCount.setText("Record(s) found: " + resources.size());
		grid.setItems(resources);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
