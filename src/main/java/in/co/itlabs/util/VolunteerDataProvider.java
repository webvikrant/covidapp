package in.co.itlabs.util;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.Volunteer;
import in.co.itlabs.business.services.VolunteerService;

public class VolunteerDataProvider extends AbstractBackEndDataProvider<Volunteer, Void> {

	private VolunteerService enquiryService;
	private VolunteerFilterParams filterParams;

	public VolunteerDataProvider(VolunteerService enquiryService) {
		this.enquiryService = enquiryService;
	}

	public void setFilterParams(VolunteerFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return enquiryService.getVolunteersCount(filterParams);
	}

	@Override
	protected Stream<Volunteer> fetchFromBackEnd(Query<Volunteer, Void> query) {
		return enquiryService.getVolunteers(query.getOffset(), query.getLimit(), filterParams).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Volunteer, Void> query) {
		return enquiryService.getVolunteersCount(filterParams);
	}

	@Override
	public Object getId(Volunteer item) {
		return item.getId();
	}

}
