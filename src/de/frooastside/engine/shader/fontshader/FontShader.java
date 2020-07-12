package de.frooastside.engine.shader.fontshader;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.frooastside.engine.shader.ShaderProgram;
import de.frooastside.engine.shader.UniformVec2;
import de.frooastside.engine.shader.UniformVec4;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/de/frooastside/engine/shader/fontshader/vertexshader.glsl";
	private static final String FRAGMENT_FILE = "/de/frooastside/engine/shader/fontshader/fragmentshader.glsl";
	
	private UniformVec4 color = new UniformVec4("color");
	private UniformVec2 translation = new UniformVec2("translation");
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords");
		storeAllUniformLocations(color, translation);
	}
	
	public void loadColor(Vector4f vector4f) {
		this.color.loadVec4(vector4f);
	}

	public void loadTranslation(Vector2f translation) {
		this.translation.loadVec2(translation);
	}

}
