package assembly;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.NdPoint;
import assembly.Genome.GState;

public class VLP extends AgentExtendCont {
	
	private double moveTick;
	
	public VLP() {
		super();
		moveTick = 0;
		setName("VLP");
		this.genXYZ();
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick && !isDead()) {
			double disp[] = {0.0,0.0,0.0};
			double r=0;
			double rerr=0;
			
			if (RunEnvironment.getInstance().isBatch()){
				r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			} else {
				r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			}
			disp = this.calcDispIfCenter(VP123.class, VP123.class, HostGenome.class, VLP.class,r,rerr);
				//disp = move2();
			if (this.getNoBound() <=1 ) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < 0.01) {
					//this.die();
				} 
			} 
			if (!isDead()){
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
				//clearBoundProteins();
				} else {
					double tmp[] = new double[3];
					NdPoint thispt = getSpace().getLocation(this);
					tmp[0] = disp[0]+thispt.getX();
					tmp[1] = disp[1]+thispt.getY();
					tmp[2] = disp[2]+thispt.getZ();
				//NdPoint pt = getSpace().moveByDisplacement(this, disp);
				//if (pt != null) {
					//this.setX(pt.getX()-thispt.getX());
					//this.setY(pt.getY()-thispt.getY());
					//this.setZ(pt.getZ() - thispt.getZ());
				//}
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
}
