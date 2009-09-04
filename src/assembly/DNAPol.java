package assembly;

import assembly.AgentExtendCont.Loc;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;

public class DNAPol extends AgentExtendCont {
	
	private double moveTick;
	private double deathTick;
	
	public DNAPol() {
		super();
		setName("DNAPol");
		moveTick = 0;
		deathTick = 0;
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick && !isDead()) {
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
	
	public void death() {
		if (!isDead()) {
			double tick = RepastEssentials.GetTickCount();
			if (tick > deathTick) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < 0.0001) {
					this.die();
				}
				deathTick = tick;
			}
		}
	}

}
