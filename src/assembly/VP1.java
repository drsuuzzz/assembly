package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

public class VP1 extends AgentExtendCont {
	
	private double moveTick;
	
	public VP1() {
		super();
		moveTick=0;
		genXYZ();
		setName("VP1");
	}
	
	public void move() {
		//cohesion
		//center of mass around vp2 or vp3 or otherwise
		
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > moveTick && !isMoving()) {
			double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			double disp[] = this.calcDisplacement(VP2.class, VP3.class,radius,rerr,vpradius,vperr);
			if (disp[0]==0 && disp[1]==0 && disp[2]==0) {
				randomWalk();
			} else {
				//getSpace().moveByDisplacement(this, disp);
				//AgentMove.moveByDisplacement(this, disp);
				NdPoint thispt = getSpace().getLocation(this);
				NdPoint pt = AgentMove.moveByDisplacement(this, disp);
				if (pt != null) {
					this.setX(pt.getX()-thispt.getX());
					this.setY(pt.getY()-thispt.getY());
					this.setZ(pt.getZ()-thispt.getZ());
				}
			}
			moveTick = tick;
		}
	}
}
