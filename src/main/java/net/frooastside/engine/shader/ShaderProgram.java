package net.frooastside.engine.shader;

import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram {

  private int identifier;

  private final List<Shader> shaders = new ArrayList<>();

  public void createShaderProgram() {
    this.identifier = GL20.glCreateProgram();
    addShaders();
    attachShaders();
    bindAttributes();
    linkProgram();
    deleteShaders();
    storeUniformLocations();
    validate();
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

  public void delete() {
    GL20.glDeleteProgram(identifier);
  }

}
