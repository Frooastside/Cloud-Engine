package net.frooastside.engine.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public class Shader {

  public static int VERTEX_SHADER = GL20.GL_VERTEX_SHADER;
  public static int TESSELATION_CONTROL_SHADER = GL40.GL_TESS_CONTROL_SHADER;
  public static int TESSELATION_EVALUATION_SHADER = GL40.GL_TESS_EVALUATION_SHADER;
  public static int GEOMETRY_SHADER = GL32.GL_GEOMETRY_SHADER;
  public static int FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;

  private final int identifier;

  private Shader(int identifier) {
    this.identifier = identifier;
  }

  public void attach(int programId) {
    GL20.glAttachShader(programId, identifier);
  }

  public void detach(int programId) {
    GL20.glDetachShader(programId, identifier);
  }

  public void delete() {
    GL20.glDeleteShader(identifier);
  }

  public static Shader createShader(InputStream source, int shaderType, ShaderConstant... shaderConstants) {
    String shaderSource = streamToShaderSource(source);
    Objects.requireNonNull(shaderSource);
    return createShader(shaderSource, shaderType, shaderConstants);
  }

  public static Shader createShader(String sourceFilePath, boolean isInClassPath, int shaderType, ShaderConstant... shaderConstants) {
    String shaderSource = fileToShaderSource(sourceFilePath, isInClassPath);
    Objects.requireNonNull(shaderSource);
    return createShader(shaderSource, shaderType, shaderConstants);
  }

  public static Shader createShader(String shaderSource, int shaderType, ShaderConstant... shaderConstants) {
    for (ShaderConstant shaderConstant : shaderConstants) {
      shaderSource = shaderSource.replace("0" + shaderConstant.name(), shaderConstant.value());
      shaderSource = shaderSource.replace(shaderConstant.name(), shaderConstant.value());
    }
    int shaderId = GL20.glCreateShader(shaderType);
    GL20.glShaderSource(shaderId, shaderSource);
    GL20.glCompileShader(shaderId);
    if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
      System.err.println(GL20.glGetShaderInfoLog(shaderId));
    return new Shader(shaderId);
  }

  private static String fileToShaderSource(String sourceFilePath, boolean isInClassPath) {
    InputStream fileAsInputStream;
    if (isInClassPath) {
      fileAsInputStream = Shader.class.getResourceAsStream(sourceFilePath);
    } else {
      try {
        fileAsInputStream = new File(sourceFilePath).toURI().toURL().openStream();
      } catch (IOException exception) {
        throw new IllegalStateException(exception);
      }
    }
    return streamToShaderSource(fileAsInputStream);
  }

  private static String streamToShaderSource(InputStream inputStream) {
    if (inputStream != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder shaderSource = new StringBuilder();
      try {
        while (reader.ready()) {
          shaderSource.append(reader.readLine()).append("\r\n");
        }
        return shaderSource.toString();
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }
    return null;
  }

}
