package net.frooastside.engine.shader;

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
    if (location == NOT_FOUND)
      System.err.printf("Didnt find the Uniform Location for %s in the program with the id %d", name, programId);
  }

  public int location() {
    return location;
  }
}
