package love.polardivision.engine.shader.uniforms;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

public class UniformVector2f extends Uniform {

  private float currentX, currentY;

  public UniformVector2f(String name) {
    super(name);
  }

  public void loadVector2f(Vector2f vector) {
    loadVector2f(vector.x, vector.y);
  }

  public void loadVector2f(float x, float y) {
    if (currentX != x || currentY != y) {
      this.currentX = x;
      this.currentY = y;
      GL20.glUniform2f(location(), x, y);
    }
  }

}
