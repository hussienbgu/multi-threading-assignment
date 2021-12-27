package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
private Inventory inv;
private Integer Qtime;
private CountDownLatch latch;
private static final Logger log=Logger.getLogger(Q.class.getName());
	public Q(String name,CountDownLatch latch) {
		super(name);
		this.latch = latch;
		inv = Inventory.getInstance();
		this.Qtime= new Integer(1);
	}
	/**
	 * Retrieves the single instance of this class.
	 */


	@Override
	protected void initialize() {
		log.log(Level.INFO,"Inventory man "+ this.getName()+" is started");
		this.BroadcastTick();
		this.HandleEvent();
		latch.countDown();
	}


	/**
	 * a {@link TickBroadcast} message which indicates the time.
	 */
	private void BroadcastTick() {
		subscribeBroadcast(TickBroadcast.class, tick -> {
			if(tick.getTick()== -1) {
				terminate();
				log.log(Level.INFO,  "Q is Terminated ");
			}
			this.Qtime = tick.getTick();
		});
	}
	private void HandleEvent(){
		subscribeEvent(GadgetAvailableEvent.class,callback->{
			String _gadgetname=callback.getGadgetname();
			if(inv.getItem(_gadgetname)){
				complete(callback,Qtime);
			}
			else {
				complete(callback,null);
			}
		});
	}




}
