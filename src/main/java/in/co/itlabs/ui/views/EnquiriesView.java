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

import in.co.itlabs.business.entities.Enquiry;
import in.co.itlabs.business.services.EnquiryService;
import in.co.itlabs.ui.components.EnquiryEditorForm;
import in.co.itlabs.ui.components.EnquiryFilterForm;
import in.co.itlabs.ui.layouts.AppLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.EnquiryDataProvider;
import in.co.itlabs.util.EnquiryFilterParams;

@PageTitle(value = "Enquiries")
@Route(value = "enquiries", layout = AppLayout.class)
public class EnquiriesView extends VerticalLayout implements BeforeEnterObserver {

	// ui
	private Div titleDiv;
//	private Button createButton;
	private EnquiryEditorForm editorForm;
	private EnquiryFilterForm filterForm;
	private HorizontalLayout toolBar;
	private Grid<Enquiry> grid;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private EnquiryService enquiryService;

	private EnquiryFilterParams filterParams;
	private EnquiryDataProvider dataProvider;
	private Enquiry enquiry;

	public EnquiriesView() {
		enquiryService = new EnquiryService();

		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		enquiry = new Enquiry();

		editorForm = new EnquiryEditorForm();
//		editorForm.addListener(EnquiryEditorForm.SaveEvent.class, this::handleSaveEvent);
		editorForm.addListener(EnquiryEditorForm.CancelEvent.class, this::handleCancelEvent);

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("500px");
		dialog.add(editorForm);

		filterParams = new EnquiryFilterParams();

		filterForm = new EnquiryFilterForm();
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(EnquiryFilterForm.FilterEvent.class, this::handleFilterEvent);

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
		
		dataProvider = new EnquiryDataProvider(enquiryService);
		dataProvider.setFilterParams(filterParams);

		grid = new Grid<>(Enquiry.class);
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
		grid.addColumn("emailId").setHeader("Email").setWidth("120px");
		grid.addColumn("message").setHeader("Message").setWidth("300px");
		
		grid.addColumn(resource -> {
			return DateUtil.humanize(resource.getCreatedAt());
		}).setHeader("Submitted");

		grid.addComponentColumn(enquiry -> {
			Button button = new Button("More", VaadinIcon.ELLIPSIS_DOTS_H.create());
			button.addThemeVariants(ButtonVariant.LUMO_SMALL);
			button.addClickListener(e -> {
				dialog.open();
				editorForm.setEnquiry(enquiry);
				editorForm.setReadOnly();
			});

			return button;
		}).setHeader("More");

		grid.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Enquiries");
	}

	public void handleFilterEvent(EnquiryFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void handleSaveEvent(EnquiryEditorForm.SaveEvent event) {
		List<String> messages = new ArrayList<String>();
		enquiry = event.getEnquiry();

		// new resource, hence create it

		LocalDateTime now = LocalDateTime.now();

		enquiry.setCreatedAt(now);

		int enquiryId = enquiryService.createEnquiry(messages, enquiry);
		if (enquiryId > 0) {
			Notification.show("Message sent successfully", 3000, Position.TOP_CENTER);
			reload();
			enquiry = new Enquiry();
			editorForm.setEnquiry(enquiry);
		} else {
			Notification.show(messages.toString(), 3000, Position.TOP_CENTER);
		}
	}

	public void handleCancelEvent(EnquiryEditorForm.CancelEvent event) {
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
