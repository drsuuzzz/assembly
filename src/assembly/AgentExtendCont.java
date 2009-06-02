package assembly;

import java.util.ArrayList;
import java.util.Iterator;

import assembly.Genome.GState;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class AgentExtendCont {
	
	public enum Loc {nucleus, cytoplasm};
	private Loc location;
	
	private Context theContext;
	private Boolean stop;
	private ContinuousSpace space;
	private double theta;
	private double phi;
	private double distance;
	private double X;
	private double Y;
	private double Z;
	private double moveTick;
	private String name;
	private boolean dead;
	private boolean moving; //moving between cytoplasm or nucleus
	private boolean bound;
	
	private int noBound;
	
	private ISchedulableAction move;
	private ISchedulableAction transcription;
	private ISchedulableAction export;
	private ISchedulableAction death;
	private ISchedulableAction splice;
	
	public AgentExtendCont() {
		//stop = false;
		theContext=null;
		dead = false;
		moving = false;
		noBound = 0;
		bound = false;
		//thetaPhiDistGen();
		//theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//phi = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//distance = RepastEssentials.RandomDraw(0, 2);
		moveTick = 0;
		name=null;
		move = null;
		transcription = null;
		export = null;
		death = null;
		splice = null;
	}
	//@Parameter(usageName="stop",displayName="Stopped")
/*	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}*/
	
	@Parameter(usageName="bound",displayName="Bound")
	public boolean isBound() {
		return bound;
	}


	public void setBound(boolean bound) {
		this.bound = bound;
	}
	@Parameter(usageName="noBound",displayName="Number Bound")
	public int getNoBound() {
		return noBound;
	}

	public void setNoBound(int noBound) {
		this.noBound = noBound;
	}


	public Loc getLocation() {
		return location;
	}

	public void setLocation(Loc location) {
		this.location = location;
	}
	@Parameter(usageName="dead",displayName="Dead")
	public boolean isDead() {
		return dead;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public ISchedulableAction getDeath() {
		return death;
	}

	public void setDeath(ISchedulableAction death) {
		this.death = death;
	}

	public ISchedulableAction getExport() {
		return export;
	}

	public ISchedulableAction getMove() {
		return move;
	}


	public void setMove(ISchedulableAction move) {
		this.move = move;
	}


	public void setExport(ISchedulableAction export) {
		this.export = export;
	}

	public ISchedulableAction getSplice() {
		return splice;
	}


	public void setSplice(ISchedulableAction splice) {
		this.splice = splice;
	}


	public Context getTheContext() {
		return theContext;
	}

	public void setTheContext(Context theContext) {
		this.theContext = theContext;
	}
	
	public ContinuousSpace getSpace() {
		return space;
	}
	
	public void setSpace(ContinuousSpace space) {
		this.space = space;
	}
	
/*	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}
	public double getPhi() {
		return phi;
	}
	public void setPhi(double phi) {
		this.phi = phi;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}*/
	
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public double getZ() {
		return Z;
	}
	public void setZ(double z) {
		Z = z;
	}
	@ProbeID()
	public String name() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void removeAnAction(ISchedulableAction action) {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		if (action != null) {
			schedule.removeAction(action);
		}
	}

	public double[] normPositionToGrid(double x1, double y1, double z1) {
		
		Dimensions dim = getSpace().getDimensions();
		x1 = x1 < 0 ? -x1 : x1;
		x1 = x1 >= dim.getWidth() ? (dim.getWidth() - x1 + dim.getWidth() -2) : x1;
		y1 = y1 < 0 ? -y1 : y1;
		y1 = y1 >= dim.getHeight() ? (dim.getHeight()- y1 + dim.getHeight() - 2) : y1;
		z1 = z1 < 0 ? -z1 : z1;
		z1 = z1 >= dim.getDepth() ? (dim.getDepth() - z1 + dim.getDepth() - 2) : z1;
		double ret[]={x1,y1,z1};
		return ret;
	}
	
	public double[] normPositionToBorder(double[] pt, double dist) {
		
		Dimensions dim = getSpace().getDimensions();
		//int d = (int)Math.ceil(dist);
		double min = dist;
		double maxX = dim.getWidth()-dist;
		double maxY = dim.getHeight()-dist;
		double maxZ = dim.getDepth()-dist;
		pt[0] = pt[0] < min ? (min+(min-pt[0])) : pt[0];
		pt[0] = pt[0] > maxX ? (maxX-(pt[0]-maxX)): pt[0];
		pt[1] = pt[1] < min ? (min+(min-pt[1])) : pt[1];
		pt[1] = pt[1] > maxY ? (maxY-(pt[1]-maxY)): pt[1];
		pt[2] = pt[2] < min ? (min+(min-pt[2])) : pt[2];
		pt[2] = pt[2] > maxZ ? (maxZ-(pt[2]-maxZ)): pt[2];
		return pt;
	}
	
/*	public void thetaPhiDistGen() {
		theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		phi = RepastEssentials.RandomDraw(0, Math.PI);
		distance = RepastEssentials.RandomDraw(0, 1);
		//System.out.println(name+","+theta+","+phi+","+distance);
	}*/
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
	
	public boolean nearWallGroup() {
		
		boolean retval = false;
		double r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
		ContinuousWithin list = new ContinuousWithin(getSpace(), this, r);
		Iterator l = list.query().iterator();
		while (l.hasNext()) {
			AgentExtendCont aec = (AgentExtendCont) l.next();
			NdPoint pt = getSpace().getLocation(aec);
			Dimensions dim = getSpace().getDimensions();
			if (pt.getX() < 1 || pt.getY() < 1 || pt.getZ() < 1) {
				retval = true;
			} else if ((pt.getX() > dim.getWidth()-2) || (pt.getY() > dim.getHeight()-2) || (pt.getZ() > dim.getDepth()-2)) {
				retval = true;
			}
		}

		return retval;
	}
	
	public void genXYZ() {
		X=RepastEssentials.RandomDraw(-1,1);
		Y=RepastEssentials.RandomDraw(-1,1);
		Z=RepastEssentials.RandomDraw(-1,1);
	}
	
	public void randomWalk() {

		double coord[] = {0.0,0.0,0.0};
		genXYZ();
		coord[0] = this.getX();
		coord[1] = this.getY();
		coord[2] = this.getZ();
		space.moveByDisplacement(this, coord);
	}
	
	public void largeStepAwayFrom(AgentExtendCont agent) {
		NdPoint thisp = space.getLocation(this);
		NdPoint pt = space.getLocation(agent);
		double vector[][] = AgentGeometry.pointDisplacement(thisp.toDoubleArray(null), pt.toDoubleArray(null));
		double disp[] = {0.0,0.0,0.0};
		disp[0] = vector[0][0]*4;
		disp[1] = vector[1][0]*4;
		disp[2] = vector[2][0]*4;
		space.moveByDisplacement(this, disp);
	}
	public int calcMax(Class agentType1, Class agentType2) {
		int x =0;
		if (agentType1.getName().equals(VP2.class.getName()) || agentType2.getName().equals(VP3.class.getName())) {
			x = 5;
		} else if (agentType1.getName().equals(MRNA.class.getName())){
			x = 20;
		} else {
			x = 1;
		}
		return x;
	}
	
	public double[] calcDisplacement(Class agentType1, Class agentType2, double distc, double cerr, double distn, double nerr) {
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
		
		ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(space,this,(distc+cerr));
		Iterator<AgentExtendCont> l = list.query().iterator();
		boolean cAgent = false;
		double center[] = {0,0,0};
		int max = calcMax(agentType1,agentType2);
		
		while (l.hasNext()) {
			AgentExtendCont obj = l.next();
			
			if (obj.getClass().getName().equals(agentType1.getName()) || obj.getClass().getName().equals(agentType2.getName())) {
				if (obj instanceof Genome) {
					if (((Genome)obj).getState()==GState.replicate) {
						max = 2;
						if (!((Genome)obj).needAgent(this)) {
							break;
						} else {
							((Genome)obj).setBoundProteins(this);
						}
					}
					if (((Genome)obj).getState() == GState.early) {
						if (this instanceof LgTAg) {
							((Genome)obj).setState(GState.replicate);
						}
					}
					if (((Genome)obj).getState() == GState.late) {
						if (this instanceof VP123) {
							((Genome)obj).setState(GState.assembly);
						}
					}
				}
					
				if (obj.getNoBound() < max || (obj.getNoBound() == max && this.isBound())) {
					cAgent = true;
					center[0] = space.getLocation(obj).getX();
					center[1] = space.getLocation(obj).getY();
					center[2] = space.getLocation(obj).getZ();
					if (coh) {
						cohesiong[0] = (center[0]-thispt.getX())/100;
						cohesiong[1] = (center[1]-thispt.getY())/100;
						cohesiong[2] = (center[2]-thispt.getZ())/100;
					}
					if (sep) {
						if (AgentGeometry.calcDistance(center, thispt) < (distc-cerr)) {
							separationg[0] = (thispt.getX()-center[0])/20;
							separationg[1] = (thispt.getY()-center[1])/20;
							separationg[2] = (thispt.getZ()-center[2])/20;
						}
					}
					if (aln) {
						alignmentg[0] = obj.getX();
						alignmentg[1] = obj.getY();
						alignmentg[2] = obj.getZ();
					}
					break;
				}
			}
		}

		int count=0;
		int counta = 0;
		list = new ContinuousWithin<AgentExtendCont>(space,this,(distn+nerr));
		l = list.query().iterator();
		if (cAgent || isBound()) {
			setBound(true);
			while (l.hasNext()) {
				AgentExtendCont obj = l.next();
				if (obj.getClass().getName().equals(this.getClass().getName()) && obj.isBound()) {
					//AgentExtendCont vp =  obj;
					NdPoint vpt = space.getLocation(obj);
					if (AgentGeometry.calcDistance(center,vpt) < (distc+cerr)) {
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (distn-nerr)) {
								separationv[0] += (thispt.getX()-vpt.getX())/30;
								separationv[1] += (thispt.getY()-vpt.getY())/30;
								separationv[2] += (thispt.getZ()-vpt.getZ())/30;
							}
						}
						if (aln) {
							alignmentv[0] += obj.getX();
							alignmentv[1] += obj.getY();
							alignmentv[2] += obj.getZ();
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
					alignmentv[0] = ((alignmentv[0]/counta)  );
					alignmentv[1] = ((alignmentv[1]/counta));
					alignmentv[2] = ((alignmentv[2]/counta));
				}
			}
		} else {
			setBound(false);
		}
		retpt[0] = (cohesiong[0] + cohesionv[0]) + 
					(separationg[0] + separationv[0]) + 
					(alignmentg[0] + alignmentv[0]);
		retpt[1] = (cohesiong[1] + cohesionv[1]) + 
					(separationg[1] + separationv[1]) + 
					(alignmentg[1] + alignmentv[1]);
		retpt[2] = (cohesiong[2] + cohesionv[2]) + 
					(separationg[2] + separationv[2]) + 
					(alignmentg[2] + alignmentv[2]);
		if (retpt[0]==0 && retpt[1]==0 && retpt[2]==0) {

		} else {
			AgentGeometry.trim(retpt, nerr/2);//maybe hardcode this?
			this.setX(retpt[0]);
			this.setY(retpt[1]);
			this.setZ(retpt[2]);
		}
		return retpt;
	}
	
	public double[] calcDispIfCenter(Class centerType1, Class centerType2, Class agentType1,Class agentType2,
			double distc, double cerr) {
		double coord[] = {0,0,0};
		NdPoint thispt = this.getSpace().getLocation(this);

		boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		//boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");
		
		ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(this.getSpace(), this, (distc+cerr));
		Iterator neighbors = list.query().iterator();
		
		double align[] = {0,0,0};
		int count = 0;
		int max = 0;
		if (this instanceof VP2 || this instanceof VP3) {
			max = 5;
		} else if (this instanceof MRNA){
			max = 20;
		} else if (this instanceof Genome) {
			if (((Genome)this).getState()==GState.replicate) {
				max = 2;
			} else {
				max = 1;
			}
		} else {
			max = 1;
		}
		while(neighbors.hasNext()){
			AgentExtendCont aec = (AgentExtendCont)neighbors.next();

			if (aec.getClass().getName().equals(centerType1.getName()) || aec.getClass().getName().equals(centerType2.getName())) {
				if (aln) {
					if (getNoBound() < max || (getNoBound() == max && aec.isBound())) {
						align[0] += aec.getX();
						align[1] += aec.getY();
						align[2] += aec.getZ();
						setBound(true);
						count++;
					}
				}
			}
		}
		if (count > 0) {
			double x = RandomHelper.nextDoubleFromTo(-.2, .2);
			double y = RandomHelper.nextDoubleFromTo(-.2, .2);
			double z = RandomHelper.nextDoubleFromTo(-.2, .2);
			align[0] = (align[0])/(count)/*+this.getX())/8*/;
			align[1] = (align[1])/(count)/*+this.getY())/8*/;
			align[2] = (align[2])/(count)/*+this.getZ())/8*/;
		}
		setNoBound(count);

		//keep distance from other vp2 & vp3
		list = new ContinuousWithin(this.getSpace(), this, 2.5*(distc+cerr));
		neighbors = list.query().iterator();
		double separ[] = {0.0,0.0,0.0};
		while (neighbors.hasNext()) {
			Object obj = neighbors.next();
			if (obj.getClass().getName().equals(agentType1.getName()) || obj.getClass().getName().equals(agentType2.getName())) {
				if (sep) {
					NdPoint other = this.getSpace().getLocation(obj);
					//if (AgentGeometry.calcDistanceNdPoints(other, thispt) < 2*(r+rerr)) {
						separ[0] += (thispt.getX()-other.getX())/10;
						separ[1] += (thispt.getY()-other.getY())/10;
						separ[2] += (thispt.getZ()-other.getZ())/10;
					//}
				}
			}
		}
		
		coord[0] = align[0] + separ[0];
		coord[1] = align[1] + separ[1];
		coord[2] = align[2] + separ[2];
		if (coord[0]==0 && coord[1]==0 && coord[2]==0) {
			;
		} else {
			AgentGeometry.trim(coord, cerr);
			this.setX(coord[0]);
			this.setY(coord[1]);
			this.setZ(coord[2]);
		}
		return coord;
	}
		
	public void removeScheduledActions() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		if (move != null) {
			schedule.removeAction(move);
			move = null;
		}
		if (transcription != null) {
			schedule.removeAction(transcription);
			transcription = null;
		}
		if (export != null) {
			schedule.removeAction(export);
			export = null;
		}
		if (death != null) {
			schedule.removeAction(death);
			death = null;
		}
		if (splice != null) {
			schedule.removeAction(splice);
			splice = null;
		}
	}
	
	public void die() {
		if (!dead) {
			//if (this instanceof MRNA) {
				//if (((MRNA) this).getLocation() == Loc.cytoplasm) {
					((Cytoplasm)getTheContext()).addToRemList(this);
			//	} else {
					//((Nucleus)getTheContext()).addToRemList(this);
				//}
			//}
			this.setDead(true);
			this.setTheContext(null);
			this.setSpace(null);
		}
	}
	
	public void bind() {
		
	}
	
	public void transcription() {
		
	}
	
	public void translation() {
		
	}
}
