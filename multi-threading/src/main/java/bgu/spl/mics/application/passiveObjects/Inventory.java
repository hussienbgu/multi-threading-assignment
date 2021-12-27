package bgu.spl.mics.application.passiveObjects;
import org.json.simple.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static class Holder {
		static final Inventory INSTANCE = new Inventory();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return Holder.INSTANCE;
	}
	private Inventory(){
		gadgets=new LinkedList<String>();
	}


	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		if(inventory!=null) {
			for (int i = 0; i < inventory.length; i++) {
				if(inventory[i]!=null) {
					this.gadgets.add(i, inventory[i]);
				}
			}
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget) {
		synchronized (gadgets) {
			return gadgets.remove(gadget);
		}
	}
	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
		JSONArray inve = new JSONArray();
		for (int i = 0; i < this.gadgets.size(); i++) {
		inve.add(i,gadgets.get(i));
		}
		FileWriter file = new FileWriter(filename);
		try {

			file.write(inve.toJSONString());
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally {
			file.flush();
			file.close();
		}
	}
}

