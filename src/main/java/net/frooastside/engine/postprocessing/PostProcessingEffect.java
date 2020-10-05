package net.frooastside.engine.postprocessing;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import org.lwjgl.opengl.GL11;

public abstract class PostProcessingEffect {

  private static final float[] FULLSCREEN_QUAD_POSITIONS = new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};

  private final VertexArrayObject fullscreenQuad = generateFullscreenQuad();

  protected void prepare() {
    fullscreenQuad.bind();
    fullscreenQuad.enableVertexAttributes();
    GL11.glDisable(GL11.GL_DEPTH_TEST);
  }

  protected void draw() {
    fullscreenQuad.draw();
  }

  protected void stop() {
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    fullscreenQuad.disableVertexAttributes();
    fullscreenQuad.unbind();
  }

  public abstract void delete();

  private static VertexArrayObject generateFullscreenQuad() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(FULLSCREEN_QUAD_POSITIONS.length / 2);
    vertexArrayObject.generateIdentifier();
    vertexArrayObject.bind();

    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(BufferUtils.store(FULLSCREEN_QUAD_POSITIONS));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

  protected VertexArrayObject fullscreenQuad() {
    return fullscreenQuad;
  }
}
