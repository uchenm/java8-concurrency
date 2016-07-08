package com.javferna.packtpub.mastering.simpleserver.common;

import com.javferna.packtpub.mastering.simpleserver.wdi.data.WDIDAO;

/**
 * Class that implements the concurrent version of the Report command. 
 * Report: The format of this query is: r:codIndicator where codIndicator 
 * is the code of the indicator you want to report
 * @author author
 *
 */
public class ConcurrentReportCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentReportCommand (String [] command) {
		super(command);
	}
	
	@Override
	/**
	 * Method that executes the command
	 */
	public String execute() {
	
		WDIDAO dao=WDIDAO.getDAO();
		return dao.report(command[1]);
	}

}
