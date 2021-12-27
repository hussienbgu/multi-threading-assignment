package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AgentsAvailableEvent implements Event<Object[]> {
    private List<String> serialAgentsNumbers;
    private int time;
    public AgentsAvailableEvent(List<String> serialAgentsnumbers,int time){
        this.serialAgentsNumbers=serialAgentsnumbers;
         this.time=time;
    }
    public List<String> getSerialAgentsNumbers() {
        return this.serialAgentsNumbers;
    }

    public int getTime() {
        return time;
    }
}
