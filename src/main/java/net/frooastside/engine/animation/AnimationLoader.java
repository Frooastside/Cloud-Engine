package net.frooastside.engine.animation;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class AnimationLoader {

  @SuppressWarnings("unchecked")
  public static Map<String, Animation> loadAnimationLibrary(String libraryFilePath) throws ClassNotFoundException, IOException {
    File libraryFile = new File(libraryFilePath);
    if (libraryFile.exists()) {
      Map<String, Animation> animationMap = new HashMap<String, Animation>();
      ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(libraryFile)));
      animationMap = (Map<String, Animation>) objectInputStream.readObject();
      objectInputStream.close();
      return animationMap;
    } else {
      return null;
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
      for (int j = 0; j < nodeAnimation.mNumPositionKeys(); j++) {
        AIVectorKey positionKey = nodeAnimation.mPositionKeys().get(j);
        AIQuatKey rotationKey = nodeAnimation.mRotationKeys().get(j);
        float time = (float) positionKey.mTime();
        if (keyFrames.containsKey(time)) {
          AIVector3D position = positionKey.mValue();
          AIQuaternion rotation = rotationKey.mValue();
          keyFrames.get(time).pose().put(name, new JointTransform(name, fromAssimp(position), fromAssimp(rotation)));
        } else {
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
        if (o2.timestamp > o1.timestamp) {
          return -1;
        } else if (o2.timestamp < o1.timestamp) {
          return 1;
        } else {
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

  public void exportAnimationLibrary(String libraryFilePath, List<Animation> animations) {
    File libraryFile = new File(libraryFilePath);
    if (!libraryFile.exists()) {
      try {
        libraryFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      libraryFile.delete();
      try {
        libraryFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Map<String, Animation> animationMap = new HashMap<String, Animation>();
    for (Animation animation : animations) {
      animationMap.put(animation.name(), animation);
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


  private void loadArmature(File file, boolean loadInstantly) {
    AIScene scene = Assimp.aiImportFile(file.getAbsolutePath(),
      Assimp.aiProcess_Triangulate |
        Assimp.aiProcess_GenSmoothNormals |
        Assimp.aiProcess_FlipUVs |
        Assimp.aiProcess_LimitBoneWeights |
        Assimp.aiProcess_JoinIdenticalVertices);

    if (scene != null) {
      AIMesh mesh = AIMesh.create(Objects.requireNonNull(scene.mMeshes()).get(0));

      float[] positions = new float[mesh.mNumVertices() * 3];
      float[] textureCoordinates = new float[mesh.mNumVertices() * 2];
      float[] normals = new float[mesh.mNumVertices() * 3];

      for (int i = 0; i < mesh.mNumVertices(); i++) {
        AIVector3D position = mesh.mVertices().get(i);
        AIVector3D textureCoordinate = Objects.requireNonNull(mesh.mTextureCoords(0)).get(i);
        AIVector3D normal = Objects.requireNonNull(mesh.mNormals()).get(i);

        positions[i * 3] = position.x();
        positions[i * 3 + 1] = position.y();
        positions[i * 3 + 2] = position.z();
        textureCoordinates[i * 2] = textureCoordinate.x();
        textureCoordinates[i * 2 + 1] = textureCoordinate.y();
        normals[i * 3] = normal.x();
        normals[i * 3 + 1] = normal.y();
        normals[i * 3 + 2] = normal.z();
      }

      Map<String, Bone> boneMap = new HashMap<>();

      Map<Integer, Integer> boneWeights = new HashMap<>();
      int[] jointIndices = new int[mesh.mNumVertices() * 4];
      float[] weights = new float[mesh.mNumVertices() * 4];

      for (int i = 0; i < mesh.mNumBones(); i++) {
        AIBone bone = AIBone.create(Objects.requireNonNull(mesh.mBones()).get(i));
        boneMap.put(bone.mName().dataString(), new Bone(i, fromAssimp(bone.mOffsetMatrix())));

        for (int j = 0; j < bone.mNumWeights(); j++) {
          AIVertexWeight weight = bone.mWeights().get(j);
          int vertexIndex = weight.mVertexId();

          if (!boneWeights.containsKey(vertexIndex)) {
            jointIndices[vertexIndex * 4] = i;
            weights[vertexIndex * 4] = weight.mWeight();
            boneWeights.put(vertexIndex, 0);
          } else if (boneWeights.get(vertexIndex) == 0) {
            jointIndices[vertexIndex * 4 + 1] = i;
            weights[vertexIndex * 4 + 1] = weight.mWeight();
            boneWeights.put(vertexIndex, 1);
          } else if (boneWeights.get(vertexIndex) == 1) {
            jointIndices[vertexIndex * 4 + 2] = i;
            weights[vertexIndex * 4 + 2] = weight.mWeight();
            boneWeights.put(vertexIndex, 2);
          } else if (boneWeights.get(vertexIndex) == 2) {
            jointIndices[vertexIndex * 4 + 3] = i;
            weights[vertexIndex * 4 + 3] = weight.mWeight();
            boneWeights.put(vertexIndex, 3);
          }

        }
      }

      int[] indices = new int[mesh.mNumFaces() * 3];
      int index = 0;

      for (int i = 0; i < mesh.mNumFaces(); i++) {
        AIFace face = mesh.mFaces().get(i);
        for (int j = 0; j < face.mNumIndices(); j++) {
          indices[index++] = face.mIndices().get(j);
        }
      }
      String rootBoneName = AIBone.create(mesh.mBones().get(0)).mName().dataString();
      AINode[] rootBoneNode = new AINode[1];
      getRootBone(scene.mRootNode(), rootBoneName, rootBoneNode);
      //rawModel = new RawModel(positions, textureCoordinates, normals, jointIndices, weights, indices, loadInstantly);
      //rootJoint = createJoints(rootBoneNode[0], boneMap);
      //jointCount = mesh.mNumBones();
    }
  }

  private boolean getRootBone(AINode node, String rootBoneName, AINode[] rootBoneNode) {
    if (node.mName().dataString().equalsIgnoreCase(rootBoneName)) {
      rootBoneNode[0] = node;
      return true;
    } else {
      for (int i = 0; i < node.mNumChildren(); i++) {
        AINode childNode = AINode.create(node.mChildren().get(i));
        if (getRootBone(childNode, rootBoneName, rootBoneNode)) {
          return true;
        }
      }
    }
    return false;
  }

  private Joint createJoints(AINode node, Map<String, Bone> bones) {
    String boneName = node.mName().dataString();
    Bone bone = bones.get(boneName);
    Joint joint = new Joint(bone.index(), boneName, bone.bindTransformation());
    for (int i = 0; i < node.mNumChildren(); i++) {
      AINode childNode = AINode.create(Objects.requireNonNull(node.mChildren()).get(i));
      joint.addChild(createJoints(childNode, bones));
    }
    return joint;
  }

  private Matrix4f fromAssimp(AIMatrix4x4 assimpMatrix) {
    return new Matrix4f(assimpMatrix.a1(), assimpMatrix.b1(), assimpMatrix.c1(), assimpMatrix.d1(), assimpMatrix.a2(), assimpMatrix.b2(), assimpMatrix.c2(), assimpMatrix.d2(), assimpMatrix.a3(), assimpMatrix.b3(), assimpMatrix.c3(), assimpMatrix.d3(), assimpMatrix.a4(), assimpMatrix.b4(), assimpMatrix.c4(), assimpMatrix.d4());
  }

}
