package net.frooastside.engine.shader;

public class UniformBoolean extends UniformFloat {

  public UniformBoolean(String name) {
    super(name);
  }

  public void loadBoolean(boolean value) {
    loadFloat(value ? 1.0f : 0.0f);
  }

}
