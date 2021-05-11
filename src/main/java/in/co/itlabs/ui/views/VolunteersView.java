package in.co.itlabs.ui.views;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.Volunteer;
import in.co.itlabs.business.services.VolunteerService;
import in.co.itlabs.ui.components.VolunteerEditorForm;
import in.co.itlabs.ui.components.VolunteerFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.VolunteerDataProvider;
import in.co.itlabs.util.VolunteerFilterParams;

@PageTitle(value = "Volunteers - Ghaziabad Covid Support")
@Route(value = "volunteers", layout = AppLayout.class)
public class VolunteersView extends VerticalLayout implements BeforeEnterObserver {

	// ui
	private Div titleDiv;
//	private Button createButton;
	private VolunteerEditorForm editorForm;
	private VolunteerFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<Volunteer> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private VolunteerService volunteerService;

	private VolunteerFilterParams filterParams;
	private VolunteerDataProvider dataProvider;
	private Volunteer volunteer;

	public VolunteersView() {
		volunteerService = new VolunteerService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		volunteer = new Volunteer();

		editorForm = new VolunteerEditorForm();
		editorForm.addListener(VolunteerEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(VolunteerEditorForm.CancelEvent.class, this::handleCancelEvent);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("500px");
		dialog.add(editorForm);

		filterParams = new VolunteerFilterParams();

		filterForm = new VolunteerFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(VolunteerFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
		recordCount.setWidth("150px");

//		createButton = new Button("New", VaadinIcon.PLUS.create());
//		createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
//		createButton.addClickListener(e -> {
//			dialog.open();
//			editorForm.setEnquiry(enquiry);
//		});
		
		toolBar = new HorizontalLayout();
		toolBar.setWidthFull();
		toolBar.setAlignItems(Alignment.END);

//		Span blank = new Span();
		toolBar.add(filterForm);
//		toolBar.expand(blank);
		
		dataProvider = new VolunteerDataProvider(volunteerService);
		dataProvider.setFilterParams(filterParams);

		grid = new Grid<>(Volunteer.class);
		configureGrid();

		VerticalLayout main = new VerticalLayout();
		main.add(toolBar, grid);

		add(titleDiv, toolBar, grid, recordCount);
		setAlignSelf(Alignment.START, recordCount);

		reload();
	}

	private void configureGrid() {
		grid.removeAllColumns();

		grid.addColumn("name").setHeader("Name").setWidth("120px");
		grid.addColumn("phone").setHeader("Mobile").setWidth("80px");
		
		grid.addColumn(resource -> {
			return DateUtil.humanize(resource.getCreatedAt());
		}).setHeader("Submitted");

		grid.addComponentColumn(volunteer -> {
			Button button = new Button("More", VaadinIcon.ELLIPSIS_DOTS_H.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
				dialog.open();
				editorForm.setVolunteer(volunteer);
				editorForm.setReadOnly();
			});

			return button;
		}).setHeader("More");

		grid.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Volunteers");
	}

	public void handleFilterEvent(VolunteerFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(VolunteerEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		volunteer = event.getVolunteer();

		// new resource, hence create it

		LocalDateTime now = LocalDateTime.now();

		volunteer.setCreatedAt(now);

		int enquiryId = volunteerService.createVolunteer(messages, volunteer);
		if (enquiryId > 0) {
			Notification.show("Record saved successfully", 3000, Position.TOP_CENTER);
			reload();
			volunteer = new Volunteer();
			editorForm.setVolunteer(volunteer);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	public void handleCancelEvent(VolunteerEditorForm.CancelEvent event) {
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
