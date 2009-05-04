package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

public class VP1 extends AgentExtendCont {
	
	private double moveTick;
	
	public VP1() {
		moveTick=0;
		genXYZ();
		setName("VP1");
	}
	
	public void move() {
		//cohesion
		//center of mass around vp2 or vp3 or otherwise
		
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > moveTick) {
		double retpt[] = {0.0,0.0,0.0};
		double cohesiong[]={0,0,0};
		double cohesionv[]={0,0,0};
		double separationg[]={0,0,0};
		double separationv[]={0,0,0};
		double alignmentg[]={0,0,0};
		double alignmentv[]={0,0,0};

		NdPoint thispt = this.getSpace().getLocation(this);
		
		boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
		boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");

		ContinuousSpace space = getSpace();
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
		ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(space,this,(radius+rerr));
		Iterator<AgentExtendCont> l = list.query().iterator();
		boolean vp23Found = false;
		double center[] = {0,0,0};
		while (l.hasNext()) {
			AgentExtendCont obj = l.next();
			if (obj instanceof VP2 || obj instanceof VP3) {
				vp23Found = true;
				center[0] = space.getLocation(obj).getX();
				center[1] = space.getLocation(obj).getY();
				center[2] = space.getLocation(obj).getZ();
				if (coh) {
					cohesiong[0] = (center[0]-thispt.getX())/100;
					cohesiong[1] = (center[1]-thispt.getY())/100;
					cohesiong[2] = (center[2]-thispt.getZ())/100;
				}
				if (sep) {
					if (AgentGeometry.calcDistance(center, thispt) < (radius-rerr)) {
						separationg[0] = (thispt.getX()-center[0])/10;
						separationg[1] = (thispt.getY()-center[1])/10;
						separationg[2] = (thispt.getZ()-center[2])/10;
					}
				}
				if (aln) {
					alignmentg[0] = (obj.getX()-this.getX())/8;
					alignmentg[1] = (obj.getY()-this.getY())/8;
					alignmentg[2] = (obj.getZ()-this.getZ())/8;
				}
				break;
			}
		}

		int count=0;
		int counta = 0;
		list = new ContinuousWithin<AgentExtendCont>(space,this,(vpradius+vperr));
		l = list.query().iterator();
		if (vp23Found) {
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP1) {
					VP1 vp = (VP1) obj;
					NdPoint vpt = space.getLocation(vp);
					if (AgentGeometry.calcDistance(center,vpt) < (radius+rerr)) {
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (vpradius-vperr)) {
								separationv[0] += (thispt.getX()-vpt.getX())/30;
								separationv[1] += (thispt.getY()-vpt.getY())/30;
								separationv[2] += (thispt.getZ()-vpt.getZ())/30;
							}
						}
						if (aln) {
							alignmentv[0] += vp.getX();
							alignmentv[1] += vp.getY();
							alignmentv[2] += vp.getZ();
							counta++;
						}
					}
				}
			}
			if (count > 0) {
				if (coh) {
					cohesionv[0] = ((cohesionv[0]/count)-thispt.getX())/100;
					cohesionv[1] = ((cohesionv[1]/count)-thispt.getY())/100;
					cohesionv[2] = ((cohesionv[2]/count)-thispt.getZ())/100;
				}
			}
			if (counta > 0) {
				if (aln) {
					alignmentv[0] = ((alignmentv[0]/counta)-this.getX())/8;
					alignmentv[1] = ((alignmentv[1]/counta)-this.getY())/8;
					alignmentv[2] = ((alignmentv[2]/counta)-this.getZ())/8;
				}
			}
		}/* else {
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP1) {
					NdPoint vpt = space.getLocation(obj);
					if (coh) {
						cohesionv[0] += vpt.getX();
						cohesionv[1] += vpt.getY();
						cohesionv[2] += vpt.getZ();
						count++;
					}
					if (sep) {
						if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (vpradius-vperr)) {
							separationv[0] += (thispt.getX()-vpt.getX())/20;
							separationv[1] += (thispt.getY()-vpt.getY())/20;
							separationv[2] += (thispt.getZ()-vpt.getZ())/20;
						}
					}
					if (aln) {
						alignmentv[0] += ((VP1)obj).getX();
						alignmentv[1] += ((VP1)obj).getY();
						alignmentv[2] += ((VP1)obj).getZ();
						counta++;
					}
				}
			}
			if (count > 0) {
				if (coh) {
					cohesionv[0] = ((cohesionv[0]/count)-thispt.getX())/100;
					cohesionv[1] = ((cohesionv[1]/count)-thispt.getY())/100;
					cohesionv[2] = ((cohesionv[2]/count)-thispt.getZ())/100;
				}
			}
			if (counta > 0) {
				if (aln) {
					alignmentv[0] = (alignmentv[0]/counta);//-this.getX())/8;
					alignmentv[1] = (alignmentv[1]/counta);//-this.getY())/8;
					alignmentv[2] = (alignmentv[2]/counta);//-this.getZ())/8;
				}
			}
		}*/
		
		retpt[0] = (cohesiong[0] + cohesionv[0])/2 + 
					(separationg[0] + separationv[0])/2 + 
					(alignmentg[0] + alignmentv[0])/2;
		retpt[1] = (cohesiong[1] + cohesionv[1])/2 + 
					(separationg[1] + separationv[1])/2 + 
					(alignmentg[1] + alignmentv[1])/2;
		retpt[2] = (cohesiong[2] + cohesionv[2])/2 + 
					(separationg[2] + separationv[2])/2 + 
					(alignmentg[2] + alignmentv[2])/2;
		if (retpt[0]==0 && retpt[1]==0 && retpt[2]==0) {
			this.genXYZ();
			retpt[0] = this.getX();
			retpt[1] = this.getY();
			retpt[2] = this.getZ();
		} else {
			AgentGeometry.trim(retpt, vperr/2);//maybe hardcode this?
			this.setX(retpt[0]);
			this.setY(retpt[1]);
			this.setZ(retpt[2]);
		}
		space.moveByDisplacement(this, retpt);
		moveTick = tick;
		}
	}
}
