package de.frooastside.engine.shader.guishader;

import org.joml.Vector4f;

import de.frooastside.engine.shader.ShaderProgram;
import de.frooastside.engine.shader.UniformBoolean;
import de.frooastside.engine.shader.UniformFloat;
import de.frooastside.engine.shader.UniformSampler;
import de.frooastside.engine.shader.UniformVec4;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/de/frooastside/engine/shader/guishader/vertexshader.glsl";
	private static final String FRAGMENT_FILE = "/de/frooastside/engine/shader/guishader/fragmentshader.glsl";
	
	private UniformSampler guiTexture = new UniformSampler("guiTexture");
	private UniformBoolean useColor = new UniformBoolean("useColor");
	private UniformFloat brightness = new UniformFloat("brightness");
	private UniformVec4 transform = new UniformVec4("transform");
	private UniformVec4 color = new UniformVec4("color");

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		storeAllUniformLocations(transform, guiTexture, useColor, brightness, color);
		start();
		guiTexture.loadTexUnit(0);
		stop();
	}
	
	public void loadTransformation(Vector4f transform) {
		this.transform.loadVec4(transform);
	}
	
	public void loadColor(Vector4f color) {
		this.color.loadVec4(color);
		useColor.loadBoolean(true);
	}
	
	public void loadTexture() {
		useColor.loadBoolean(false);
	}

	public void loadBrightness(float brightness) {
		this.brightness.loadFloat(brightness);
	}

}
