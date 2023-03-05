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

import java.io.InputStream;
import java.util.Objects;
import love.polardivision.engine.utils.BufferUtils;
import love.polardivision.engine.wrappers.NativeObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader implements NativeObject {

  private final ShaderType shaderType;

  private int identifier;

  public Shader(ShaderType shaderType) {
    this.shaderType = shaderType;
  }

  @Override
  public void initialize() {
    this.identifier = GL20.glCreateShader(shaderType.value());
  }

  @Override
  public void delete() {
    GL20.glDeleteShader(identifier);
  }

  public void attach(int programId) {
    GL20.glAttachShader(programId, identifier);
  }

  public void detach(int programId) {
    GL20.glDetachShader(programId, identifier);
  }

  public void compile() {
    GL20.glCompileShader(identifier);
  }

  public void shaderSource(String source) {
    GL20.glShaderSource(identifier, source);
  }

  public void checkErrorLog() {
    if (GL20.glGetShaderi(identifier, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      System.err.println(GL20.glGetShaderInfoLog(identifier));
    }
  }

  public static Shader createShader(
      InputStream source, ShaderType shaderType, int version, ShaderConstant... shaderConstants) {
    String shaderSource = BufferUtils.streamToString(source);
    Objects.requireNonNull(shaderSource);
    return createShader(shaderSource, shaderType, version, shaderConstants);
  }

  public static Shader createShader(
      String shaderSource, ShaderType shaderType, int version, ShaderConstant... shaderConstants) {
    StringBuilder builder = new StringBuilder(String.format("#version %d%n", version));
    for (ShaderConstant shaderConstant : shaderConstants) {
      builder.append(
          String.format("#define %s %s%n", shaderConstant.name(), shaderConstant.value()));
    }
    builder.append(shaderSource);
    Shader shader = new Shader(shaderType);
    shader.initialize();
    shader.shaderSource(builder.toString());
    shader.compile();
    shader.checkErrorLog();
    return shader;
  }
}
