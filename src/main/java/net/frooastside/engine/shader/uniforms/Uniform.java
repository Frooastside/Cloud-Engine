package net.frooastside.engine.shader.uniforms;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {

  private static final int NOT_FOUND = -1;

  private final String name;
  private int location;

  public Uniform(String name) {
    this.name = name;
  }

  public void storeUniformLocation(int programId) {
    location = GL20.glGetUniformLocation(programId, name);
    if (location == NOT_FOUND) {
      //TODO Language
      //System.err.println(I18n.get("error.shader.uniformLocation", name, programId));
      System.err.println("error.shader.uniformLocation, " + name + ", " + programId);
    }
  }

  public int location() {
    return location;
  }
}
