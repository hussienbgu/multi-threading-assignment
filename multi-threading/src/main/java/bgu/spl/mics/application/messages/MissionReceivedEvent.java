package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {

private MissionInfo mission;
public MissionReceivedEvent(MissionInfo missionInfo){

    this.mission=missionInfo;
}
    public MissionInfo getMission(){
    return this.mission;
    }

}
