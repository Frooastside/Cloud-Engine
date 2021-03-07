package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.userinterface.elements.UiElement;
import net.frooastside.engine.userinterface.elements.UiFunctionalElement;
import net.frooastside.engine.userinterface.elements.UiRenderElement;
import net.frooastside.engine.userinterface.elements.container.UiScreen;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import net.frooastside.engine.userinterface.elements.render.UiText;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class UiRenderer {

  private static final float[] DEFAULT_BOX_POSITIONS = new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};
  private static final float[] DEFAULT_BOX_TEXTURE_COORDINATES = new float[]{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1};

  private final VertexArrayObject defaultBox = generateDefaultBox();

  private final BasicBoxShader boxShader;
  private final BasicFontShader fontShader;

  private UiRenderElement.RenderType currentRenderType;

  public UiRenderer(BasicBoxShader boxShader, BasicFontShader fontShader) {
    this.boxShader = boxShader;
    this.fontShader = fontShader;
  }

  public void initialize() {
    boxShader.initialize();
    fontShader.initialize();
  }

  public void render(UiScreen screen) {
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

  private void renderElements(UiElement element, float alpha) {
    if(element instanceof UiRenderElement) {
      UiRenderElement renderElement = ((UiRenderElement) element);
      prepareRendering(renderElement);
      renderElement(renderElement, alpha);
    }else if(element instanceof UiFunctionalElement) {
      UiFunctionalElement functionalElement = ((UiFunctionalElement) element);
      functionalElement.children().forEach(child -> renderElements(child, alpha * child.alpha()));
    }
  }

  public void prepareRendering(UiRenderElement renderElement) {
    UiRenderElement.RenderType renderType = renderElement.renderType();
    if (currentRenderType != renderType) {
      endShader(currentRenderType);
      prepareShader(renderType);
      currentRenderType = renderType;
    }
  }

  public void prepareShader(UiRenderElement.RenderType renderType) {
    if(renderType == UiRenderElement.RenderType.BOX) {
      boxShader.start();
      defaultBox.bind();
      defaultBox.enableVertexAttributes();
      return;
    }
    if(renderType == UiRenderElement.RenderType.TEXT) {
      fontShader.start();
    }
  }

  public void endShader(UiRenderElement.RenderType renderType) {
    if(renderType == UiRenderElement.RenderType.BOX) {
      defaultBox.disableVertexAttributes();
      defaultBox.unbind();
      boxShader.stop();
      return;
    }
    if(renderType == UiRenderElement.RenderType.TEXT) {
      fontShader.stop();
    }
  }

  public void renderElement(UiRenderElement renderElement, float alpha) {
    if(renderElement.renderType() == UiRenderElement.RenderType.BOX) {
      UiBox box = ((UiBox) renderElement);
      boxShader.loadTranslation(box.bounds());
      boxShader.loadAlpha(alpha);
      boxShader.loadUseColor(box.useColor());
      if(box.useColor()) {
        boxShader.loadColor(box.color().rawColor());
      }
      boxShader.loadUseTexture(box.useTexture());
      if (box.useTexture()) {
        boxShader.loadTexture(box.texture());
      }
      defaultBox.draw();
    }else if(renderElement.renderType() == UiRenderElement.RenderType.TEXT) {
      UiText text = ((UiText) renderElement);
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
