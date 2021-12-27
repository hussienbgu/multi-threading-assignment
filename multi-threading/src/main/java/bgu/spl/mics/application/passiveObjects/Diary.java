package bgu.spl.mics.application.passiveObjects;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;


/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private List<Report> reports;
	private int total;
	public Diary(){
		reports=new LinkedList<>();
		this.total=0;
	}
	private static class Holder {
		static final Diary INSTANCE = new Diary();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return Holder.INSTANCE;
	}

	public List<Report> getReports() {
		return this.reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
			this.reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
		JSONObject diary = new JSONObject();
		JSONArray reports = new JSONArray();
		for (int i = 0; i < this.getReports().size(); i++) {
			Report x=this.getReports().get(i);
			JSONObject report=new JSONObject();
			report.put("m",x.getM());
			report.put("moneypenny",x.getMoneypenny());
			report.put("agentsSerialNumbers",x.getAgentsSerialNumbers());
			report.put("agentsNames",x.getAgentsNames());
			report.put("gadgetName",x.getGadgetName());
			report.put("timeCreated",x.getTimeCreated());
			report.put("timeIssued",x.getTimeIssued());
			report.put("qTime",x.getQTime());
			report.put("missionName",x.getMissionName());
			reports.add(i,report);
		}

		diary.put("reports", reports);
		diary.put("total", total);
		FileWriter file = new FileWriter(filename);
		try {

			file.write(diary.toJSONString());
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally {
			file.flush();
			file.close();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total;
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		total++;
	}
}
