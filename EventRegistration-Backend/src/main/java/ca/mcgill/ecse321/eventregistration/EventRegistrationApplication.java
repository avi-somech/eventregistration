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
	private EventRegistrationRestController controller;
	
	@Autowired
	public EventRegistrationService service;


  public static void main(String[] args) {
    SpringApplication.run(EventRegistrationApplication.class, args);
  }

  @RequestMapping("/")
  public String greeting(){
    List<Person> avi = service.getAllPersons();
    
    return "Hello world! this is very very cool lets try to deploy automatically";

  }
  
  @RequestMapping("/abcpersons")
  public List<Person> getPersons(){
    return  service.getAllPersons();
  }
  
  @RequestMapping(value = { "/abcpersons/{name}", "/persons/{name}/" })
	public Person createPersonHere(@PathVariable("name") String name) throws IllegalArgumentException {
		return service.createPerson(name);
	}
  
  

  
  

}