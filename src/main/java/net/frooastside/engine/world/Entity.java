package net.frooastside.engine.world;

import org.joml.Matrix4f;

public abstract class Entity {

  private float x, y, z, pitch, yaw, roll;
  private float scale;

  private final Matrix4f transformationMatrix = new Matrix4f();

  //TODO TEXTURED MODEL



  public Entity(float x, float y, float z, float pitch, float yaw, float roll) {
    this(x, y, z, pitch, yaw, roll, 1);
  }

  public Entity(float x, float y, float z, float pitch, float yaw, float roll, float scale) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.pitch = pitch;
    this.yaw = yaw;
    this.roll = roll;
    this.scale = scale;
  }

  public void setPosition(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setRotation(float pitch, float yaw, float roll) {
    this.pitch = pitch;
    this.yaw = yaw;
    this.roll = roll;
  }

  public float x() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float y() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float z() {
    return z;
  }

  public void setZ(float z) {
    this.z = z;
  }

  public float pitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public float yaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float roll() {
    return roll;
  }

  public void setRoll(float roll) {
    this.roll = roll;
  }

  public float scale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public Matrix4f getTransformationMatrix() {
    return transformationMatrix;
  }
}
