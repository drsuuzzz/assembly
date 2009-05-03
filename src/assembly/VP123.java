package assembly;

//import repast.simphony.context.Context;
import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
//import repast.simphony.essentials.RepastEssentials;
//import repast.simphony.space.SpatialMath;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
//import repast.simphony.space.grid.GridPoint;
import repast.simphony.parameter.Parameter;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.continuous.ContinuousWithin;
//import repast.simphony.util.ContextUtils;

public class VP123 extends AgentExtendCont{
	
	//private static final int HEXA = 6;
	//private static final int PENTA = 5;
/*	private static final int BETA = 1;
	private static final int BETAP = 0;
	private static final int GAMMA = 5;
	private static final int ALPHAP = 4;
	private static final int ALPHAPP = 2;
	private static final int ALPHA = 3;
	private static final int ALPHA0 = 0;
	private static final int ALPHA1 = 1;
	private static final int ALPHA2 = 2;
	private static final int ALPHA3 = 3;
	private static final int ALPHA4 = 4;

	
	private static boolean nucleating = false;*/
	
//	private int nSides = 6;
	public enum State {PENT, HEX};
	public enum Bound {BOUND, UNBOUND};
	private State state;
	private Bound bound;
	
	//angles
	private double defectPent; //60 degrees + pi/2
	private double defectHex;
	private double normal[] = {0f, 0f, 1f};
	private ArrayList<NdPoint> ptList;
	/*private double ptList[][] = {{0.0,0.0,0.0,0.0,0.0},
								 {0.0,0.0,0.0,0.0,0.0},
								 {0.0,0.0,0.0,0.0,0.0},
								 {0.0,0.0,0.0,0.0,0.0}
	};*/
	
	//
	//private boolean complete;
	//private boolean nucleate;
	
	//private double rotatetheta;
	//private double rotatephi;
	//private double angles[] = {0,60,119,175,231,287};
	//private double phi0;
	
	private double golden;
	
	//neighbors
	private Genome genome;
	//private VP1 sides[] = new VP1[6];

	//private boolean flock;
	
	private double moveTick;
	private double move2Tick;
	private double neighborTick;
	private static int id = 0;
	private int myid;
	
	public VP123() {
		super();
		state = State.PENT;
		bound = Bound.UNBOUND;
		//defectPent = 60*Math.PI/180;
		//ptList = null;
		//defectPent = ;  
		//double dist = (Double)RunEnvironment.getInstance().getParameters().getValue("distance");
		//ptList = AgentGeometry.calcPentPts(defectPent, dist);
		/*for (int i = 0; i < nSides; i++) {
			sides[i] = null;
		}*/
		//complete = false;
		//nucleate = false;
		//rotatetheta = 0;
		//rotatephi = 0;
		//phi0 = 90;
		genome = null;
		//flock=false;
		moveTick = 0;
		move2Tick = 0;
		neighborTick = 0;
		setName("VP1");
		myid=id++;
		this.genXYZ();
	}
	
/*	@Parameter(usageName="complete",displayName="Sides completed")
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	@Parameter(usageName="type",displayName="Sides")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Parameter(usageName="flock",displayName = "Flock")
	public boolean isFlock() {
		return flock;
	}

	public void setFlock(boolean flock) {
		this.flock = flock;
	}


	public void moveMolecule() {
		//for assembly type 2
		if (genome != null) {
			double phi = genome.getPhi();
			double theta = genome.getTheta();
			double dist = genome.getDistance();
			double coord[] = {0.0,0.0,0.0};
			coord[0] = dist*Math.sin(theta)*Math.sin(phi); //x
			coord[1] = dist*Math.cos(phi);                 //y
			coord[2] = dist*Math.cos(theta)*Math.sin(phi); //z
			ContinuousSpace space = this.getSpace();
			space.moveByDisplacement(this, coord);
		}
	}*/
	
/*	public VP1 getSides(int index) {
		return sides[index];
	}

	public void setSides(int index, VP1 neighbor) {
		this.sides[index] = neighbor;
	}*/
	
	public Genome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}
	
	@Parameter(usageName="state",displayName="State", converter = "assembly.StateConverter")
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	@Parameter(usageName="bound",displayName="Bound State", converter = "assembly.BoundConverter")
	public Bound getBound() {
		return bound;
	}

	public void setBound(Bound bound) {
		this.bound = bound;
	}
	public double [] findCenter(Iterator l) {
		double [] c={0.0,0.0,0.0};
		int count=0;
		while (l.hasNext()) {
			Object obj = l.next();
			if (obj instanceof VP123) {
				NdPoint pt = this.getSpace().getLocation(obj);
				c[0] += pt.getX();
				c[1] += pt.getY();
				c[2] += pt.getZ();
				count++;
			}
		}
		if (count > 0) {
			NdPoint thispt = this.getSpace().getLocation(this);
			c[0] = (c[0]+thispt.getX())/(count+1);
			c[1] = (c[1]+thispt.getY())/(count+1);
			c[2] = (c[2]+thispt.getZ())/(count+1);
		}
		return c;
	}
	
	public int getSize (Iterator list) {
		
		int i =0;
		while (list.hasNext()) {
			list.next();
			i++;
		}
		return i;
	}
	
	public void move4(){
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > move2Tick) {
			double retpt[] = {0.0,0.0,0.0};
			double cohesiong[]={0,0,0};
			double cohesionv[]={0,0,0};
			double separationg[]={0,0,0};
			double separationv[]={0,0,0};
			double alignmentg[]={0,0,0};
			double alignmentv[]={0,0,0};
			
			ContinuousSpace space = getSpace();
			NdPoint thispt = space.getLocation(this);
			
			boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
			boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
			boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");

			double radius;
			double vpradius;
			double vplerr;
			double rerr;
			double vpserr;
			if (RunEnvironment.getInstance().isBatch()){
				radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
				vpserr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
				vplerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			} else {
				radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
				vpserr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
				vplerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			}

			int count=0;
			int counta = 0;
			ContinuousWithin list;
			Iterator l;
			
			count=0;
			counta=0;
			
			int i=1;  //list won't include self

			list = new ContinuousWithin(space,this,(vpradius+vplerr));
			l = list.query().iterator();
			int size = getSize(l);
			ArrayList refpts = null;
			if (size >= 2) {
				double cent[] = findCenter(list.query().iterator());  //including this point
				//guess based on neighbors where this guy should be and move there
				refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, thispt.toDoubleArray(null), cent);
				//double loc[][]=AgentGeometry.objectsWithin();
			}
			count=0;
			counta=0;
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					NdPoint pt = space.getLocation(obj);
					if (coh) {
						cohesiong[0] += pt.getX();
						cohesiong[1] += pt.getY();
						cohesiong[2] += pt.getZ();
						count++;
					}
					if (sep) {
						if (AgentGeometry.calcDistanceNdPoints(pt, thispt) < 2*(radius-rerr)) {
							separationg[0] = (thispt.getX()-pt.getX())/10;
							separationg[1] = (thispt.getY()-pt.getY())/10;
							separationg[2] = (thispt.getZ()-pt.getZ())/10;
						}
					}
					/*if (aln) {
						//pseudo alignment values since center doesn't exist
						//double x = RepastEssentials.RandomDraw(-1,1);
						//double y = RepastEssentials.RandomDraw(-1,1);
						//double z = RepastEssentials.RandomDraw(-1,1);
						alignmentg[0] += pt.getX();
						alignmentg[1] += pt.getY();
						alignmentg[2] += pt.getZ();
						counta++;
					}*/
				}
			}
			if (count > 0) {
				if (coh) {
					//double mypt[] = space.getLocation(this).toDoubleArray(null);
					cohesiong[0] = ((cohesiong[0]/count)-thispt.getX())/100;
					cohesiong[1] = ((cohesiong[1]/count)-thispt.getY())/100;
					cohesiong[2] = ((cohesiong[2]/count)-thispt.getZ())/100;
				}
			}
			//if (counta > 0) {
				if (aln) {
					double x = RepastEssentials.RandomDraw(-1,1);
					double y = RepastEssentials.RandomDraw(-1,1);
					double z = RepastEssentials.RandomDraw(-1,1);
					alignmentg[0] = (x-this.getX())/8;
					alignmentg[1] = (y-this.getY())/8;
					alignmentg[2] = (z-this.getZ())/8;
				}
			//}
			list = new ContinuousWithin(space,this,(vpradius+vplerr));
			l = list.query().iterator();
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					VP123 vp = (VP123) obj;
					NdPoint vpt = space.getLocation(vp);
					if (coh) {
						cohesionv[0] += vpt.getX();
						cohesionv[1] += vpt.getY();
						cohesionv[2] += vpt.getZ();
						count++;
					}
					if (sep) {
						if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (vpradius-vpserr)) {
							separationv[0] += (thispt.getX()-vpt.getX())/20;
							separationv[1] += (thispt.getY()-vpt.getY())/20;
							separationv[2] += (thispt.getZ()-vpt.getZ())/20;
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
			if (count > 0) {
				if (coh) {
					double mypt[] = space.getLocation(this).toDoubleArray(null);
					cohesionv[0] = ((cohesionv[0]/count)-mypt[0])/100;
					cohesionv[1] = ((cohesionv[1]/count)-mypt[1])/100;
					cohesionv[2] = ((cohesionv[2]/count)-mypt[2])/100;
				}
			}
			if (counta > 0) {
				if (counta <=2) {
					alignmentv[0] += RepastEssentials.RandomDraw(-1,1);
					alignmentv[1] += RepastEssentials.RandomDraw(-1,1);
					alignmentv[2] += RepastEssentials.RandomDraw(-1,1);
				}
				if (aln) {
					alignmentv[0] = ((alignmentv[0]/counta)-this.getX())/8;
					alignmentv[1] = ((alignmentv[1]/counta)-this.getY())/8;
					alignmentv[2] = ((alignmentv[2]/counta)-this.getZ())/8;
				}
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
				this.genXYZ();
				retpt[0] = this.getX();
				retpt[1] = this.getY();
				retpt[2] = this.getZ();
			} else {
				AgentGeometry.trim(retpt, rerr);
				this.setX(retpt[0]);
				this.setY(retpt[1]);
				this.setZ(retpt[2]);
			}
			space.moveByDisplacement(this, retpt);
			move2Tick = tick;
		}
	}
	
	public double[] findPseudoCenter() {
		//as if there is a genome there
		double radius;
		double rerr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
		}
		NdPoint thispt = this.getSpace().getLocation(this);
		//find all points within 2 times radius
		ContinuousWithin list = new ContinuousWithin(this.getSpace(),this,2*(radius+rerr));
		Iterator l = list.query().iterator();
		double center[] = {thispt.getX(),thispt.getY(),thispt.getZ()};
		int count=1;
		while (l.hasNext()) {
			Object obj = l.next();
			if (obj instanceof VP123) {
				VP123 vp = (VP123) obj;
				NdPoint vpt = this.getSpace().getLocation(vp);
				center[0] += vpt.getX();
				center[1] += vpt.getY();
				center[2] += vpt.getZ();
				count++;
			}
		}
		if (count > 1) {
			center[0] = center[0]/count;
			center[1] = center[1]/count;
			center[2] = center[2]/count;
			//calculate plane
			double normal[] = {center[0]-thispt.getX(),center[1]-thispt.getY(),center[2]-thispt.getZ()};
			double d = -(normal[0]*thispt.getX()+normal[1]*thispt.getY()+normal[2]*thispt.getZ());
			l = list.query().iterator();
			//re-initialize center to current agent
			center[0]=thispt.getX();
			center[1]=thispt.getY();
			center[2]=thispt.getZ();
			count=1;
			//only objects in direction of pseudo center
			while(l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					VP123 vp = (VP123) obj;
					NdPoint vpt = this.getSpace().getLocation(vp);
					double sign = normal[0]*vpt.getX()+normal[1]*vpt.getY()+normal[2]*vpt.getZ()+d;
					if (sign >=0) {
						center[0] += vpt.getX();
						center[1] += vpt.getY();
						center[2] += vpt.getZ();
						count++;
					}
				}
			}
			//new center in direction of our capsid assembly
			center[0] = center[0]/count;
			center[1] = center[1]/count;
			center[2] = center[2]/count;
			//initialize to current agent
			if (count > 8) {
				double nc[] = {0,0,0};//{thispt.getX(),thispt.getY(),thispt.getZ()};
				count=0;
				l = list.query().iterator();
				while(l.hasNext()) {
					Object obj = l.next();
					if (obj instanceof VP123) {
						NdPoint vpt = this.getSpace().getLocation(obj);
						if (AgentGeometry.calcDistance(center, vpt) <= (radius+rerr)) {
							nc[0] += vpt.getX();
							nc[1] += vpt.getY();
							nc[2] += vpt.getZ();
							count++;
						}
					}
				}
				center[0] = nc[0]/count;
				center[1] = nc[1]/count;
				center[2] = nc[2]/count;
				
			} else {
				//less then 12
				//System.out.println("Count: "+count);
			}
		} 
		if (center[0] == thispt.getX() && center[1] == thispt.getY() && center[2] == thispt.getZ()) {
			center[0] = 0.0;
			center[1] = 0.0;
			center[2] = 0.0;
		}
		return center;
				
	}

	public void move3() {
		//center of mass of group
		
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > move2Tick) {
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
		double vpserr;
		double rerr;
		double vplerr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
			vplerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			vpserr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
			vplerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			vpserr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
		}

		//find the pseudo-center
		double center[] = {0.0,0.0,0.0};
		center = findPseudoCenter();
		/*if (AgentGeometry.calcDistance(center, thispt) > (radius+rerr)) {
			center[0] = 0.0;
			center[1] = 0.0;
			center[2] = 0.0;
		}*/
		if (center[0] != 0.0 && center[1] != 0.0 && center[2] != 0.0) {
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
				//pseudo alignment values since center doesn't exist
				double x = RepastEssentials.RandomDraw(-1,1);
				double y = RepastEssentials.RandomDraw(-1,1);
				double z = RepastEssentials.RandomDraw(-1,1);
				alignmentg[0] = (x-this.getX())/8;
				alignmentg[1] = (y-this.getY())/8;
				alignmentg[2] = (z-this.getZ())/8;
			}
		}

		//adjust between VP1 agents
		ContinuousWithin list = new ContinuousWithin(space,this,(vpradius+vplerr));
		Iterator l = list.query().iterator();
		if (center[0] != 0.0 && center[1] != 0.0 && center[2] != 0.0) {
			int count=0;
			int counta=0;
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					VP123 vp = (VP123) obj;
					NdPoint vpt = space.getLocation(vp);
					if (AgentGeometry.calcDistance(center, vpt) < (radius+rerr)) { 
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (vpradius-vpserr)) {
								separationv[0] += (thispt.getX()-vpt.getX())/20;
								separationv[1] += (thispt.getY()-vpt.getY())/20;
								separationv[2] += (thispt.getZ()-vpt.getZ())/20;
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
					double mypt[] = space.getLocation(this).toDoubleArray(null);
					cohesionv[0] = ((cohesionv[0]/count)-mypt[0])/100;
					cohesionv[1] = ((cohesionv[1]/count)-mypt[1])/100;
					cohesionv[2] = ((cohesionv[2]/count)-mypt[2])/100;
				}
			}
			if (counta > 0) {
				if (aln) {
					alignmentv[0] = ((alignmentv[0]/counta)-this.getX())/8;
					alignmentv[1] = ((alignmentv[1]/counta)-this.getY())/8;
					alignmentv[2] = ((alignmentv[2]/counta)-this.getZ())/8;
				}
			}
		} else {
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					if (sep) {
						NdPoint vpt = this.getSpace().getLocation(obj);
						if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (vpradius-vpserr)) {
							separationv[0] += (thispt.getX()-vpt.getX())/20;
							separationv[1] += (thispt.getY()-vpt.getY())/20;
							separationv[2] += (thispt.getZ()-vpt.getZ())/20;
						}
					}
				}
			}
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
			this.genXYZ();
			retpt[0] = this.getX();
			retpt[1] = this.getY();
			retpt[2] = this.getZ();
		} else {
			AgentGeometry.trim(retpt, rerr);
			this.setX(retpt[0]);
			this.setY(retpt[1]);
			this.setZ(retpt[2]);
		}
		space.moveByDisplacement(this, retpt);
		move2Tick = tick;
		}
	}
	
	public void move2() {
		//cohesion
		//center of mass around genome or otherwise
		
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > move2Tick) {
		double retpt[] = {0.0,0.0,0.0};
		double cohesiong[]={0,0,0};
		double cohesionv[]={0,0,0};
		double separationg[]={0,0,0};
		double separationv[]={0,0,0};
		double alignmentg[]={0,0,0};
		double alignmentv[]={0,0,0};

		NdPoint tmp = this.getSpace().getLocation(this);
		
		boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
		boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");

		ContinuousSpace space = getSpace();
		double radius;
		double vpradius;
		double vpserr;
		double rerr;
		double vplerr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
			vplerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			vpserr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsid");
			vplerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidLookError");
			vpserr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceCapsidSepError");
		}
		ContinuousWithin list = new ContinuousWithin(space,this,(radius+rerr));
		Iterator l = list.query().iterator();
		boolean gFound = false;
		double center[] = {0,0,0};
		while (l.hasNext()) {
			Object obj = l.next();
			if (obj instanceof Genome) {
				gFound = true;
				genome = (Genome)obj;
				center[0] = space.getLocation(genome).getX();
				center[1] = space.getLocation(genome).getY();
				center[2] = space.getLocation(genome).getZ();
				NdPoint ptv = space.getLocation(this);
				if (coh) {
					cohesiong[0] = (center[0]-ptv.getX())/100;
					cohesiong[1] = (center[1]-ptv.getY())/100;
					cohesiong[2] = (center[2]-ptv.getZ())/100;
				}
				if (sep) {
					if (AgentGeometry.calcDistance(center, ptv) < (radius-rerr)) {
						separationg[0] = (ptv.getX()-center[0])/10;
						separationg[1] = (ptv.getY()-center[1])/10;
						separationg[2] = (ptv.getZ()-center[2])/10;
					}
				}
				if (aln) {
					alignmentg[0] = (genome.getX()-this.getX())/8;
					alignmentg[1] = (genome.getY()-this.getY())/8;
					alignmentg[2] = (genome.getZ()-this.getZ())/8;
				}
				break;
			}
		}

		int count=0;
		int counta = 0;
		list = new ContinuousWithin(space,this,(vpradius+vplerr));
		l = list.query().iterator();
		if (gFound) {
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					VP123 vp = (VP123) obj;
					NdPoint vpt = space.getLocation(vp);
					NdPoint gpt = space.getLocation(genome);
					if (AgentGeometry.calcDistanceNdPoints(vpt, gpt) < (radius+rerr)) {
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							NdPoint tvp = space.getLocation(this);
							if (AgentGeometry.calcDistanceNdPoints(vpt, tvp) < (vpradius-vpserr)) {
								separationv[0] += (tvp.getX()-vpt.getX())/20;
								separationv[1] += (tvp.getY()-vpt.getY())/20;
								separationv[2] += (tvp.getZ()-vpt.getZ())/20;
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
					double mypt[] = space.getLocation(this).toDoubleArray(null);
					cohesionv[0] = ((cohesionv[0]/count)-mypt[0])/100;
					cohesionv[1] = ((cohesionv[1]/count)-mypt[1])/100;
					cohesionv[2] = ((cohesionv[2]/count)-mypt[2])/100;
				}
			}
			if (counta > 0) {
				if (aln) {
					alignmentv[0] = ((alignmentv[0]/counta)-this.getX())/8;
					alignmentv[1] = ((alignmentv[1]/counta)-this.getY())/8;
					alignmentv[2] = ((alignmentv[2]/counta)-this.getZ())/8;
				}
			}
		} else {
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123) {
					if (sep) {
						NdPoint tvp = space.getLocation(this);
						NdPoint vpt = space.getLocation(obj);
						if (AgentGeometry.calcDistanceNdPoints(vpt, tvp) < (vpradius-vpserr)) {
							separationv[0] += (tvp.getX()-vpt.getX())/20;
							separationv[1] += (tvp.getY()-vpt.getY())/20;
							separationv[2] += (tvp.getZ()-vpt.getZ())/20;
						}
					}
				}
			}
		}
		
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
			AgentGeometry.trim(retpt, vpserr);
			this.setX(retpt[0]);
			this.setY(retpt[1]);
			this.setZ(retpt[2]);
		}
		space.moveByDisplacement(this, retpt);
		move2Tick = tick;
		}
	}
		
/*	public double[] center(double c[]) {
		//cohesion
		//center of mass around genome or otherwise
		double retpt[] = {0.0,0.0,0.0};
		boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
		if (!coh)
			return retpt;
		ContinuousSpace space = getSpace();
		double radius;
		double vpradius;
		double err;
		double rerr;
		double vperr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			err = (Float)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			err = (Double)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		}
		
		ArrayList list = AgentGeometry.objectsWithin(space, this, c,(radius+rerr));
		boolean gFound = false;
		NdPoint ptg=null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Genome) {
				gFound = true;
				genome = (Genome)list.get(i);
				ptg = space.getLocation(genome);
				NdPoint ptv = space.getLocation(this);
				double dist = AgentGeometry.calcDistance(c,ptg);//space.getDistance(ptvp,ptg);
				if (dist > radius) {
				double v[] = {(ptg.getX()-ptv.getX()),(ptg.getY()-ptv.getY()),(ptg.getZ()-ptv.getZ())};

					//double l = AgentGeometry.lengthOfVectorSquared(v);
					//v = AgentGeometry.unit(v);
					retpt[0] = retpt[0]+v[0]/radius;  
					retpt[1] = retpt[1]+v[1]/radius;
					retpt[2] = retpt[2]+v[2]/radius;
				}
				break;
			}
		}
		double pt[] = {0f, 0f, 0f};
		if (!gFound) {
			int count=0;
			genome = null;
			list = AgentGeometry.objectsWithin(space, this, c, (vpradius+vperr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					NdPoint vpt = space.getLocation(vp);
					pt[0] += vpt.getX();
					pt[1] += vpt.getY();
					pt[2] += vpt.getZ();
					count++;
				}
			}
			//find center of "mass"
			if (count >0) {
				ArrayList refpts = new ArrayList();
				refpts.addAll(ptList);
				pt[0] = pt[0]/count;
				pt[1] = pt[1]/count;
				pt[2] = pt[2]/count;
				//rotate it to a point
				//list = AgentGeometry.objectsWithin(space, this, c, (vpradius+vperr));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof VP1) {
						VP1 vp = (VP1) list.get(i);
						if (vp.getBound() == Bound.BOUND) {
							double fpt[] = space.getLocation(vp).toDoubleArray(null);
							refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, fpt, c);
							break;
						}
					}
				}
				//vector to center of mass from coordinate is the direction vector
				list = AgentGeometry.objectsWithinPts(space,this,refpts,pt,c,vperr);
				count=0;
				pt[0]=pt[1]=pt[2]=0;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof VP1) {
						this.setBound(Bound.BOUND);
						VP1 vp = (VP1) list.get(i);
						vp.setBound(Bound.BOUND);
						NdPoint vpt = space.getLocation(vp);
						pt[0] += vpt.getX();
						pt[1] += vpt.getY();
						pt[2] += vpt.getZ();
						count++;
					}
				}
				if (count > 0) {
					pt[0] = ((pt[0]/count)-c[0])/vpradius;
					pt[1] = ((pt[1]/count)-c[1])/vpradius;
					pt[2] = ((pt[2]/count)-c[2])/vpradius;
				} else {
					this.setBound(Bound.UNBOUND);
				}
			}
		} else {

			ArrayList refpts = new ArrayList();
			refpts.addAll(ptList);

			list = AgentGeometry.objectsWithin(space, this, c, (vpradius+vperr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					//if (vp.getBound() == Bound.BOUND) {
					//within vp1 distance
						double l = AgentGeometry.calcDistanceNdPoints(space.getLocation(vp), space.getLocation(genome));
					//within genome distance
						if (l <= (radius+rerr)) {
							double fpt[] = space.getLocation(vp).toDoubleArray(null);
							refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, fpt, c);
							//double[][] t = AgentGeometry.pointDisplacement(vp1, c);
							//rotation = AgentGeometry.calcThetaPhiAngles(space.getLocation(vp), c);
							break;
						}
					//}
				}
			}
			list = AgentGeometry.objectsWithinPts(space,this,refpts,ptg.toDoubleArray(null),c,vperr);
			int count=0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					this.setBound(Bound.BOUND);
					VP1 vp = (VP1) list.get(i);
					vp.setBound(Bound.BOUND);
					NdPoint vpt = space.getLocation(vp);
					pt[0] += vpt.getX();
					pt[1] += vpt.getY();
					pt[2] += vpt.getZ();
					count++;
				}
			}
			if (count > 0) {
				pt[0] = ((pt[0]/count)-c[0])/vpradius;
				pt[1] = ((pt[1]/count)-c[1])/vpradius;
				pt[2] = ((pt[2]/count)-c[2])/vpradius;
			} else {
				this.setBound(Bound.UNBOUND);
			}
		}
		retpt[0] = retpt[0] + pt[0];
		retpt[1] = retpt[1] + pt[1];
		retpt[2] = retpt[2] + pt[2];

		return retpt;
	}*/
	
/*	public double[] collision(double c[]) {
		//separation
		ContinuousSpace space = getSpace();
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");
		double pt[] = {0.0,0.0,0.0};
		if (!sep)
			return pt;
		//check genome collision
		boolean closeToG = false;
		double radius;
		double vpradius;
		double perr;
		double rerr;
		double vperr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			perr = (Float)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			perr = (Double)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		}
		ArrayList list = AgentGeometry.objectsWithin(space, this, c, (radius-rerr)); 
		NdPoint ptg = null;
		double refpt[]={0,0,0};
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Genome) {
				closeToG = true;
				genome = (Genome)list.get(i);
				ptg = space.getLocation(list.get(i));
				double v[][] = {{(ptg.getX()-c[0])},{(ptg.getY()-c[1])},{(ptg.getZ()-c[2])}};
				double l = AgentGeometry.lengthOfVectorSquared(v);
				v = AgentGeometry.unit(v);
				refpt[0] = refpt[0] - v[0][0]/radius;
				refpt[1] = refpt[1] - v[1][0]/radius;
				refpt[2] = refpt[2] - v[2][0]/radius;
				//pt[0] = pt[0] - (ptg.getX()-c[0])/radius;
				//pt[1] = pt[1] - (ptg.getY()-c[1])/radius;
				//pt[2] = pt[2] - (ptg.getZ()-c[2])/radius;
				break;
			}
		}
		//now find neighboring capsid proteins
		if (closeToG) {
			//double angles[] = AgentGeometry.calcThetaPhiAngles(ptg, c);
			//double rotation[] = {0f, 0f};  //theta, phi
			ArrayList refpts = new ArrayList();
			refpts.addAll(ptList);
			list = AgentGeometry.objectsWithin(space, this, c, (vpradius-vperr));
			//list = AgentGeometry.objectsWithin2Objects (space, this, c, (vpradius+vperr), genome, (radius+rerr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					//if (vp.getBound() == Bound.BOUND) {
						double l = AgentGeometry.calcDistanceNdPoints(space.getLocation(vp), space.getLocation(genome));
						if (l <= radius) {
							double fpt[] = space.getLocation(vp).toDoubleArray(null);
							refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, fpt, c);
							//rotation = AgentGeometry.calcThetaPhiAngles(space.getLocation(vp), c);
							break;
						}
					//}
				}
			}
			//ArrayList ptL = AgentGeometry.calcPentPts(defectPent, (vpradius-vperr), angles[0]+rotation[0], angles[1], c);
			list = AgentGeometry.objectsWithinPts(space,this,refpts,ptg.toDoubleArray(null),c,vperr);
			//list = AgentGeometry.objectsWithinPts(space,this,ptL,c,perr);
			int count = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					this.setBound(Bound.BOUND);
					VP1 vp = (VP1) list.get(i);
					vp.setBound(Bound.BOUND);
					NdPoint vpt = space.getLocation(vp);
					double v[][] = {{(c[0]-vpt.getX())},{(c[1]-vpt.getY())},{(c[2]-vpt.getZ())}};
					double l = AgentGeometry.lengthOfVectorSquared(v);
					v = AgentGeometry.unit(v);
					pt[0] += v[0][0];
					pt[1] += v[1][0];
					pt[2] += v[2][0];
					count++;
					//pt[0] = pt[0]-(vpt.getX()-c[0])/vpradius;  //fix
					//pt[1] = pt[1]-(vpt.getY()-c[1])/vpradius;
					//pt[2] = pt[2]-(vpt.getZ()-c[2])/vpradius;
				}
			}
			if (count == 0) {
				this.setBound(Bound.UNBOUND);
			} else {
				pt[0] = pt[0]/vpradius;
				pt[1] = pt[1]/vpradius;
				pt[2] = pt[2]/vpradius;
			}
			
		} else {
			//check vp1 collisions
			int count=0;
			genome = null;
			list = AgentGeometry.objectsWithin(space, this, c, (vpradius+vperr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					NdPoint vpt = space.getLocation(vp);
					pt[0] += vpt.getX();
					pt[1] += vpt.getY();
					pt[2] += vpt.getZ();
					count++;
				}
			}
			//find center of "mass"
			if (count >0) {
				ArrayList refpts = new ArrayList();
				refpts.addAll(ptList);
				pt[0] = pt[0]/count;
				pt[1] = pt[1]/count;
				pt[2] = pt[2]/count;
				//rotate it to a point
				//list = AgentGeometry.objectsWithin(space, this, c, (vpradius+vperr));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof VP1) {
						VP1 vp = (VP1) list.get(i);
						if (vp.getBound() == Bound.BOUND) {
							double fpt[] = space.getLocation(vp).toDoubleArray(null);
							refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, fpt, c);
							break;
						}
					}
				}
				//vector to center of mass from coordinate is the direction vector
				list = AgentGeometry.objectsWithinPts(space,this,refpts,pt,c,vperr);
				count=0;
				pt[0]=pt[1]=pt[2]=0;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof VP1) {
						this.setBound(Bound.BOUND);
						VP1 vp = (VP1) list.get(i);
						vp.setBound(Bound.BOUND);
						NdPoint vpt = space.getLocation(vp);
						pt[0] = pt[0]+(c[0]-vpt.getX());
						pt[1] = pt[1]+(c[1]-vpt.getY());
						pt[2] = pt[2]+(c[2]-vpt.getZ());
						count++;
					}
				}
				if (count > 0) {
					pt[0] = (pt[0]/vpradius);
					pt[1] = (pt[1]/vpradius);
					pt[2] = (pt[2]/vpradius);
				} else {
					this.setBound(Bound.UNBOUND);
				}
			}
		}
		
		pt[0] = pt[0]+refpt[0];
		pt[1] = pt[1]+refpt[1];
		pt[2] = pt[2]+refpt[2];

		return pt;
	}*/
	
/*	public double[] align(double[] pos) {
		//alignment
		//only the heading (the angles) information not the distance traveled.
		
		ContinuousSpace space = getSpace();
		double radius;
		double dist;
		double perr;
		double rerr;
		double vperr;
		if (RunEnvironment.getInstance().isBatch()){
			radius = (Float)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			dist = (Float)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			perr = (Float)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		} else {
			radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
			rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("radiusThreshold");
			dist = (Double)RunEnvironment.getInstance().getParameters().getValue("distance");
			vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			perr = (Double)RunEnvironment.getInstance().getParameters().getValue("errorThreshold");
		}
		ArrayList list = AgentGeometry.objectsWithin(space, this, pos,(radius+rerr));
		double pt[]={0.0,0.0,0.0};
		boolean align = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		if (!align)
			return pt;
		int count=0;
		boolean gfound = false;
		NdPoint ptg = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Genome) {
				gfound = true;
				genome = (Genome)list.get(i);
				ptg = space.getLocation(genome);
				pt[0] += Math.sin(genome.getTheta())*Math.sin(genome.getPhi());
				pt[1] += Math.cos(genome.getPhi());
				pt[2] += Math.cos(genome.getTheta())*Math.sin(genome.getPhi());
				count++;
				break;
			}
		}
		if (gfound) {
			//double angles[] = AgentGeometry.calcThetaPhiAngles(space.getLocation(genome), pos);
			//double rotation[] = {0f, 0f};  //theta, phi
			ArrayList refpts = new ArrayList();
			refpts.addAll(ptList);
			list = AgentGeometry.objectsWithin(space, this, pos, (dist+vperr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					//if (vp.getBound() == Bound.BOUND) {
						double l = AgentGeometry.calcDistanceNdPoints(space.getLocation(vp), space.getLocation(genome));
						if (l <= radius) {
							double fpt[] = space.getLocation(vp).toDoubleArray(null);
							refpts = AgentGeometry.rotateRefPtsAroundZAxis(ptList, fpt, pos);
							//rotation = AgentGeometry.calcThetaPhiAngles(space.getLocation(vp), pos);
							break;
						}
					//}
				}
			}
			//ArrayList ptL = AgentGeometry.calcPentPts(defectPent, (dist+vperr), angles[0]+rotation[0], angles[1], pos);
			//list = AgentGeometry.objectsWithinPts(space,this,refpts,pos,perr);
			list = AgentGeometry.objectsWithinPts(space,this,refpts,ptg.toDoubleArray(null),pos,vperr);

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					pt[0] += Math.sin(vp.getTheta())*Math.sin(vp.getPhi());
					pt[1] += Math.cos(vp.getPhi());
					pt[2] += Math.cos(vp.getTheta())*Math.sin(vp.getPhi());		
					count++;
				}
			}
		} else {
			genome = null;
			list = AgentGeometry.objectsWithin(space, this, pos,(dist+vperr));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof VP1) {
					VP1 vp = (VP1) list.get(i);
					pt[0] += Math.sin(vp.getTheta())*Math.sin(vp.getPhi());
					pt[1] += Math.cos(vp.getPhi());
					pt[2] += Math.cos(vp.getTheta())*Math.sin(vp.getPhi());		
					count++;
				}
			}
		}
		if (count > 0) {
			pt[0] += Math.sin(this.getTheta())*Math.sin(this.getPhi());
			pt[1] += Math.cos(this.getPhi());
			pt[2] += Math.cos(this.getTheta())*Math.sin(this.getPhi());
			count++;
			pt[0]=pt[0]/count;
			pt[1]=pt[1]/count;
			pt[2]=pt[2]/count;
		}
		return pt;
	}*/
	

/*	public void move() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			int asstype = (Integer)RunEnvironment.getInstance().getParameters().getValue("assembleType");

			NdPoint pt = getSpace().getLocation(this);
			if (genome == null) {
				thetaPhiDistGen();
			} else {
				this.setDistance(genome.getDistance());
				this.setTheta(genome.getTheta());
				this.setPhi(genome.getPhi());
			}
			double coord[] = {0.0,0.0,0.0};
			coord[0] = pt.getX()+this.getDistance()*Math.sin(this.getTheta())*Math.sin(this.getPhi()); //x
			coord[1] = pt.getY()+this.getDistance()*Math.cos(this.getPhi());                 		//y
			coord[2] = pt.getZ()+this.getDistance()*Math.cos(this.getTheta())*Math.sin(this.getPhi()); //z	
				
			double ptcol[] = collision(coord);
			coord[0] = coord[0]+ptcol[0];
			coord[1] = coord[1]+ptcol[1];
			coord[2] = coord[2]+ptcol[2];
			//double ptadh[] = align(coord);
			//coord[0] = coord[0]+ptadh[0];
			//coord[1] = coord[1]+ptadh[1];
			//coord[2] = coord[2]+ptadh[2];
			double ptcent[] = center(coord);
			coord[0] = coord[0] + ptcent[0] - pt.getX();
			coord[1] = coord[1] + ptcent[1] - pt.getY();
			coord[2] = coord[2] + ptcent[2] - pt.getZ();
				
			NdPoint p = getSpace().moveByDisplacement(this, coord);
				
			if (p == null) {
				System.out.println("bad point");
			}

			moveTick = tick;
		}
	}*/

}