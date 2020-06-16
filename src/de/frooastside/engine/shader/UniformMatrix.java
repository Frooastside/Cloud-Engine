package de.frooastside.engine.shader;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class UniformMatrix extends Uniform {
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix4f matrix) {
		store(matrixBuffer, matrix);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(super.getLocation(), false, matrixBuffer);
	}
	

	public void store(FloatBuffer buf, Matrix4f matrix) {
		buf.put(matrix.m00());
		buf.put(matrix.m01());
		buf.put(matrix.m02());
		buf.put(matrix.m03());
		buf.put(matrix.m10());
		buf.put(matrix.m11());
		buf.put(matrix.m12());
		buf.put(matrix.m13());
		buf.put(matrix.m20());
		buf.put(matrix.m21());
		buf.put(matrix.m22());
		buf.put(matrix.m23());
		buf.put(matrix.m30());
		buf.put(matrix.m31());
		buf.put(matrix.m32());
		buf.put(matrix.m33());
	}

}
