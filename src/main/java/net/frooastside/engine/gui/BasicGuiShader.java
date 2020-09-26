package net.frooastside.engine.gui;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.shader.*;
import net.frooastside.engine.shader.uniforms.UniformBoolean;
import net.frooastside.engine.shader.uniforms.UniformFloat;
import net.frooastside.engine.shader.uniforms.UniformTexture;
import net.frooastside.engine.shader.uniforms.UniformVector3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BasicGuiShader extends ShaderProgram {

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

  public void loadColor(Vector3f color) {
    loadColor(color.x, color.y, color.z);
  }

  public void loadColor(float r, float g, float b) {
    loadUseTexture(false);
    uniformColor.loadVector3f(r, g, b);
  }

  public void loadColor(Vector4f color) {
    loadColor(color.x, color.y, color.z, color.w);
  }

  public void loadColor(float r, float g, float b, float a) {
    loadUseTexture(false);
    uniformColor.loadVector3f(r, g, b);
    loadVisibility(a);
  }

}
