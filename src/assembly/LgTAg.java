package assembly;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;

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
			if (getLocation() == Loc.cytoplasm) {
				randomWalk();
			} else {
				double disp[] = this.calcDisplacement(Genome.class, HostGenome.class,radius,rerr,vpradius,vperr);
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
				} else {
					getSpace().moveByDisplacement(this, disp);
				}
			}
			moveTick = tick;
		}
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > expTick  && !isDead()) {
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

	public void death () {
		double tick = RepastEssentials.GetTickCount();
		if (tick > deathTick) {
			if (getLocation() == Loc.nucleus && !isMoving()) {
				double rand = RandomHelper.nextDoubleFromTo(0.0,1.0);
				if (rand < 0.001) {
					die();
				}
			}
			deathTick = tick;
		}
	}

}
