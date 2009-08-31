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
			double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
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
