package net.frooastside.engine.postprocessing;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import org.lwjgl.opengl.GL11;

public abstract class PostProcessingEffect {

  private static final VertexArrayObject FULLSCREEN_QUAD = createFullScreenQuad();

  protected static void prepare() {
    FULLSCREEN_QUAD.bind();
    FULLSCREEN_QUAD.enableVertexAttributes();
    GL11.glDisable(GL11.GL_DEPTH_TEST);
  }

  protected static void draw() {
    FULLSCREEN_QUAD.draw();
  }

  protected static void stop() {
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    FULLSCREEN_QUAD.disableVertexAttributes();
    FULLSCREEN_QUAD.unbind();
  }

  private static VertexArrayObject createFullScreenQuad() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(6);
    vertexArrayObject.generateIdentifier();
    vertexArrayObject.bind();
    VertexBufferObject vertexBufferObject = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    vertexBufferObject.storeFloatData(BufferUtils.store(new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1}));
    vertexArrayObject.appendVertexBufferObject(vertexBufferObject, 0, 2, false, 0, 0);
    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

}
