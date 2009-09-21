package assembly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import assembly.AgentExtendCont.Loc;
import assembly.Genome.GState;
import assembly.MRNA.MType;
import assembly.VP123.VPType;

import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.util.collections.IndexedIterable;

public class CytoNuc extends DefaultContext<AgentExtendCont> {
	private List<AgentExtendCont> mAgents;
	private List<AgentExtendCont> addList;
	private List<AgentExtendCont> remList;
	
	private static DateFormat format = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss_z");
	
	private File countp;
	private double fileTick;
	private double moveTick;
	private double addTick;
	private double remTick;
	
	private int virions;
	private int VLP;
	
	private ContinuousSpace<AgentExtendCont> cspace;
	
	public CytoNuc() {
		super("CytoNuc");
		
		fileTick = 0;
		moveTick = 0;
		addTick = 0;
		remTick = 0;
		virions = 0;
		VLP = 0;
		
		mAgents = new ArrayList<AgentExtendCont>();
		addList = new ArrayList<AgentExtendCont>();
		remList = new ArrayList<AgentExtendCont>();
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("cellRadius") * 2;
		int y = x;
		int z = x;
		
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
		ScheduleParameters sparamseven = ScheduleParameters.createRepeating(starteven, 2);
		ScheduleParameters sparamsodd = ScheduleParameters.createRepeating(startodd,2);
		schedule.schedule(sparamseven,this,"moveAgents");
		schedule.schedule(sparamsodd,this,"fileWrite");
		
		String path = (String) RunEnvironment.getInstance().getParameters().getValue("filePath");
		String d = format.format(new Date());
		String fname = "count."+d+".txt";
		countp  = new File(path,fname);
		try {
			FileWriter fw = new FileWriter(countp, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("tick,Tag,T-mRNA,tag,t-mRNA,VP1,vp1-mRNA,VP2,vp2-mRNA,VP3,vp3-mRNA,VP123,Genome,DNAPol,virions,VLP");
			fw.close();
		} catch (IOException e) {
			System.out.println("Something wrong with count file.");
			e.printStackTrace();
		}
		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		cspace = factory.createContinuousSpace("CellSpace",this,
				new RandomCartesianAdder<AgentExtendCont>(), new BouncyBorders(x,y,z), x,y,z);
		
		//add nucleus agents
		int numg = (Integer)parm.getValue("numberofGenomes");
		for (int i = 0; i < numg; i++) {
			Genome g = new Genome();
			g.setTheContext(this);
			g.setSpace(cspace);
			g.setLocation(Loc.nucleus);
			//g.setState(GState.assembly);
			this.add(g);
			cspace.moveTo(g, AgentMove.adjustPointToSpace(g));
			g.setMove(schedule.schedule(sparamsodd, g, "move"));
			g.setTranscription(schedule.schedule(sparamsodd,g,"transcription"));
			g.setEgress(schedule.schedule(sparamsodd, g, "egress"));
		}
		
		int numh = (Integer)parm.getValue("numberofHost");
		for (int i = 0; i < numh; i++) {
			HostGenome hg = new HostGenome();
			hg.setTheContext(this);
			hg.setSpace(cspace);
			hg.setLocation(Loc.nucleus);
			this.add(hg);
			cspace.moveTo(hg, AgentMove.adjustPointToSpace(hg));
			schedule.schedule(sparamsodd, hg, "move");
			schedule.schedule(sparamsodd, hg, "transcription");
		}
		
		int numtf = (Integer)parm.getValue("numberofTF");
		for (int i = 0; i < numtf; i++) {
			TranscriptionFactor tf = new TranscriptionFactor();
			tf.setTheContext(this);
			tf.setSpace(cspace);
			tf.setLocation(Loc.nucleus);
			this.add(tf);
			cspace.moveTo(tf, AgentMove.adjustPointToSpace(tf));
			schedule.schedule(sparamsodd, tf, "move");
		}
		
		int numvp123 = (Integer)parm.getValue("numberofVP123");
		for (int i = 0; i < numvp123; i++) {
			VP123 vp = new VP123();
			vp.setTheContext(this);
			vp.setSpace(cspace);
			vp.setLocation(Loc.nucleus);
			int rand = RandomHelper.nextIntFromTo(1, 2);
			if (rand == 1) {
				vp.setVptype(VPType.VP12);
			} else {
				vp.setVptype(VPType.VP13);
			}
			this.add(vp);
			cspace.moveTo(vp, AgentMove.adjustPointToSpace(vp));
			vp.setMove(schedule.schedule(sparamsodd,vp,"move"));
		}
		schedule.schedule(sparamseven, this, "addAgents");
		schedule.schedule(sparamseven, this, "remAgents");
		
		int numvlp = 0;
		for (int i = 0; i < numvlp; i++) {
			VLP vlp = new VLP();
			vlp.setTheContext(this);
			vlp.setSpace(cspace);
			vlp.setLocation(Loc.nucleus);
			this.add(vlp);
			cspace.moveTo(vlp, AgentMove.adjustPointToSpace(vlp));
			vlp.setMove(schedule.schedule(sparamsodd,vlp,"move"));
		}
		
		//add cytoplasm/ER agents
		
		int numribo = (Integer)parm.getValue("numberofRibosomes");
		for (int i = 0; i < numribo; i++) {
			Ribosome ribo = new Ribosome();
			ribo.setTheContext(this);
			ribo.setSpace(cspace);
			ribo.setLocation(Loc.cytoplasm);
			this.add(ribo);
			cspace.moveTo(ribo, AgentMove.adjustPointToSpace(ribo));
			schedule.schedule(sparamsodd, ribo, "move");
			schedule.schedule(sparamsodd, ribo, "translation");
		}

		int numvp1 = (Integer)parm.getValue("numberofVP1");
		for (int i = 0; i < numvp1; i++) {
			VP1 vp1 = new VP1();
			vp1.setTheContext(this);
			vp1.setSpace(cspace);
			vp1.setLocation(Loc.cytoplasm);
			this.add(vp1);
			cspace.moveTo(vp1, AgentMove.adjustPointToSpace(vp1));
			vp1.setMove(schedule.schedule(sparamsodd,vp1,"move"));
		}
		int numvp2 = (Integer)parm.getValue("numberofVP2");
		for (int i = 0; i < numvp2; i++) {
			VP2 vp2 = new VP2();
			vp2.setTheContext(this);
			vp2.setSpace(cspace);
			vp2.setLocation(Loc.cytoplasm);
			this.add(vp2);
			cspace.moveTo(vp2, AgentMove.adjustPointToSpace(vp2));
			vp2.setMove(schedule.schedule(sparamsodd,vp2,"move"));
			vp2.setExport(schedule.schedule(sparamsodd,vp2,"export"));
		}
		int numvp3 = (Integer)parm.getValue("numberofVP3");
		for (int i = 0; i < numvp3; i++) {
			VP3 vp3 = new VP3();
			vp3.setTheContext(this);
			vp3.setSpace(cspace);
			vp3.setLocation(Loc.cytoplasm);
			this.add(vp3);
			cspace.moveTo(vp3, AgentMove.adjustPointToSpace(vp3));
			vp3.setMove(schedule.schedule(sparamsodd,vp3,"move"));
			vp3.setExport(schedule.schedule(sparamsodd,vp3,"export"));

		}
		int numtag = 0;//20;
		for (int i = 0; i < numtag; i++) {
			LgTAg lt = new LgTAg();
			lt.setTheContext(this);
			lt.setSpace(cspace);
			lt.setLocation(Loc.nucleus);
			this.add(lt);
			cspace.moveTo(lt, AgentMove.adjustPointToSpace(lt));
			lt.setMove(schedule.schedule(sparamsodd,lt,"move"));
		}
		int numdna = 0;//10;
		for (int i = 0; i < numdna; i++) {
			DNAPol dnap = new DNAPol();
			dnap.setTheContext(this);
			dnap.setSpace(cspace);
			dnap.setLocation(Loc.nucleus);
			this.add(dnap);
			cspace.moveTo(dnap, AgentMove.adjustPointToSpace(dnap));
			dnap.setMove(schedule.schedule(sparamsodd,dnap,"move"));
			dnap.setDeath(schedule.schedule(sparamsodd, dnap,"death"));
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
		IndexedIterable<AgentExtendCont> i = this.getObjects(LgTAg.class);
		int ret = 0;
		if (i != null) {
			ret  = i.size();
		}
		
		return ret;
	}
	
	public int getNotag() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(SmTAg.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP1() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(VP1.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP2() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(VP2.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP3() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(VP3.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoVP123() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(VP123.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int[] getNoMRNAs() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(MRNA.class);
		int[] ret = {0,0,0,0,0};
		for (int j = 0; j < i.size(); j++) {
			MRNA mrna = (MRNA)i.get(j);
			if (mrna.getMType() == MType.Tag) {
				ret[0]+=1;
			} else if (mrna.getMType() == MType.tag) {
				ret[1] += 1;
			} else if (mrna.getMType() == MType.vp1) {
				ret[2] += 1;
			} else if (mrna.getMType() == MType.vp2) {
				ret[3] += 1;
			} else if (mrna.getMType() == MType.vp3) {
				ret[4] += 1;
			}
		}
		
		return ret;
	}
	
	public int getNoGenome() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(Genome.class);
		int ret = 0;
		if (i != null) {
			ret = i.size();
		}
		return ret;
	}
	
	public int getNoDNAPol() {
		IndexedIterable<AgentExtendCont> i = this.getObjects(DNAPol.class);
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
				int[] mrna = this.getNoMRNAs();
				pw.println(tick+","+getNoTag()+","+mrna[0]+","+getNotag()+","+mrna[1]+","+this.getNoVP1()+","
						+mrna[2]+","+this.getNoVP2()+","+mrna[3]+","+this.getNoVP3()+","+mrna[4]+","+this.getNoVP123()+
						","+this.getNoGenome()+","+this.getNoDNAPol()+","+virions+","+VLP);
				fw.close();
			} catch (IOException e) {
				System.out.println("Something wrong with count file.");
				e.printStackTrace();
			}
			fileTick = tick;
			if (virions >= 1) {
				if (RunEnvironment.getInstance().isBatch()) {
					RunEnvironment.getInstance().endRun();
				}
			}
		}
	}
	
	//schedules methods
	//even tick
	public void moveAgents() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			Iterator<AgentExtendCont> agents = mAgents.iterator();
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
			double start = tick;
			if ((int)start%2 == 0) {
				start = start + 1.0f;
			}
			ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
			while (agents.hasNext()) {
				AgentExtendCont aec = agents.next();
				if (aec instanceof MRNA) {
					//System.out.println("Moving mRNA"+((MRNA) aec).getMyid());
					((MRNA) aec).setLocation(Loc.cytoplasm);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					//schedule death rule in cytoplasm
					aec.setDeath(schedule.schedule(sparams,aec,"death"));
					//remove the export rule;
					aec.removeAnAction(aec.getExport());
					aec.removeAnAction(aec.getSplice());
					aec.setExport(null);
					aec.setSplice(null);
					aec.setMoving(false);
				} else if (aec instanceof LgTAg) {
					aec.setLocation(Loc.nucleus);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					aec.removeAnAction(aec.getExport());
					aec.setExport(null);
					aec.setMoving(false);
				} else if (aec instanceof VP123) {
					this.add(aec);
					aec.setTheContext(this);
					aec.setSpace(cspace);
					aec.setLocation(Loc.nucleus);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					aec.setMove(schedule.schedule(sparams, aec, "move"));
					//aec.setEgress(schedule.schedule(sparams,aec, "egress"));
				}
				agents.remove();

			}
			moveTick = tick;
		}
	}
	
	public void addAgents() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > addTick) {
			Iterator<AgentExtendCont> agents = addList.iterator();
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
			double start = RepastEssentials.GetTickCount();
			if ((int)start %2 == 0) {
				start = start +1.0f;
			}
			ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
			while (agents.hasNext()) {
				AgentExtendCont aec = agents.next();
				if (aec instanceof Genome) {
					if (((Genome) aec).getState() == GState.RR) {
						this.add(aec);
						//((Genome)aec).setState(GState.RR);
						cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
						aec.setMove(schedule.schedule(sparams,aec,"move"));
						aec.setTranscription(schedule.schedule(sparams,aec,"transcription"));
						aec.setEgress(schedule.schedule(sparams,aec,"egress"));
					} else {
						((Genome)aec).makeBabyMRNA();
					}
				}/* else if (aec instanceof MRNA) {
					this.add(aec);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					aec.setMove(schedule.schedule(sparams,aec,"move"));
					aec.setExport(schedule.schedule(sparams,aec,"export"));
					aec.setSplice(schedule.schedule(sparams,aec,"splice"));
				} */else if (aec instanceof DNAPol) {
					this.add(aec);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					aec.setMove(schedule.schedule(sparams,aec,"move"));
					aec.setDeath(schedule.schedule(sparams,aec,"death"));
				} else if (aec instanceof Ribosome) {
					((Ribosome)aec).makeProtein();
				} else {
					this.add(aec);
					cspace.moveTo(aec, AgentMove.adjustPointToSpace(aec));
					aec.setMove(schedule.schedule(sparams, aec, "move"));
					if (aec instanceof LgTAg || aec instanceof VP2 || aec instanceof VP3) {
						aec.setExport(schedule.schedule(sparams, aec, "export"));
					}
					if (aec instanceof LgTAg) {
						//aec.setDeath(schedule.schedule(sparams, aec, "death"));
					}
				}
				agents.remove();
			}
			addTick = tick;
		}
	}
	
	public void remAgents() {
		Iterator<AgentExtendCont> l = remList.iterator();
		while (l.hasNext()) {
			AgentExtendCont aec = l.next();
			aec.removeScheduledActions();
			this.remove(aec);
			l.remove();
		}
	}
}
