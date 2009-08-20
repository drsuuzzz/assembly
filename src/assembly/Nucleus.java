package assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import assembly.Genome.GState;
import assembly.VP123.VPType;

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
	private List<AgentExtendCont> remList;
	
	private double addTick;
	private double infectTick;
	//public Context build(Context<AgentExtendCont> context) {
	public Nucleus() {
		super("Nucleus");
		
		addList = new ArrayList<AgentExtendCont>();
		remList = new ArrayList<AgentExtendCont>();
		
		addTick = 0;
		infectTick = 0;
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("nucleusSizeX");
		int y = (Integer)parm.getValue("nucleusSizeY");
		int z = (Integer)parm.getValue("nucleusSizeZ");
		
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
			g.setState(GState.assembly);
			this.add(g);
			//schedule.schedule(sparams, g, "move3");
			schedule.schedule(sparams, g, "move");
			schedule.schedule(sparams,g,"transcription");
			schedule.schedule(sparams, g, "egress");
		}
		
		int numh = (Integer)parm.getValue("numberofHost");
		for (int i = 0; i < numh; i++) {
			HostGenome hg = new HostGenome();
			hg.setTheContext(this);
			hg.setSpace(space);
			this.add(hg);
			schedule.schedule(sparams, hg, "move");
			schedule.schedule(sparams, hg, "transcription");
		}
		
		int numtf = (Integer)parm.getValue("numberofTF");
		for (int i = 0; i < numtf; i++) {
			TranscriptionFactor tf = new TranscriptionFactor();
			tf.setTheContext(this);
			tf.setSpace(space);
			this.add(tf);
			schedule.schedule(sparams, tf, "move");
		}
		
		int numvp123 =72;
		for (int i = 0; i < numvp123; i++) {
			VP123 vp = new VP123();
			vp.setTheContext(this);
			vp.setSpace(space);
			int rand = RandomHelper.nextIntFromTo(1, 2);
			if (rand == 1) {
				vp.setVptype(VPType.VP12);
			} else {
				vp.setVptype(VPType.VP13);
			}
			this.add(vp);
			//schedule.schedule(sparams,vp,"move3");
			schedule.schedule(sparams,vp,"move");
		}
		schedule.schedule(sparamseven, this, "addAgents");
		schedule.schedule(sparamseven, this, "remAgents");
		
		int numvlp = 0;
		for (int i = 0; i < numvlp; i++) {
			VLP vlp = new VLP();
			vlp.setTheContext(this);
			vlp.setSpace(space);
			this.add(vlp);
			schedule.schedule(sparams,vlp,"move");
		}
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
	
	public void addToRemList (AgentExtendCont aec) {
		remList.add(aec);
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
					schedule.schedule(sparams,aec,"move");
					schedule.schedule(sparams,aec,"transcription");
					schedule.schedule(sparams,aec,"egress");
				} else if (aec instanceof MRNA) {
					this.add(aec);
					aec.setMove(schedule.schedule(sparams,aec,"move"));
					aec.setExport(schedule.schedule(sparams,aec,"export"));
					aec.setSplice(schedule.schedule(sparams,aec,"splice"));
				} else if (aec instanceof DNAPol) {
					this.add(aec);
					schedule.schedule(sparams,aec,"move");
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
