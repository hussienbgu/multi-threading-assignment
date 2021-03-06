package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	private String gadget;
	private List<String> serialAgentsNumbers;
	private int timeIssued;
	private int timeExpired;
	private int duration;
	private String MissionName;
	public MissionInfo(List<String> serialAgentsNumbers,int duration,String gadget,String missionName,int timeExpired,int timeIssued){
		this.serialAgentsNumbers=serialAgentsNumbers;
		this.duration=duration;
		this.gadget=gadget;
		this.MissionName=missionName;
		this.timeExpired=timeExpired;
		this.timeIssued=timeIssued;
	}


	/**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
        	this.MissionName=missionName;
    }

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		return this.MissionName;
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {

    	this.serialAgentsNumbers=serialAgentsNumbers;
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		return this.serialAgentsNumbers;
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
        this.gadget=gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		return this.gadget;
	}

    /**
     * Sets the time the mission was issued in time ticks.
     */
    public void setTimeIssued(int timeIssued) {
        this.timeIssued=timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in time ticks.
     */
	public int getTimeIssued() {
		return this.timeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
        this.timeExpired=timeExpired;
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return this.timeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        this.duration=duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return this.duration;
	}
}
