package net.frooastside.engine.world.entity;

import org.joml.Vector3f;

public class MovableEntity extends Entity {

  private final Vector3f lastPosition = new Vector3f();
  private final Vector3f nextPosition = new Vector3f();
  private final Vector3f interpolatedPosition = new Vector3f();

  private final Vector3f lastRotation = new Vector3f();
  private final Vector3f nextRotation = new Vector3f();
  private final Vector3f interpolatedRotation = new Vector3f();

  private boolean moved = false;

  public MovableEntity(float x, float y, float z, float pitch, float yaw, float roll) {
    this(x, y, z, pitch, yaw, roll, 1);
  }

  public MovableEntity(float x, float y, float z, float pitch, float yaw, float roll, float scale) {
    super(x, y, z, pitch, yaw, roll, scale);
    this.lastPosition.set(x, y, z);
    this.nextPosition.set(x, y, z);
    this.lastRotation.set(pitch, yaw, roll);
    this.nextRotation.set(pitch, yaw, roll);
  }

  public void update(float partialTime) {
    if(moved) {
      lastPosition.lerp(position(), partialTime, interpolatedPosition);
      lastRotation.lerp(rotation(), partialTime, interpolatedRotation);
      getTransformationMatrix().identity().translate(interpolatedPosition).rotateXYZ(interpolatedRotation);
    }
  }

  @Override
  public void tick() {
    if(!position().equals(nextPosition) || !rotation().equals(nextRotation)) {
      lastPosition.set(position());
      position().set(nextPosition);
      lastRotation.set(rotation());
      rotation().set(nextRotation);
      moved = true;
    }else {
      moved = false;
    }
  }

  public void teleport(float x, float y, float z) {
    this.moved = true;
    this.position().set(x, y, z);
    this.lastPosition.set(x, y, z);
    this.nextPosition.set(x, y, z);
    this.interpolatedPosition.set(x, y, z);
  }

  public void teleport(float x, float y, float z, float pitch, float yaw, float roll) {
    teleport(x, y, z);
    this.rotation().set(pitch, yaw, roll);
    this.lastRotation.set(pitch, yaw, roll);
    this.nextRotation.set(pitch, yaw, roll);
    this.interpolatedRotation.set(pitch, yaw, roll);
  }

  @Override
  public void setPosition(Vector3f position) {
    this.nextPosition.set(position);
  }

  @Override
  public void setPosition(float x, float y, float z) {
    this.nextPosition.set(x, y, z);
  }

  @Override
  public void setRotation(Vector3f rotation) {
    this.nextRotation.set(rotation);
  }

  @Override
  public void setRotation(float pitch, float yaw, float roll) {
    this.nextRotation.set(pitch, yaw, roll);
  }

  public Vector3f interpolatedPosition() {
    return interpolatedPosition;
  }

  public Vector3f interpolatedRotation() {
    return interpolatedRotation;
  }

  public boolean moved() {
    return moved;
  }
}
