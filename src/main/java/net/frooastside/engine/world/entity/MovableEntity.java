package net.frooastside.engine.world.entity;

public class MovableEntity extends Entity {

  public MovableEntity(float x, float y, float z, float pitch, float yaw, float roll) {
    super(x, y, z, pitch, yaw, roll);
  }

  public MovableEntity(float x, float y, float z, float pitch, float yaw, float roll, float scale) {
    super(x, y, z, pitch, yaw, roll, scale);
  }
  
}
