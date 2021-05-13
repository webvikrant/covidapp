package in.co.itlabs.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.services.ResourceService;
import in.co.itlabs.ui.components.GuestPlasmaDonorFilterForm;
import in.co.itlabs.ui.layouts.GuestLayout;
import in.co.itlabs.util.DateUtil;
import in.co.itlabs.util.GuestPlasmaDonorDataProvider;
import in.co.itlabs.util.PlasmaDonorFilterParams;

@PageTitle(value = "Plasma seeker - Ghaziabad Covid Support")
@Route(value = "plasma-seeker", layout = GuestLayout.class)
@CssImport("./styles/shared-styles.css")
public class PlasmaSeekerIndexView extends VerticalLayout implements BeforeEnterObserver {

//	private static final Logger logger = LoggerFactory.getLogger(IndexView.class);

	// ui
	private Div titleDiv;
	private GuestPlasmaDonorFilterForm filterForm;
	private ListBox<PlasmaDonor> listBox;
	private Div recordCount;
	private Dialog dialog;

	// non-ui
	private ResourceService resourceService;

	private PlasmaDonorFilterParams filterParams;
	private GuestPlasmaDonorDataProvider dataProvider;

	public PlasmaSeekerIndexView() {

		resourceService = new ResourceService();

		setMargin(false);
		setPadding(false);
		setAlignItems(Alignment.CENTER);

		titleDiv = new Div();
		buildTitle();

		dialog = new Dialog();
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setWidth("75%");

		filterParams = new PlasmaDonorFilterParams();

		City city = resourceService.getCityById(1);

		filterParams.setCity(city);

		filterForm = new GuestPlasmaDonorFilterForm();
//		filterForm.setWidth("87%");
		filterForm.addClassName("card");
		filterForm.setFilterParams(filterParams);
		filterForm.addListener(GuestPlasmaDonorFilterForm.FilterEvent.class, this::handleFilterEvent);

		recordCount = new Div();
		recordCount.addClassName("small-text");
//		recordCount.setWidth("150px");

		dataProvider = new GuestPlasmaDonorDataProvider(resourceService);
		dataProvider.setFilterParams(filterParams);

		listBox = new ListBox<PlasmaDonor>();
		configureListBox();

		add(titleDiv, filterForm, recordCount, listBox);

		reload();
	}

	private void configureListBox() {

		listBox.setWidthFull();
		listBox.setRenderer(new ComponentRenderer<VerticalLayout, PlasmaDonor>(plasmaDonor -> {
			VerticalLayout root = new VerticalLayout();

			root.setMargin(false);
			root.setPadding(true);
			root.setSpacing(false);

			root.setAlignItems(Alignment.CENTER);
			root.setWidthFull();
			root.addClassName("card");

			Div updatedDiv = new Div();
			updatedDiv.getStyle().set("fontSize", "small");
			updatedDiv.getStyle().set("color", "gray");
			updatedDiv.setText("Updated " + DateUtil.humanize(plasmaDonor.getUpdatedAt()));

			TextField nameField = new TextField("Name");
			nameField.setValue(plasmaDonor.getName());
			nameField.setWidthFull();
			nameField.setReadOnly(true);

			Button requestButton = new Button("Request contact details");
			requestButton.setWidthFull();
			root.add(updatedDiv, nameField, requestButton);
			return root;

		}));

		listBox.setDataProvider(dataProvider);
	}

	private void buildTitle() {
		titleDiv.addClassName("view-title");
		titleDiv.add("Plasma Seeker");
	}

	public void handleFilterEvent(GuestPlasmaDonorFilterForm.FilterEvent event) {
		filterParams = event.getFilterParams();
		dataProvider.setFilterParams(filterParams);
		reload();
	}

	public void reload() {
		dataProvider.refreshAll();
		recordCount.setText("Record(s) found: " + dataProvider.getCount());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}
