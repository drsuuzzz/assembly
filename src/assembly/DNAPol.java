package assembly;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;

public class DNAPol extends AgentExtendCont {
	
	private double moveTick;
	
	public DNAPol() {
		super();
		setName("DNAPol");
		moveTick = 0;
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double radius;
			double vpradius;
			double rerr;
			double vperr;
			if (RunEnvironment.getInstance().isBatch()){
				radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
				vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			} else {
				radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
				vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			}
			double disp[] = this.calcDisplacement(Genome.class, Genome.class, radius,rerr,vpradius,vperr);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				//getSpace().moveByDisplacement(this, disp);
				AgentMove.moveByDisplacement(this, disp);
			}
			moveTick = tick;
		}
	}

}
