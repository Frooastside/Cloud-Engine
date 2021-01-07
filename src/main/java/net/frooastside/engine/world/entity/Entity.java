package net.frooastside.engine.world.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Entity {

  private final Vector3f position = new Vector3f();
  private final Vector3f rotation = new Vector3f();
  private float scale;

  private final Matrix4f transformationMatrix = new Matrix4f();

  //TODO TEXTURED MODEL

  public Entity(float x, float y, float z, float pitch, float yaw, float roll) {
    this(x, y, z, pitch, yaw, roll, 1);
  }

  public Entity(float x, float y, float z, float pitch, float yaw, float roll, float scale) {
    this.position.set(x, y, z);
    this.rotation.set(pitch, yaw, roll);
    this.scale = scale;
  }

  public abstract void tick();

  public Vector3f position() {
    return position;
  }

  public void setPosition(float x, float y, float z) {
    this.position.set(x, y, z);
  }

  public void setPosition(Vector3f position) {
    this.position.set(position);
  }

  public Vector3f rotation() {
    return rotation;
  }

  public void setRotation(float pitch, float yaw, float roll) {
    this.rotation.set(pitch, yaw, roll);
  }

  public void setRotation(Vector3f rotation) {
    this.rotation.set(rotation);
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
