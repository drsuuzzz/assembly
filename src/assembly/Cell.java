package assembly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import assembly.AgentExtendCont.Loc;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.util.collections.IndexedIterable;

public class Cell extends DefaultContext<AgentExtendCont> {

	private Nucleus n;
	private Cytoplasm c;
	
	private List<AgentExtendCont> mAgents;
	private List<AgentExtendCont> addList;
	private List<AgentExtendCont> remList;
	
	private static DateFormat format = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss_z");
	
	private File countp;
	private double fileTick;
	private double moveTick;
	
	private int virions;
	private int VLP;

	public Cell() {
		super("Cell");
		n = new Nucleus();
		n.setCell(this);
		this.addSubContext(n);
		c = new Cytoplasm();
		c.setCell(this);
		this.addSubContext(c);
		
		fileTick = 0;
		moveTick = 0;
		virions = 0;
		
		mAgents = new ArrayList<AgentExtendCont>();
		addList = new ArrayList<AgentExtendCont>();
		remList = new ArrayList<AgentExtendCont>();
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount() <= 0 ? 1 : RepastEssentials.GetTickCount();
		double starteven=0;
		double startodd=0;
		if ((int)start%2 != 0) {
			starteven = start + 1.0f;
			startodd = start;
		} else {
			starteven = start;
			startodd = start + 1.0f;
		}
		//even schedule
		ScheduleParameters sparams = ScheduleParameters.createRepeating(starteven, 2);
		ScheduleParameters sparamsodd = ScheduleParameters.createRepeating(startodd,2);
		schedule.schedule(sparams,this,"moveAgents");
		schedule.schedule(sparamsodd,this,"fileWrite");
		
		String path = (String) RunEnvironment.getInstance().getParameters().getValue("filePath");
		String d = format.format(new Date());
		String fname = "count."+d+".txt";
		countp  = new File(path,fname);
		try {
			FileWriter fw = new FileWriter(countp, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("tick,Tag,tag,VP1,VP2,VP3,VP123,Genome,DNAPol,virions,VLP");
			fw.close();
		} catch (IOException e) {
			System.out.println("Something wrong with count file.");
			e.printStackTrace();
		}
	}
	
	public int getVirions() {
		return virions;
	}

	public void addVirions() {
		this.virions += 1;
	}
	
	public int getVLP() {
		return VLP;
	}
	
	public void addVLP() {
		this.VLP += 1;
	}

	public int getNoTag() {
		IndexedIterable<AgentExtendCont> i = n.getObjects(LgTAg.class);
		IndexedIterable<AgentExtendCont> j = c.getObjects(LgTAg.class);
		int ret = 0;
		if (i != null) {
			ret  = i.size();
		}
		if (j != null) {
			ret += j.size();
		}
		return ret;
	}
	
	public int getNotag() {
		IndexedIterable<AgentExtendCont> i = c.getObjects(SmTAg.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP1() {
		IndexedIterable<AgentExtendCont> i = c.getObjects(VP1.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP2() {
		IndexedIterable<AgentExtendCont> i = c.getObjects(VP2.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP3() {
		IndexedIterable<AgentExtendCont> i = c.getObjects(VP3.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP123() {
		IndexedIterable<AgentExtendCont> i = n.getObjects(VP123.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoGenome() {
		IndexedIterable<AgentExtendCont> i = n.getObjects(Genome.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoDNAPol() {
		IndexedIterable<AgentExtendCont> i = n.getObjects(DNAPol.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	//should only happen on an odd tick
	public void addToMoveList(AgentExtendCont aec) {
		mAgents.add(aec);
	}
	
	public void addToAddList(AgentExtendCont aec) {
		addList.add(aec);
	}
	
	public void addToRemList(AgentExtendCont aec) {
		remList.add(aec);
	}
	
	//odd tick
	public void fileWrite() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > fileTick) {
			try {
				FileWriter fw = new FileWriter(countp, true);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(tick+","+getNoTag()+","+getNotag()+","+this.getNoVP1()+","+this.getNoVP2()+","+this.getNoVP3()+","+this.getNoVP123()+
						","+this.getNoGenome()+","+this.getNoDNAPol()+","+virions+","+VLP);
				fw.close();
			} catch (IOException e) {
				System.out.println("Something wrong with count file.");
				e.printStackTrace();
			}
			fileTick = tick;
		}
	}
	
	//schedules methods
	//even tick
	public void moveAgents() {
		Iterator<AgentExtendCont> agents = mAgents.iterator();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount();
		if ((int)start%2 == 0) {
			start = start + 1.0f;
		}
		//schedule death rule in cytoplasm
		ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
		while (agents.hasNext()) {
			AgentExtendCont aec = agents.next();
			if (aec instanceof MRNA) {
				System.out.println("Moving mRNA"+((MRNA) aec).getMyid());
				n.remove(aec);
				c.add(aec);
				aec.setTheContext(c);
				aec.setSpace(c.getSpace());
				((MRNA) aec).setLocation(Loc.cytoplasm);
				aec.setDeath(schedule.schedule(sparams,aec,"death"));
				//remove the export rule;
				aec.removeAnAction(aec.getExport());
				aec.removeAnAction(aec.getSplice());
				aec.setExport(null);
				aec.setSplice(null);
				aec.setMoving(false);
			} else if (aec instanceof LgTAg) {
				c.remove(aec);
				n.add(aec);
				aec.setTheContext(n);
				aec.setSpace(n.getSpace());
				aec.setLocation(Loc.nucleus);
				aec.removeAnAction(aec.getExport());
				aec.setExport(null);
				aec.setMoving(false);
			} else if (aec instanceof VP123) {
				n.add(aec);
				aec.setTheContext(n);
				aec.setSpace(n.getSpace());
				aec.setLocation(Loc.nucleus);
				aec.setMove(schedule.schedule(sparams, aec, "move2"));
			}
			agents.remove();

		}
	}
	

}
