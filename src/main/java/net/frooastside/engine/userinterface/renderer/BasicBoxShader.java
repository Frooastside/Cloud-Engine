package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.shader.*;
import net.frooastside.engine.shader.uniforms.*;
import org.joml.Vector4f;

public class BasicBoxShader extends ShaderProgram {

  private final UniformVector4f uniformTranslation = new UniformVector4f("translation");
  private final UniformFloat uniformAlpha = new UniformFloat("alpha");
  private final UniformBoolean uniformUseColor = new UniformBoolean("useColor");
  private final UniformVector4f uniformColor = new UniformVector4f("color");
  private final UniformBoolean uniformUseTexture = new UniformBoolean("useTexture");
  private final UniformTexture uniformTexture = new UniformTexture("guiTexture");

  @Override
  protected void addShaders() {
    addShader(Shader.createShader("/net/frooastside/engine/userinterface/renderer/basicboxshader/vertexshader.glsl", true, Shader.VERTEX_SHADER));
    addShader(Shader.createShader("/net/frooastside/engine/userinterface/renderer/basicboxshader/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformTranslation);
    storeUniformLocation(uniformAlpha);
    storeUniformLocation(uniformUseColor);
    storeUniformLocation(uniformColor);
    storeUniformLocation(uniformUseTexture);
    storeUniformLocation(uniformTexture);
  }

  @Override
  protected void loadTextureUnits() {
    uniformTexture.loadTextureUnit(0);
  }

  public void loadTranslation(Vector4f translation) {
    loadTranslation(translation.x, translation.y, translation.z, translation.w);
  }

  public void loadTranslation(float x, float y, float z, float w) {
    uniformTranslation.loadVector4f(x, y, z, w);
  }

  public void loadAlpha(float alpha) {
    uniformAlpha.loadFloat(alpha);
  }

  public void loadUseColor(boolean useColor) {
    uniformUseColor.loadBoolean(useColor);
  }

  public void loadColor(Vector4f color) {
    loadColor(color.x, color.y, color.z, color.w);
  }

  public void loadColor(float r, float g, float b, float a) {
    uniformColor.loadVector4f(r, g, b, a);
  }

  public void loadUseTexture(boolean useTexture) {
    uniformUseTexture.loadBoolean(useTexture);
  }

  public void loadTexture(Texture texture) {
    uniformTexture.activeTextureUnit();
    texture.bind();
  }

}
