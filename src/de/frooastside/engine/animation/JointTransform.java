package de.frooastside.engine.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class JointTransform implements Externalizable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7266059362940614822L;
	
	private String name;
	private Vector3f position;
	private Quaternionf rotation;
	
	private Matrix4f localTransform = new Matrix4f();
	private static Matrix4f calculationBuffer = new Matrix4f();
	
	public JointTransform() {}
	
	public JointTransform(String name, Vector3f position, Quaternionf rotation) {
		this.name = name;
		this.position = position;
		this.rotation = rotation;
	}
	
	public JointTransform(Vector3f position, Quaternionf rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	protected Matrix4f getLocalTransform() {
		localTransform.identity();
		localTransform.translate(position);
		localTransform.mul(rotation.get(calculationBuffer));
		return localTransform;
	}
	
	protected static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression) {
		Vector3f position = interpolate(frameA.position, frameB.position, progression);
		Quaternionf rotation = frameA.rotation.nlerp(frameB.rotation, progression, new Quaternionf());
		
		return new JointTransform(position, rotation);
	}
	
	private static Vector3f interpolate(Vector3f start, Vector3f end, float progression) {
		float x = start.x + (end.x - start.x) * progression;
		float y = start.y + (end.y - start.y) * progression;
		float z = start.z + (end.z - start.z) * progression;
		return new Vector3f(x, y, z);
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

	public String getName() {
		return name;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

}
