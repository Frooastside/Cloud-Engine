package love.polardivision.engine.shader.uniforms;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;

public class UniformMatrix extends Uniform {

  private static final float[] MATRIX_BUFFER = new float[16];

  public UniformMatrix(String name) {
    super(name);
  }

  public void load(Matrix4f matrix) {
    GL20.glUniformMatrix4fv(location(), false, matrix.get(MATRIX_BUFFER));
  }
}
