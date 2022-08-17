package love.polardivision.engine.shader.uniforms;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class UniformTexture extends Uniform {

  private int textureUnit;

  public UniformTexture(String name) {
    super(name);
  }

  public void loadTextureUnit(int value) {
    this.textureUnit = value;
    GL20.glUniform1i(location(), value);
  }

  public void activeTextureUnit() {
    GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
  }

}
