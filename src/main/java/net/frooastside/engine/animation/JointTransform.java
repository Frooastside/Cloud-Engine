package net.frooastside.engine.animation;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class JointTransform implements Externalizable {

  private static final long serialVersionUID = -7266059362940614822L;

  private String name;
  private Vector3f position;
  private Quaternionf rotation;

  private final Matrix4f localTransform = new Matrix4f();

  private static final Matrix4f MATRIX_CACHE = new Matrix4f();

  public JointTransform(String name, Vector3f position, Quaternionf rotation) {
    this.name = name;
    this.position = position;
    this.rotation = rotation;
  }

  public JointTransform(Vector3f position, Quaternionf rotation) {
    this.position = position;
    this.rotation = rotation;
  }

  public JointTransform() {
  }

  public static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression) {
    Vector3f position = frameA.position.lerp(frameB.position, progression, new Vector3f());
    Quaternionf rotation = frameA.rotation.nlerp(frameB.rotation, progression, new Quaternionf());
    return new JointTransform(position, rotation);
  }

  public Matrix4f calculateLocalTransform() {
    return localTransform.identity().translate(position).mul(rotation.get(MATRIX_CACHE));
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    name = in.readUTF();
    position = (Vector3f) in.readObject();
    rotation = (Quaternionf) in.readObject();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(name);
    out.writeObject(position);
    out.writeObject(rotation);
  }

  public String name() {
    return name;
  }

  public Vector3f position() {
    return position;
  }

  public Quaternionf rotation() {
    return rotation;
  }

}
