package in.co.itlabs.ui.components;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Locale;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

import in.co.itlabs.business.entities.Circular;
import in.co.itlabs.business.services.AuthService.AuthenticatedUser;
import in.co.itlabs.business.services.AuthService.Role;
import in.co.itlabs.util.DateUtil;

public class CircularEditorForm extends VerticalLayout {

	// ui

	private Div createdDiv;

	private DatePicker dateField;
	private TextArea subjectField;
	private FileField fileField;

	private Button removeFileButton;
	private Button saveButton;
	private Button cancelButton;

	private Binder<Circular> binder;

	// non-ui
	private AuthenticatedUser authUser;

	public CircularEditorForm() {

		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);

		setAlignItems(Alignment.START);

		createdDiv = new Div();

		dateField = new DatePicker();
		configureDateField();

		subjectField = new TextArea();
		configureSubjectField();

		fileField = new FileField();
		configureFileField();

		binder = new Binder<>(Circular.class);

		binder.forField(dateField).asRequired("Date can not be blank")
				.withValidator(date -> !date.isAfter(LocalDate.now()), "Date can not be in future").bind("date");

		binder.forField(subjectField).asRequired("Subject can not be blank")
				.withValidator(address -> address.length() < 2000, "").bind("subject");

		removeFileButton = new Button("Remove file", VaadinIcon.MINUS.create());
		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(createdDiv, dateField, subjectField, removeFileButton, fileField, buttonBar);
	}

	private void configureDateField() {
		dateField.setWidth("150px");
		dateField.setLabel("Date");
		dateField.setPlaceholder("Select date");
		dateField.setLocale(new Locale("in"));
	}

	private void configureSubjectField() {
		subjectField.setWidthFull();
		subjectField.setHeight("100px");
		subjectField.setLabel("Subject");
		subjectField.setPlaceholder("Type subject");
	}

	private void configureFileField() {
		fileField.setWidthFull();
		fileField.setPadding(false);

		Upload upload = fileField.getUpload();
		upload.setAutoUpload(true);
		upload.setMaxFiles(1);
		upload.setDropLabel(new Span("Upload file (JPEG/PNG/PDF, max size: 512 KB)"));
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf");
		upload.setMaxFileSize(1024 * 512);
	}

	public void setCircular(Circular circular) {
		if (circular.getId() > 0) {
			createdDiv.setVisible(true);
			createdDiv.setText("Created " + DateUtil.ddMMMyyyyhhmm(circular.getCreatedAt()));

			// existing media file
			byte[] fileBytes = circular.getFileBytes();
			String fileName = circular.getFileName();
			String fileMime = circular.getFileMime();

			if (fileName != null && fileMime != null && fileBytes != null) {
				StreamResource resource = new StreamResource(circular.getFileName(),
						() -> new ByteArrayInputStream(fileBytes));
				fileField.setResource(resource, circular.getFileMime(), circular.getFileName());
				removeFileButton.setVisible(true);
			} else {
				fileField.setResource(null, null, null);
				removeFileButton.setVisible(false);
			}

		} else {
			createdDiv.setVisible(false);
			removeFileButton.setVisible(false);
			fileField.setResource(null, null, null);
		}

		binder.setBean(circular);

		// for logged in user
		// enable save button is authUser is manager or is the creator of the record,
		// else diable

		if (authUser != null) {
			boolean enabled = false;

			if (authUser.getRole() == Role.Manager) {
				enabled = true;
			} else {
				if (circular.getCreatedBy() != 0) {
					if (circular.getCreatedBy() == authUser.getId()) {
						enabled = true;
					} else {
						enabled = false;
					}
				} else {
					enabled = true;
				}
			}

			subjectField.setReadOnly(!enabled);
			fileField.setReadOnly(!enabled);
			saveButton.setEnabled(enabled);

		}
	}

	private void buildButtonBar(HorizontalLayout root) {

		removeFileButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		removeFileButton.addClickListener(e -> {
			fileField.setResource(null, null, null);
			removeFileButton.setVisible(false);
		});

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				String fileName = fileField.getFileName();
				String fileMime = fileField.getFileMime();
				byte[] fileBytes = fileField.getFileBytes();

				binder.getBean().setFileName(fileName);
				binder.getBean().setFileMime(fileMime);
				binder.getBean().setFileBytes(fileBytes);

				fireEvent(new SaveEvent(this, binder.getBean()));
			}
		});

		cancelButton.addClickListener(e -> {
			fireEvent(new CancelEvent(this, binder.getBean()));
		});

		Span blank = new Span();

		root.add(saveButton, blank, cancelButton);
		root.expand(blank);
	}

	public static abstract class CircularEditorFormEvent extends ComponentEvent<CircularEditorForm> {
		private Circular circular;

		protected CircularEditorFormEvent(CircularEditorForm source, Circular circular) {

			super(source, false);
			this.circular = circular;
		}

		public Circular getCircular() {
			return circular;
		}
	}

	public static class SaveEvent extends CircularEditorFormEvent {
		SaveEvent(CircularEditorForm source, Circular resource) {
			super(source, resource);
		}
	}

	public static class CancelEvent extends CircularEditorFormEvent {
		CancelEvent(CircularEditorForm source, Circular circular) {
			super(source, circular);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {

		return getEventBus().addListener(eventType, listener);
	}

}
