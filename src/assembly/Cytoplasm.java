package assembly;

import java.util.HashMap;

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

	//public Context build(Context<AgentExtendCont> context) {
	public Cytoplasm() {
		super("Cytoplasm");
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("axisSizeX");
		int y = (Integer)parm.getValue("axisSizeY");
		int z = (Integer)parm.getValue("axisSizeZ");
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount() <=0 ? 1 : RepastEssentials.GetTickCount();
		ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 1);
		//create space, make sure dimensions set in model.score		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		ContinuousSpace<AgentExtendCont> space = factory.createContinuousSpace("Cytoplasm",this/*context*/,
				new RandomCartesianAdder<AgentExtendCont>(), new BouncyBorders(x,y,z), x,y,z);
		
		int numribo = (Integer)parm.getValue("numberofRibosomes");
		for (int i = 0; i < numribo; i++) {
			Ribosome ribo = new Ribosome();
			ribo.setTheContext(this/*context*/);
			ribo.setSpace(space);
			this/*context*/.add(ribo);
			schedule.schedule(sparams, ribo, "move");
			schedule.schedule(sparams,ribo,"bind");
		}
		//return context;
	}

}
