package com.javferna.packtpub.mastering.simpleserver.common;

import com.javferna.packtpub.mastering.simpleserver.wdi.data.WDIDAO;

/**
 * Class that implements the concurrent version of the Query Command. The format of 
 * this query is: q;codCountry;codIndicator;year where codCountry is the code of the country, 
 * codIndicator is the code of the indicator and the year is an optional parameter with the year 
 * you want to query
 * @author author
 *
 */
public class ConcurrentQueryCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentQueryCommand (String [] command) {
		super(command);
	}
	
	@Override
	/**
	 * Method that executes the query 
	 */
	public String execute() {

		WDIDAO dao=WDIDAO.getDAO();
		
		if (command.length==3) {
			return dao.query(command[1], command[2]);
		} else if (command.length==4) {
			try {
				return dao.query(command[1], command[2], Short.parseShort(command[3]));
			} catch (Exception e) {
				return "ERROR;Bad Command";
			}
		} else {
			return "ERROR;Bad Command";
		}
	} 

}
