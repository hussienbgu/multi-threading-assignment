package bgu.spl.mics.application.passiveObjects;
import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
	private String MissionName;
	private int M;
	private int MoneyPenny;
	private List<String> agentsSerialNumbers;
	private String gadgetName;
	private int timeIssued;
	private int QTime;
	private int timeCreated;
	private List<String> agentsNames;
	public Report(String MissionName, int M, int MoneyPenny, List<String> agentsSerialNumbers, List<String> agentsNames ,String gadgetName, int timeIssued, int QTime, int timeCreated){
		this.agentsNames=agentsNames;
		this.agentsSerialNumbers=agentsSerialNumbers;
		this.gadgetName=gadgetName;
		this.M=M;
		this.MoneyPenny=MoneyPenny;
		this.QTime=QTime;
		this.timeIssued=timeIssued;
		this.timeCreated=timeCreated;
		this.MissionName=MissionName;
	}
	/**
     * Retrieves the mission name.
     */
	public String getMissionName() {
		return this.MissionName;
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		if (missionName!=null)
			this.MissionName=missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {

		return this.M;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		this.M=m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {

		return this.MoneyPenny;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		this.MoneyPenny=moneypenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbers() {
		return this.agentsSerialNumbers;

	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbers(List<String> agentsSerialNumbers) {
		if(agentsSerialNumbers!=null)
	        this.agentsSerialNumbers=agentsSerialNumbers;
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		return this.agentsNames;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		if(agentsNames!=null)
		    this.agentsNames=agentsNames;
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		return this.gadgetName;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		if(gadgetName!=null)
			this.gadgetName=gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		return this.QTime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		this.QTime=qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		return this.timeIssued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		this.timeIssued=timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		return this.timeCreated;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		this.timeCreated=timeCreated;
	}
}
