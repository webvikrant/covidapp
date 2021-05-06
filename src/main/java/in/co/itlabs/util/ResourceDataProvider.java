package in.co.itlabs.util;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;

public class ResourceDataProvider extends AbstractBackEndDataProvider<Resource, Void> {

	private ResourceService resourceService;
	private ResourceFilterParams filterParams;
	private boolean guest;

	public ResourceDataProvider(ResourceService resourceService, boolean guest) {
		this.resourceService = resourceService;
		this.guest = guest;
	}

	public void setFilterParams(ResourceFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return resourceService.getResourcesCount(filterParams);
	}

	@Override
	protected Stream<Resource> fetchFromBackEnd(Query<Resource, Void> query) {
		return resourceService.getResources(query.getOffset(), query.getLimit(), filterParams, guest).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Resource, Void> query) {
		return resourceService.getResourcesCount(filterParams);
	}

	@Override
	public Object getId(Resource item) {
		return item.getId();
	}

}
