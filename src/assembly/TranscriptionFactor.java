package assembly;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.continuous.NdPoint;

public class TranscriptionFactor extends AgentExtendCont {
	
	private double moveTick;
	
	public TranscriptionFactor() {
		super();
		setName("TranscriptionFactor");
		moveTick = 0;
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			double disp[] = this.calcDisplacement(Genome.class, Genome.class,radius,rerr,vpradius,vperr);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				//double tmp[] = new double[3];
				NdPoint thispt = getSpace().getLocation(this);
				//tmp[0] = disp[0]+thispt.getX();
				//tmp[1] = disp[1]+thispt.getY();
				//tmp[2] = disp[2]+thispt.getZ();
				//NdPoint pt = getSpace().moveByDisplacement(this, disp);
				NdPoint pt = AgentMove.moveByDisplacement(this, disp);
				if (pt != null) {
					this.setX(pt.getX()-thispt.getX());
					this.setY(pt.getY()-thispt.getY());
					this.setZ(pt.getZ()-thispt.getZ());
				}
				//double r = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				//normPositionToBorder(tmp,r);
				//getSpace().moveTo(this, tmp);
				//this.setX(tmp[0]-thispt.getX());
				//this.setY(tmp[1]-thispt.getY());
				//this.setZ(tmp[2]-thispt.getZ());
			}
			moveTick = tick;
		}
	}

}
