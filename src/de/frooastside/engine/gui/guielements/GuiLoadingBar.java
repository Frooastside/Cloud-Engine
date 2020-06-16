package de.frooastside.engine.gui.guielements;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import de.frooastside.engine.gui.ElementConstraints;
import de.frooastside.engine.gui.GuiElement;
import de.frooastside.engine.gui.constraints.PixelConstraint;
import de.frooastside.engine.gui.constraints.RawConstraint;

public class GuiLoadingBar extends GuiElement {
	
	private List<GuiRenderElement> elements;
	private int listLength;
	private int currentElement = 0;
	private float progress = -1;
	private float speed;
	private Vector4f standardColor;
	private Vector4f highlightedColor;
	
	public GuiLoadingBar(ElementConstraints constraints, int size, int space, int count, float speed, Vector4f standardColor, Vector4f highlightedColor) {
		this.constraints = constraints;
		this.speed = speed;
		this.standardColor = standardColor;
		this.highlightedColor = highlightedColor;
		this.elements = new ArrayList<GuiRenderElement>();
		for(int i = 0; i < count; i++) {
			GuiRenderElement element = new GuiRenderElement(standardColor);
			element.getConstraints().setParent(constraints);
			element.getConstraints().setX(new PixelConstraint((size + space) * i));
			element.getConstraints().setY(new RawConstraint(0));
			element.getConstraints().setWidth(new PixelConstraint(size));
			element.getConstraints().setHeight(new PixelConstraint(size));
			elements.add(element);
		}
		listLength = elements.size();
	}
	
	@Override
	public void update(float delta) {
		if(progress < 1) {
			progress += speed * delta;
			elements.get(currentElement).setRenderColor(mix(standardColor, highlightedColor, (progress + 1) / 2));
		}else {
			if(currentElement >= listLength - 1) {
				elements.get(currentElement).setRenderColor(standardColor);
				currentElement = 0;
				progress = -1;
				elements.get(currentElement).setRenderColor(standardColor);
			}else {
				elements.get(currentElement).setRenderColor(standardColor);
				currentElement++;
				progress = -1;
				elements.get(currentElement).setRenderColor(standardColor);
			}
		}
	}
	
	public static Vector4f mix(Vector4f var1, Vector4f var2, float mix) {
		float x = lerp(var1.x, var2.x, mix);
		float y = lerp(var1.y, var2.y, mix);
		float z = lerp(var1.z, var2.z, mix);
		float w = lerp(var1.w, var2.w, mix);
		return new Vector4f(x, y, z, w);
	}
	
	public static float lerp(float var1, float var2, float mix) {
		return (mix * (var2 - var1)) + var1;
	}

	public List<GuiRenderElement> getElements() {
		return elements;
	}

}
