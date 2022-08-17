package love.polardivision.engine.shader.uniforms;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

public class UniformVector3f extends Uniform {

  private float currentX, currentY, currentZ;

  public UniformVector3f(String name) {
    super(name);
  }

  public void loadVector3f(Vector3f vector) {
    loadVector3f(vector.x, vector.y, vector.z);
  }

  public void loadVector3f(float x, float y, float z) {
    if (currentX != x || currentY != y || currentZ != z) {
      this.currentX = x;
      this.currentY = y;
      this.currentZ = z;
      GL20.glUniform3f(location(), x, y, z);
    }
  }

}
