package com.javferna.packtpub.mastering.commonFriends.concurrent.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.javferna.packtpub.mastering.commonFriends.data.Person;
import com.javferna.packtpub.mastering.commonFriends.data.PersonPair;

public class CommonPersonMapper implements Function<Person, List<PersonPair>> {

	@Override
	public List<PersonPair> apply(Person person) {
		
		List<PersonPair> ret=new ArrayList<>();
		
		List<String> contacts=person.getContacts();
		Collections.sort(contacts);
		
		for (String contact : contacts) {
			PersonPair personExt=new PersonPair();
			if (person.getId().compareTo(contact) < 0) {
				personExt.setId(person.getId());
				personExt.setOtherId(contact);
			} else {
				personExt.setId(contact);
				personExt.setOtherId(person.getId());
			}
			personExt.setContacts(contacts);
			ret.add(personExt);
		}
		
		return ret;
	}

}
