package de.frooastside.engine.gui;

public class ElementConstraints {
	
	private ElementConstraints parent;
	
	private Constraint x;
	private Constraint y;
	private Constraint width;
	private Constraint height;
	
	public Constraint getX() {
		return x;
	}
	
	public void setX(Constraint x) {
		this.x = x;
		this.x.constraints = this;
	}

	public Constraint getY() {
		return y;
	}

	public void setY(Constraint y) {
		this.y = y;
		this.y.constraints = this;
	}

	public Constraint getWidth() {
		return width;
	}

	public void setWidth(Constraint width) {
		this.width = width;
		this.width.constraints = this;
	}

	public Constraint getHeight() {
		return height;
	}

	public void setHeight(Constraint height) {
		this.height = height;
		this.height.constraints = this;
	}

	public ElementConstraints getParent() {
		return parent;
	}

	public void setParent(ElementConstraints parent) {
		this.parent = parent;
	}

}
