package in.co.itlabs.util;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import in.co.itlabs.business.entities.Enquiry;
import in.co.itlabs.business.services.EnquiryService;

public class EnquiryDataProvider extends AbstractBackEndDataProvider<Enquiry, Void> {

	private EnquiryService enquiryService;
	private EnquiryFilterParams filterParams;

	public EnquiryDataProvider(EnquiryService enquiryService) {
		this.enquiryService = enquiryService;
	}

	public void setFilterParams(EnquiryFilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public int getCount() {
		return enquiryService.getEnquiriesCount(filterParams);
	}

	@Override
	protected Stream<Enquiry> fetchFromBackEnd(Query<Enquiry, Void> query) {
		return enquiryService.getEnquiries(query.getOffset(), query.getLimit(), filterParams).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Enquiry, Void> query) {
		return enquiryService.getEnquiriesCount(filterParams);
	}

	@Override
	public Object getId(Enquiry item) {
		return item.getId();
	}

}
