package net.frooastside.engine.shader;

import org.lwjgl.opengl.GL20;

public class UniformTexture extends Uniform {

  public UniformTexture(String name) {
    super(name);
  }

  public void loadTextureUnit(int value) {
    GL20.glUniform1i(location(), value);
  }

}
