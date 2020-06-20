package de.frooastside.engine.renderer;

import de.frooastside.engine.shader.ShaderProgram;

public abstract class Renderer {
	
	protected ShaderProgram shader;
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
