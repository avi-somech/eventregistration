package ca.mcgill.ecse321.eventregistration.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.models.Event;
import ca.mcgill.ecse321.eventregistration.models.Person;
import ca.mcgill.ecse321.eventregistration.models.Registration;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

	List<Registration> findByPerson(Person personName);

	boolean existsByPersonAndEvent(Person person, Event eventName);

	Registration findByPersonAndEvent(Person person, Event eventName);

}