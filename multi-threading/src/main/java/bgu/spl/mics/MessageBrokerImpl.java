package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl instance = null;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> list;
	private ConcurrentHashMap<Class<? extends Event>,LinkedBlockingQueue<Subscriber>> EventsTypesList;
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<Subscriber>> BroadcastTypesList;
	private ConcurrentHashMap<Event, Future> eventFutureObject;
	protected MessageBrokerImpl() {
		list = new ConcurrentHashMap<>();
		EventsTypesList = new ConcurrentHashMap<>();
		BroadcastTypesList = new ConcurrentHashMap<>();
		eventFutureObject = new ConcurrentHashMap<>();
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type,Subscriber m) {
		synchronized(type) {
			if (!EventsTypesList.containsKey(type)) {
				EventsTypesList.put(type, new LinkedBlockingQueue<>());
				try {
					EventsTypesList.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					EventsTypesList.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized(type) {
			if (!BroadcastTypesList.containsKey(type)) {
				BroadcastTypesList.put(type, new LinkedBlockingQueue<>());
				try {
					BroadcastTypesList.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					BroadcastTypesList.get(type).put(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}
	}
	@Override
	public <T> void complete(Event<T> e, T result) {
			eventFutureObject.get(e).resolve(result);
	}
	@Override
	public void sendBroadcast(Broadcast b) {
		if (BroadcastTypesList.containsKey(b.getClass())) {
			synchronized (BroadcastTypesList.get(b.getClass())) {
				LinkedBlockingQueue<Subscriber> a = BroadcastTypesList.get(b.getClass());
				for (Subscriber s : a) {
					if (list != null && list.containsKey(s)) {
						try {
							list.get(s).put(b);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> futureObject = new Future<>();
		eventFutureObject.put(e, futureObject);
		if (EventsTypesList.containsKey(e.getClass())) {
			synchronized (e.getClass()) {
				if (EventsTypesList.get(e.getClass()).size() != 0) {
					try {
						Subscriber s = EventsTypesList.get(e.getClass()).poll();
						if (s != null)
							EventsTypesList.get(e.getClass()).put(s);
						list.get(s).put(e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					return eventFutureObject.get(e);
				}
				return null;
			}
		}
			return null;
	}
	@Override
	public void register(Subscriber m) {
			list.putIfAbsent(m, new LinkedBlockingQueue<>());
	}
	@Override
	public void unregister(Subscriber m) {
		if (list.containsKey(m)) {
			for (int i = 0; i < list.get(m).size(); i++) {
				complete((Event) list.get(m).poll(), null);
			}
			list.remove(m);
			for (Class<? extends Event> x : EventsTypesList.keySet()) {

				if (EventsTypesList.get(x).contains(m)) {
					EventsTypesList.get(x).remove(m);
					if (EventsTypesList.get(x).isEmpty()) {

					}
				}

			}
			for (Class<? extends Broadcast> x : BroadcastTypesList.keySet()) {

				if (BroadcastTypesList.get(x).contains(m)) {
					BroadcastTypesList.get(x).remove(m);
					if (BroadcastTypesList.get(x).isEmpty()) {

					}
				}

			}
		}
	}
	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		Message f = null;
		if (list.containsKey(m))
			f = list.get(m).take();
		else
			throw new IllegalStateException("The Subscriber " + m.getName() + " is unregistered");
		return f;
	}
	public static  MessageBrokerImpl getInstance() {
		if (instance == null) {
			instance = new MessageBrokerImpl();
		}
		return instance;

	}
}