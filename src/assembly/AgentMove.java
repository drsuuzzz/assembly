package assembly;

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
	
	public static NdPoint moveByDisplacement(ContinuousSpace space, AgentExtendCont aec, double [] disp) {
	
		Dimensions dim = space.getDimensions();
		if (dim.size() < disp.length) {
			throw new IllegalArgumentException(
					"Displacement matrix cannot have more dimensions than space");
		}
		
		NdPoint pt = space.getLocation(aec);
		double[] npt = {0.0,0.0,0.0};
		npt[0] = pt.getX() + disp[0];
		npt[1] = pt.getY() + disp[1];
		npt[2] = pt.getZ() + disp[2];
		if (npt[0] <= 0.0 || npt[0] >= dim.getWidth()-1 ||
				npt[1] <= 0.0 || npt[1] >= dim.getHeight()-1 ||
				npt[2] <= 0.0 || npt[2] >= dim.getDepth()-1){
			npt[0] = pt.getX() - disp[0];
			npt[1] = pt.getY() - disp[1];
			npt[2] = pt.getZ() - disp[2];
		}
		
		space.moveTo(aec, npt);
		
		return new NdPoint(npt);
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
