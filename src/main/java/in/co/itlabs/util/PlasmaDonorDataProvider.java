package in.co.itlabs.util;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.services.ResourceService;

public class PlasmaDonorDataProvider extends AbstractBackEndDataProvider<PlasmaDonor, Void> {

	private ResourceService resourceService;
	private PlasmaDonorFilterParams filterParams;

	public PlasmaDonorDataProvider(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public void setFilterParams(PlasmaDonorFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return resourceService.getPlasmaDonorsCount(filterParams);
	}

	@Override
	protected Stream<PlasmaDonor> fetchFromBackEnd(Query<PlasmaDonor, Void> query) {
		return resourceService.getPlasmaDonors(query.getOffset(), query.getLimit(), filterParams).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<PlasmaDonor, Void> query) {
		return resourceService.getPlasmaDonorsCount(filterParams);
	}

	@Override
	public Object getId(PlasmaDonor item) {
		return item.getId();
	}

}
