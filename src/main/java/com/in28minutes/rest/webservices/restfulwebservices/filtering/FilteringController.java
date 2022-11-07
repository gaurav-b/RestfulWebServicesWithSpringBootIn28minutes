package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {

	@GetMapping("/filtering")
	public MappingJacksonValue filtering() {
		SomeBean someBean = new SomeBean("value1","value2","value3");

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field3");
		MappingJacksonValue mappingJacksonValue = configuringFilter(someBean, filter, "SomeBeanFilter");

		return mappingJacksonValue;
	}

	@GetMapping("/filtering-list")
	public MappingJacksonValue filteringList() {
		List<SomeBean> someBeanList = Arrays.asList(
				new SomeBean("value1","value2","value3"),
				new SomeBean("value4","value5","value6")
				);

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");
		MappingJacksonValue mappingJacksonValue = configuringFilter(someBeanList, filter, "SomeBeanFilter");

		return mappingJacksonValue;
	}

	@GetMapping("/filtering-list1")
	public MappingJacksonValue filteringList1() {
		List<SomeBean> someBeanList = Arrays.asList(
				new SomeBean("value1","value2","value3"),
				new SomeBean("value4","value5","value6")
				);

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
		MappingJacksonValue mappingJacksonValue = configuringFilter(someBeanList, filter, "SomeBeanFilter");

		return mappingJacksonValue;
	}
	
	private MappingJacksonValue configuringFilter(SomeBean someBean, SimpleBeanPropertyFilter filter, String filterId) {
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
		return configuringFilter(filter, filterId, mappingJacksonValue);
	}

	private MappingJacksonValue configuringFilter(List<SomeBean> someBeanList, SimpleBeanPropertyFilter filter, String filterId) {
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBeanList);
		return configuringFilter(filter, filterId, mappingJacksonValue);
	}
	
	private MappingJacksonValue configuringFilter(SimpleBeanPropertyFilter filter, String filterId,
			MappingJacksonValue mappingJacksonValue) {
		FilterProvider filters = new SimpleFilterProvider().addFilter(filterId, filter);
		mappingJacksonValue.setFilters(filters);
		return mappingJacksonValue;
	}
	
}
