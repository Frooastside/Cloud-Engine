package net.frooastside.engine.world.terrain;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class TerrainSection {

  private final TerrainVertexArray[] LODs = new TerrainVertexArray[4];
  private int currentLOD = 3;

  private final HeightGenerator heightGenerator;
  private final float[][] heights;

  private final Vector2f gridPosition = new Vector2f();
  private final int vertexCount;
  private final int size;

  public TerrainSection(int gridX, int gridY, int size, int vertexCount, int seed) {
    this.heightGenerator = new HeightGenerator(gridX, gridY, vertexCount, seed);
    this.heights = new float[vertexCount + 5][vertexCount + 5];
    this.gridPosition.set(gridX, gridY);
    this.vertexCount = vertexCount;
    this.size = size;
  }

  float average(float a, float b) {
    return a / 2f + b / 2f;
  }

  private Vector3f calculateNormal(int x, int z, int factor) {
    float heightL = getHeight((x - 1) * factor, z * factor);
    float heightR = getHeight((x + 1) * factor, z * factor);
    float heightU = getHeight(x * factor, (z - 1) * factor);
    float heightD = getHeight(x * factor, (z + 1) * factor);
    Vector3f normal = new Vector3f(heightL - heightR, 2f * factor, heightU - heightD);
    normal.normalize();
    return normal;
  }

  private float getHeight(int x, int y) {
    return heights[x + 2][y + 2];
  }

  private void generateHeights() {
    for(int y = 0; y < vertexCount + 5; y++) {
      for(int x = 0; x < vertexCount + 5; x++) {
        heights[x][y] = heightGenerator.generateHeight(x - 2, y - 2);
      }
    }
  }

  private void generateLOD0() {
    int LOD0VertexCount = vertexCount + 1;
    int totalVertexCount = LOD0VertexCount * LOD0VertexCount;
    float[] positions = new float[3 * totalVertexCount];
    float[] textureCoordinates = new float[2 * totalVertexCount];
    float[] normals = new float[3 * totalVertexCount];
    short[] indices = new short[6 * vertexCount * vertexCount];
    int vertexPointer = 0;
    for(int y = 0; y < LOD0VertexCount; y++) {
      for(int x = 0; x < LOD0VertexCount; x++) {
        float height = getHeight(x, y);
        if(x == 0 || x == LOD0VertexCount - 1) {
          if(y % 2 == 1) {
            height = average(getHeight(x, y - 1), getHeight(x, y + 1));
          }
        }else if(y == 0 || y == LOD0VertexCount - 1) {
          if(x % 2 == 1) {
            height = average(getHeight(x - 1, y), getHeight(x + 1, y));
          }
        }
        positions[vertexPointer*3] = (((float) x / ((float) LOD0VertexCount - 1)) * size) + (size * gridPosition.x);
        positions[vertexPointer*3+1] = height;
        positions[vertexPointer*3+2] = (((float) y / ((float) LOD0VertexCount - 1)) * size) + (size * gridPosition.y);
        Vector3f normal = calculateNormal(x, y, 1);
        normals[vertexPointer*3] = normal.x;
        normals[vertexPointer*3+1] = normal.y;
        normals[vertexPointer*3+2] = normal.z;
        textureCoordinates[vertexPointer*2] = x / (float) (LOD0VertexCount - 1);
        textureCoordinates[vertexPointer*2+1] = y / (float) (LOD0VertexCount - 1);
        vertexPointer++;
      }
    }
    int indexPointer = 0;
    for(int gz = 0; gz < LOD0VertexCount - 1; gz++) {
      for(int gx = 0; gx < LOD0VertexCount - 1; gx++) {
        int topLeft = (gz * LOD0VertexCount) + gx;
        int topRight = topLeft + 1;
        int bottomLeft = ((gz + 1)*LOD0VertexCount) + gx;
        int bottomRight = bottomLeft + 1;
        indices[indexPointer++] = (short) topLeft;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) bottomRight;

      }
    }

    LODs[0] = new TerrainVertexArray(
      BufferUtils.store(positions),
      BufferUtils.store(textureCoordinates),
      BufferUtils.store(normals),
      BufferUtils.store(indices));
  }

  private void generateLOD1() {
    int LOD1VertexCount = (vertexCount / 2) + 1;
    int totalVertexCount = vertexCount * 4 + (LOD1VertexCount - 2) * (LOD1VertexCount - 2);
    float[] positions = new float[3 * totalVertexCount];
    float[] textureCoordinates = new float[2 * totalVertexCount];
    float[] normals = new float[3 * totalVertexCount];
    short[] indices = new short[6 * vertexCount * vertexCount];
    int vertexPointer = 0;
    for(int y = 0; y < LOD1VertexCount; y++) {
      for(int x = 0; x < LOD1VertexCount; x++) {
        float height = getHeight(x * 2, y * 2);
        positions[vertexPointer*3] = (((float) x / ((float) LOD1VertexCount - 1)) * size) + (size * gridPosition.x);
        positions[vertexPointer*3+1] = height;
        positions[vertexPointer*3+2] = (((float) y / ((float) LOD1VertexCount - 1)) * size) + (size * gridPosition.y);
        Vector3f normal = calculateNormal(x, y, 2);
        normals[vertexPointer*3] = normal.x;
        normals[vertexPointer*3+1] = normal.y;
        normals[vertexPointer*3+2] = normal.z;
        textureCoordinates[vertexPointer*2] = x / (float) (LOD1VertexCount - 1);
        textureCoordinates[vertexPointer*2+1] = y / (float) (LOD1VertexCount - 1);
        vertexPointer++;
      }
    }
    int indexPointer = 0;
    for(int gz = 0; gz < LOD1VertexCount - 1; gz++) {
      for(int gx = 0; gx < LOD1VertexCount - 1; gx++) {
        int topLeft = (gz * LOD1VertexCount) + gx;
        int topRight = topLeft + 1;
        int bottomLeft = ((gz + 1)*LOD1VertexCount) + gx;
        int bottomRight = bottomLeft + 1;
        indices[indexPointer++] = (short) topLeft;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) bottomRight;
      }
    }

    LODs[1] = new TerrainVertexArray(
      BufferUtils.store(positions),
      BufferUtils.store(textureCoordinates),
      BufferUtils.store(normals),
      BufferUtils.store(indices));
  }

  private void generateLOD2() {
    int LOD1VertexCount = (vertexCount / 2) + 1;
    int LOD2VertexCount = (vertexCount / 4) + 1;
    int totalVertexCount = vertexCount * 4 + (LOD2VertexCount - 2) * (LOD2VertexCount - 2);
    float[] positions = new float[3 * totalVertexCount];
    float[] textureCoordinates = new float[2 * totalVertexCount];
    float[] normals = new float[3 * totalVertexCount];
    short[] indices = new short[6 * vertexCount * vertexCount];
    int vertexPointer = 0;


    for(int y = 1; y < LOD2VertexCount - 1; y++) {
      for(int x = 1; x < LOD2VertexCount - 1; x++) {
        float height = getHeight(x * 4, y * 4);
        positions[vertexPointer*3] = (float) x / ((float) LOD2VertexCount - 1) * size + size * gridPosition.x;
        positions[vertexPointer*3+1] = height;
        positions[vertexPointer*3+2] = (float) y / ((float) LOD2VertexCount - 1) * size + size * gridPosition.y;
        Vector3f normal = calculateNormal(x, y, 4);
        normals[vertexPointer*3] = normal.x;
        normals[vertexPointer*3+1] = normal.y;
        normals[vertexPointer*3+2] = normal.z;
        textureCoordinates[vertexPointer*2] = x / (float) (LOD2VertexCount - 1);
        textureCoordinates[vertexPointer*2+1] = y / (float) (LOD2VertexCount - 1);
        vertexPointer++;
      }
    }

    int edgeOffset = vertexPointer;
    for(int x = 0; x < LOD1VertexCount; x++) {
      float height = getHeight(x * 2, 0);
      positions[vertexPointer*3] = (float) x / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) 0 / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, 0, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD1VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = 0;
      vertexPointer++;
    }
    for(int y = 1; y < LOD1VertexCount - 1; y++) {
      float height = getHeight(0, y * 2);
      positions[vertexPointer*3] = (float) 0 / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(0, y, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = 0;
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD1VertexCount - 1);
      vertexPointer++;
      float height2 = getHeight((LOD1VertexCount - 1) * 2, y * 2);
      positions[vertexPointer*3] = (float) (LOD1VertexCount - 1) / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height2;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal2 = calculateNormal(LOD1VertexCount - 1, y, 2);
      normals[vertexPointer*3] = normal2.x;
      normals[vertexPointer*3+1] = normal2.y;
      normals[vertexPointer*3+2] = normal2.z;
      textureCoordinates[vertexPointer*2] = 1;
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD1VertexCount - 1);
      vertexPointer++;
    }
    for(int x = 0; x < LOD1VertexCount; x++) {
      float height = getHeight(x * 2, (LOD1VertexCount - 1) * 2);
      positions[vertexPointer*3] = (float) x / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) (LOD1VertexCount - 1) / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, LOD1VertexCount - 1, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD1VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = 1;
      vertexPointer++;
    }


    int indexPointer = 0;
    for(int ty = 0; ty < LOD2VertexCount - 3; ty++) {
      for(int tx = 0; tx < LOD2VertexCount - 3; tx++) {
        int topLeft = (ty * (LOD2VertexCount - 2)) + tx;
        int topRight = topLeft + 1;
        int bottomLeft = ((ty + 1) * (LOD2VertexCount - 2)) + tx;
        int bottomRight = bottomLeft + 1;
        indices[indexPointer++] = (short) topLeft;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) bottomRight;
      }
    }


    for(int tx = 0; tx < LOD2VertexCount - 3; tx++) {
      int current = tx;
      int right = tx + 1;
      int top = edgeOffset + tx * 2 + 2;
      int topRight = top + 1;
      int topDoubleRight = topRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) topRight;
      indices[indexPointer++] = (short) top;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topRight;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topDoubleRight;
      indices[indexPointer++] = (short) topRight;
    }
    for(int ty = 0; ty < LOD2VertexCount - 3; ty++) {
      int current = ty * (LOD2VertexCount - 2) + LOD2VertexCount - 3;
      int down = current + (LOD2VertexCount - 2);
      int right = edgeOffset + LOD1VertexCount + ty * 4 + 3;
      int rightDown = right + 2;
      int rightDoubleDown = rightDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDown;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDoubleDown;
      indices[indexPointer++] = (short) rightDown;
    }
    for(int tx = 0; tx < LOD2VertexCount - 3; tx++) {
      int current = tx + edgeOffset - (LOD2VertexCount - 2);
      int right = current + 1;
      int down = edgeOffset + ((LOD1VertexCount - 1) * 3) + 1 + tx * 2;
      int downRight = down + 1;
      int downDoubleRight = downRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) downRight;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) downDoubleRight;
    }
    for(int ty = 0; ty < LOD2VertexCount - 3; ty++) {
      int current = ty * (LOD2VertexCount - 2);
      int down = current + (LOD2VertexCount - 2);
      int right = edgeOffset + LOD1VertexCount + ty * 4 + 2;
      int rightDown = right + 2;
      int rightDoubleDown = rightDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) rightDown;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) down;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) rightDoubleDown;
    }
    int upperLeftInnerCorner = 0;
    int upperLeftOuterCorner = edgeOffset;
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + LOD1VertexCount);
    indices[indexPointer++] = (short) (upperLeftOuterCorner + LOD1VertexCount + 2);
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) upperLeftOuterCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + LOD1VertexCount);
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + 1);
    indices[indexPointer++] = (short) upperLeftOuterCorner;
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + 2);
    indices[indexPointer++] = (short) (upperLeftOuterCorner + 1);

    int upperRightInnerCorner = LOD2VertexCount - 3;
    int upperRightOuterCorner = edgeOffset + LOD1VertexCount - 1;
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner + 4);
    indices[indexPointer++] = (short) (upperRightOuterCorner + 2);
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner + 2);
    indices[indexPointer++] = (short) upperRightOuterCorner;
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) upperRightOuterCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner - 1);
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner - 1);
    indices[indexPointer++] = (short) (upperRightOuterCorner - 2);

    int lowerRightInnerCorner = edgeOffset - 1;
    int lowerRightOuterCorner = edgeOffset + (LOD1VertexCount - 1) * 4 - 1;
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - 2);
    indices[indexPointer++] = (short) (lowerRightOuterCorner - 1);
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - 1);
    indices[indexPointer++] = (short) lowerRightOuterCorner;
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) lowerRightOuterCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - LOD1VertexCount);
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - LOD1VertexCount);
    indices[indexPointer++] = (short) (lowerRightOuterCorner - LOD1VertexCount - 2);

    int lowerLeftInnerCorner = edgeOffset - (LOD2VertexCount - 2);
    int lowerLeftOuterCorner = edgeOffset + (LOD1VertexCount - 1) * 3 - 1;
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner - 4);
    indices[indexPointer++] = (short) (lowerLeftOuterCorner - 2);
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner - 2);
    indices[indexPointer++] = (short) lowerLeftOuterCorner;
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) lowerLeftOuterCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner + 1);
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner + 1);
    indices[indexPointer] = (short) (lowerLeftOuterCorner + 2);

    LODs[2] = new TerrainVertexArray(
      BufferUtils.store(positions),
      BufferUtils.store(textureCoordinates),
      BufferUtils.store(normals),
      BufferUtils.store(indices));
  }

  private void generateLOD3() {
    int LOD1VertexCount = (vertexCount / 2) + 1;
    int LOD2VertexCount = (vertexCount / 4) + 1;
    int LOD3VertexCount = (vertexCount / 8) + 1;
    int totalVertexCount = vertexCount * 4 + (LOD2VertexCount - 3) * 4 + (LOD3VertexCount - 2) * (LOD3VertexCount - 2);
    float[] positions = new float[3 * totalVertexCount];
    float[] textureCoordinates = new float[2 * totalVertexCount];
    float[] normals = new float[3 * totalVertexCount];
    short[] indices = new short[6 * vertexCount * vertexCount];
    int vertexPointer = 0;


    for(int y = 1; y < LOD3VertexCount - 1; y++) {
      for(int x = 1; x < LOD3VertexCount - 1; x++) {
        float height = getHeight(x * 8, y * 8);
        positions[vertexPointer*3] = (float) x / ((float) LOD3VertexCount - 1) * size + size * gridPosition.x;
        positions[vertexPointer*3+1] = height;
        positions[vertexPointer*3+2] = (float) y / ((float) LOD3VertexCount - 1) * size + size * gridPosition.y;
        Vector3f normal = calculateNormal(x, y, 8);
        normals[vertexPointer*3] = normal.x;
        normals[vertexPointer*3+1] = normal.y;
        normals[vertexPointer*3+2] = normal.z;
        textureCoordinates[vertexPointer*2] = x / (float) (LOD3VertexCount - 1);
        textureCoordinates[vertexPointer*2+1] = y / (float) (LOD3VertexCount - 1);
        vertexPointer++;
      }
    }


    int edgeOffset1 = vertexPointer;
    for(int x = 1; x < LOD2VertexCount - 1; x++) {
      float height = getHeight(x * 4, 4);
      positions[vertexPointer*3] = (float) x / ((float) LOD2VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) 1 / ((float) LOD2VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, 1, 4);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD2VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = 1 / (float) (LOD2VertexCount - 1);
      vertexPointer++;
    }
    for(int y = 2; y < LOD2VertexCount - 2; y++) {
      float height = getHeight(4, y * 4);
      positions[vertexPointer*3] = (float) 1 / ((float) LOD2VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD2VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(1, y, 4);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = 1 / (float) (LOD2VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD2VertexCount - 1);
      vertexPointer++;
      float height2 = getHeight((LOD2VertexCount - 2) * 4, y * 4);
      positions[vertexPointer*3] = (float) (LOD2VertexCount - 2) / ((float) LOD2VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height2;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD2VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal2 = calculateNormal(LOD2VertexCount - 2, y, 4);
      normals[vertexPointer*3] = normal2.x;
      normals[vertexPointer*3+1] = normal2.y;
      normals[vertexPointer*3+2] = normal2.z;
      textureCoordinates[vertexPointer*2] = (LOD2VertexCount - 2) / (float) (LOD2VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD2VertexCount - 1);
      vertexPointer++;
    }
    for(int x = 1; x < LOD2VertexCount - 1; x++) {
      float height = getHeight(x * 4, (LOD2VertexCount - 2) * 4);
      positions[vertexPointer*3] = (float) x / ((float) LOD2VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) (LOD2VertexCount - 2) / ((float) LOD2VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, LOD2VertexCount - 2, 4);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD2VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = (LOD2VertexCount - 2) / (float) (LOD2VertexCount - 1);
      vertexPointer++;
    }


    int edgeOffset2 = vertexPointer;
    for(int x = 0; x < LOD1VertexCount; x++) {
      float height = getHeight(x * 2, 0);
      positions[vertexPointer*3] = (float) x / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) 0 / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, 0, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD1VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = 0;
      vertexPointer++;
    }
    for(int y = 1; y < LOD1VertexCount - 1; y++) {
      float height = getHeight(0, y * 2);
      positions[vertexPointer*3] = (float) 0 / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(0, y, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = 0;
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD1VertexCount - 1);
      vertexPointer++;
      float height2 = getHeight((LOD1VertexCount - 1) * 2, y * 2);
      positions[vertexPointer*3] = (float) (LOD1VertexCount - 1) / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height2;
      positions[vertexPointer*3+2] = (float) y / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal2 = calculateNormal(LOD1VertexCount - 1, y, 2);
      normals[vertexPointer*3] = normal2.x;
      normals[vertexPointer*3+1] = normal2.y;
      normals[vertexPointer*3+2] = normal2.z;
      textureCoordinates[vertexPointer*2] = 1;
      textureCoordinates[vertexPointer*2+1] = y / (float) (LOD1VertexCount - 1);
      vertexPointer++;
    }
    for(int x = 0; x < LOD1VertexCount; x++) {
      float height = getHeight(x * 2, (LOD1VertexCount - 1) * 2);
      positions[vertexPointer*3] = (float) x / ((float) LOD1VertexCount - 1) * size + size * gridPosition.x;
      positions[vertexPointer*3+1] = height;
      positions[vertexPointer*3+2] = (float) (LOD1VertexCount - 1) / ((float) LOD1VertexCount - 1) * size + size * gridPosition.y;
      Vector3f normal = calculateNormal(x, LOD1VertexCount - 1, 2);
      normals[vertexPointer*3] = normal.x;
      normals[vertexPointer*3+1] = normal.y;
      normals[vertexPointer*3+2] = normal.z;
      textureCoordinates[vertexPointer*2] = x / (float) (LOD1VertexCount - 1);
      textureCoordinates[vertexPointer*2+1] = 1;
      vertexPointer++;
    }


    int indexPointer = 0;
    for(int ty = 0; ty < LOD3VertexCount - 3; ty++) {
      for(int tx = 0; tx < LOD3VertexCount - 3; tx++) {
        int topLeft = (ty * (LOD3VertexCount - 2)) + tx;
        int topRight = topLeft + 1;
        int bottomLeft = ((ty + 1) * (LOD3VertexCount - 2)) + tx;
        int bottomRight = bottomLeft + 1;
        indices[indexPointer++] = (short) topLeft;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) topRight;
        indices[indexPointer++] = (short) bottomLeft;
        indices[indexPointer++] = (short) bottomRight;
      }
    }

    for(int tx = 0; tx < LOD3VertexCount - 3; tx++) {
      int current = tx;
      int right = tx + 1;
      int top = edgeOffset1 + tx * 2 + 1;
      int topRight = top + 1;
      int topDoubleRight = topRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) topRight;
      indices[indexPointer++] = (short) top;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topRight;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topDoubleRight;
      indices[indexPointer++] = (short) topRight;
    }
    for(int ty = 0; ty < LOD3VertexCount - 3; ty++) {
      int current = ty * (LOD3VertexCount - 2) + LOD3VertexCount - 3;
      int down = current + (LOD3VertexCount - 2);
      int right = edgeOffset1 + LOD2VertexCount + ty * 4 - 1;
      int rightDown = right + 2;
      int rightDoubleDown = rightDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDown;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDoubleDown;
      indices[indexPointer++] = (short) rightDown;
    }
    for(int tx = 0; tx < LOD3VertexCount - 3; tx++) {
      int current = tx + edgeOffset1 - (LOD3VertexCount - 2);
      int right = current + 1;
      int down = edgeOffset1 + ((LOD2VertexCount - 3) * 3) + tx * 2;
      int downRight = down + 1;
      int downDoubleRight = downRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) downRight;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) downDoubleRight;
    }
    for(int ty = 0; ty < LOD3VertexCount - 3; ty++) {
      int current = ty * (LOD3VertexCount - 2);
      int down = current + (LOD3VertexCount - 2);
      int right = edgeOffset1 + LOD2VertexCount + ty * 4 - 2;
      int rightDown = right + 2;
      int rightDoubleDown = rightDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) rightDown;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) down;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) rightDoubleDown;
    }

    int upperLeftInnerCorner = 0;
    int upperLeftOuterCorner = edgeOffset1;
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) upperLeftOuterCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + LOD2VertexCount - 2);
    indices[indexPointer++] = (short) upperLeftInnerCorner;
    indices[indexPointer++] = (short) (upperLeftOuterCorner + 1);
    indices[indexPointer++] = (short) upperLeftOuterCorner;

    int upperRightInnerCorner = LOD3VertexCount - 3;
    int upperRightOuterCorner = edgeOffset1 + LOD2VertexCount - 3;
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner + 2);
    indices[indexPointer++] = (short) upperRightOuterCorner;
    indices[indexPointer++] = (short) upperRightInnerCorner;
    indices[indexPointer++] = (short) upperRightOuterCorner;
    indices[indexPointer++] = (short) (upperRightOuterCorner - 1);

    int lowerRightInnerCorner = edgeOffset1 - 1;
    int lowerRightOuterCorner = edgeOffset1 + (LOD2VertexCount - 3) * 4 - 1;
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - 1);
    indices[indexPointer++] = (short) lowerRightOuterCorner;
    indices[indexPointer++] = (short) lowerRightInnerCorner;
    indices[indexPointer++] = (short) lowerRightOuterCorner;
    indices[indexPointer++] = (short) (lowerRightOuterCorner - LOD2VertexCount + 2);

    int lowerLeftInnerCorner = edgeOffset1 - (LOD3VertexCount - 2);
    int lowerLeftOuterCorner = edgeOffset1 + (LOD2VertexCount - 3) * 3 - 1;
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner - 2);
    indices[indexPointer++] = (short) lowerLeftOuterCorner;
    indices[indexPointer++] = (short) lowerLeftInnerCorner;
    indices[indexPointer++] = (short) lowerLeftOuterCorner;
    indices[indexPointer++] = (short) (lowerLeftOuterCorner + 1);

    for(int tx = 0; tx < LOD2VertexCount - 3; tx++) {
      int current = edgeOffset1 + tx;
      int right = current + 1;
      int top = edgeOffset2 + tx * 2 + 2;
      int topRight = top + 1;
      int topDoubleRight = topRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) topRight;
      indices[indexPointer++] = (short) top;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topRight;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) topDoubleRight;
      indices[indexPointer++] = (short) topRight;
    }
    for(int ty = 0; ty < LOD2VertexCount - 4; ty++) {
      int current = edgeOffset1 + ty * 2 + (LOD2VertexCount - 3);
      int down = current + 2;
      int right = edgeOffset2 + LOD1VertexCount + ty * 4 + 3;
      int rightDown = right + 2;
      int rightDoubleDown = rightDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) rightDown;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDown;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) rightDoubleDown;
      indices[indexPointer++] = (short) rightDown;
    }
    for(int tx = 0; tx < LOD2VertexCount - 3; tx++) {
      int current = tx + edgeOffset2 - (LOD2VertexCount - 2);
      int right = current + 1;
      int down = edgeOffset2 + ((LOD1VertexCount - 1) * 3) + 1 + tx * 2;
      int downRight = down + 1;
      int downDoubleRight = downRight + 1;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) downRight;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) right;

      indices[indexPointer++] = (short) right;
      indices[indexPointer++] = (short) downRight;
      indices[indexPointer++] = (short) downDoubleRight;
    }
    for(int ty = 1; ty < LOD2VertexCount - 3; ty++) {
      int current = edgeOffset1 + ty * 2 + (LOD2VertexCount - 3) - 1;
      int down = current + 2;
      int left = edgeOffset2 + LOD1VertexCount + ty * 4 + 2;
      int leftDown = left + 2;
      int leftDoubleDown = leftDown + 2;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) left;
      indices[indexPointer++] = (short) leftDown;

      indices[indexPointer++] = (short) current;
      indices[indexPointer++] = (short) leftDown;
      indices[indexPointer++] = (short) down;

      indices[indexPointer++] = (short) down;
      indices[indexPointer++] = (short) leftDown;
      indices[indexPointer++] = (short) leftDoubleDown;
    }

    int current1 = edgeOffset1 + (LOD2VertexCount - 4) * 2 + (LOD2VertexCount - 3);
    int down1 = edgeOffset2 - 1;
    int right1 = edgeOffset2 + LOD1VertexCount + (LOD2VertexCount - 4) * 4 + 3;
    int rightDown1 = right1 + 2;
    int rightDoubleDown1 = rightDown1 + 2;

    indices[indexPointer++] = (short) current1;
    indices[indexPointer++] = (short) rightDown1;
    indices[indexPointer++] = (short) right1;

    indices[indexPointer++] = (short) current1;
    indices[indexPointer++] = (short) down1;
    indices[indexPointer++] = (short) rightDown1;

    indices[indexPointer++] = (short) down1;
    indices[indexPointer++] = (short) rightDoubleDown1;
    indices[indexPointer++] = (short) rightDown1;

    int current2 = edgeOffset1;
    int down2 = edgeOffset1 + (LOD2VertexCount - 3) + 1;
    int left2 = edgeOffset2 + LOD1VertexCount + 0 * 4 + 2;
    int leftDown2 = left2 + 2;
    int leftDoubleDown2 = leftDown2 + 2;

    indices[indexPointer++] = (short) current2;
    indices[indexPointer++] = (short) left2;
    indices[indexPointer++] = (short) leftDown2;

    indices[indexPointer++] = (short) current2;
    indices[indexPointer++] = (short) leftDown2;
    indices[indexPointer++] = (short) down2;

    indices[indexPointer++] = (short) down2;
    indices[indexPointer++] = (short) leftDown2;
    indices[indexPointer++] = (short) leftDoubleDown2;

    int upperLeftInnerInnerCorner = edgeOffset1;
    int upperLeftInnerOuterCorner = edgeOffset2;
    indices[indexPointer++] = (short) upperLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + LOD1VertexCount);
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + LOD1VertexCount + 2);
    indices[indexPointer++] = (short) upperLeftInnerInnerCorner;
    indices[indexPointer++] = (short) upperLeftInnerOuterCorner;
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + LOD1VertexCount);
    indices[indexPointer++] = (short) upperLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + 1);
    indices[indexPointer++] = (short) upperLeftInnerOuterCorner;
    indices[indexPointer++] = (short) upperLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + 2);
    indices[indexPointer++] = (short) (upperLeftInnerOuterCorner + 1);

    int upperRightInnerInnerCorner = edgeOffset1 + LOD2VertexCount - 3;
    int upperRightInnerOuterCorner = edgeOffset2 + LOD1VertexCount - 1;
    indices[indexPointer++] = (short) upperRightInnerInnerCorner;
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner + 4);
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner + 2);
    indices[indexPointer++] = (short) upperRightInnerInnerCorner;
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner + 2);
    indices[indexPointer++] = (short) upperRightInnerOuterCorner;
    indices[indexPointer++] = (short) upperRightInnerInnerCorner;
    indices[indexPointer++] = (short) upperRightInnerOuterCorner;
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner - 1);
    indices[indexPointer++] = (short) upperRightInnerInnerCorner;
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner - 1);
    indices[indexPointer++] = (short) (upperRightInnerOuterCorner - 2);

    int lowerRightInnerInnerCorner = edgeOffset2 - 1;
    int lowerRightInnerOuterCorner = edgeOffset2 + (LOD1VertexCount - 1) * 4 - 1;
    indices[indexPointer++] = (short) lowerRightInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - 2);
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - 1);
    indices[indexPointer++] = (short) lowerRightInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - 1);
    indices[indexPointer++] = (short) lowerRightInnerOuterCorner;
    indices[indexPointer++] = (short) lowerRightInnerInnerCorner;
    indices[indexPointer++] = (short) lowerRightInnerOuterCorner;
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - LOD1VertexCount);
    indices[indexPointer++] = (short) lowerRightInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - LOD1VertexCount);
    indices[indexPointer++] = (short) (lowerRightInnerOuterCorner - LOD1VertexCount - 2);

    int lowerLeftInnerInnerCorner = edgeOffset2 - (LOD2VertexCount - 2);
    int lowerLeftInnerOuterCorner = edgeOffset2 + (LOD1VertexCount - 1) * 3 - 1;
    indices[indexPointer++] = (short) lowerLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftInnerOuterCorner - 4);
    indices[indexPointer++] = (short) (lowerLeftInnerOuterCorner - 2);
    indices[indexPointer++] = (short) lowerLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftInnerOuterCorner - 2);
    indices[indexPointer++] = (short) lowerLeftInnerOuterCorner;
    indices[indexPointer++] = (short) lowerLeftInnerInnerCorner;
    indices[indexPointer++] = (short) lowerLeftInnerOuterCorner;
    indices[indexPointer++] = (short) (lowerLeftInnerOuterCorner + 1);
    indices[indexPointer++] = (short) lowerLeftInnerInnerCorner;
    indices[indexPointer++] = (short) (lowerLeftInnerOuterCorner + 1);
    indices[indexPointer] = (short) (lowerLeftInnerOuterCorner + 2);

    LODs[3] = new TerrainVertexArray(
      BufferUtils.store(positions),
      BufferUtils.store(textureCoordinates),
      BufferUtils.store(normals),
      BufferUtils.store(indices));
  }

  private static class TerrainVertexArray extends VertexArrayObject {

    private FloatBuffer positionBuffer;
    private FloatBuffer textureCoordinateBuffer;
    private FloatBuffer normalBuffer;
    private ShortBuffer indexBuffer;

    public TerrainVertexArray(FloatBuffer positionBuffer, FloatBuffer textureCoordinateBuffer, FloatBuffer normalBuffer, ShortBuffer indexBuffer) {
      super(indexBuffer.capacity());
      this.positionBuffer = positionBuffer;
      this.textureCoordinateBuffer = textureCoordinateBuffer;
      this.normalBuffer = normalBuffer;
      this.indexBuffer = indexBuffer;
    }

    public void generateModel() {
      this.generateIdentifier();
      this.bind();

      VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
      positionBuffer.storeFloatData(this.positionBuffer);
      this.appendVertexBufferObject(positionBuffer, 0, 3, false, 0, 0);

      VertexBufferObject textureCoordinateBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
      textureCoordinateBuffer.storeFloatData(this.textureCoordinateBuffer);
      this.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);

      VertexBufferObject normalBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
      normalBuffer.storeFloatData(this.normalBuffer);
      this.appendVertexBufferObject(normalBuffer, 2, 3, false, 0, 0);

      VertexBufferObject indexBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.UNSIGNED_SHORT, BufferTarget.ELEMENT_ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
      indexBuffer.storeShortData(this.indexBuffer);
      this.appendIndices(indexBuffer);

      this.unbind();
      this.positionBuffer = null;
      this.textureCoordinateBuffer = null;
      this.normalBuffer = null;
      this.indexBuffer = null;
    }
  }

  private static class HeightGenerator {

    private static final int HALF_MAX_VALUE = 8388608;
    private static final float ROUGHNESS = 0.4f;
    public static final float AMPLITUDE = 120;
    private static final int OCTAVES = 8;

    private final Random random = new Random();
    private final int seed;

    private final int xOffset;
    private final int zOffset;

    public HeightGenerator(int gridX, int gridZ, int vertexCount, int seed) {
      this.seed = seed;
      xOffset = gridX * (vertexCount);
      zOffset = gridZ * (vertexCount);
    }

    public float generateHeight(float x, float z) {
      x += HALF_MAX_VALUE;
      z += HALF_MAX_VALUE;
      float total = 0;
      float d = (float) Math.pow(2, OCTAVES - 1);
      for(int i = 0; i < OCTAVES; i++) {
        float freq = (float) (Math.pow(2, i) / d);
        float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
        total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
      }
      return total;
    }

    private float getInterpolatedNoise(float x, float z) {
      int intX = (int) x;
      int intZ = (int) z;
      float fractionX = x - intX;
      float fractionZ = z - intZ;
      float v1 = getSmoothNoise(intX, intZ);
      float v2 = getSmoothNoise(intX + 1, intZ);
      float v3 = getSmoothNoise(intX, intZ + 1);
      float v4 = getSmoothNoise(intX + 1, intZ + 1);
      float i1 = interpolate(v1, v2, fractionX);
      float i2 = interpolate(v3, v4, fractionX);
      return interpolate(i1, i2, fractionZ);
    }

    private float interpolate(float a, float b, float blend) {
      double theta = blend * Math.PI;
      float f = (float) (1f - Math.cos(theta)) * 0.5f;
      return a * (1f - f) + b * f;
    }

    private float getSmoothNoise(int x, int z) {
      float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1)) / 16f;
      float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
      float center = getNoise(x, z) / 4f;
      return corners + sides + center;
    }

    private float getNoise(int x, int z) {
      random.setSeed((x * 2048L) + (z * 3650L) + seed);
      return random.nextFloat() * 2f - 1f;
    }

  }

  public Vector2f gridPosition() {
    return gridPosition;
  }

  public VertexArrayObject[] getLODs() {
    return LODs;
  }

  public int getCurrentLOD() {
    return currentLOD;
  }

  public void setCurrentLOD(int currentLOD) {
    this.currentLOD = currentLOD;
  }

}
