package de.frooastside.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class VertexArrayObjectLoader {
	
	private static List<Integer> textures = new ArrayList<>();
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();
	
	public static VaoData loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVao();
		storeIndexBufferInAttributeList(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVao();
		vaos.add(vaoID);
		return new VaoData(vaoID, indices.length);
	}
	
	public static VaoData loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] jointIndices, float[] weights, int[] indices) {
		int vaoID = createVao();
		storeIndexBufferInAttributeList(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeIntegerDataInAttributeList(3, 4, jointIndices);
		storeDataInAttributeList(4, 4, weights);
		unbindVao();
		vaos.add(vaoID);
		return new VaoData(vaoID, indices.length);
	}
	
	public static VaoData loadToVao(float[] positions) {
		int vaoID = createVao();
		storeDataInAttributeList(0, 2, positions);
		unbindVao();
		vaos.add(vaoID);
		return new VaoData(vaoID, positions.length/2);
	}
	
	public static VaoData loadToVao(float[] positions, float[] textureCoords) {
		int vaoID = createVao();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		vaos.add(vaoID);
		unbindVao();
		return new VaoData(vaoID, positions.length/2);
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
	
	public static int storeIntegerDataInAttributeList(int index, int dataSize, int[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribIPointer(index, dataSize, GL11.GL_INT, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vbos.add(vbo);
		return vbo;
	}
	
	public static int storeIndexBufferInAttributeList(int[] indices) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		vbos.add(vbo);
		return vbo;
	}
	
	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
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
		for(int texture : textures) {
			GL13.glDeleteTextures(texture);
		}
	}

}
