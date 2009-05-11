package assembly;

import repast.simphony.essentials.RepastEssentials;

public class SmTAg extends AgentExtendCont {
	
	private double moveTick;
	
	public SmTAg() {
		super();
		setName("SmTAg");
		moveTick = 0;
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			randomWalk();
			moveTick = tick;
		}
	}

}
