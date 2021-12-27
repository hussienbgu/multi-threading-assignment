package bgu.spl.mics.application.passiveObjects;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private HashMap<String, Agent> agentsSquad;
	private static class Holder {
		static final Squad INSTANCE = new Squad();
	}
	private Squad(){
		agentsSquad=new HashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
			for(int i=0 ; i<agents.length;i++){
				this.agentsSquad.put(agents[i].getSerialNumber(),agents[i]);
			}
	}
	/**
	 * Releases agents.
	 */
	public synchronized void releaseAgents(List<String> serials) {
			for (int i = 0; i < serials.size(); i++) {
				if (this.agentsSquad.containsKey(serials.get(i))) {
					this.agentsSquad.get(serials.get(i)).release();
				}
			}
			notify();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){//???????
			try {
				Thread.sleep(time*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
			for (int i = 0; i < serials.size(); i++) {
				if (!agentsSquad.containsKey(serials.get(i)))
					return false;
			}

			for (int i = 0; i < serials.size(); i++) {
				while (!agentsSquad.get(serials.get(i)).isAvailable()) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					agentsSquad.get(serials.get(i)).acquire();
				}
			}
			return true;
	}



    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public synchronized List<String> getAgentsNames(List<String> serials){
		List<String> AgentsNames=new LinkedList<>();
			for(int i=0;i<serials.size();i++){
				AgentsNames.add(serials.get(i));
			}
		return AgentsNames;
    }
}
