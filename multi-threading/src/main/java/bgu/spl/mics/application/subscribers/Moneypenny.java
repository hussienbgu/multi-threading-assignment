package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int tick;
	private CountDownLatch latch;
	private Integer serialnumber;
	private static final Logger log=Logger.getLogger(Moneypenny.class.getName());
	public Moneypenny(int serialnumber,CountDownLatch latch) {
		super(("Moneypenny : "+ serialnumber));
		this.latch=latch;
		tick=1;

		this.serialnumber=serialnumber;
	}

	@Override
	protected void initialize() {
		log.log(Level.INFO,this.getName()+" is started");
		this.BroadcastTick();
		this.HandleEvents();
		latch.countDown();
	}
	/**
	 * a {@link TickBroadcast} message which indicates the time.
	 */
	private void BroadcastTick() {
		subscribeBroadcast(TickBroadcast.class, tick -> {
			if(tick.getTick()==-1) {
				terminate();
				log.log(Level.INFO,  "The MoneyPenny : " + this.getName() + " is Terminated ");
			}
			this.tick = tick.getTick();
		});
	}
	private void HandleEvents(){
		subscribeEvent(AgentsAvailableEvent.class, callback->{
			Squad squad=Squad.getInstance();
			List<String> SerialAgentnumber=callback.getSerialAgentsNumbers();
			Object[] o = new Object[3];
			if(squad.getAgents(callback.getSerialAgentsNumbers())){
				Future<Boolean> f = new Future<>();
				o[1] = this.serialnumber;// moneypenny Id
				o[2] =  squad.getAgentsNames(SerialAgentnumber);//Agents names
				o[0] = f;
				complete(callback,o);
					if ((Boolean)(((Future)o[0]).get())) {
						complete(callback,o);
						squad.sendAgents((List<String>) o[2], callback.getTime());
					}
					else {
							squad.releaseAgents(SerialAgentnumber);
							complete(callback, null);
						}
			}
			else {
				complete(callback,null);
			}
		});

	}

}
