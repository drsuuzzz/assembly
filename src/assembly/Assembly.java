package assembly;

import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.graph.Network;

//public class Assembly extends DefaultContext {
public class Assembly implements ContextBuilder {
	
	public Context build(Context context) {
	//public Assembly() {
		//super("Assembly");
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		int x = (Integer)parm.getValue("xAxisSize");
		int y = (Integer)parm.getValue("yAxisSize");
		int z = (Integer)parm.getValue("zAxisSize");
		
		//create space, make sure dimensions set in model.score
		/*int size[]={0,0,0};
		size[0]=x;
		size[1]=y;
		size[2]=z;*/
		Network<AgentExtendCont> network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
				"Bonds", context, false);
		
		ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(new HashMap());
		ContinuousSpace space = factory.createContinuousSpace("nucleus",context/*this*/,
				new RandomCartesianAdder(), /*new WrapAroundBorders()*/new BouncyBorders(x,y,z), x,y,z);
		//Dimensions dim = space.getDimensions();
		//System.out.println("Dimensions: x"+dim.getWidth()+" y "+dim.getHeight()+" z "+dim.getDepth());
		
		ContinuousSpace space2 = (ContinuousSpace)context/*this*/.getProjection("nucleus");
		//add agents
		double pos[] = {20.0,20.0,20.0};
		int numg = (Integer)parm.getValue("numberofGenomes");
//		for (int i = 0; i < numg; i++) {
			Genome g = new Genome();
			g.setTheContext(context/*this*/);
			g.setSpace(space);
			g.setNetwork(network);
			//g.setStop(true);
			context/*this*/.add(g);
			boolean result = space.moveTo(g,pos);
		//	boolean res2 = space2.moveTo(g, pos);
		//	NdPoint loc = space.getLocation(g);
		//	if (!result)
		//		System.out.println("Move failed");

	//	}
	
	
	int numvp1 = (Integer)parm.getValue("numberofVP1");
		for (int i = 0; i < numvp1; i++) {
			VP1 vp1 = new VP1();
			vp1.setTheContext(context/*this*/);
			vp1.setSpace(space);
			context/*this*/.add(vp1);
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
		return context;
	}
}
