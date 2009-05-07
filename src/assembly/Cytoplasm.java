package assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import assembly.AgentExtendCont.Loc;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;

//public class Cytoplasm implements ContextBuilder<AgentExtendCont> {
public class Cytoplasm extends DefaultContext<AgentExtendCont> {

	private ContinuousSpace<AgentExtendCont> space;
	private Cell cell;
	
	private List<AgentExtendCont> remList;
	private List<AgentExtendCont> addList;
	
	//public Context build(Context<AgentExtendCont> context) {
	public Cytoplasm() {
		super("Cytoplasm");
		
		remList = new ArrayList<AgentExtendCont>();
		addList = new ArrayList<AgentExtendCont>();
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("axisSizeX");
		int y = (Integer)parm.getValue("axisSizeY");
		int z = (Integer)parm.getValue("axisSizeZ");
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double startodd = RepastEssentials.GetTickCount() <= 0 ? 1 : RepastEssentials.GetTickCount();
		double starteven;
		if ((int)startodd%2 == 0) {
			starteven = startodd;
			startodd = startodd + 1.0f;
		} else {
			starteven = startodd+1.0f;
		}
		ScheduleParameters sparams = ScheduleParameters.createRepeating(startodd, 2);
		ScheduleParameters sparamseven = ScheduleParameters.createRepeating(starteven,2);
		//create space, make sure dimensions set in model.score		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		space = factory.createContinuousSpace("Cytoplasm",this/*context*/,
				new RandomCartesianAdder<AgentExtendCont>(), new BouncyBorders(x,y,z), x,y,z);
		
		int numribo = 0;//(Integer)parm.getValue("numberofRibosomes");
		for (int i = 0; i < numribo; i++) {
			Ribosome ribo = new Ribosome();
			ribo.setTheContext(this);
			ribo.setSpace(space);
			this.add(ribo);
			schedule.schedule(sparams, ribo, "move");
			schedule.schedule(sparams, ribo, "translation");
		}
		for (int i =0; i < 0; i++) {
			MRNA mrna = new MRNA();
			mrna.setTheContext(this);
			mrna.setSpace(space);
			mrna.setLocation(Loc.cytoplasm);
			this.add(mrna);
			mrna.setMove(schedule.schedule(sparams, mrna, "move"));
			mrna.setDeath(schedule.schedule(sparams, mrna, "death"));
		}
		int numvp1 = 10;//(Integer)parm.getValue("numberofVP1");
		for (int i = 0; i < numvp1; i++) {
			VP1 vp1 = new VP1();
			vp1.setTheContext(this);
			vp1.setSpace(space);
			this.add(vp1);
			vp1.setMove(schedule.schedule(sparams,vp1,"move"));
		}
		int numvp2 = 1;//(Integer)parm.getValue("numberofVP2");
		for (int i = 0; i < numvp2; i++) {
			VP2 vp2 = new VP2();
			vp2.setTheContext(this);
			vp2.setSpace(space);
			this.add(vp2);
			vp2.setMove(schedule.schedule(sparams,vp2,"move"));
			vp2.setExport(schedule.schedule(sparams,vp2,"export"));
		}
		int numvp3 = 1;//(Integer)parm.getValue("numberofVP3");
		for (int i = 0; i < numvp3; i++) {
			VP3 vp3 = new VP3();
			vp3.setTheContext(this);
			vp3.setSpace(space);
			this.add(vp3);
			vp3.setMove(schedule.schedule(sparams,vp3,"move"));
			vp3.setExport(schedule.schedule(sparams,vp3,"export"));

		}
		//return context;
		schedule.schedule(sparamseven, this, "removeAgents");
		schedule.schedule(sparamseven, this, "addAgents");
	}
	
	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public ContinuousSpace<AgentExtendCont> getSpace() {
		return space;
	}

	public void addToRemList(AgentExtendCont aec) {
		remList.add(aec);
	}
	
	public void addToAddList(AgentExtendCont aec) {
		addList.add(aec);
	}
	
	//scheduled methods
	//even tick
	public void removeAgents() {
		Iterator<AgentExtendCont> l = remList.iterator();
		while (l.hasNext()) {
			AgentExtendCont aec = l.next();
			aec.removeScheduledActions();
			this.remove(aec);
			l.remove();
		}
	}
	
	public void addAgents() {
		Iterator<AgentExtendCont> l = addList.iterator();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double startodd = RepastEssentials.GetTickCount();
		if ((int)startodd%2 == 0) {
			startodd = startodd +1.0f;
		}
		ScheduleParameters sparams = ScheduleParameters.createRepeating(startodd, 2);
		while(l.hasNext()) {
			AgentExtendCont aec = l.next();
			this.add(aec);
			aec.setMove(schedule.schedule(sparams, aec, "move"));
			if (aec instanceof LgTAg) {
				aec.setExport(schedule.schedule(sparams, aec, "export"));
			}
			l.remove();
		}
	}
}
