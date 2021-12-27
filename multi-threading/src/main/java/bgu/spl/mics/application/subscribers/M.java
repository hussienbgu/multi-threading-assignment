package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int tick;
	private int processTick;
	private CountDownLatch latch;
	private Diary diary;
	private static final Logger log=Logger.getLogger(Moneypenny.class.getName());
	public M(String name,CountDownLatch latch) {
		super(name);
		this.latch =latch;
		this.tick=0;
		this.processTick=0;
		diary=Diary.getInstance();
	}
	@Override
	public void initialize() {
		log.log(Level.INFO, "Manger "+this.getName() + " is started");
		this.BroadcastTick();
		this.handleEvents();
		latch.countDown();
	}
	/**
	 * handling {@Link MissionReceivedEvent}
	 * and sends {@Link CheckAvailabilityEvent}
	 */
	private void handleEvents() {
		subscribeEvent(MissionReceivedEvent.class, ev -> {
			log.log(Level.INFO, "mission " + ev.getMission().getMissionName() + " is handeld by m " + Integer.parseInt(this.getName()));
			this.diary.incrementTotal();
			this.processTick = tick;
			Future<Object[]> FMP = getSimplePublisher().sendEvent(new AgentsAvailableEvent(ev.getMission().getSerialAgentsNumbers(), ev.getMission().getDuration()));
			if (FMP != null && FMP.get() != null && FMP.get()[1] != null && FMP.get()[2] != null) {//if 1
				Future<Integer> FQ = getSimplePublisher().sendEvent(new GadgetAvailableEvent(ev.getMission().getGadget()));//if the gadget in the inventory as Integer equal the QTime
				if (FQ != null && FQ.get() != null) {//if 2
					int Qtime = FQ.get();
					int MP_Id = (Integer) FMP.get()[1];
					List<String> serialagentnames = (List<String>) FMP.get()[2];
					if (tick < ev.getMission().getTimeExpired()) {//if 3
						((Future) FMP.get()[0]).resolve(true);
						fillDiary(ev, MP_Id, Qtime, serialagentnames);
					}
					else {//else 3
						((Future) FMP.get()[0]).resolve(false);
						FMP.resolve(null);
						complete(ev, null);
					}
				} else {//else 2
					((Future) FMP.get()[0]).resolve(false);
					FMP.resolve(null);
					complete(ev, null);
				}
			}
			else {//else 1
				if(FMP.get()!=null) {
					((Future) FMP.get()[0]).resolve(false);
				}
				FMP.resolve(null);
				complete(ev, null);
			}
		});
	}
	private void fillDiary(MissionReceivedEvent ev,Integer MPID,Integer QTime,List<String> Names){
		String MissionName=ev.getMission().getMissionName();
		int mHandler=Integer.parseInt(this.getName());
		List<String> agentsSerialNumbers=ev.getMission().getSerialAgentsNumbers();//Agents serialnumber
		String GadgetName=ev.getMission().getGadget();//gadget
		int TimeIssued=processTick;
		Report toBeAdded=new Report(MissionName,mHandler,MPID,agentsSerialNumbers,Names,GadgetName,TimeIssued,QTime,this.tick);
		this.diary.addReport(toBeAdded);
		log.log(Level.INFO,  "The mission : "+ev.getMission().getMissionName() + " is completed at this tick : " + this.tick);
	}
	/**
	 * a {@link TickBroadcast} message which indicates the time.
	 */
	private void BroadcastTick() {
		subscribeBroadcast(TickBroadcast.class, tick -> {
			if (tick.getTick()==-1) {
				terminate();
				log.log(Level.INFO,  "The Manager : " + this.getName() + " is Terminated ");
			}
			this.tick = tick.getTick();
		});
	}
}