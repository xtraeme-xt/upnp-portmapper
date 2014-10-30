package org.chris.portmapper;

/**
 * @author chris
 * @version $Id: PortMapperStarter.java 126 2013-08-04 15:18:04Z christoph $
 */
public class PortMapperStarter {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final PortMapperCli cli = new PortMapperCli();
		cli.start(args);
	}
}
