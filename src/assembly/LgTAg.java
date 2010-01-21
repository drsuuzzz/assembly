package assembly;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.NdPoint;

public class LgTAg extends AgentExtendCont {
		
	private double moveTick;
	private double expTick;
	private double deathTick;
	
	public LgTAg() {
		super();
		setLocation(Loc.cytoplasm);
		moveTick = 0;
		expTick = 0;
		deathTick = 0;
		setName("LgTag");
	}

	//scheduled methods
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick && !isDead()) {
			double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			//double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");

			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			//double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");

			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");

			if (getLocation() == Loc.cytoplasm) {
				randomWalk();
			} else {
				double disp[] = this.calcDisplacement(Genome.class, HostGenome.class,radius,rerr,vpradius,vperr);
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
				} else {
					//AgentMove.moveByDisplacement(this, disp);
					NdPoint thispt = getSpace().getLocation(this);
					NdPoint pt = AgentMove.moveByDisplacement(this, disp);
					if (pt != null) {
						this.setX(pt.getX()-thispt.getX());
						this.setY(pt.getY()-thispt.getY());
						this.setZ(pt.getZ()-thispt.getZ());
					}
				}
			}
			moveTick = tick;
		}
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > expTick  && !isDead()) {
			if (getLocation() == Loc.cytoplasm && !isMoving() && !isDead()) {
				if (nearWall(getSpace().getLocation(this))) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < AgentProbabilities.importTag) {
						((CytoNuc)getTheContext()).addToMoveList(this);
						setMoving(true);
					}
				}
			}
			expTick = tick;
		}
	}

	public void death () {
		double tick = RepastEssentials.GetTickCount();
		if (tick > deathTick) {
			if (getLocation() == Loc.nucleus && !isMoving()) {
				double rand = RandomHelper.nextDoubleFromTo(0.0,1.0);
				if (rand < AgentProbabilities.TagDeath) {
					die();
				}
			}
			deathTick = tick;
		}
	}

}
