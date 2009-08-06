package assembly;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.AbstractContinuousSpace.PointHolder;
import repast.simphony.space.projection.ProjectionEvent;

public class AgentMove {

	public AgentMove () {
		
	}
	
	public static NdPoint moveByDisplacement(ContinuousSpace space, AgentExtendCont aec, double [] disp) {
	
		if (dimensions.size() < displacement.length) {
			throw new IllegalArgumentException(
					"Displacement matrix cannot have more dimensions than space");
		}

		if (doMove(object, displacement, null)) {
			return agentLocationMap.get(object).point;
		} else {
			return null;
		}
		NdPoint pt = null;
		
		return pt;
	}
	
	public static boolean moveTo (ContinuousSpace space, AgentExtendCont aec, double[] newpt) {
		
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
	}
	
}
