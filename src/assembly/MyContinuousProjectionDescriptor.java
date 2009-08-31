package assembly;

import java.util.ArrayList;
import java.util.List;

import repast.score.SProjection;
import repast.simphony.visualization.continuous.ContinuousProjectionDescriptor;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.decorator.ProjectionDecoratorFactory;

public class MyContinuousProjectionDescriptor extends
		ContinuousProjectionDescriptor {
	
	public MyContinuousProjectionDescriptor(SProjection proj) {
		super(proj);
	}

	public Iterable<ProjectionDecorator3D> get3DDecorators() {
		List<ProjectionDecorator3D> list = new ArrayList<ProjectionDecorator3D>();
		MyProjectionDecoratorFactory fac = new MyProjectionDecoratorFactory();
		for (String key : props.keySet()) {
			ProjectionDecorator3D deco = fac.create3DDecorator(key, props.get(key));
			if (deco != null) list.add(deco);
		}
		return list;
	}
}
