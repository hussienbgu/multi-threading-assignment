package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
public class GadgetAvailableEvent implements Event<Integer> {
private String gadgetname;
public GadgetAvailableEvent(String gadgetname){
    this.gadgetname=gadgetname;
}

    public String getGadgetname() {
        return gadgetname;
    }
}
