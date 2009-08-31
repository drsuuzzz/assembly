package assembly;

import java.awt.Color;
import java.util.Map;

import repast.simphony.visualization.continuous.Continuous3DProjectionDecorator;
import repast.simphony.visualization.continuous.ContinuousDecorator;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.grid.Grid3DProjectionDecorator;
import repast.simphony.visualization.grid.GridDecorator;

public class MyProjectionDecoratorFactory {

	
	public ProjectionDecorator3D create3DDecorator(String decoratorID, Map<String, Object> props) {
		if (decoratorID.equals(GridDecorator.GRID_DECORATOR)) {
			// create a 3D grid decorator from those properties.
			Grid3DProjectionDecorator deco = new Grid3DProjectionDecorator();
			if ((Boolean) props.get(Grid3DProjectionDecorator.SHOW_DECORATOR)) {
				deco.setUnitSize((Float) props.get(AbstractProjectionDecorator.UNIT_SIZE));
				deco.setColor(new Color((Integer) props.get(AbstractProjectionDecorator.COLOR)));
				return deco;
			}
		} else if (decoratorID.equals(ContinuousDecorator.CONTINUOUS_DECORATOR)) {
			MyContinuous3DProjectionDecorator deco = new MyContinuous3DProjectionDecorator();
			if ((Boolean) props.get(MyContinuous3DProjectionDecorator.SHOW_DECORATOR)) {
				deco.setUnitSize((Float) props.get(MyContinuous3DProjectionDecorator.UNIT_SIZE));
				deco.setColor(new Color((Integer) props.get(MyContinuous3DProjectionDecorator.COLOR)));
				return deco;
			}
		}
		return null;
	}
}
