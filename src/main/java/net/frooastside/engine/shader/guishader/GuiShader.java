package net.frooastside.engine.shader.guishader;

import net.frooastside.engine.resource.texture.Texture;
import net.frooastside.engine.shader.*;

public class GuiShader extends ShaderProgram {

  private final UniformBoolean uniformUseTexture = new UniformBoolean("useTexture");
  private final UniformFloat uniformVisibility = new UniformFloat("visibility");
  private final UniformTexture uniformTexture = new UniformTexture("guiTexture");
  private final UniformVector3f uniformColor = new UniformVector3f("color");

  @Override
  protected void addShaders() {
    addShader(Shader.createShader("/net/frooastside/engine/shader/basicguishader/vertexshader.glsl", true, Shader.VERTEX_SHADER));
    addShader(Shader.createShader("/net/frooastside/engine/shader/basicguishader/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformUseTexture);
    storeUniformLocation(uniformVisibility);
    storeUniformLocation(uniformTexture);
    storeUniformLocation(uniformColor);
  }

  @Override
  protected void setDefaults() {
    uniformTexture.loadTextureUnit(0);
    loadVisibility(1);
  }

  private void loadUseTexture(boolean useTexture) {
    uniformUseTexture.loadBoolean(useTexture);
  }

  public void loadVisibility(float visibility) {
    uniformVisibility.loadFloat(visibility);
  }

  public void loadTexture(Texture texture) {
    loadUseTexture(true);
    uniformTexture.activeTextureUnit();
    texture.bind();
  }

  public void loadColor(float x, float y, float z, float w) {
    loadUseTexture(false);
    uniformColor.loadVector3f(x, y, z);
    loadVisibility(w);
  }

}
