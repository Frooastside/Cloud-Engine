package de.frooastside.engine.model;

public class VaoData {
	
	private int ID;
	private int vertexCount;
	
	public VaoData(int id, int vertexCount) {
		this.ID = id;
		this.vertexCount = vertexCount;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

}
