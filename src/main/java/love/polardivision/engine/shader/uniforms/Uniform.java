package love.polardivision.engine.shader.uniforms;

import love.polardivision.engine.language.I18n;
import love.polardivision.engine.language.UsesTranslation;
import org.lwjgl.opengl.GL20;

public abstract class Uniform {

  private static final int NOT_FOUND = -1;

  private final String name;
  private int location;

  public Uniform(String name) {
    this.name = name;
  }

  @UsesTranslation("error.shader.uniformLocation")
  public void storeUniformLocation(int programId) {
    location = GL20.glGetUniformLocation(programId, name);
    if (location == NOT_FOUND) {
      throw new IllegalStateException(I18n.get("error.shader.uniformLocation", name, programId));
    }
  }

  public int location() {
    return location;
  }
}
