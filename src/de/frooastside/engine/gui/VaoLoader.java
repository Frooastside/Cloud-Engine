package de.frooastside.engine.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VaoLoader {
	
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();
	
	public static VaoData loadToVao(float[] positions) {
		int vaoID = createVao();
		storeDataInAttributeList(0, 2, positions);
		unbindVao();
		vaos.add(vaoID);
		return new VaoData(vaoID, positions.length / 2);
	}
	
	public static VaoData loadToVao(float[] positions, float[] textureCoords) {
		int vaoID = createVao();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		vaos.add(vaoID);
		unbindVao();
		return new VaoData(vaoID, positions.length / 2);
	}
	
	public static void loadToExistingVao(float[] positions, float[] textureCoords, int vaoID) {
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		vaos.add(vaoID);
		unbindVao();
	}
	
	public static int storeDataInAttributeList(int index, int dataSize, float[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, dataSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vbos.add(vbo);
		return vbo;
	}
	
	public static int createVao() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	public static void unbindVao() {
		GL30.glBindVertexArray(0);
	}
	
	public static void cleanUp() {
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}

}
