package net.frooastside.engine.shader;

import org.lwjgl.opengl.GL20;

public class UniformInt extends Uniform {

  private int currentValue;

  public UniformInt(String name) {
    super(name);
  }

  public void loadInt(int value) {
    if (value != currentValue) {
      GL20.glUniform1i(location(), value);
      currentValue = value;
    }
  }

}
