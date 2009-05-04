package assembly;

import java.util.HashMap;

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
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.graph.Network;

public class Nucleus extends DefaultContext<AgentExtendCont> {
//public class Nucleus implements ContextBuilder<AgentExtendCont> {
	
	//public Context build(Context<AgentExtendCont> context) {
	public Nucleus() {
		super("Nucleus");
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("axisSizeX");
		int y = (Integer)parm.getValue("axisSizeY");
		int z = (Integer)parm.getValue("axisSizeZ");
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount() <=0 ? 1 : RepastEssentials.GetTickCount();
		ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 1);
		//create space, make sure dimensions set in model.score
		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		ContinuousSpace<AgentExtendCont> space = factory.createContinuousSpace("Nucleus",/*context*/this,
				new RandomCartesianAdder<AgentExtendCont>(), new BouncyBorders(x,y,z), x,y,z);
		
		//add agents
		double pos[] = {20.0,20.0,20.0};
		int numg = (Integer)parm.getValue("numberofGenomes");
		for (int i = 0; i < numg; i++) {
			Genome g = new Genome();
			g.setTheContext(/*context*/this);
			g.setSpace(space);
			this.add(g);
			schedule.schedule(sparams, g, "move2");
		}
	
	
	int numvp1 = (Integer)parm.getValue("numberofVP123");
		for (int i = 0; i < numvp1; i++) {
			VP123 vp123 = new VP123();
			vp123.setTheContext(/*context*/this);
			vp123.setSpace(space);
			/*context*/this.add(vp123);
			schedule.schedule(sparams, vp123, "move2");
			
		}
		
	/*	int numvp2 = (Integer)parm.getValue("numberofVP2");
		for (int i = 0; i < numvp2; i++) {
			VP2 vp2 = new VP2();
			vp2.setTheContext(this);
			vp2.setSpace(space);
			this.add(vp2);
		}
		int numvp3 = (Integer)parm.getValue("numberofVP3");
		for (int i = 0; i < numvp3; i++) {
			VP3 vp3 = new VP3();
			vp3.setTheContext(this);
			vp3.setSpace(space);
			this.add(vp3);
		}*/
	//	loc = space.getLocation(g);
		//return context;
	}
}
