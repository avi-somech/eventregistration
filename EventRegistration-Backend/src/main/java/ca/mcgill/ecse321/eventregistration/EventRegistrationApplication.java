package ca.mcgill.ecse321.eventregistration;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.eventregistration.controller.EventRegistrationRestController;
import ca.mcgill.ecse321.eventregistration.dto.PersonDto;
import ca.mcgill.ecse321.eventregistration.models.Person;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class EventRegistrationApplication {
	
	@Autowired
	public EventRegistrationService service;
	
	@Autowired
	public EventRegistrationRestController controller;


  public static void main(String[] args) {
    SpringApplication.run(EventRegistrationApplication.class, args);
  }

  @RequestMapping("/")
  public String greeting(){
    
    return "Hello world! this is very very cool lets try to deploy automatically";

  }
  
  @RequestMapping("/abcpersons")
  public List<Person> getPersons(){
    return  service.getAllPersons();
  }
  
//  @PostMapping(value = { "/titles/create/{name}", "/titles/create/{name}/" })
//	public TitleDto createTitle(@PathVariable("name") String name, @RequestParam String description,
//			@RequestParam String genre, @RequestParam String isAvailable, @RequestParam String titleType)
//			throws IllegalArgumentException {
//
//		Library library = getLibrary();
//
//		Title title = service.createTitle(name, description, genre, Boolean.parseBoolean(isAvailable),
//				parseTitleType(titleType), library);
//
//		return convertToDto(title);
//	}
  
  @RequestMapping(value = { "/abcpersons/{name}/{description}" })
	public Person createPersonHere(@PathVariable("name") String name, @PathVariable String description) throws Exception {
	  if (name.equals("abe")) {
		  throw new Exception("You cannot add abe over here");
	  }
	  String hi = name+description;
		return service.createPerson(hi);
	}
  
  @RequestMapping(value = { "/abcpersons1/{name}", "/persons/{name}/" })
	public Person createPersonHere1(@PathVariable("name") String name) throws Exception {
	  if (name.equals("abe")) {
		  throw new Exception("You cannot add abe over here");
	  }
	  String hi = name;
		return service.createPerson(hi);
	}
  
  @RequestMapping("/controller/persons")
  public List<PersonDto> getPersonsDto(){
    return  controller.getAllPersons();
  }
  
  

  
  

}