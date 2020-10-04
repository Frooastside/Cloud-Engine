package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.UiRenderElement;

public class UiBox extends UiRenderElement {

  public static final VertexArrayObject DEFAULT_SHAPE = createDefaultShape();

  private final boolean useTexture;
  private Texture texture;

  public UiBox(UiColor color) {
    super.setColor(color);
    this.useTexture = false;
  }

  public UiBox(Texture texture) {
    this.texture = texture;
    this.useTexture = true;
  }

  private static VertexArrayObject createDefaultShape() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(6);
    vertexArrayObject.generateIdentifier();
    vertexArrayObject.bind();

    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(BufferUtils.store(new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1}));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    VertexBufferObject textureCoordinateBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(new float[]{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1}));
    vertexArrayObject.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

  @Override
  public RenderType renderType() {
    return RenderType.BOX;
  }

  @Override
  public VertexArrayObject model() {
    return DEFAULT_SHAPE;
  }

  public boolean useTexture() {
    return useTexture;
  }

  public Texture texture() {
    return texture;
  }
}
