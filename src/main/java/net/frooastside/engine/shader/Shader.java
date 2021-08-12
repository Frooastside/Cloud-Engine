package net.frooastside.engine.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

import java.io.*;
import java.util.Objects;

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

  public static Shader createShader(String sourceFilePath, boolean isInClassPath, int shaderType) {
    int shaderId = GL20.glCreateShader(shaderType);
    String shaderSource = fileToShaderSource(sourceFilePath, isInClassPath);
    Objects.requireNonNull(shaderSource);
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
    if (fileAsInputStream != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(fileAsInputStream));
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
