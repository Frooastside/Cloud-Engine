package net.frooastside.engine.gui;

import net.frooastside.engine.datatypes.texture.Texture;
import net.frooastside.engine.shader.*;
import org.joml.Vector3f;

public class BasicFontShader extends ShaderProgram {

  private final UniformTexture uniformTexture = new UniformTexture("fontAtlas");
  private final UniformVector3f uniformColor = new UniformVector3f("color");
  private final UniformFloat uniformVisibility = new UniformFloat("visibility");
  private final UniformFloat uniformWidth = new UniformFloat("width");
  private final UniformFloat uniformEdge = new UniformFloat("edge");

  @Override
  protected void addShaders() {
    addShader(Shader.createShader("/net/frooastside/engine/shader/basicfontshader/vertexshader.glsl", true, Shader.VERTEX_SHADER));
    addShader(Shader.createShader("/net/frooastside/engine/shader/basicfontshader/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformVisibility);
    storeUniformLocation(uniformTexture);
    storeUniformLocation(uniformColor);
    storeUniformLocation(uniformWidth);
    storeUniformLocation(uniformEdge);
  }

  @Override
  protected void setDefaults() {
    uniformTexture.loadTextureUnit(0);
    loadVisibility(1);
  }

  public void loadTexture(Texture texture) {
    uniformTexture.activeTextureUnit();
    texture.bind();
  }

  public void loadColor(Vector3f color) {
    loadColor(color.x, color.y, color.z);
  }

  public void loadColor(float r, float g, float b) {
    uniformColor.loadVector3f(r, g, b);
  }

  public void loadVisibility(float visibility) {
    uniformVisibility.loadFloat(visibility);
  }

  public void loadWidth(float width) {
    uniformWidth.loadFloat(width);
  }

  public void loadEdge(float edge) {
    uniformEdge.loadFloat(edge);
  }
}
