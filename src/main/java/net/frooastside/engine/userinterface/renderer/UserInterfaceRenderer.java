package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.userinterface.elements.Element;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.elements.RenderElement;
import net.frooastside.engine.userinterface.elements.container.Screen;
import net.frooastside.engine.userinterface.elements.render.Box;
import net.frooastside.engine.userinterface.elements.render.Text;
import org.lwjgl.opengl.GL11;

public class UserInterfaceRenderer {

  private static final float[] DEFAULT_BOX_POSITIONS = new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};
  private static final float[] DEFAULT_BOX_TEXTURE_COORDINATES = new float[]{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1};

  private final VertexArrayObject defaultBox = generateDefaultBox();

  private final BasicBoxShader boxShader;
  private final BasicFontShader fontShader;

  private RenderElement.RenderType currentRenderType;

  public UserInterfaceRenderer(BasicBoxShader boxShader, BasicFontShader fontShader) {
    this.boxShader = boxShader;
    this.fontShader = fontShader;
  }

  public void initialize() {
    boxShader.initialize();
    fontShader.initialize();
  }

  public void render(Screen screen) {
    prepare();
    renderElements(screen, screen.alpha());
    endShader(currentRenderType);
    currentRenderType = null;
    end();
  }

  private void prepare() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_CULL_FACE);
  }

  private void renderElements(Element element, float alpha) {
    if (element instanceof RenderElement) {
      RenderElement renderElement = ((RenderElement) element);
      if (renderElement.visible() || renderElement.doingDisplayAnimation()) {
        prepareRendering(renderElement);
        renderElement(renderElement, alpha);
      }
    } else if (element instanceof FunctionalElement) {
      FunctionalElement functionalElement = ((FunctionalElement) element);
      functionalElement.children().forEach(child -> renderElements(child, alpha * child.alpha()));
    }
  }

  public void prepareRendering(RenderElement renderElement) {
    RenderElement.RenderType renderType = renderElement.renderType();
    if (currentRenderType != renderType) {
      endShader(currentRenderType);
      prepareShader(renderType);
      currentRenderType = renderType;
    }
  }

  public void prepareShader(RenderElement.RenderType renderType) {
    if (renderType == RenderElement.RenderType.BOX) {
      boxShader.start();
      defaultBox.bind();
      defaultBox.enableVertexAttributes();
      return;
    }
    if (renderType == RenderElement.RenderType.TEXT) {
      fontShader.start();
    }
  }

  public void endShader(RenderElement.RenderType renderType) {
    if (renderType == RenderElement.RenderType.BOX) {
      defaultBox.disableVertexAttributes();
      defaultBox.unbind();
      boxShader.stop();
      return;
    }
    if (renderType == RenderElement.RenderType.TEXT) {
      fontShader.stop();
    }
  }

  public void renderElement(RenderElement renderElement, float alpha) {
    if (renderElement.renderType() == RenderElement.RenderType.BOX) {
      Box box = ((Box) renderElement);
      boxShader.loadTranslation(box.bounds());
      boxShader.loadAlpha(alpha);
      boxShader.loadUseColor(box.useColor());
      if (box.useColor()) {
        boxShader.loadColor(box.color().rawColor());
      }
      boxShader.loadUseTexture(box.useTexture());
      if (box.useTexture()) {
        boxShader.loadTexture(box.texture());
      }
      defaultBox.draw();
    } else if (renderElement.renderType() == RenderElement.RenderType.TEXT) {
      Text text = ((Text) renderElement);
      fontShader.loadOffset(text.bounds().x, text.bounds().y);
      fontShader.loadTexture(text.font().texture());
      fontShader.loadColor(text.color().rawColor());
      fontShader.loadAlpha(alpha);
      //TODO EDGE
      fontShader.loadEdge(0.15f);
      VertexArrayObject textMesh = text.model();
      textMesh.bind();
      textMesh.enableVertexAttributes();
      textMesh.draw();
      textMesh.disableVertexAttributes();
      textMesh.unbind();
    }
  }

  private void end() {
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_BLEND);
  }

  public void delete() {
    boxShader.delete();
    fontShader.delete();
  }

  private static VertexArrayObject generateDefaultBox() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(DEFAULT_BOX_POSITIONS.length / 2);
    vertexArrayObject.generateIdentifier();
    vertexArrayObject.bind();

    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_POSITIONS));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    VertexBufferObject textureCoordinateBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_TEXTURE_COORDINATES));
    vertexArrayObject.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

}
