package de.frooastside.engine.util;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maths {
	
	public static void scale(Vector3f var1, float value) {
		var1.x *= value;
		var1.y *= value;
		var1.z *= value;
	}

	public static void scale(Vector2f var1, float value) {
		var1.x *= value;
		var1.y *= value;
	}
	
	public static Vector4f mix(Vector4f var1, Vector4f var2, float progression) {
		float x = lerp(var1.x, var2.x, progression);
		float y = lerp(var1.y, var2.y, progression);
		float z = lerp(var1.z, var2.z, progression);
		float w = lerp(var1.w, var2.w, progression);
		return new Vector4f(x, y, z, w);
	}
	
	public static Vector3f mix(Vector3f var1, Vector3f var2, float progression) {
		float x = lerp(var1.x, var2.x, progression);
		float y = lerp(var1.y, var2.y, progression);
		float z = lerp(var1.z, var2.z, progression);
		return new Vector3f(x, y, z);
	}
	
	public static Vector2f mix(Vector2f var1, Vector2f var2, float progression) {
		float x = lerp(var1.x, var2.x, progression);
		float y = lerp(var1.y, var2.y, progression);
		return new Vector2f(x, y);
	}
	
	public static float lerp(float var1, float var2, float progression) {
		float dif = var2 - var1;
		float output = (progression * dif) + var1;
		return output;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f transformMatrix(float x, float y, float z, float pitch, float yaw, float roll, float scale, Matrix4f matrix) {
		matrix.identity();
		matrix.translate(x, y, z);
		matrix.rotate((float) Math.toRadians(pitch), new Vector3f(0, 0, 1));
		matrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(roll), new Vector3f(1, 0, 0));
		matrix.scale(scale);
		return matrix;
	}
	
	public static Matrix4f transformMatrix(Vector3f translation, Vector3f rotation, Vector3f scale, Matrix4f matrix) {
		matrix.identity();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
		matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		matrix.scale(new Vector3f(scale.x, scale.y, scale.z));
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(new Vector3f(translation, 0));
		matrix.scale(new Vector3f(scale.x, scale.y, 1f));
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix2D(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
		matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
		matrix.scale(new Vector3f(scale, 1, scale));
		return matrix;
	}
	
	public static String generateUUID(Random random, int length) {
		String characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
		char[] characters = characterSet.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(characters[random.nextInt(characters.length)]);
		}
		return stringBuilder.toString();
	}

}
