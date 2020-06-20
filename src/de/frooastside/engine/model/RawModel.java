package de.frooastside.engine.model;

import de.frooastside.engine.Engine;
import de.frooastside.engine.language.I18n;
import de.frooastside.engine.resource.ILoadingQueueElement;

public class RawModel implements ILoadingQueueElement {
	
	private VaoData vao;
	
	private int processID;
	private float[] positions;
	private float[] textureCoords;
	private float[] normals;
	private float[] weights;
	private int[] jointIndices;
	private int[] indices;
	
	private boolean processed = false;
	
	public RawModel(float[] positions, float[] textureCoords, float[] normals, int[] indices, boolean loadInstantly) {
		if(loadInstantly) {
			this.vao = VertexArrayObjectLoader.loadToVao(positions, textureCoords, normals, indices);
			processed = true;
		}else {
			processID = 0;
			this.positions = positions;
			this.textureCoords = textureCoords;
			this.normals = normals;
			this.indices = indices;
			Engine.getEngine().getLoadingQueue().addToLoadingQueue(this);
		}
	}
	
	public RawModel(float[] positions, float[] textureCoords, float[] normals, int[] jointIndices, float[] weights, int[] indices, boolean loadInstantly) {
		if(loadInstantly) {
			this.vao = VertexArrayObjectLoader.loadToVao(positions, textureCoords, normals, jointIndices, weights, indices);
			processed = true;
		}else {
			processID = 1;
			this.positions = positions;
			this.textureCoords = textureCoords;
			this.normals = normals;
			this.indices = indices;
			this.jointIndices = jointIndices;
			this.weights = weights;
			Engine.getEngine().getLoadingQueue().addToLoadingQueue(this);
		}
	}
	
	public RawModel(float[] positions, boolean loadInstantly) {
		if(loadInstantly) {
			this.vao = VertexArrayObjectLoader.loadToVao(positions);
			processed = true;
		}else {
			processID = 2;
			this.positions = positions;
			Engine.getEngine().getLoadingQueue().addToLoadingQueue(this);
		}
	}

	@Override
	public void process() {
		switch (processID) {
		case 0:
			this.vao = VertexArrayObjectLoader.loadToVao(positions, textureCoords, normals, indices);
			processed = true;
			break;
		case 1:
			this.vao = VertexArrayObjectLoader.loadToVao(positions, textureCoords, normals, jointIndices, weights, indices);
			processed = true;
			break;
		case 2:
			this.vao = VertexArrayObjectLoader.loadToVao(positions);
			processed = true;
			break;
		default:
			System.err.println(I18n.get("error.lq.model.processID") + processID);
			break;
		}
	}

	public VaoData getVao() {
		return vao;
	}

	public boolean isProcessed() {
		return processed;
	}

}
