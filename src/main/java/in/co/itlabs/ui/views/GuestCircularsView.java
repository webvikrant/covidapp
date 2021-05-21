package in.co.itlabs.ui.views;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import in.co.itlabs.business.entities.Circular;
import in.co.itlabs.business.services.CircularService;
import in.co.itlabs.ui.components.GuestCircularFilterForm;
import in.co.itlabs.ui.layouts.GuestLayout;
import in.co.itlabs.util.CircularDataProvider;
import in.co.itlabs.util.CircularFilterParams;
import in.co.itlabs.util.DateUtil;

@PageTitle(value = "Ghaziabad Covid Support")
@Route(value = "public-circulars", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class GuestCircularsView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private GuestCircularFilterForm filterForm;
//	private ListBox<Resource> listBox;
	private Grid<Circular> grid;
	private Div recordCountDiv;
	private Dialog dialog;

	// non-ui
	private CircularService circularService;

	private CircularFilterParams filterParams;
	private CircularDataProvider dataProvider;

	public GuestCircularsView() {

		circularService = new CircularService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("75%");

		filterParams = new CircularFilterParams();

		filterForm = new GuestCircularFilterForm();
//		filterForm.setWidth("87%");
		filterForm.addClassName("card");
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(GuestCircularFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCountDiv = new Div();
		recordCountDiv.addClassName("small-text");
//		recordCount.setWidth("150px");

		dataProvider = new CircularDataProvider(circularService);
		dataProvider.setFilterParams(filterParams);

//		listBox = new ListBox<Resource>();
//		configureListBox();

		grid = new Grid<>(Circular.class);
		configureGrid();

		add(titleDiv, filterForm, grid);

		reload();
	}

	private void configureGrid() {

		grid.removeAllColumns();
		grid.setWidthFull();
		grid.setHeight("420px");

		grid.setPageSize(10);

		grid.addComponentColumn(circular -> {
			VerticalLayout root = new VerticalLayout();

			root.setMargin(false);
			root.setPadding(true);
			root.setSpacing(true);

			root.setAlignItems(Alignment.CENTER);
			root.setWidthFull();
			root.addClassName("card");
			root.getStyle().set("backgroundColor", "#fcfcfc");

			root.getStyle().set("marginTop", "6px");
			root.getStyle().set("marginBottom", "6px");

			Div createdDiv = new Div();
			createdDiv.getStyle().set("fontSize", "small");
			createdDiv.getStyle().set("color", "gray");
			createdDiv.setText("Uploaded on:  " + DateUtil.ddMMMyyyyhhmm(circular.getCreatedAt()));

			TextArea subjectField = new TextArea("Subject");
			subjectField.setValue(circular.getSubject());
			subjectField.setWidthFull();
			subjectField.setReadOnly(true);

			root.add(createdDiv, subjectField);

			if (circular.isImage()) {
				Image photo = new Image();
				photo.addClassName("photo");
				photo.getStyle().set("objectFit", "contain");
//				photo.setHeight("100px");
				photo.setWidth("100%");

				byte[] imageBytes = circular.getFileBytes();
				StreamResource resource = new StreamResource(circular.getFileName(),
						() -> new ByteArrayInputStream(imageBytes));
				photo.setSrc(resource);

				root.add(photo);
			} else {
				TextField attachmentField = new TextField("Attachment");
				attachmentField.setWidthFull();

				String value = "None";
				if (circular.getFileName() != null) {
					value = circular.getFileName();
				}
				attachmentField.setValue(value);
				attachmentField.setReadOnly(true);

				root.add(attachmentField);
			}

			if (circular.getFileName() != null) {
				byte[] imageBytes = circular.getFileBytes();
				StreamResource resource = new StreamResource(circular.getFileName(),
						() -> new ByteArrayInputStream(imageBytes));

				Anchor downloadLink = new Anchor();
				downloadLink.setHref(resource);
				downloadLink.setTarget("_blank");
				downloadLink.getElement().setAttribute("download", true);

				if (circular.isImage()) {
					downloadLink.setText("Show full-size image");
				} else {
					downloadLink.setText("Download attachment");
				}

				root.add(downloadLink);
			}

			return root;

		}).setHeader(recordCountDiv).setTextAlign(ColumnTextAlign.CENTER);

		grid.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Circulars / Notifications");
	}

	public void handleFilterEvent(GuestCircularFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void reload() {
		dataProvider.refreshAll();
		recordCountDiv.setText("Record(s) found: " + dataProvider.getCount());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
