package assembly;


import java.util.ArrayList;
import java.util.Iterator;

import assembly.MRNA.mState;
import assembly.VP123.VPType;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class Genome extends AgentExtendCont{
	
	//private String name;
/*	private static final double golden = 1.618033988749895;
	private static final double coord[][]={{0,golden,1},
		 {0,golden,-1},
		 {-golden,1,0},
		 {-1,0,golden},
		 {1,0,golden},
		 {golden,1,0},
		 {golden,-1,0},
		 {1,0,-golden},
		 {-1,0,-golden},
		 {-golden,-1,0},
		 {0,-golden,1},
		 {0,-golden,-1}, 
	};
	
	private static final double factor = 2;
	
	private VP1 vertices[]={null,null,null,null,null,null,null,null,null,null,null,null};
	
	private static double radius = 0.9510565163;
	
	private Network<VP1> network;
	*/
	public enum GState {early, replicate, late, assembly};
	private GState state;
	private double neighborTick;
	private double lmoveTick;
	private double moveTick;
	private double move2Tick;
	private double xcriptTick;
	private double repTick;
	private double egressTick;
	
	private AgentExtendCont bind1;
	private AgentExtendCont bind2;

	public Genome() {
		super();
		//name = "Genome";
		//network = null;
		neighborTick = 0;
		lmoveTick = 0;
		moveTick = 0;
		move2Tick = 0;
		xcriptTick = 0;
		egressTick = 0;
		setName("Genome");
		state = GState.early;
		this.genXYZ();
		bind1 = null;
		bind2 = null;
	}
	
	public void setBoundProteins (AgentExtendCont agent) {
		//routine only necessary for Tag recruitment of DNA Pol
		if (bind1 == null) {
			bind1 = agent;
		} else if (bind1 instanceof LgTAg) {
			if (agent instanceof DNAPol) {
				bind2 = agent;
			}
		} else if (bind1 instanceof DNAPol) {
			if (agent instanceof LgTAg) {
				bind2 = agent;
			}
		}
	}
	
	public void clearBoundProteins () {
		bind1 = null;
		bind2 = null;
	}
	
	public boolean needAgent (AgentExtendCont agent) {
		
		boolean need = false;
		if (bind1 != null) {
			if (bind1 instanceof LgTAg && bind2 == null) {
				if (agent instanceof DNAPol) {
					need = true;
				} else if (bind1.equals(agent)) {
					need = true;
				}
			} else if (bind1 instanceof DNAPol && bind2 == null) {
				if (agent instanceof LgTAg) {
					need = true;
				} else if (bind1.equals(agent)) {
					need = true;
				}
			} else if (bind1.equals(agent) || bind2.equals(agent)) {
				need = true;
			}
		} else if (agent instanceof DNAPol || agent instanceof LgTAg){
			need = true;
		}
		
		return need;
	}
	
/*	public Network getNetwork() {
		return network;
	}



	public void setNetwork(Network network) {
		this.network = network;
	}*/


	/*public void genomeMove(){
		//not used yet
		double coord[] = {0.0,0.0,0.0};
		thetaPhiDistGen();
		coord[0] = getDistance()*Math.sin(getTheta())*Math.sin(getPhi()); //x
		coord[1] = getDistance()*Math.cos(getPhi());                 //y
		coord[2] = getDistance()*Math.cos(getTheta())*Math.sin(getPhi()); //z
		NdPoint pt = getSpace().getLocation(this);
		
		double coord2[] = {0.0,0.0,0.0};
		coord2[0] = radius*factor*Math.sin(getTheta())*Math.sin(getPhi());
		coord2[1] = radius*factor*Math.cos(getPhi());
		coord2[2] = radius*factor*Math.cos(getTheta())*Math.sin(getPhi());
		
		NdPoint npt = new NdPoint(coord2[0]+pt.getX(),coord2[1]+pt.getY(),coord2[2]+pt.getZ());
		Dimensions d = getSpace().getDimensions();
		if (npt.getX() < 0 || npt.getX() >= d.getWidth()) {
			;
		} else if (npt.getY() < 0 || npt.getY() >=d.getHeight()) {
			;
		} else if (npt.getZ() < 0 || npt.getZ() >= d.getDepth()) {
			;
		} else {
			getSpace().moveByDisplacement(this, coord);
		}
	}
	
	public double[] center(double c[]) {
		
		double retpt[] = {0.0,0.0,0.0};
		ContinuousSpace space = getSpace();
		double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
		ArrayList list = AgentGeometry.objectsWithin(space,this,c,radius+2);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof VP1) {
				VP1 vp = (VP1)list.get(i);
				NdPoint ptv = space.getLocation(vp);
				double dist = AgentGeometry.calcDistance(c,ptv);//space.getDistance(ptvp,ptg);
				if (dist > radius) {
					retpt[0] = retpt[0]+(ptv.getX()-c[0])/radius;  //fix
					retpt[1] = retpt[1]+(ptv.getY()-c[1])/radius;
					retpt[2] = retpt[2]+(ptv.getZ()-c[2])/radius;
				}
			}
		}

		retpt[0]=retpt[0]/100;
		retpt[1]=retpt[1]/100;
		retpt[2]=retpt[2]/100;
		return retpt;
	}
	
	public double[] collision(double c[]) {
		
		ContinuousSpace space = getSpace();
		
		//check vp1 collisions
		double pt[] = {0.0,0.0,0.0};

		//check genome collision
		double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
		ArrayList list2 = AgentGeometry.objectsWithin(space,this,c, radius-1); 
		for (int i = 0; i < list2.size(); i++) {
			if (list2.get(i) instanceof VP1) {
				//double theta1 = getTheta();//((VP1)list2.get(i)).getTheta();
				//double phi1 = getPhi();//((VP1)list2.get(i)).getPhi();
				//double dist = getDistance();
				//pt[0] = pt[0] + dist*Math.sin((Math.PI + theta1))*Math.sin((Math.PI+phi1));
				//pt[1] = pt[1] + dist*Math.cos((Math.PI+phi1));
				//pt[2] = pt[2] + dist*Math.cos((Math.PI+theta1))*Math.sin((Math.PI+phi1));
				NdPoint ptv = space.getLocation(list2.get(i));
				pt[0] = pt[0] - (ptv.getX()-c[0])/(radius-1);
				pt[1] = pt[1] - (ptv.getY()-c[1])/(radius-1);
				pt[2] = pt[2] - (ptv.getZ()-c[2])/(radius-1);
			}
		}
					
		return pt;
	}
	
	public double[] align(double[] pos) {
		//only the heading (the angles) information not the distance traveled.
		
		ContinuousSpace space = getSpace();
		double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
		ArrayList list = AgentGeometry.objectsWithin(space,this,pos,radius);
		double pt[]={0.0,0.0,0.0};
		int count=0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof VP1) {
				VP1 vp = (VP1) list.get(i);
				pt[0] += Math.sin(vp.getTheta())*Math.sin(vp.getPhi());
				pt[1] += Math.cos(vp.getPhi());
				pt[2] += Math.cos(vp.getTheta())*Math.sin(vp.getPhi());				
				count++;
			}
		}
		if (count > 0) {
			pt[0]=pt[0]/count;
			pt[1]=pt[1]/count;
			pt[2]=pt[2]/count;
			pt[0]=(pt[0]+Math.sin(this.getTheta())*Math.sin(this.getPhi()))/2.0f;
			pt[1]=(pt[1]+Math.cos(this.getPhi()))/2.0f;
			pt[2]=(pt[2]+Math.cos(this.getTheta())*Math.sin(this.getPhi()))/2.0f;
		}
		return pt;
	}*/
	@Parameter(usageName="state",displayName="Genome State", converter = "assembly.GStateConverter")
	public GState getState() {
		return state;
	}



	public void setState(GState state) {
		this.state = state;
	}



	//Scheduled methods
	public double[] move2() {
		//double tick = (double)RepastEssentials.GetTickCount();
		//if (tick > move2Tick) {

			double coord[] = {0,0,0};
			NdPoint pt = this.getSpace().getLocation(this);

			double r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
			boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
			//boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
			boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");
			
			ContinuousWithin list = new ContinuousWithin(this.getSpace(), this, (r+rerr));
			Iterator neighbors = list.query().iterator();
			

			double align[] = {0,0,0};
			int count = 0;
			while(neighbors.hasNext()){
				AgentExtendCont aec = (AgentExtendCont)neighbors.next();
				if (aec instanceof VP123) {
					if (aln) {
						align[0] += aec.getX();
						align[1] += aec.getY();
						align[2] += aec.getZ();
						count++;
					}
				}
			}
			double disp[] = {0,0,0};
			if (count > 0) {
				align[0] = (align[0]/count)/*-this.getX())/8*/;
				align[1] = (align[1]/count)/*-this.getY())/8*/;
				align[2] = (align[2]/count)/*-this.getZ())/8*/;
				/*this.setX(align[0]);
				this.setY(align[1]);
				this.setZ(align[2]);
				coord[0] = pt.getX() + align[0];
				coord[1] = pt.getY() + align[1];
				coord[2] = pt.getZ() + align[2];*/
				if (count ==72) {
					RepastEssentials.PauseSimulationRun();
				}
				System.out.println("capsid count="+count);
			} /*else {
				this.genXYZ();
				disp[0] = this.getX();
				disp[1] = this.getY();
				disp[2] = this.getZ();
				coord[0] = (disp[0]+pt.getX());
				coord[1] = (disp[1]+pt.getY());
				coord[2] = (disp[2]+pt.getZ());
			}*/
			
			//keep distance from other genomes
			list = new ContinuousWithin(this.getSpace(), this, 2*(r+rerr));
			neighbors = list.query().iterator();
			double separ[] = {0.0,0.0,0.0};
			while (neighbors.hasNext()) {
				Object obj = neighbors.next();
				if (obj instanceof Genome) {
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
				/*this.genXYZ();
				coord[0] = this.getX() + pt.getX();
				coord[1] = this.getY() + pt.getY();
				coord[2] = this.getZ() + pt.getZ();*/
				;
			} else {
				AgentGeometry.trim(coord, rerr/2);
				this.setX(coord[0]);
				this.setY(coord[1]);
				this.setZ(coord[2]);
				//coord[0] = coord[0] + pt.getX();
				//coord[1] = coord[1] + pt.getY();
				//coord[2] = coord[2] + pt.getZ();
			}
			return coord;
			//coord = this.normPositionToBorder(coord, r);
		//	boolean p = getSpace().moveTo(this, coord);
			//if (!p) {
				//System.out.println("bad point");
			//}
			//move2Tick = tick;
		//}
	}
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double disp[] = {0.0,0.0,0.0};
			double r=0;
			double rerr=0;
			
			if (state == GState.early) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(TranscriptionFactor.class, LgTAg.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.replicate) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(LgTAg.class, DNAPol.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.late) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(LgTAg.class, VP123.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.assembly) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				}
				disp = this.calcDispIfCenter(VP123.class, VP123.class, Genome.class, HostGenome.class,r,rerr);
				//disp = move2();
			}
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
				clearBoundProteins();
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
			moveTick = tick;
		}
	}
	
	public void transcription() {
		double tick = RepastEssentials.GetTickCount();
		if (xcriptTick < tick) {
			double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			ContinuousWithin<AgentExtendCont> list = new ContinuousWithin(getSpace(), this, (dist+err));
			Iterator<AgentExtendCont> l = list.query().iterator();
			if (state == GState.early) {
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof TranscriptionFactor) {
						if (aec.isBound()) {
							double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
							if (rand < 0.1) {
								MRNA mrna = new MRNA();
								//mrna.setMType(MType.Tag);
								mrna.setState(mState.early);
								mrna.setLocation(Loc.nucleus);
								mrna.setTheContext(this.getTheContext());
								mrna.setSpace(this.getSpace());
								((Nucleus)getTheContext()).addToAddList(mrna);
								aec.largeStepAwayFrom(this);
								aec.setBound(false);
								this.setNoBound(0);
								//state = GState.replicate;
								break;
							}
						}
					} 
				}
			} else if (state == GState.replicate) {
				boolean dfound = false;
				boolean lfound = false;
				AgentExtendCont daec = null;
				AgentExtendCont laec = null;
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof DNAPol && !dfound) {
						if (aec.isBound()) {
							daec = aec;
							dfound = true;
						}
					} else if (aec instanceof LgTAg && !lfound) {
						if (aec.isBound()) {
							laec = aec;
							lfound = true;
						}
					}
				}
				if (dfound && lfound) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < .2) {
						Genome g = new Genome();
						g.setState(GState.early);
						state = GState.late;
						g.setSpace(this.getSpace());
						g.setTheContext(this.getTheContext());
						((Nucleus)getTheContext()).addToAddList(g);
						daec.largeStepAwayFrom(this);
						daec.setBound(false);
						laec.largeStepAwayFrom(this);
						laec.setBound(false);
						this.setNoBound(0);
						this.clearBoundProteins();
						state = GState.late;
					}
				}
			} else if (state == GState.late) {
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof LgTAg) {
						MRNA m = new MRNA();
						m.setState(mState.late);
						m.setLocation(Loc.nucleus);
						m.setSpace(this.getSpace());
						m.setTheContext(this.getTheContext());
						((Nucleus)getTheContext()).addToAddList(m);
						aec.largeStepAwayFrom(this);
						aec.setBound(false);
						this.setNoBound(0);
					}
				}
			}
			xcriptTick = tick;
		}
	}
	
	public void egress() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > egressTick) {
			if (getNoBound() == 72) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < 0.5) {
					double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
					ContinuousWithin list = new ContinuousWithin(getSpace(),this,(dist+err));
					Iterator<AgentExtendCont> l = list.query().iterator();
					while (l.hasNext()) {
						AgentExtendCont aec = l.next();
						if (aec instanceof VP123 && aec.isBound()) {
								//aec.setMoving(true);
							((Nucleus)getTheContext()).addToRemList(aec);
						}
					}
						//this.setMoving(true);
					((Nucleus)getTheContext()).addToRemList(this);
						//VP123 vp = new VP123();
						//vp.setVptype(VPType.VP12);
						//((Cytoplasm)getTheContext()).getCell().addToMoveList(vp);
					((Nucleus)getTheContext()).getCell().setVirions();
				}
			}
			egressTick = tick;

		}
	}
/*	public void move() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			int asstype = (Integer)RunEnvironment.getInstance().getParameters().getValue("assembleType");
			switch (asstype) {
			case 1:
				double r = (Double)RunEnvironment.getInstance().getParameters().getValue("radius");
				NdPoint pt = getSpace().getLocation(this);
				thetaPhiDistGen();
				double coord[] = {0.0,0.0,0.0};
				coord[0] = pt.getX()+getDistance()*Math.sin(getTheta())*Math.sin(getPhi()); //x
				coord[1] = pt.getY()+getDistance()*Math.cos(getPhi());                 //y
				coord[2] = pt.getZ()+getDistance()*Math.cos(getTheta())*Math.sin(getPhi()); //z	
				//System.out.println(getName()+","+getTheta()+","+getPhi()+","+getDistance());

				double ptcol[] = {0,0,0};//collision(coord);
				double ptadh[] = {0,0,0};//align(coord);
				double ptcent[] = {0,0,0};//center(coord);
			
				//System.out.println("genome pt,"+pt.getX()+","+pt.getY()+","+pt.getZ());
				//System.out.println("genome ptcent,"+ptcent[0]+","+ptcent[1]+","+ptcent[2]);
				//System.out.println("genome ptcol,"+ptcol[0]+","+ptcol[1]+","+ptcol[2]);
				//System.out.println("genome ptadh,"+ptadh[0]+","+ptadh[1]+","+ptadh[2]);

				//coord[0] = coord[0] + ptcent[0] + ptcol[0] + ptadh[0] - pt.getX();
				//coord[1] = coord[1] + ptcent[1] + ptcol[1] + ptadh[1] - pt.getY();
				//coord[2] = coord[2] + ptcent[2] + ptcol[2] + ptadh[2] - pt.getZ();
				//System.out.println("genome coord,"+coord[0]+","+coord[1]+","+coord[2]);

				//NdPoint p = getSpace().moveByDisplacement(this, coord);
				coord[0] = coord[0] + ptcent[0] + ptcol[0] + ptadh[0];
				coord[1] = coord[1] + ptcent[1] + ptcol[1] + ptadh[1];
				coord[2] = coord[2] + ptcent[2] + ptcol[2] + ptadh[2];
				coord = this.normPositionToBorder(coord, r);
				boolean p = getSpace().moveTo(this, coord);
				if (!p) {
					System.out.println("bad point");
				}
				//coord = normPositionToGrid(coord[0],coord[1],coord[2]);  //don't go outside boundaries
				//getSpace().moveTo(this,coord[0],coord[1],coord[2]);
				//System.out.println("Genome:("+coord[0]+", "+coord[1]+", "+coord[2]+")");
				break;
			default:
				break;
			}
			moveTick=tick;
		}
	}*/
	
/*	public void lmMove() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > lmoveTick) {
		NdPoint loc = getSpace().getLocation(this);
		//System.out.println("("+loc.getX()+", "+loc.getY()+", "+loc.getZ()+")");
    	boolean rand = RepastEssentials.RandomDrawAgainstThreshold(0.5);
    	//if (rand) {
    		int asstype = (Integer)RunEnvironment.getInstance().getParameters().getValue("assembleType");
    		switch (asstype) {
			case 2:
				//super.move();
				//for (int i = 0; i < 12; i++) {
	    			//if (vertices[i] != null) {
	    				//vertices[i].moveMolecule();
	    			//}
	    		//}			
				break;
			case 3:
			case 1:
				super.move();
				break;
			default:
				break;
			}
    	
    	//}
    	lmoveTick = tick;
		}
	}
	
	public int findFreeVertex(Object obj) {
		//for assembly type 2
		int retval = 0;
		boolean found = false;
		for (int i = 0; i < 12; i++) {
			if (vertices[i] == null) {
				retval = i;
				found = true;
				break;
			}
		}
		if (!found)
			retval = -1;
		return retval;
	}
	
	public void setEdges(int index) {
		//for assembly type 2
		if (index != 0) {
		//	network.addEdge(vertices[index-1], vertices[index]);
			//if (i > 1 && i <=6) {
				//network.addEdge(vertices[0],vertices[index]);
			//}
			switch (index) {
			case 0:
				if (vertices[1] != null)
					if (network.getEdge(vertices[0],vertices[1]) == null)
						network.addEdge(vertices[0],vertices[1]);
				if (vertices[2] != null) 
					if (network.getEdge(vertices[0], vertices[2]) == null)
					network.addEdge(vertices[0], vertices[2]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[0], vertices[3]) == null)
						network.addEdge(vertices[0], vertices[3]);
				if (vertices[4] != null) 
					if (network.getEdge(vertices[0], vertices[4]) == null)
						network.addEdge(vertices[0],vertices[4]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[0], vertices[5]) == null)
						network.addEdge(vertices[0], vertices[5]);
				break;
			case 1:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[1]) == null)
					network.addEdge(vertices[0], vertices[1]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[1], vertices[2]) == null)
					network.addEdge(vertices[1], vertices[2]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[1], vertices[5]) == null)
					network.addEdge(vertices[1], vertices[5]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[1], vertices[7]) == null)
					network.addEdge(vertices[1], vertices[7]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[1], vertices[8]) == null)
					network.addEdge(vertices[1], vertices[8]);
				break;
			case 2:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[2]) == null)
					network.addEdge(vertices[0], vertices[2]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[2], vertices[1]) == null)
					network.addEdge(vertices[2], vertices[1]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[2], vertices[3]) == null)
					network.addEdge(vertices[2], vertices[3]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[2], vertices[8]) == null)
					network.addEdge(vertices[2], vertices[8]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[2], vertices[9]) == null)
					network.addEdge(vertices[2], vertices[9]);
				break;
			case 3:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[3]) == null)
					network.addEdge(vertices[0], vertices[3]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[3], vertices[2]) == null)
					network.addEdge(vertices[3], vertices[2]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[3], vertices[4]) == null)
					network.addEdge(vertices[3], vertices[4]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[3], vertices[9]) == null)
				network.addEdge(vertices[3], vertices[9]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[3], vertices[10]) == null)
				network.addEdge(vertices[3], vertices[10]);
				break;
			case 4:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[4]) == null)
					network.addEdge(vertices[0], vertices[4]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[4], vertices[3]) == null)
					network.addEdge(vertices[4], vertices[3]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[4], vertices[5]) == null)
					network.addEdge(vertices[4], vertices[5]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[4], vertices[6]) == null)
					network.addEdge(vertices[4], vertices[6]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[4], vertices[10]) == null)
					network.addEdge(vertices[4], vertices[10]);
				break;
			case 5:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[5]) == null)
				network.addEdge(vertices[0], vertices[5]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[5], vertices[4]) == null)
					network.addEdge(vertices[5], vertices[4]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[5], vertices[1]) == null)
					network.addEdge(vertices[5], vertices[1]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[5], vertices[6]) == null)
					network.addEdge(vertices[5], vertices[6]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[5], vertices[7]) == null)
					network.addEdge(vertices[5], vertices[7]);
				break;				
			case 6:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[6]) == null)
					network.addEdge(vertices[11], vertices[6]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[6], vertices[5]) == null)
				network.addEdge(vertices[6], vertices[5]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[6], vertices[7]) == null)
					network.addEdge(vertices[6], vertices[7]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[6], vertices[4]) == null)
					network.addEdge(vertices[6], vertices[4]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[6], vertices[10]) == null)
					network.addEdge(vertices[6], vertices[10]);
				break;
			case 7:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[7]) == null)
					network.addEdge(vertices[11], vertices[7]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[7], vertices[5]) == null)
					network.addEdge(vertices[7], vertices[5]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[7], vertices[6]) == null)
				network.addEdge(vertices[7], vertices[6]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[7], vertices[8]) == null)
					network.addEdge(vertices[7], vertices[8]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[7], vertices[1]) == null)
					network.addEdge(vertices[7], vertices[1]);
				break;			
			case 8:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[8]) == null)
					network.addEdge(vertices[11], vertices[8]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[8], vertices[1]) == null)
					network.addEdge(vertices[8], vertices[1]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[8], vertices[7]) == null)
					network.addEdge(vertices[8], vertices[7]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[8], vertices[9]) == null)
					network.addEdge(vertices[8], vertices[9]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[8], vertices[2]) == null)
					network.addEdge(vertices[8], vertices[2]);
				break;
			case 9:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[9]) == null)
					network.addEdge(vertices[11], vertices[9]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[9], vertices[3]) == null)
					network.addEdge(vertices[9], vertices[3]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[9], vertices[2]) == null)
					network.addEdge(vertices[9], vertices[2]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[9], vertices[8]) == null)
					network.addEdge(vertices[9], vertices[8]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[9], vertices[10]) == null)
					network.addEdge(vertices[9], vertices[10]);
				break;
			case 10:
				if (vertices[11] != null)
					if (network.getEdge(vertices[10], vertices[11]) == null)
					network.addEdge(vertices[10], vertices[11]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[10], vertices[3]) == null)
					network.addEdge(vertices[10], vertices[3]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[10], vertices[4]) == null)
					network.addEdge(vertices[10], vertices[4]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[10], vertices[6]) == null)
					network.addEdge(vertices[10], vertices[6]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[10], vertices[9]) == null)
					network.addEdge(vertices[10], vertices[9]);
				break;
			case 11:
				if (vertices[6] != null)
					if (network.getEdge(vertices[6], vertices[11]) == null)
					network.addEdge(vertices[6], vertices[11]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[11], vertices[7]) == null)
					network.addEdge(vertices[11], vertices[7]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[11], vertices[8]) == null)
					network.addEdge(vertices[11], vertices[8]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[11], vertices[9]) == null)
					network.addEdge(vertices[11], vertices[9]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[11], vertices[10]) == null)
					network.addEdge(vertices[11], vertices[10]);
				break;
				}
		}
	}*/

	public void checkNeighbors() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > neighborTick) {
		int asstype = (Integer)RunEnvironment.getInstance().getParameters().getValue("assembleType");
		switch (asstype) {
		case 2:
			
		/*	ContinuousSpace space = getSpace();//(ContinuousSpace)getTheContext().getProjection("nucleus");
			ContinuousWithin contlist = new ContinuousWithin(space,this,factor*2);
			Iterable list = contlist.query();
			for (Object obj:list) {
				if (obj instanceof VP1) {
					VP1 vp1 = (VP1)obj;
					int i = findFreeVertex(vp1);
					if (i > -1  && vp1.getGenome() == null) {
						NdPoint locvp1 = space.getLocation(vp1);
						NdPoint locg = space.getLocation(this);
						NdPoint pt = new NdPoint((locg.getX()+factor*coord[i][0]),
								(locg.getY()+factor*coord[i][1]),
								(locg.getZ()+factor*coord[i][2]));
						double d = space.getDistance(locg, pt);
						double dist = space.getDistance(pt, locvp1);
						System.out.println("("+pt.getX()+", "+pt.getY()+", "+pt.getZ()+")");
						System.out.println("("+locvp1.getX()+", "+locvp1.getY()+", "+locvp1.getZ()+")");
						System.out.println("d = "+d+", dist = "+dist);
						if (dist <= 1) {
							double newd[] = {0,0,0};
							newd[0] = factor*coord[i][0]+locg.getX()-locvp1.getX();
							newd[1] = factor*coord[i][1]+locg.getY()-locvp1.getY();
							newd[2] = factor*coord[i][2]+locg.getZ()-locvp1.getZ();
							space.moveByDisplacement(vp1, newd);
							vertices[i] = vp1;
							vp1.setGenome(this);
							vp1.setStop(true);
							setEdges(i);
						}
					}
				}
			}*/
			break;

		default:
			break;
		}
		neighborTick = tick;
		}
	}
		
}
