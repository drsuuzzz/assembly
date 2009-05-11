package assembly;

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
			double disp[] = this.calcDisplacement(Genome.class, Genome.class);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				getSpace().moveByDisplacement(this, disp);
			}
			moveTick = tick;
		}
	}

}
