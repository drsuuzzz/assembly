package assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.IndexedIterable;

public class Nucleus extends DefaultContext<AgentExtendCont> {
//public class Nucleus implements ContextBuilder<AgentExtendCont> {
	
	private ContinuousSpace<AgentExtendCont> space;
	private Cell cell;
	
	private List<AgentExtendCont> addList;
	
	private double addTick;
	private double infectTick;
	//public Context build(Context<AgentExtendCont> context) {
	public Nucleus() {
		super("Nucleus");
		
		addList = new ArrayList<AgentExtendCont>();
		
		addTick = 0;
		infectTick = 0;
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("axisSizeX");
		int y = (Integer)parm.getValue("axisSizeY");
		int z = (Integer)parm.getValue("axisSizeZ");
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double startodd = RepastEssentials.GetTickCount() <= 0 ? 1 : RepastEssentials.GetTickCount();
		double starteven;
		if ((int)startodd%2==0) {
			starteven = startodd;
			startodd = startodd + 1;
		} else {
			starteven = startodd+1;
		}
		//odd tick
		ScheduleParameters sparams = ScheduleParameters.createRepeating(startodd, 2);
		ScheduleParameters sparamseven = ScheduleParameters.createRepeating(starteven, 2);
		//ScheduleParameters sparams100 = ScheduleParameters.createRepeating(startodd, 100);
		//create space, make sure dimensions set in model.score
		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		space = factory.createContinuousSpace("Nucleus",/*context*/this,
				new RandomCartesianAdder<AgentExtendCont>(), new BouncyBorders(x,y,z), x,y,z);
		
		//add agents
		int numg = (Integer)parm.getValue("numberofGenomes");
		for (int i = 0; i < numg; i++) {
			Genome g = new Genome();
			g.setTheContext(/*context*/this);
			g.setSpace(space);
			this.add(g);
			//schedule.schedule(sparams, g, "move2");
			schedule.schedule(sparams, g, "move");
			schedule.schedule(sparams,g,"transcription");
		}
	
	
		int numvp1 = 0;//(Integer)parm.getValue("numberofVP123");
		for (int i = 0; i < numvp1; i++) {
			VP123 vp123 = new VP123();
			vp123.setTheContext(/*context*/this);
			vp123.setSpace(space);
			/*context*/this.add(vp123);
			schedule.schedule(sparams, vp123, "move2");
			
		}

		int numpol =0;
		for (int i = 0; i < numpol; i++) {
			DNAPol pol = new DNAPol();
			pol.setTheContext(this);
			pol.setSpace(space);
			this.add(pol);
			schedule.schedule(sparams, pol, "move");
		}
		
		int numtf = 2;
		for (int i = 0; i < numtf; i++) {
			TranscriptionFactor tf = new TranscriptionFactor();
			tf.setTheContext(this);
			tf.setSpace(space);
			this.add(tf);
			schedule.schedule(sparams, tf, "move");
		}
		schedule.schedule(sparamseven, this, "addAgents");
	}
	
	public ContinuousSpace<AgentExtendCont> getSpace() {
		return space;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	public void addToAddList (AgentExtendCont aec) {
		addList.add(aec);
	}

	//scheduled methods
	//even tick
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
					this.add(aec);
					schedule.schedule(sparams,aec,"move2");
					schedule.schedule(sparams,aec,"transcription");
					addTick = tick;
				} else if (aec instanceof MRNA) {
					this.add(aec);
					aec.setMove(schedule.schedule(sparams,aec,"move"));
					aec.setExport(schedule.schedule(sparams,aec,"export"));
					aec.setSplice(schedule.schedule(sparams,aec,"splice"));
				}
				agents.remove();
			}
			addTick = tick;
		}
	}
}
