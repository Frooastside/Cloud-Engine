package net.frooastside.engine.shader;

import org.lwjgl.opengl.GL20;

public class UniformFloat extends Uniform {

  private float currentValue;

  public UniformFloat(String name) {
    super(name);
  }

  public void loadFloat(float value) {
    if (value != currentValue) {
      GL20.glUniform1f(location(), value);
      currentValue = value;
    }
  }

}
