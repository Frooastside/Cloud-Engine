package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.shader.*;
import net.frooastside.engine.shader.uniforms.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class BasicFontShader extends ShaderProgram {

  private final UniformVector2f uniformOffset = new UniformVector2f("offset");
  private final UniformTexture uniformTexture = new UniformTexture("fontAtlas");
  private final UniformVector4f uniformColor = new UniformVector4f("color");
  private final UniformFloat uniformAlpha = new UniformFloat("alpha");
  private final UniformFloat uniformWidth = new UniformFloat("width");
  private final UniformFloat uniformEdge = new UniformFloat("edge");

  @Override
  protected void addShaders() {
    addShader(Shader.createShader("/net/frooastside/engine/userinterface/renderer/basicfontshader/vertexshader.glsl", true, Shader.VERTEX_SHADER));
    addShader(Shader.createShader("/net/frooastside/engine/userinterface/renderer/basicfontshader/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformOffset);
    storeUniformLocation(uniformTexture);
    storeUniformLocation(uniformColor);
    storeUniformLocation(uniformAlpha);
    storeUniformLocation(uniformWidth);
    storeUniformLocation(uniformEdge);
  }

  @Override
  protected void loadTextureUnits() {
    uniformTexture.loadTextureUnit(0);
    loadWidth(0.5f);
    loadEdge(0.15f);
  }

  public void loadOffset(Vector2f offset) {
    loadOffset(offset.x, offset.y);
  }

  public void loadOffset(float x, float y) {
    uniformOffset.loadVector2f(x, y);
  }

  public void loadTexture(Texture texture) {
    uniformTexture.activeTextureUnit();
    texture.bind();
  }

  public void loadColor(Vector4f color) {
    loadColor(color.x, color.y, color.z, color.w);
  }

  public void loadColor(float r, float g, float b, float a) {
    uniformColor.loadVector4f(r, g, b, a);
  }

  public void loadAlpha(float alpha) {
    uniformAlpha.loadFloat(alpha);
  }

  public void loadWidth(float width) {
    uniformWidth.loadFloat(width);
  }

  public void loadEdge(float edge) {
    uniformEdge.loadFloat(edge);
  }
}
