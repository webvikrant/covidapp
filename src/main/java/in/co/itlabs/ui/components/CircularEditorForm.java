package in.co.itlabs.ui.components;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
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

	private TextArea subjectField;
	private FileField fileField;

	private Button saveButton;
	private Button cancelButton;

	private Binder<Circular> binder;

	// non-ui
	private AuthenticatedUser authUser;

	public CircularEditorForm() {

		authUser = VaadinSession.getCurrent().getAttribute(AuthenticatedUser.class);

		setAlignItems(Alignment.START);

		createdDiv = new Div();

		subjectField = new TextArea();
		configureSubjectField();

		fileField = new FileField();
		configureFileField();

		binder = new Binder<>(Circular.class);

		binder.forField(subjectField).asRequired("Subject can not be blank")
				.withValidator(address -> address.length() < 1000, "").bind("subject");

		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());

		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidthFull();
		buildButtonBar(buttonBar);

		add(createdDiv, subjectField, fileField, buttonBar);
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
		upload.setDropLabel(new Span("Upload a 1024 KB file (JPEG or PNG)"));
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf");
		upload.setMaxFileSize(1024 * 512);
	}

	public void setCircular(Circular circular) {
		if (circular.getId() > 0) {
			createdDiv.setVisible(true);
			createdDiv.setText("Uploaded " + DateUtil.humanize(circular.getCreatedAt()));

			// existing media file
			byte[] fileBytes = circular.getFileBytes();
			StreamResource resource = new StreamResource(circular.getFileName(),
					() -> new ByteArrayInputStream(fileBytes));
			fileField.setResource(resource, circular.getFileMime(), circular.getFileName());

		} else {
			createdDiv.setVisible(false);
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
			saveButton.setEnabled(enabled);
		}
	}

	private void buildButtonBar(HorizontalLayout root) {

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				String fileName = fileField.getFileName();
				String fileMime = fileField.getFileMime();
				byte[] fileBytes = fileField.getFileBytes();

				if (fileName != null && fileMime != null && fileBytes != null) {
					binder.getBean().setFileName(fileName);
					binder.getBean().setFileMime(fileMime);
					binder.getBean().setFileBytes(fileBytes);

					fireEvent(new SaveEvent(this, binder.getBean()));
				} else {
					Notification.show("Please upload a file", 3000, Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
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
