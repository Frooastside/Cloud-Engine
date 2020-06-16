package de.frooastside.engine.shader;

import org.lwjgl.opengl.GL20;

import de.frooastside.engine.language.I18n;

public abstract class Uniform {
	
	private static final int NOT_FOUND = -1;
	
	private String name;
	private int location;
	
	protected Uniform(String name) {
		this.name = name;
	}
	
	protected void storeUniformLocation(int programID) {
		location = GL20.glGetUniformLocation(programID, name);
		if(location == NOT_FOUND) {
			System.err.println(I18n.get("error.shader.uniformLocation", name, programID));
		}
	}
	
	protected int getLocation() {
		return location;
	}

}
