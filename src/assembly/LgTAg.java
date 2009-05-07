package assembly;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;

public class LgTAg extends AgentExtendCont {
		
	private double moveTick;
	private double expTick;
	
	public LgTAg() {
		super();
		setLocation(Loc.cytoplasm);
		moveTick = 0;
		expTick = 0;
		setName("LgTag");
	}

	//scheduled methods
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			randomWalk();
			moveTick = tick;
		}
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > expTick) {
			if (getLocation() == Loc.cytoplasm && !isMoving()) {
				if (nearWall(getSpace().getLocation(this))) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < 0.4) {
						((Cytoplasm)getTheContext()).getCell().addToMoveList(this);
						setMoving(true);
					}
				}
			}
			expTick = tick;
		}
	}


}
