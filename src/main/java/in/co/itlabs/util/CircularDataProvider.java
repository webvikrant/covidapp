package in.co.itlabs.util;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.Circular;
import in.co.itlabs.business.services.CircularService;

public class CircularDataProvider extends AbstractBackEndDataProvider<Circular, Void> {

	private CircularService circularService;
	private CircularFilterParams filterParams;

	public CircularDataProvider(CircularService circularService) {
		this.circularService = circularService;
	}

	public void setFilterParams(CircularFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return circularService.getCircularsCount(filterParams);
	}

	@Override
	protected Stream<Circular> fetchFromBackEnd(Query<Circular, Void> query) {
		return circularService.getCirculars(query.getOffset(), query.getLimit(), filterParams).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Circular, Void> query) {
		return circularService.getCircularsCount(filterParams);
	}

	@Override
	public Object getId(Circular item) {
		return item.getId();
	}

}
