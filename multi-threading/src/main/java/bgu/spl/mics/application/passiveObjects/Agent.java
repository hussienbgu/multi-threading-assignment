package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
	private String name;
	private String SerialNumber;
	private boolean avaliable;
public Agent(String name,String SerialNumber){
	this.name=name;
	this.SerialNumber=SerialNumber;
	this.avaliable=true;
}
	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		if(serialNumber!=null)
		this.SerialNumber=serialNumber;
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {

		return this.SerialNumber;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		if (name!=null)
			this.name=name;
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		return this.avaliable;
	}

	/**
	 * Acquires an agent.
	 */
	public void acquire(){
		this.avaliable=false;
	}

	/**
	 * Releases an agent.
	 */
	public void release(){
		this.avaliable=true;
	}
}
