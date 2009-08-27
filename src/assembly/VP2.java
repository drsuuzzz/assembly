package assembly;

import java.util.Iterator;

import assembly.VP123.VPType;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

public class VP2 extends AgentExtendCont{

	private double moveTick;
	private double exTick;
	
	public VP2() {
		super();
		moveTick=0;
		exTick = 0;
		genXYZ();
		setName("VP2");
	}

	public void move() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double r = 0;
			double rerr = 0;
			if (RunEnvironment.getInstance().isBatch()){
				r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			} else {
				r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			}
			double disp[] = this.calcDispIfCenter(VP1.class, VP1.class, VP2.class, VP3.class, r, rerr);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
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
				//getSpace().moveByDisplacement(this, disp);
			}
			moveTick = tick;
		}
	}
	
	public void export() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > exTick) {
			if (getNoBound() == 5) {
				double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				if (nearWallGroup(dist, err)) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < 0.4) {
						ContinuousWithin list = new ContinuousWithin(getSpace(),this,(dist+err));
						Iterator<AgentExtendCont> l = list.query().iterator();
						while (l.hasNext()) {
							AgentExtendCont aec = l.next();
							if (aec instanceof VP1 && aec.isBound()) {
								aec.setMoving(true);
								((CytoNuc)getTheContext()).addToRemList(aec);
							}
						}
						this.setMoving(true);
						((CytoNuc)getTheContext()).addToRemList(this);
						VP123 vp = new VP123();
						vp.setVptype(VPType.VP12);
						((CytoNuc)getTheContext()).addToMoveList(vp);
					}
				}
			}
			exTick = tick;
		}
	}
}
