package assembly;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.NdPoint;
import assembly.Genome.MType;

public class MRNA extends AgentExtendCont {
	
	private MType type;
	public enum Loc {nucleus, cytoplasm};
	private Loc location;
	
	private double moveTick;
	private double deathTick;
	private double exTick;
	
	public MRNA() {
		moveTick = 0;
		deathTick = 0;
		exTick = 0;
		setName("mRNA");
		type = MType.Tag;
		location = Loc.nucleus;
	}

	public void setType(MType type) {
		this.type = type;
	}
	
	public boolean nearWall(NdPoint pt) {
		
		boolean retval = false;
		Dimensions dim = getSpace().getDimensions();
		if (pt.getX() < 1 || pt.getY() < 1 || pt.getZ() < 1) {
			retval = true;
		} else if ((pt.getX() > dim.getWidth()-2) || (pt.getY() > dim.getHeight()-2) || (pt.getZ() > dim.getDepth()-2)) {
			retval = true;
		}
		return retval;
	}

	public Loc getLocation() {
		return location;
	}

	public void setLocation(Loc location) {
		this.location = location;
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			if (location == Loc.nucleus) {
				randomWalk();
			}
			moveTick=tick;
		}
		
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > exTick) {
			if (location == Loc.nucleus) {
				if (nearWall(getSpace().getLocation(this))) {
					//remove from nucleus
					((Nucleus)getTheContext()).getCell().addToMoveList(this);
					//add to cytoplasm
				}
			}
			exTick = tick;
		}
	}
	
	public void death() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > deathTick) {
			double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
			if (rand < 0.0001) {
				
			}
			deathTick = tick;
		}
	}
}
