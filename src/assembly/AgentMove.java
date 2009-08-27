package assembly;

import assembly.AgentExtendCont.Loc;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.AbstractContinuousSpace.PointHolder;
import repast.simphony.space.projection.ProjectionEvent;

public class AgentMove {
	
	public static double[] adjustPointToSpace(AgentExtendCont aec) {
		
		double[] npt = aec.getSpace().getLocation(aec).toDoubleArray(null);
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius");
		int cr = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		
		double[] center = new double[3];
		center[0] = cr;
		center[1] = cr;
		center[2] = cr;

		double[] v = new double[3];
		v[0] = npt[0] - center[0];
		v[1] = npt[1] - center[1];
		v[2] = npt[2] - center[2];
		
		double d = AgentGeometry.calcDistance(center, npt);
		
		if (aec.getLocation() == Loc.cytoplasm) {
			if (d < r) {
				double scale = (r-d)*(center[0]-r)/r + r;
				AgentGeometry.scale(v,scale);
			} else if (d > center[0]-1) {
				double scale = RandomHelper.nextDoubleFromTo(r, center[0]);
				AgentGeometry.scale(v, scale);
			}
		} else if (aec.getLocation() == Loc.nucleus) {
			if (d > r) {
				double scale;
				if (d > center[0]-1) {
					scale = RandomHelper.nextDoubleFromTo(0, r);
				} else {
					scale = (d-r)*r/(center[0] - r);
				}
				AgentGeometry.scale(v, scale);
			}
		}

		npt[0] = center[0] + v[0];
		npt[1] = center[1] + v[1];
		npt[2] = center[2] + v[2];
		
		return npt;
	}
	
	public static double[] bounceInLocation(double[] npt, Loc loc) {
//returns a new point
		
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius");
		int cr = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		
		double[] center = new double[3];
		center[0] = cr;
		center[1] = cr;
		center[2] = cr;
		
		double[] v = new double[3];
		v[0] = npt[0] - center[0];
		v[1] = npt[1] - center[1];
		v[2] = npt[2] - center[2];
		
		double d = AgentGeometry.calcDistance(center, npt);
		if (loc == Loc.cytoplasm) {
			if (d < r) {
				AgentGeometry.scale(v,(r+(r-d)));
			} else if (d > center[0]) {
				AgentGeometry.scale(v, center[0] - (d-(center[0])));
			}
		} else if (loc == Loc.nucleus) {
			if (d > r) {
				AgentGeometry.scale(v,(r-(d-r)));
			}

		}
		npt[0] = center[0] + v[0];
		npt[1] = center[1] + v[1];
		npt[2] = center[2] + v[2];
		return npt;
	}
	
	public static NdPoint moveByDisplacement(AgentExtendCont aec, double [] disp) {
	
		Dimensions dim = aec.getSpace().getDimensions();
		if (dim.size() < disp.length) {
			throw new IllegalArgumentException(
					"Displacement matrix cannot have more dimensions than space");
		}
		
		NdPoint pt = aec.getSpace().getLocation(aec);
		double maxX = dim.getWidth()-1;
		double maxY = dim.getHeight()-1;
		double maxZ = dim.getDepth()-1;
		double minX=0;
		double minY=0;
		double minZ=0;
		double[] rpt = pt.toDoubleArray(null);
		rpt[0] = rpt[0] + disp[0];
		rpt[1] = rpt[1] + disp[1];
		rpt[2] = rpt[2] + disp[2];
		rpt[0] = rpt[0] < minX ? (minX+(minX-rpt[0])) : rpt[0];
		rpt[0] = rpt[0] > maxX ? (maxX-(rpt[0]-maxX)): rpt[0];
		rpt[1] = rpt[1] < minY ? (minY+(minY-rpt[1])) : rpt[1];
		rpt[1] = rpt[1] > maxY ? (maxY-(rpt[1]-maxY)): rpt[1];
		rpt[2] = rpt[2] < minZ ? (minZ+(minZ-rpt[2])) : rpt[2];
		rpt[2] = rpt[2] > maxZ ? (maxZ-(rpt[2]-maxZ)): rpt[2];
		AgentMove.bounceInLocation(rpt, aec.getLocation());
		aec.getSpace().moveTo(aec, rpt);
		
		return new NdPoint(rpt);
	}
	
	public static boolean addAtRandomLocation (AgentExtendCont aec) {
		
		boolean retval = false;
		double[] npt = aec.getSpace().getLocation(aec).toDoubleArray(null);
		int X = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeX");
		int	Y = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeY");
		int Z = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeZ");
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		double[] center = new double[3];
		center[0] = X/2;
		center[1] = Y/2;
		center[2] = Z/2;
		
		double[] v = new double[3];
		v[0] = npt[0] - center[0];
		v[1] = npt[1] - center[1];
		v[2] = npt[2] - center[2];
		double d = AgentGeometry.calcDistanceSq(center, npt);
		boolean found = false;
		
		while (!found) {
			double x = RandomHelper.nextDoubleFromTo(0, X-1);
			double y = RandomHelper.nextDoubleFromTo(0, Y-1);
			double z = RandomHelper.nextDoubleFromTo(0, Z-1);
			
		}
		return retval;
		
	}
	
/*	public static boolean moveTo (ContinuousSpace space, AgentExtendCont aec, double[] newpt) {
		
		boolean retval = false;
		

		double[] movedCoords = new double[space.getDimensions().size()];
		if (holder.point == null || displacement == null) {
			translator.transform(movedCoords, newLocation);
		} else {
			holder.point.toDoubleArray(movedCoords);
			translator.translate(movedCoords, displacement);
		}
		NdPoint movedPoint = new NdPoint(movedCoords);

		if (accessor.put(object, locationStorage, movedPoint)) {
			if (holder.point != null) {
				accessor.remove(object, locationStorage, holder.point);
			} else {
				// if the object hasn't yet been put in the space
				size++;
			}
			holder.point = movedPoint; 
			fireProjectionEvent(new ProjectionEvent(this, object,
					ProjectionEvent.OBJECT_MOVED));
			return true;
		} else {
			return false;
		}
		return retval;
	}*/
	
}
