package edu.iastate.sdmay1809.shared.InstanceTracker;

public class InvalidInstanceFormatException extends Exception {
	/**
	 * Verion UID for Serializability
	 */
	private static final long serialVersionUID = -1088681006747789178L;

	/**
	 * Default constructor
	 */
	public InvalidInstanceFormatException() {
		super();
	}
	
	/**
	 * Message Constructor
	 * @param message a message for this exception
	 */
	public InvalidInstanceFormatException(String message) {
		super(message);
	}
}
