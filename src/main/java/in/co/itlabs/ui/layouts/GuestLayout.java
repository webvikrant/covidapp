package in.co.itlabs.ui.layouts;

import java.util.Objects;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

@CssImport("./styles/shared-styles.css")
public class GuestLayout extends VerticalLayout implements RouterLayout {

	private VerticalLayout content;

	public GuestLayout() {
		addClassName("guest-layout");

		setHeightFull();

		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		content = new VerticalLayout();
		content.addClassName("content");
		content.getStyle().set("margin", "auto");
		content.setWidth("1000px");

		add(content);
	}

	@Override
	public void removeRouterLayoutContent(HasElement oldContent) {
		// TODO Auto-generated method stub
		content.getElement().removeAllChildren();
	}

	@Override
	public void showRouterLayoutContent(HasElement newContent) {
		// TODO Auto-generated method stub
		if (newContent != null) {
			content.getElement().appendChild(Objects.requireNonNull(newContent.getElement()));
		}
	}

}
