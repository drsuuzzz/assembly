package assembly;

//import repast.simphony.context.Context;
import java.util.ArrayList;
import java.util.Iterator;

import assembly.Genome.GState;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
//import repast.simphony.essentials.RepastEssentials;
//import repast.simphony.space.SpatialMath;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
//import repast.simphony.space.grid.GridPoint;
import repast.simphony.parameter.Parameter;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.continuous.ContinuousWithin;
//import repast.simphony.util.ContextUtils;

public class VP123 extends AgentExtendCont{
	
	public enum VPType {VP12, VP13};
	private VPType vptype;
	
	//neighbors
	private Genome genome;
	private VLP vlp;
	
	private double moveTick;
	private double move2Tick;
	private double neighborTick;
	private static int id = 0;
	private int myid;
	
	public VP123() {
		super();
		genome = null;
		vlp = null;
		//flock=false;
		moveTick = 0;
		move2Tick = 0;
		neighborTick = 0;
		setName("VP123");
		myid=id++;
		this.genXYZ();
	}
	
	public VPType getVptype() {
		return vptype;
	}

	public void setVptype(VPType vptype) {
		this.vptype = vptype;
	}

	public Genome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double radius;
			double vpradius;
			double rerr;
			double vperr;
			if (RunEnvironment.getInstance().isBatch()){
				radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
				vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidError");
			} else {
				radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
				vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidError");
			}
			double disp[] = this.calcDisplacement(Genome.class, VLP.class, radius,rerr,vpradius,vperr);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				AgentMove.moveByDisplacement(getSpace(),this, disp);
				//getSpace().moveByDisplacement(this, disp);
			}
			moveTick = tick;
		}
	}
}
