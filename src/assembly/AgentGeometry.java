package assembly;

import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

public class AgentGeometry {
	
	public enum Axis {X,Y,Z};
	public Axis axis;
	
	public static double noX[][] = {{0,0,0,0},
			{0,1,0,0},
			{0,0,1,0},
			{0,0,0,1}
	};
	public static double noY[][] = {{1,0,0,0},
			{0,0,0,0},
			{0,0,1,0},
			{0,0,0,1}
	};
	public static double noZ[][] = {{1,0,0,0},
			{0,1,0,0},
			{0,0,0,0},
			{0,0,0,1}
	};
	
	// vector and matrix stuff
	public static double lengthOfVector (double[][] v) {
		
		double l = v[0][0]*v[0][0]+v[1][0]*v[1][0]+v[2][0]*v[2][0];
		l = Math.sqrt(l);
		return l;
	}
	
	public static double lengthOfVector (double[] v) {
		double l = v[0]*v[0] + v[1]*v[1] + v[2]*v[2];
		l = Math.sqrt(l);
		return l;
	}
	
	public static double lengthOfVectorSquared (double[][] v) {
		
		double l = v[0][0]*v[0][0]+v[1][0]*v[1][0]+v[2][0]*v[2][0];
		return l;
	}
	
	public static double[][] unit(double[][] v) {
		
		double l = lengthOfVector(v);
		double vreturn[][] = {{v[0][0]/l},{v[1][0]/l},{v[2][0]/l}};
		
		return vreturn;
	}
	
	public static void unit(double[] v) {
		double l = lengthOfVector(v);
		v[0] = v[0]/l;
		v[1] = v[1]/l;
		v[2] = v[2]/l;
		//return vreturn;
	}
	
	public static void scale(double[] v, double s){
		unit(v);
		v[0] = v[0]*s;
		v[1] = v[1]*s;
		v[2] = v[2]*s;
		
	}
	public static double[][] dotProduct(double[][] m1, double[][] m2) {
		//tryin to do matrix multiplication
		double ret[][] = new double[m1.length][m2[0].length];
		if (m1[0].length != m2.length) {
			System.out.println("Matrix dimensions don't work");
			ret = null;
		} else {
			for (int i = 0; i < m1.length; i++) {
				for (int j = 0; j < m2[0].length; j++) {
					for (int k = 0; k < m1[0].length; k++) {
						ret[i][j] += m1[i][k] * m2[k][j];
					}
				}
			}
		}
		return ret;
	
	}
	
	public static double[][] crossProduct(double[][] v1, double[][] v2) {
		double ret[][] = new double[v1.length][1];
		ret[0][0] = v1[1][0]*v2[2][0]-v1[2][0]*v2[1][0];
		ret[1][0] = v1[2][0]*v2[0][0]-v1[0][0]*v2[2][0];
		ret[2][0] = v1[0][0]*v2[1][0]-v1[1][0]*v2[0][0];
		return ret;
	}
	
	public static double vectorTimesVector(double[][] v1, double[][]v2){
		
		double ret = 0;
		for (int i = 0; i < (v1.length-1); i++) {
			ret = ret + v1[i][0]*v2[i][0];
		}
		return ret;
	}
	
	public static double[][] pointDisplacement (double[] pt, double[] disp) {
		
		double coord[][] = new double[4][1];
		//translate point with displacement
		coord[0][0] = pt[0]-disp[0];
		coord[1][0] = pt[1]-disp[1];
		coord[2][0] = pt[2]-disp[2];
		coord[3][0] = 1;
		
		return coord;
	}
	
	public static double[][] newPoint (double phi, double theta, double length) {
		//to cartesian coordinates from spherical
		double coord[][] = new double[4][1];
		coord[0][0] = length*Math.cos(theta)*Math.sin(phi); //x
		coord[1][0] = length*Math.sin(theta)*Math.sin(phi);                 //y
		coord[2][0] = length*Math.cos(phi); //z
		coord[3][0] = 1;
		
		return coord;

	}
	
	public static double[][] translate (double[] d) {
		
		double T[][]={{1,0,0,d[0]},
				{0,1,0,d[1]},
				{0,0,1,d[2]},
				{0,0,0,1}
		};
		return T;
	}
	
	public static double[][] rotateX (double cosTheta, double sinTheta) {
		//x axis of rotation
		
		double Rx[][] = {{1,0,0,0},
				{0, cosTheta,-sinTheta, 0},
				{0, sinTheta, cosTheta, 0},
				{0,0,0,1}};
		return Rx;
	}
	
	public static double[][] rotateY(double cosTheta, double sinTheta) {
		//y axis of rotation
		
		double Ry[][] = {{cosTheta, 0, sinTheta, 0},
				{0, 1, 0, 0},
				{-sinTheta, 0, cosTheta, 0},
				{0, 0, 0, 1}
		};
		return Ry;
	}
	
	public static double[][] rotateZ(double cosTheta, double sinTheta) {
		//z axis of rotation
		
		double Rz[][] = {{cosTheta, -sinTheta, 0, 0},
				{sinTheta, cosTheta, 0, 0},
				{0, 0, 1, 0},
				{0, 0, 0, 1}
		};
		return Rz;
	}

	
	public static double cosTheta(double[][] v1, double [][]v2){

		double val = -10;
		double denom = lengthOfVector(v1)*lengthOfVector(v2);
		if (denom > 0) {
			val = vectorTimesVector(v1,v2)/denom;
		}
		return val;
	}
	
	public static double sinTheta(double[][] v1, double[][] v2, Axis axis) {
		
		double ret = -10;
		double v[][] = crossProduct(v1,v2);
		double denom = lengthOfVector(v1)*lengthOfVector(v2);
		if (denom > 0) {
			if (axis == Axis.X) {
				ret = v[0][0]/denom;
			} else if (axis == Axis.Y) {
				ret = v[1][0]/denom;
			} else {
				ret = v[2][0]/denom;
			}
		}
		return ret;
	}


	public static double distanceBetween2Pts (NdPoint pt1, NdPoint pt2) {
		
		double dist = (pt1.getX()-pt2.getX())*(pt1.getX()-pt2.getX()) +
		              (pt1.getY()-pt2.getY())*(pt1.getY()-pt2.getY()) +
		              (pt1.getZ()-pt2.getZ())*(pt1.getZ()-pt2.getZ());
		dist = Math.sqrt(dist);
		return dist;
	}
	
	public static double calcDistance(double c[],NdPoint pt){
		
		double dist = 0.0;
		dist = Math.sqrt((c[0]-pt.getX())*(c[0]-pt.getX())+
				         (c[1]-pt.getY())*(c[1]-pt.getY())+
				         (c[2]-pt.getZ())*(c[2]-pt.getZ()));
		return dist;
	}
	
	public static double calcDistance(double[] pt1, double[] pt2) {
		double dist = 0.0;
		dist = (pt1[0]-pt2[0])*(pt1[0]-pt2[0]) +
		       (pt1[1] - pt2[1]) * (pt1[1]-pt2[1]) +
		       (pt1[2] - pt2[2])*(pt1[2]-pt2[2]);
		dist = Math.sqrt(dist);
		return dist;
	}
	
	public static double calcDistanceSq(double[] pt1, double[] pt2) {
		double dist = 0.0;
		dist = (pt1[0]-pt2[0])*(pt1[0]-pt2[0]) +
			(pt1[1] - pt2[1]) * (pt1[1]-pt2[1]) +
	       (pt1[2] - pt2[2])*(pt1[2]-pt2[2]);
		return dist;
	}
	
	public static double calcDistanceNdPoints(NdPoint pt1,NdPoint pt2){
		
		double dist = 0.0;
		dist = Math.sqrt((pt1.getX()-pt2.getX())*(pt1.getX()-pt2.getX())+
				         (pt1.getY()-pt2.getY())*(pt1.getY()-pt2.getY())+
				         (pt1.getZ()-pt2.getZ())*(pt1.getZ()-pt2.getZ()));
		return dist;
	}
	
	
	public static void trim(double[] vector, double trimby) {
		
		double max = Math.max(Math.abs(vector[0]), Math.abs(vector[1]));
		max = Math.max(max, Math.abs(vector[2]));
		if (max < trimby)
			return;
		double scale = trimby/max;
		vector[0]=vector[0]*scale;
		vector[1]=vector[1]*scale;
		vector[2]=vector[2]*scale;
	}
	
	public static ArrayList objectsWithin(ContinuousSpace space, AgentExtendCont agent, double c[],double distance) {
		//Iterator i = getSpace().getObjects().iterator();
		Iterable list = space.getObjects();
		ArrayList al = new ArrayList();
		for (Object obj:list){
			NdPoint pt = space.getLocation(obj);
			double dist = calcDistance(c,pt);
			if (dist <= distance && obj != agent) {
				al.add(obj);
			}
		}
		return al;
	}
	public static ArrayList objectsWithinPts(ContinuousSpace space, Iterator list, ArrayList pts, double[] pt1, double[] pt2, double distance) {
		//new reference set
		ArrayList al = new ArrayList();
		al.addAll(pts);
		//return
		ArrayList ret = new ArrayList();
		//rotate reference points to face genome
		//vector towards genome g-pt
		double v2[][] = AgentGeometry.pointDisplacement(pt1, pt2);
		double v1[][] = {{0},{0},{1},{1}};
		//find y angle on zx plane
		double v1p[][] = AgentGeometry.dotProduct(noY,v1);
		double v2p[][] = AgentGeometry.dotProduct(noY, v2);
		double cos = AgentGeometry.cosTheta(v1p, v2p);
		double sin = AgentGeometry.sinTheta(v1p, v2p, Axis.Y);
		//now rotate points through y angle
		if (cos > -10 && sin > -10) {
			double roty[][] = AgentGeometry.rotateY(cos, sin);
			for (int i = 0; i < pts.size(); i++) {
				double npt[][] = AgentGeometry.dotProduct(roty, (double[][]) al.get(i));
				al.set(i, npt);
			}
			//rotate norm vector
			v1p = AgentGeometry.dotProduct(roty,v1);
		}
		//find x angle on yz plane
		v1p = AgentGeometry.dotProduct(noX, v1p);
		v2p = AgentGeometry.dotProduct(noX, v2);
		cos = AgentGeometry.cosTheta(v1p, v2p);
		sin = AgentGeometry.sinTheta(v1p, v2p, Axis.X);
		if (cos > -10 && sin > -10) {
			double rotx[][] = AgentGeometry.rotateX(cos, sin);
			for (int i = 0; i < pts.size(); i++) {
				double npt[][] = AgentGeometry.dotProduct(rotx, (double[][]) al.get(i));
				al.set(i, npt);
			}
			v1p = AgentGeometry.dotProduct(rotx, v1p);
		}
		//find z angle on xy plane
		v1p = AgentGeometry.dotProduct(noZ, v1p);
		v2p = AgentGeometry.dotProduct(noZ, v2);
		cos = AgentGeometry.cosTheta(v1p, v2p);
		sin = AgentGeometry.sinTheta(v1p, v2p, Axis.Z);
		if (cos > -10 && sin > -10) {
			double rotz[][] = AgentGeometry.rotateZ(cos, sin);
			for (int i = 0; i < pts.size(); i++) {
				double npt[][]=AgentGeometry.dotProduct(rotz, (double [][]) al.get(i));
				al.set(i,npt);
			}
		}
		
		//now translate points
		double T[][] = AgentGeometry.translate(pt2);
		for (int i = 0; i < pts.size(); i++) {
			double npt[][] = AgentGeometry.dotProduct(T, (double [][])al.get(i));
			al.set(i, npt);
		}
		
		while (list.hasNext()){
			Object obj = list.next();
			NdPoint spacept = space.getLocation(obj);
			for (int i = 0; i < al.size(); i++) {
				double x = ((double[][])al.get(i))[0][0];
				double y = ((double[][])al.get(i))[1][0];
				double z = ((double[][])al.get(i))[2][0];
				double refpt[] = {x,y,z};
				double dist = AgentGeometry.calcDistance(refpt,spacept);
				if (dist <= distance) {
					ret.add(obj);  //return list of AgentExtendCont
					break;
				}
			}
		}
		return ret;
	}
	
	
	public static ArrayList objectsWithinThetaAngle(ContinuousSpace space, AgentExtendCont agent, ArrayList pts, double c[],double distance, double[] angleOffset) {
		//Iterator i = getSpace().getObjects().iterator();
		double theta = angleOffset[0];
		double phi = angleOffset[1];
		Iterable list = space.getObjects();
		ArrayList al = new ArrayList();
		for (Object obj:list){
			NdPoint pt = space.getLocation(obj);
			for (int i = 0; i < pts.size(); i++) {
				double x = ((NdPoint)pts.get(i)).getX()+c[0];
				double y = ((NdPoint)pts.get(i)).getY()+c[1];
				double z = ((NdPoint)pts.get(i)).getZ()+c[2];
				double npt[] = {x,y,z};
				double dist = AgentGeometry.calcDistance(npt,pt);
				if (dist <= distance) {
					//((VP1)obj).setSides(i,(VP1)agent);
					al.add(obj);
				}
			}
		}
		return al;
	}
/*	public static boolean lieOnPlane (double[] plane, NdPoint pt, double threshold) {
		
		boolean ret = false;
		double eval = plane[0]*pt.getX()+plane[1]*pt.getY()+plane[2]*pt.getZ()+plane[3];
		if (0 <= Math.abs(eval) && Math.abs(eval) <= threshold) {
			ret = true;
		}
		return ret;
	}*/

	public static ArrayList objectsWithin2Objects(ContinuousSpace space,
			AgentExtendCont agent1, double[] coord, double dist1, AgentExtendCont agent2, double dist2) {
 
		ContinuousWithin cw = new ContinuousWithin(space,agent2,dist2);
		Iterable listg = cw.query();
		ArrayList listv = objectsWithin(space, agent1, coord, dist1);
		ArrayList retlist = new ArrayList();
		while(listg.iterator().hasNext()) {
			for (int i = 0; i < listv.size(); i++) {
				AgentExtendCont g = (AgentExtendCont)listg.iterator().next();
				if (!g.equals(agent2) && !g.equals(agent1)) {
					if (g.equals(listv.get(i))) {
						retlist.add(listv.get(i));
					}
				}
			}
			
		}
		return retlist;
	}
	 
}
