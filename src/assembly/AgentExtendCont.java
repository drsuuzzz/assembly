package assembly;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

public class AgentExtendCont {
	
	private Context theContext;
	private Boolean stop;
	private ContinuousSpace space;
	private double theta;
	private double phi;
	private double distance;
	
	public AgentExtendCont() {
		stop=false;
		theContext=null;
		thetaPhiDistGen();
		//theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//phi = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//distance = RepastEssentials.RandomDraw(0, 2);
	}
	@Parameter(usageName="stop",displayName="Stopped")
	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
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
	
	public double getTheta() {
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
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority=ScheduleParameters.RANDOM_PRIORITY)	
	public void thetaPhiDistGen() {
		theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		phi = RepastEssentials.RandomDraw(0, 2*Math.PI);
		distance = RepastEssentials.RandomDraw(0, 2);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority=ScheduleParameters.RANDOM_PRIORITY)
	public void move() {
		//Context context = ContextUtils.getContext("Assembly");
		if (!getStop()) {
			//ContinuousSpace space = (ContinuousSpace)theContext.getProjection("nucleus");
			//calculate new angles
			//calculate distance
			//calculate displacement
			double coord[] = {0.0,0.0,0.0};
			thetaPhiDistGen();
			coord[0] = distance*Math.sin(theta)*Math.sin(phi); //x
			coord[1] = distance*Math.cos(phi);                 //y
			coord[2] = distance*Math.cos(theta)*Math.sin(phi); //z
			space.moveByDisplacement(this, coord);
		
        //double xy = RepastEssentials.RandomDraw(0, 2*Math.PI);
        //double yz = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//double xz = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//double distance = RepastEssentials.RandomDraw(0, 1);
		
        //space.moveByVector(this, distance, xy, yz, xz); 
        //find new position
        //find potential obstacles within collision range
		}
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.RANDOM_PRIORITY)
	public void separation() {
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double x = 0;
		double y = 0;
		double z = 0;
		int count = 0;
		NdPoint loc = space.getLocation(this);
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			x = x - (loc.getX()-pt.getX());
			y = y - (loc.getY()-pt.getY());
			z = z - (loc.getZ()-pt.getZ());
			count++;
		}
		if (count > 0) {
			theta += Math.atan2(x,z);
			phi += Math.atan2((Math.sqrt(z*z+x*x)),y);
			distance += Math.sqrt(x*x+y*y+z*z);
		}
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.RANDOM_PRIORITY)
	public void alignment() {
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double ptdist = 0.0;
		int count = 0;
		for (Object obj:list) {
			ptdist += ((AgentExtendCont) obj).getDistance();
			count++;
		}
		if (count > 0) {
			ptdist = ptdist/count;
			distance = (ptdist - distance)/8;
		}
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.RANDOM_PRIORITY)
	public void cohesion() {

		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double x = 0;
		double y = 0;
		double z = 0;
		int count = 0;
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			x += pt.getX();
			y += pt.getY();
			z += pt.getZ();
			count ++;
		}
		if (count > 0) {
			x = x/count;
			y = y/count;
			z = z/count;
			NdPoint loc = space.getLocation(this);
			NdPoint newLoc = new NdPoint(loc.getX(),loc.getY(),loc.getZ());
			x += (x-loc.getX())/100;
			y += (y-loc.getY())/100;
			z += (z-loc.getZ())/100;
			theta += Math.atan2(x,z);
			phi += Math.atan2((Math.sqrt(z*z+x*x)),y);
			distance += Math.sqrt(x*x+y*y+z*z);
		}
	}
	
	public double calcDistanceBetween(ContinuousSpace space, Object o1, Object o2) {
		double value = 0.0;
		NdPoint pt1 = space.getLocation(o1);
		NdPoint pt2 = space.getLocation(o2);
		//simple euclidean distance
		value = Math.sqrt((pt1.getX()-pt2.getX())*(pt1.getX()-pt2.getX()) + 
				(pt1.getY()-pt2.getY())*(pt1.getY()-pt2.getY()) + 
				(pt1.getZ()-pt2.getZ())*(pt1.getZ()-pt2.getZ()));
		return value;
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.RANDOM_PRIORITY)
	public void avoidCollisions() {
		ContinuousSpace space = getSpace();
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double min = 1;
		Object m = null;
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			double dist = calcDistanceBetween(space,this,obj);
			if (dist < min) {
				m = obj;
				min = dist;
			}
		}
		if (m != null) {
			NdPoint pt = space.getLocation(this);
			double xy = Math.atan(Math.sqrt(pt.getX()*pt.getX()+pt.getZ()*pt.getZ())/pt.getY()) + Math.PI;
			double yz = xy;
			double xz = Math.atan2(pt.getX(), pt.getZ())+Math.PI;
	        space.moveByVector(this, 1, xy, yz, xz);
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority=ScheduleParameters.RANDOM_PRIORITY)
	public void checkNeighbors() {
		
	}
	
/*	public void moveContinuous() {
		
		Context context = ContextUtils.getContext("Assemble");
		ContinuousSpace space = (ContinuousSpace)context.getProjection("nucleus");
		
		double xy = RepastEssentials.RandomDraw(0, 2*Math.PI);
		double xz = RepastEssentials.RandomDraw(0, 2*Math.PI);
		double speed = RepastEssentials.RandomDraw(-2, 2);
        space.moveByDisplacement(this, speed * xy, speed * xy, speed * xz);
	}*/

}
