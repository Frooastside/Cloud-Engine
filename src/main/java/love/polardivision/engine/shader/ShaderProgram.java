/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.shader;

import love.polardivision.engine.glwrapper.GraphicalObject;
import love.polardivision.engine.shader.uniforms.Uniform;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram extends GraphicalObject {

  private int identifier;

  private final List<Shader> shaders = new ArrayList<>();

  @Override
  public void initialize() throws UniformLocationError {
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

  protected abstract void storeUniformLocations() throws UniformLocationError;

  protected void storeUniformLocation(Uniform uniform) throws UniformLocationError {
    uniform.storeUniformLocation(identifier);
  }

  protected void loadTextureUnits() {
  }
}
