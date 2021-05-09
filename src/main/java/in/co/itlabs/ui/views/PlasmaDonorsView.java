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

import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.PlasmaDonorEditorForm;
import in.co.itlabs.ui.components.PlasmaDonorFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.PlasmaDonorDataProvider;
import in.co.itlabs.util.PlasmaDonorFilterParams;

@PageTitle(value = "Plasma Donors")
@Route(value = "plasma-donors", layout = AppLayout.class)
public class PlasmaDonorsView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger logger = LoggerFactory.getLogger(PlasmaDonorsView.class);

	// ui
	private Div titleDiv;
	private PlasmaDonorEditorForm editorForm;
	private PlasmaDonorFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<PlasmaDonor> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private AuthenticatedUser authUser;
	private ResourceService resourceService;

	private PlasmaDonorFilterParams filterParams;
	private PlasmaDonorDataProvider dataProvider;
	private PlasmaDonor plasmaDonor;

	public PlasmaDonorsView() {
		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);
		if (authUser == null) {
			logger.info("User not logged in.");
			return;
		}

		resourceService = new ResourceService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		plasmaDonor = new PlasmaDonor();

		editorForm = new PlasmaDonorEditorForm(resourceService);
		editorForm.addListener(PlasmaDonorEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(PlasmaDonorEditorForm.CancelEvent.class, this::handleCancelEvent);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("400px");
		dialog.add(editorForm);

		filterParams = new PlasmaDonorFilterParams();

		filterForm = new PlasmaDonorFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(PlasmaDonorFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
		recordCount.setWidth("150px");

		toolBar = new HorizontalLayout();
		toolBar.setWidthFull();
		buildToolBar();

		dataProvider = new PlasmaDonorDataProvider(resourceService);
		dataProvider.setFilterParams(filterParams);

		grid = new Grid<>(PlasmaDonor.class);
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

		grid.addColumn("name").setHeader("Name").setWidth("140px");
		grid.addColumn("bloodGroup").setHeader("Blood Group").setWidth("100px");
		grid.addColumn("gender").setHeader("Gender").setWidth("80px");
		grid.addColumn("address").setHeader("Address").setWidth("100px");

		grid.addComponentColumn(plasmaDonor -> {
			Button button = new Button();
			button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

			if (plasmaDonor.isVerified()) {
				button.setText("Verified");
				button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

			} else {
				button.setText("Not verified");
				button.addThemeVariants(ButtonVariant.LUMO_ERROR);
			}
			return button;

		}).setHeader("Status").setWidth("90px");

		grid.addColumn(resource -> {
			return DateUtil.humanize(resource.getUpdatedAt());
		}).setHeader("Last updated");

		grid.addComponentColumn(plasmaDonor -> {
			Button button = new Button("More", VaadinIcon.ELLIPSIS_DOTS_H.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
				dialog.open();
				editorForm.setPlasmaDonor(plasmaDonor);
			});

			return button;
		}).setHeader("More");

		grid.setDataProvider(dataProvider);
	}

	private void buildToolBar() {
		toolBar.setAlignItems(Alignment.END);

		Button createButton = new Button("New", VaadinIcon.PLUS.create());
		createButton.setWidth("100px");
		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		createButton.addClickListener(e -> {
			dialog.open();
			editorForm.setPlasmaDonor(plasmaDonor);
		});

		Span blank = new Span();

		toolBar.add(recordCount, blank, createButton);
		toolBar.setAlignItems(Alignment.CENTER);
		toolBar.expand(blank);

	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Donors");
	}

	public void handleFilterEvent(PlasmaDonorFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(PlasmaDonorEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		plasmaDonor = event.getPlasmaDonor();

		if (plasmaDonor.getId() > 0) {
			// existing resource, hence update it

			plasmaDonor.setUpdatedAt(LocalDateTime.now());

			boolean success = resourceService.updatePlasmaDonor(messages, plasmaDonor);
			if (success) {
				Notification.show("Resource updated successfully", 3000, Position.TOP_CENTER);
				reload();
				plasmaDonor = new PlasmaDonor();
				dialog.close();
			} else {
				Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
			}
		} else {
			// new resource, hence create it

			LocalDateTime now = LocalDateTime.now();

			plasmaDonor.setCreatedAt(now);
			plasmaDonor.setUpdatedAt(now);

			int plasmaDonorId = resourceService.createPlasmaDonor(messages, plasmaDonor);
			if (plasmaDonorId > 0) {
				Notification.show("Plasma Donor created successfully", 3000, Position.TOP_CENTER);
				reload();
				plasmaDonor = new PlasmaDonor();
				editorForm.setPlasmaDonor(plasmaDonor);
			} else {
				Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
			}
		}
	}

	public void handleCancelEvent(PlasmaDonorEditorForm.CancelEvent event) {
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
