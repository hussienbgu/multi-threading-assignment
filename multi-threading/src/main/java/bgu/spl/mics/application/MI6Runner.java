package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    private static final Logger log = Logger.getLogger(TimeService.class.getName());
    public static void main(String[] args) {
        JsonParser parser = new JsonParser();
        log.log(Level.INFO, "start");
        try {
            Object obj = (Object) parser.parse(new FileReader(args[0]));
            JsonObject main = (JsonObject) obj;
            //--------------------------------------------------------------------------------------------------------//
                                       //Read From The Json File//
            JsonArray inventory = (JsonArray) main.get("inventory");//inventory array
            JsonArray squad = (JsonArray) main.get("squad");//squad array
            JsonObject services = main.get("services").getAsJsonObject();//services
            JsonArray Intelligence = (JsonArray) services.get("intelligence");//intelligence in services
            int numofMangers = (services.get("M").getAsInt());// num of mangers
            int numofMoneypenny = (services.get("Moneypenny").getAsInt());//num of moneypennys
            int numofIntelligences = Intelligence.size();//num of Intelligence
            int Qman=1;
            int time = services.get("time").getAsInt();// time
            //------------------------>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>-----------------------------------------//
                                      //Initializing Threads,Inventory,Squad
            //------------------------>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>-----------------------------------------//
            int counter = numofMoneypenny + numofMangers + numofIntelligences + Qman;
            CountDownLatch latch = new CountDownLatch(counter);
            initializingInventory(inventory);
            initializingSquad(squad);
            InitMangers(numofMangers,latch,time);
            InitMoneyPenny(numofMoneypenny,latch);
            InitIntelligences(numofIntelligences,Intelligence,latch);
            InitQ(latch);
            InitTimeservice(time,latch);
            while (Thread.activeCount()>2){}
            log.log(Level.INFO,"Waiting Until All Threads terminate then creating out put files");
            Diary diary=Diary.getInstance();
            diary.printToFile(args[2]);
            Inventory inventor=Inventory.getInstance();
            inventor.printToFile(args[1]);
            log.log(Level.INFO,"MI6");


            //--------------------------------------------------------------------------------------------------------//

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void initializingInventory(JsonArray inventory) {
        Inventory invgadegts = Inventory.getInstance();
        String[] GadegtsList = new String [inventory.size()];
        for (int i = 0; i < inventory.size(); i++) {
            String gadget = inventory.get(i).getAsString();
            GadegtsList[i] = gadget;
        }
        invgadegts.load(GadegtsList);
    }
    private static void initializingSquad(JsonArray squad){
    Squad s=Squad.getInstance();
    Agent[] agents=new Agent[squad.size()];
    for(int i=0;i<squad.size();i++){
        JsonObject index = (JsonObject)squad.get(i);
        String name=index.get("name").getAsString();// the name of the agent
        String serialnumber=index.get("serialNumber").getAsString();
        Agent newagent=new Agent(name,serialnumber);
        agents[i]=newagent;
    }
    s.load(agents);
    }
    private static List<MissionInfo> MissionsForEachIntelligence(JsonArray missions){
        List<MissionInfo> ListOfMissions=new LinkedList<MissionInfo>();
            for (int a=0; a<missions.size(); a++){
                JsonObject missionInfo =missions.get(a).getAsJsonObject();
                int duration = missionInfo.get("duration").getAsInt();
                String gadget= missionInfo.get("gadget").getAsString();
                String name= missionInfo.get("name").getAsString();
                int timeExpired=missionInfo.get("timeExpired").getAsInt();
                int timeIssued=missionInfo.get("timeIssued").getAsInt();
                JsonArray Agents=missionInfo.get("serialAgentsNumbers").getAsJsonArray();
                List<String> serialAgentsNumbers= new ArrayList<>();
                for (int b=0; b<Agents.size(); b++){
                    serialAgentsNumbers.add(Agents.get(b).getAsString());
                }
                serialAgentsNumbers = serialAgentsNumbers.stream().sorted().collect(Collectors.toList());
                ListOfMissions.add(new MissionInfo(serialAgentsNumbers,duration,gadget,name,timeExpired,timeIssued));
        }
            return ListOfMissions;
    }
    private static void InitTimeservice(int Time,CountDownLatch latch){// 1
        TimeService timeService = new TimeService(Time, latch);// Time Service publisher
        Thread t = new Thread(timeService);
        t.start();
    }
    private static void InitMangers(int Num_of_Mangers, CountDownLatch latch,int time){
        M[] mangersarray = new M[Num_of_Mangers];// Array for all the mangers
        for (int i = 1; i <= Num_of_Mangers; i++) {
            mangersarray[i - 1] = new M(String.valueOf(i), latch);
        }
        for (M m : mangersarray) {
            Thread manger = new Thread(m);
            manger.start();
        }
    }
    private static void InitMoneyPenny(int Num_of_MoneyPenny,CountDownLatch latch){
        Moneypenny[] monneypennyarray = new Moneypenny[Num_of_MoneyPenny];//Array for the moneypenny
        for (int i = 1; i <= Num_of_MoneyPenny; i++) {
            monneypennyarray[i - 1] = new Moneypenny(i, latch);
        }

        for (Moneypenny mo : monneypennyarray) {
            Thread mon = new Thread(mo);
            mon.start();
        }
    }
    private static void InitIntelligences(int Num_of_Intelligences,JsonArray Intelligence,CountDownLatch latch){
        Intelligence[] intelligencesarray = new Intelligence[Num_of_Intelligences];//Array for all the intelligence
        for (int i = 0; i < Num_of_Intelligences; i++) {//needs to be modified
            JsonArray MissionforThis = Intelligence.get(i).getAsJsonObject().get("missions").getAsJsonArray();
            intelligencesarray[i] = new Intelligence(String.valueOf(i + 1), latch, MissionsForEachIntelligence(MissionforThis));
        }
        for (Intelligence in : intelligencesarray) {
            Thread i = new Thread(in);
            i.start();
        }
    }

    private static void InitQ(CountDownLatch latch){
        Thread Q = new Thread(new Q("Q", latch));
        Q.start();
    }

}

