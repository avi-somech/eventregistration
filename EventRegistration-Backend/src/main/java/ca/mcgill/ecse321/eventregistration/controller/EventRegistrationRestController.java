package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.eventregistration.dto.EventDto;
import ca.mcgill.ecse321.eventregistration.dto.PersonDto;
import ca.mcgill.ecse321.eventregistration.dto.RegistrationDto;
import ca.mcgill.ecse321.eventregistration.models.Event;
import ca.mcgill.ecse321.eventregistration.models.Person;
import ca.mcgill.ecse321.eventregistration.models.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
//import ca.mcgill.ecse321.eventregistration.Dto;


@CrossOrigin(origins = "*")
//denotes this as a cross origin REST API
//cross origin allows you to share info regardless of where teh request for the information is coming from
//you can then choose which request to let through but thats beyond this course
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;
	
	//HTTP - hyperlink text transfer protocol has 2 main methods for transfering info:
	//1. get - retrieve data
	//2. post - send data
	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		return service.getAllPersons().stream().map(p -> convertToDto(p)).collect(Collectors.toList());
		//we call the controller method getAllPersons
		//stream object that allows us to map the returned ppl and converts them to a Dto
	}

	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		//name variable is accesed with the @PathVariable JPA tag
		//throws IllegalArgumentException if it is not a string
		Person person = service.createPerson(name);
		return convertToDto(person);
	}
	
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
	@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
	@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
	//special way of dealing with date times to be made to strings properly within http packets
	//this is just following the formats
	//Requestparam are stored in the packet itself
	throws IllegalArgumentException {
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
		@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createEventDtosForPerson(p);
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(),e.getDate(),e.getStartTime(),e.getEndTime());
		return eventDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		//you should do error handling here because you don't want the program to crash
		//every time the person does not exist
		//well talk about this when we talk about unit testing
		//we name all the convertToDto functions with the same name
		//we can do this because they all have different types
		//if the type does not match then it will fail when trying to send to database
		//so there is more checking to do
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEvents(createEventDtosForPerson(p));
		return personDto;
	}

	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		//Registration depends on person and event existing already (association of dependence on those 2 classes)
		//so the Dto will have those classes
		//so you need to make the Dto for event and person before you can make the registration one
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private Person convertToDomainObject(PersonDto pDto) {
		//converts from Dto to Dao
		//we iterate through all persons because we want to know if it does not exist in the database
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	private List<EventDto> createEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}
	//these are just some helper methods to help with Dto and Dao interactions
	//you can make as many as you want

}