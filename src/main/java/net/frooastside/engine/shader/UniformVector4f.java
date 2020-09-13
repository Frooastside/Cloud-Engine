package net.frooastside.engine.shader;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class UniformVector4f extends Uniform {

  private float currentX, currentY, currentZ, currentW;

  public UniformVector4f(String name) {
    super(name);
  }

  public void loadVector4f(Vector4f vector) {
    loadVector4f(vector.x, vector.y, vector.z, vector.w);
  }

  public void loadVector4f(float x, float y, float z, float w) {
    if (currentX != x || currentY != y || currentZ != z || currentW != w) {
      this.currentX = x;
      this.currentY = y;
      this.currentZ = z;
      this.currentW = w;
      GL20.glUniform4f(location(), x, y, z, w);
    }
  }

}
