package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int tick;
	private CountDownLatch latch;
	private static final Logger log = Logger.getLogger(Intelligence.class.getName());
	private Map<Integer, LinkedList<MissionInfo>> missionInfolist;
	public Intelligence(String name, CountDownLatch latch, List<MissionInfo> listofmissions) {
		super(name);
		this.tick=1;
		this.latch=latch;
		missionInfolist= new HashMap<>();
		for (int i=0;i<listofmissions.size();i++){
			if(missionInfolist.containsKey(listofmissions.get(i).getTimeIssued())){
				missionInfolist.get(listofmissions.get(i).getTimeIssued()).addLast(listofmissions.get(i));
			}
			else {
				  missionInfolist.put(listofmissions.get(i).getTimeIssued(), new LinkedList<MissionInfo>());
				  missionInfolist.get(listofmissions.get(i).getTimeIssued()).addLast(listofmissions.get(i));
			}
		}
	}
	@Override
	protected void initialize() {
		log.log(Level.INFO, "Intelligence : " + this.getName() + " is started");
		this.BrodcastTick();
		latch.countDown();
	}
	private void BrodcastTick(){
		subscribeBroadcast(TickBroadcast.class,tick->{
			if(tick.getTick()==-1) {
				terminate();
				log.log(Level.INFO,  "The Intelligence : " + this.getName() + " is Terminated ");
			}
			this.tick=tick.getTick();
			if(missionInfolist.containsKey(this.tick)){
				LinkedList<MissionInfo> missionInfoLinkedList=missionInfolist.get(this.tick);
				for (int i = 0; i < missionInfoLinkedList.size(); i++) {
					MissionInfo Missiontobe_Sent=missionInfoLinkedList.get(i);
					getSimplePublisher().sendEvent(new MissionReceivedEvent(Missiontobe_Sent));
				}
			}
		});
	}
}
