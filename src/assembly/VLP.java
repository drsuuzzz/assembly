package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.NdPoint;
import assembly.Genome.GState;

public class VLP extends AgentExtendCont {
	
	private double moveTick;
	private double egressTick;
	private boolean prev;
	
	public VLP() {
		super();
		moveTick = 0;
		egressTick = 0;
		setName("VLP");
		this.genXYZ();
		prev = false;
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick && !isDead()) {
			double disp[] = {0.0,0.0,0.0};
			double r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
		
			disp = this.calcDispIfCenter(VP123.class, VP123.class, HostGenome.class, VLP.class,r,rerr);
				//disp = move2();
			if (this.getNoBound() <=1 && !prev) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < AgentProbabilities.VLPDeath) {
					//this.die();
				} 
			} 
			if (!isDead()){
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
					prev = false;
				//clearBoundProteins();
				} else {
					prev = true;
					double tmp[] = new double[3];
					NdPoint thispt = getSpace().getLocation(this);
					tmp[0] = disp[0]+thispt.getX();
					tmp[1] = disp[1]+thispt.getY();
					tmp[2] = disp[2]+thispt.getZ();
					this.normPositionToBorder(tmp, r);
					getSpace().moveTo(this, tmp);
					this.setX(tmp[0]-thispt.getX());
					this.setY(tmp[1]-thispt.getY());
					this.setZ(tmp[2]-thispt.getZ());
				}
			}
			moveTick = tick;
		}
	}
	
	public void egress() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > egressTick) {
			if (getNoBound() == 72) {
				//RunEnvironment.getInstance().pauseRun();
				//double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				//if (rand < AgentProbabilities.VLPEgress) {
				double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				if (nearWallGroup(dist,err)) {
					ContinuousWithin list = new ContinuousWithin(getSpace(),this,(dist+err));
					Iterator<AgentExtendCont> l = list.query().iterator();
					while (l.hasNext()) {
						AgentExtendCont aec = l.next();
						if (aec instanceof VP123 && aec.isBound()) {
							((CytoNuc)getTheContext()).addToRemList(aec);
							aec.setMoving(true);
						}
					}
					this.setMoving(true);
					((CytoNuc)getTheContext()).addToRemList(this);
					((CytoNuc)getTheContext()).addVLP();
				}
				//}
			}
			egressTick = tick;
		}
	}
}
