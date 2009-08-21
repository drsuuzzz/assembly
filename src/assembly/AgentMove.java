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

	public AgentMove () {
		
	}
	
	public void bounce () {
		
	}
	
	public static double[] adjustPointToSpace(Loc loc) {
		
		double[] npt = {0.0,0.0,0.0};
		if (loc == Loc.cytoplasm) {
			if (npt[0] > min) {
				npt[0] = max + min -npt[0];
			}
			if (npt[1] > min) {
				npt[1] = max + min-npt[1];
			}
			if (npt[2] > min) {
				npt[2] = max + min - npt[2];
			}
		} else if (loc == Loc.nucleus) {
			npt[0] = npt[0] + min;
			npt[1] = npt[1] + min;
			npt[2] = npt[2] + min;
		}
		
		return npt;
	}
	
	public static double[] randomLocation (Loc loc) {
		
		int X = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeX");
		int	Y = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeY");
		int Z = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellSizeZ");
		
		double min = 0+X/4;
		double max = X-X/4;
		double[] npt = {0.0,0.0,0.0};
		npt[0] = RandomHelper.nextDoubleFromTo(0.0, (X-1)/2);
		npt[1] = RandomHelper.nextDoubleFromTo(0.0, (Y-1)/2);
		npt[2] = RandomHelper.nextDoubleFromTo(0.0, (Z-1)/2);
		if (loc == Loc.cytoplasm) {
			if (npt[0] > min) {
				npt[0] = max + min -npt[0];
			}
			if (npt[1] > min) {
				npt[1] = max + min-npt[1];
			}
			if (npt[2] > min) {
				npt[2] = max + min - npt[2];
			}
		} else if (loc == Loc.nucleus) {
			npt[0] = npt[0] + min;
			npt[1] = npt[1] + min;
			npt[2] = npt[2] + min;
		}
		
		return npt;
		
	}
	
	public static NdPoint moveByDisplacement(ContinuousSpace space, AgentExtendCont aec, double [] disp) {
	
		Dimensions dim = space.getDimensions();
		if (dim.size() < disp.length) {
			throw new IllegalArgumentException(
					"Displacement matrix cannot have more dimensions than space");
		}
		
		NdPoint pt = space.getLocation(aec);
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
		space.moveTo(aec, rpt);
		
		return new NdPoint(rpt);
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
