package test;

import net.frooastside.engine.shader.Shader;
import net.frooastside.engine.shader.ShaderProgram;

public class TriangleShaderProgram extends ShaderProgram {

  @Override
  protected void addShaders() {
    addShader(Shader.createShader("/vertexshader.glsl", true, Shader.VERTEX_SHADER));
    addShader(Shader.createShader("/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "color");
  }

  @Override
  protected void storeUniformLocations() {

  }

}
