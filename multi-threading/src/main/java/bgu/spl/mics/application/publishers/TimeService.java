package bgu.spl.mics.application.publishers;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int duration;
    private int speed;
    private CountDownLatch latch;
	private Timer timer;
	private int tick;
	public TimeService(int duration,CountDownLatch latch) {
		super("timer");
		this.duration=duration;
		this.speed=100;
		this.latch=latch;
		this.tick=1;
		timer=new Timer();
	}
	@Override
	protected void initialize() {
		this.run();

	}
	@Override
	public void run() {
		this.latch.countDown();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (tick <=duration) {
					while (latch.getCount() > 0);
					System.out.println(tick);
					getSimplePublisher().sendBroadcast(new TickBroadcast(tick));
					tick++;
				}
				else{
					getSimplePublisher().sendBroadcast(new TickBroadcast(-1));
					timer.cancel();
				}
			}
		},0,speed);
	}
}
