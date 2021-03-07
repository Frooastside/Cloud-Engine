package net.frooastside.engine.shader;

import net.frooastside.engine.graphicobjects.RenderObject;
import net.frooastside.engine.shader.uniforms.Uniform;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram extends RenderObject {

  private int identifier;

  private final List<Shader> shaders = new ArrayList<>();

  @Override
  public void initialize() {
    this.identifier = GL20.glCreateProgram();
    addShaders();
    attachShaders();
    bindAttributes();
    linkProgram();
    deleteShaders();
    storeUniformLocations();
    validate();
    start();
    loadTextureUnits();
    stop();
  }

  protected abstract void addShaders();

  protected void addShader(Shader shader) {
    shaders.add(shader);
  }

  protected abstract void bindAttributes();

  protected void bindAttribute(int index, String attributeName) {
    GL20.glBindAttribLocation(identifier, index, attributeName);
  }

  protected abstract void storeUniformLocations();

  protected void storeUniformLocation(Uniform uniform) {
    uniform.storeUniformLocation(identifier);
  }

  protected void loadTextureUnits() {
  }

  protected void attachShaders() {
    shaders.forEach(shader -> shader.attach(identifier));
  }

  protected void linkProgram() {
    GL20.glLinkProgram(identifier);
  }

  protected void deleteShaders() {
    shaders.forEach(shader -> {
      shader.detach(identifier);
      shader.delete();
    });
  }

  protected void validate() {
    GL20.glValidateProgram(identifier);
  }

  public void start() {
    GL20.glUseProgram(identifier);
  }

  public void stop() {
    GL20.glUseProgram(0);
  }

  @Override
  public void delete() {
    GL20.glDeleteProgram(identifier);
  }

}
