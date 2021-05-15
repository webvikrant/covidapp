package in.co.itlabs.util;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.services.ResourceService;

public class GuestResourceDataProvider extends AbstractBackEndDataProvider<Resource, Void> {
	private static final Logger logger = LoggerFactory.getLogger(GuestResourceDataProvider.class);
	private ResourceService resourceService;
	private ResourceFilterParams filterParams;

	public GuestResourceDataProvider(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public void setFilterParams(ResourceFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return resourceService.getResourcesCountForGuests(filterParams);
	}

	@Override
	protected Stream<Resource> fetchFromBackEnd(Query<Resource, Void> query) {
		logger.info("fetchFromBackEnd()...limit:  " + query.getLimit());
		logger.info("fetchFromBackEnd()...offset:  " + query.getOffset());
		return resourceService.getResourcesForGuests(query.getOffset(), query.getLimit(), filterParams).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Resource, Void> query) {
		logger.info("sizeInBackEnd()...limit:  " + query.getLimit());
		logger.info("sizeInBackEnd()...offset:  " + query.getOffset());
		return resourceService.getResourcesCountForGuests(filterParams);
	}

	@Override
	public Object getId(Resource item) {
		return item.getId();
	}

}
