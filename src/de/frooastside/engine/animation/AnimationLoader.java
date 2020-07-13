package de.frooastside.engine.animation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.Assimp;

public class AnimationLoader {
	
	@SuppressWarnings("unchecked")
	public static Map<String, Animation> loadAnimationLibrary(String libraryFilePath) throws ClassNotFoundException, IOException {
		File libraryFile = new File(libraryFilePath);
		if(libraryFile.exists()) {
			Map<String, Animation> animationMap = new HashMap<String, Animation>();
			ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(libraryFile)));
			animationMap = (Map<String, Animation>) objectInputStream.readObject();
			objectInputStream.close();
			return animationMap;
		}else {
			return null;
		}
	}
	
	public void exportAnimationLibrary(String libraryFilePath, List<Animation> animations) {
		File libraryFile = new File(libraryFilePath);
		if(!libraryFile.exists()) {
			try {
				libraryFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			libraryFile.delete();
			try {
				libraryFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Animation> animationMap = new HashMap<String, Animation>();
		for(Animation animation : animations) {
			animationMap.put(animation.getName(), animation);
		}
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(libraryFile)));
			objectOutputStream.writeObject(animationMap);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Animation loadDAEAnimation(String filePath) {
		AIScene scene = Assimp.aiImportFile(filePath,
				Assimp.aiProcess_Triangulate |
				Assimp.aiProcess_GenSmoothNormals |
				Assimp.aiProcess_FlipUVs |
				Assimp.aiProcess_LimitBoneWeights |
				Assimp.aiProcess_JoinIdenticalVertices);
		AIAnimation animation = AIAnimation.create(scene.mAnimations().get(0));
		Map<Float, KeyFrame> keyFrames = new HashMap<Float, KeyFrame>();
		for (int k = 0; k < animation.mNumChannels(); k++) {
			AINodeAnim nodeAnimation = AINodeAnim.create(animation.mChannels().get(k));
			String name = nodeAnimation.mNodeName().dataString();
			for(int j = 0; j < nodeAnimation.mNumPositionKeys(); j++) {
				AIVectorKey positionKey = nodeAnimation.mPositionKeys().get(j);
				AIQuatKey rotationKey = nodeAnimation.mRotationKeys().get(j);
				float time = (float) positionKey.mTime();
				if(keyFrames.containsKey(time)) {
					AIVector3D position = positionKey.mValue();
					AIQuaternion rotation = rotationKey.mValue();
					keyFrames.get(time).getJointKeyFrames().put(name, new JointTransform(name, fromAssimp(position), fromAssimp(rotation)));
				}else {
					Map<String, JointTransform> transforms = new HashMap<String, JointTransform>();
					AIVector3D position = positionKey.mValue();
					AIQuaternion rotation = rotationKey.mValue();
					transforms.put(name, new JointTransform(name, fromAssimp(position), fromAssimp(rotation)));
					KeyFrame keyFrame = new KeyFrame(time, transforms);
					keyFrames.put(time, keyFrame);
				}
			}
		}
		KeyFrame[] keyFrameArray = new KeyFrame[keyFrames.size()];
		int index = 0;
		for (KeyFrame keyFrame : keyFrames.values()) {
			keyFrameArray[index] = keyFrame;
			index++;
		}
		Arrays.sort(keyFrameArray, new Comparator<KeyFrame>() {

			@Override
			public int compare(KeyFrame o1, KeyFrame o2) {
				if(o2.timeStamp > o1.timeStamp) {
					return -1;
				}else if(o2.timeStamp < o1.timeStamp) {
					return 1;
				}else {
					return 0;
				}
			}
			
		});
		String animationName = JOptionPane.showInputDialog(new File(filePath).getName());
		return new Animation(animationName, (float) animation.mDuration(), keyFrameArray);
	}
	
	private static Vector3f fromAssimp(AIVector3D assimpVector) {
		return new Vector3f(assimpVector.x(), assimpVector.y(), assimpVector.z());
	}
	
	private static Quaternionf fromAssimp(AIQuaternion assimpQuaternion) {
		return new Quaternionf(assimpQuaternion.x(), assimpQuaternion.y(), assimpQuaternion.z(), assimpQuaternion.w());
	}
	
}
