package love.polardivision.engine.shader;

import java.io.InputStream;
import java.util.Objects;
import love.polardivision.engine.glwrapper.NativeObject;
import love.polardivision.engine.utils.BufferUtils;
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

  public static Shader createShader(InputStream source, ShaderType shaderType, ShaderConstant... shaderConstants) {
    String shaderSource = BufferUtils.streamToString(source);
    Objects.requireNonNull(shaderSource);
    return createShader(shaderSource, shaderType, shaderConstants);
  }

  public static Shader createShader(String shaderSource, ShaderType shaderType, ShaderConstant... shaderConstants) {
    for (ShaderConstant shaderConstant : shaderConstants) {
      shaderSource = shaderSource.replace("0" + shaderConstant.name(), shaderConstant.value());
      shaderSource = shaderSource.replace(shaderConstant.name(), shaderConstant.value());
    }
    Shader shader = new Shader(shaderType);
    shader.initialize();
    shader.shaderSource(shaderSource);
    shader.compile();
    shader.checkErrorLog();
    return shader;
  }

}
