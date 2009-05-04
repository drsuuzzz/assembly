package assembly;

import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

public class VP3 extends AgentExtendCont{
	
	private double moveTick;
	
	public VP3() {
		moveTick=0;
		genXYZ();
		setName("VP3");
	}
	
	public void move() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > moveTick) {

			double coord[] = {0,0,0};
			NdPoint pt = this.getSpace().getLocation(this);

			double r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
			//boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
			boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");
			
			ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(this.getSpace(), this, (r+rerr));
			Iterator neighbors = list.query().iterator();
			
			//this.genXYZ();

			double align[] = {0,0,0};
			int count = 0;
			while(neighbors.hasNext()){
				AgentExtendCont aec = (AgentExtendCont)neighbors.next();
				if (aec instanceof VP1) {
					if (aln) {
						align[0] += aec.getX();
						align[1] += aec.getY();
						align[2] += aec.getZ();
						count++;
					}
				}
			}
			if (count > 0) {
				align[0] = (align[0]/count)/*-this.getX())/8*/;
				align[1] = (align[1]/count)/*-this.getY())/8*/;
				align[2] = (align[2]/count)/*-this.getZ())/8*/;
				//this.setX(align[0]);
				//this.setY(align[1]);
				//this.setZ(align[2]);
				//coord[0] = pt.getX() + align[0];
				//coord[1] = pt.getY() + align[1];
				//coord[2] = pt.getZ() + align[2];
			//} else {
				//this.genXYZ();
				//disp[0] = this.getX();
				//disp[1] = this.getY();
				//disp[2] = this.getZ();
				//coord[0] = (disp[0]+pt.getX());
				//coord[1] = (disp[1]+pt.getY());
				//coord[2] = (disp[2]+pt.getZ());
				if (count ==72) {
					RepastEssentials.PauseSimulationRun();
				}
				//System.out.println("capsid count="+count);

			}
			
			//keep distance from other vp2 & vp3
			list = new ContinuousWithin(this.getSpace(), this, 2*(r+rerr));
			neighbors = list.query().iterator();
			double separ[] = {0.0,0.0,0.0};
			while (neighbors.hasNext()) {
				Object obj = neighbors.next();
				if (obj instanceof VP2 || obj instanceof VP3) {
					if (sep) {
						NdPoint other = this.getSpace().getLocation(obj);
						if (AgentGeometry.calcDistanceNdPoints(other, pt) < 2*(r+rerr)) {
							separ[0] += (pt.getX()-other.getX())/20;
							separ[1] += (pt.getY()-other.getY())/20;
							separ[2] += (pt.getZ()-other.getZ())/20;
						}
					}
				}
			}
			
			coord[0] = align[0] + separ[0];
			coord[1] = align[1] + separ[1];
			coord[2] = align[2] + separ[2];
			if (coord[0]==0 && coord[1]==0 && coord[2]==0) {
				this.genXYZ();
				coord[0] = this.getX() + pt.getX();
				coord[1] = this.getY() + pt.getY();
				coord[2] = this.getZ() + pt.getZ();
			} else {
				AgentGeometry.trim(coord, rerr);
				this.setX(coord[0]);
				this.setY(coord[1]);
				this.setZ(coord[2]);
				coord[0] = coord[0] + pt.getX();
				coord[1] = coord[1] + pt.getY();
				coord[2] = coord[2] + pt.getZ();
			}
			coord = this.normPositionToBorder(coord, r);
			boolean p = getSpace().moveTo(this, coord);
			if (!p) {
				System.out.println("bad point");
			}
			moveTick = tick;
		}
	}

}
