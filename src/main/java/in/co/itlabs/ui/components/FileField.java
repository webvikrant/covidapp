package in.co.itlabs.ui.components;

import java.io.ByteArrayInputStream;

import java.io.IOException;

import com.google.common.io.ByteStreams;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

public class FileField extends VerticalLayout {
	private Image image;
	private MemoryBuffer buffer;
	private Upload upload;
	private byte[] fileBytes;
	private String fileMime;
	private String fileName;

	public FileField() {

		image = new Image();

		buffer = new MemoryBuffer();
		upload = new Upload(buffer);

		add(image, upload);
		configureImage();

		// listeners
		upload.addStartedListener(event -> {
			System.out.println("FileField...Started");
		});

		upload.addFinishedListener(event -> {
			System.out.println("FileField...Finished");
		});

		upload.addFailedListener(event -> {
			System.out.println("FileField...Failed");
			Notification.show(event.getReason().getMessage(), 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		});

		upload.addFileRejectedListener(event -> {
			System.out.println("FileField...Failed");
			Notification.show(event.getErrorMessage(), 5000, Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		});

		upload.addSucceededListener(event -> {
			System.out.println("FileField...Succeeded");
			try {
				fileBytes = ByteStreams.toByteArray(buffer.getInputStream());
				StreamResource resource = new StreamResource(buffer.getFileName(),
						() -> new ByteArrayInputStream(fileBytes));
				fileMime = event.getMIMEType();
				fileName = event.getFileName();
				if (fileMime.equalsIgnoreCase("image/jpeg") || fileMime.equalsIgnoreCase("image/png")) {
					image.setSrc(resource);
					image.setVisible(true);
				} else {
					image.setVisible(false);
				}

			} catch (IOException e) {
				Notification.show(e.getMessage(), 5000, Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
	}

	private void configureImage() {
		image.addClassName("photo");
		image.setWidthFull();
		image.setHeight("100px");
		image.getStyle().set("objectFit", "contain");

		image.setVisible(false);
	}

	public void setReadOnly(boolean readOnly) {
		upload.setVisible(!readOnly);
	}

	public void setResource(StreamResource resource, String mime, String name) {
		if (resource != null) {
			image.setVisible(true);
			image.setSrc(resource);
			if (name != null) {
				image.setAlt(name);
			}
		} else {
			fileName = null;
			fileMime = null;
			fileBytes = null;
			image.setVisible(false);
		}
	}

	public void setBase64(String base64, String mime, String name) {
		if (base64 != null && mime != null) {
			image.setSrc(String.format("data:" + mime + ";base64,%s", base64));
			if (name != null) {
				image.setAlt(name);
			}
		}
	}

	public Upload getUpload() {
		return upload;
	}

	public byte[] getFileBytes() {
		return fileBytes;
	}

	public String getFileMime() {
		return fileMime;
	}

	public String getFileName() {
		return fileName;
	}
}
