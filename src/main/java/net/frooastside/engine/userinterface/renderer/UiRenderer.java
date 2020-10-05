package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.userinterface.UiRenderElement;
import net.frooastside.engine.userinterface.UiScreen;
import net.frooastside.engine.userinterface.elements.UiBox;
import net.frooastside.engine.userinterface.elements.UiText;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class UiRenderer {

  private static final float[] DEFAULT_BOX_POSITIONS = new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};
  private static final float[] DEFAULT_BOX_TEXTURE_COORDINATES = new float[]{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1};

  private final VertexArrayObject defaultBox = generateDefaultBox();

  private final BasicBoxShader boxShader;
  private final BasicFontShader fontShader;

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
    renderBoxes(screen.renderElements(UiRenderElement.RenderType.BOX));
    renderTexts(screen.renderElements(UiRenderElement.RenderType.TEXT));
    end();
  }

  private void prepare() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_CULL_FACE);
  }

  public void renderBoxes(List<UiRenderElement> boxes) {
    if (boxes != null) {
      boxShader.start();
      defaultBox.bind();
      defaultBox.enableVertexAttributes();

      for (UiRenderElement renderElement : boxes) {
        UiBox box = ((UiBox) renderElement);
        boxShader.loadTranslation(box.bounds());
        boxShader.loadVisibility(box.visibility());
        if (box.useTexture()) {
          boxShader.loadTexture(box.texture());
        } else {
          boxShader.loadColor(box.color().rawColor());
        }
        defaultBox.draw();
      }

      defaultBox.disableVertexAttributes();
      defaultBox.unbind();
      boxShader.stop();
    }
  }

  public void renderTexts(List<UiRenderElement> texts) {
    if (texts != null) {
      fontShader.start();

      for (UiRenderElement renderElement : texts) {
        UiText text = ((UiText) renderElement);
        fontShader.loadOffset(text.bounds().x, text.bounds().y);
        fontShader.loadTexture(text.font().texture());
        fontShader.loadColor(text.color().rawColor());
        fontShader.loadVisibility(text.visibility());
        //TODO EDGE
        fontShader.loadEdge(0.1f);
        VertexArrayObject textMesh = text.model();
        textMesh.bind();
        textMesh.enableVertexAttributes();
        textMesh.draw();
        textMesh.disableVertexAttributes();
        textMesh.unbind();
      }

      fontShader.stop();
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
