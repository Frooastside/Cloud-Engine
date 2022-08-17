package love.polardivision.engine.shader;

import java.util.ArrayList;
import java.util.List;
import love.polardivision.engine.glwrapper.GraphicalObject;
import love.polardivision.engine.shader.uniforms.Uniform;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram extends GraphicalObject {

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
    bind();
    loadTextureUnits();
    unbind();
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

  @Override
  public void bind() {
    GL20.glUseProgram(identifier);
  }

  @Override
  public void unbind() {
    GL20.glUseProgram(0);
  }

  @Override
  public void delete() {
    GL20.glDeleteProgram(identifier);
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
}
