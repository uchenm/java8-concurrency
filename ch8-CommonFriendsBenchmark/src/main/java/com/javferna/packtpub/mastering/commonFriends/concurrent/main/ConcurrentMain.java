package com.javferna.packtpub.mastering.commonFriends.concurrent.main;

import java.util.Date;
import java.util.List;

import com.javferna.packtpub.mastering.commonFriends.data.DataLoader;
import com.javferna.packtpub.mastering.commonFriends.data.Person;
import com.javferna.packtpub.mastering.commonFriends.data.PersonPair;

public class ConcurrentMain {

	public static void main(String[] args) {

		Date start, end;
		System.out.println("Concurrent Main Bidirectional - Test");
		List<Person> people=DataLoader.load("data","test.txt");
		start=new Date();
		List<PersonPair> peopleCommonContacts=ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
		end=new Date();
		peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+formatContacts(p.getContacts())));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

		System.out.println("Concurrent Main Bidirectional - Facebook");
		people=DataLoader.load("data","facebook_contacts.txt");
		start=new Date();
		peopleCommonContacts=ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
		end=new Date();
		//peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+getContacts(p.getContacts())));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

	}

	private static String formatContacts(List<String> contacts) {
		StringBuffer buffer=new StringBuffer();
		for (String contact: contacts) {
			buffer.append(contact+",");
		}
		return buffer.toString();
	}

}
