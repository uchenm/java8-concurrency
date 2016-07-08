package com.javferna.packtpub.mastering.commonFriends.serial.main;

import java.util.Date;
import java.util.List;

import com.javferna.packtpub.mastering.commonFriends.data.DataLoader;
import com.javferna.packtpub.mastering.commonFriends.data.Person;
import com.javferna.packtpub.mastering.commonFriends.data.PersonPair;

public class SerialMain {

	public static void main(String[] args) {

		System.out.println("Serial Main Bidirectional - Test");
		Date start, end;
		List<Person> people=DataLoader.load("data","test.txt");
		start=new Date();
		List<PersonPair> peopleCommonContacts=SerialSocialNetwork.bidirectionalCommonContacts(people);
		end=new Date();
		peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+formatContacts(p.getContacts())));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

		System.out.println("Serial Main Bidirectional - Facebook");
		people=DataLoader.load("data","facebook_contacts.txt");
		start=new Date();
		peopleCommonContacts=SerialSocialNetwork.bidirectionalCommonContacts(people);
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
