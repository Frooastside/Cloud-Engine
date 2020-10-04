package net.frooastside.engine.userinterface.renderer;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.UiRenderElement;
import net.frooastside.engine.userinterface.UiScreen;
import net.frooastside.engine.userinterface.elements.UiBox;
import net.frooastside.engine.userinterface.elements.UiText;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class UiRenderer {

  private final BasicBoxShader boxShader;
  private final BasicFontShader fontShader;

  public UiRenderer(BasicBoxShader boxShader, BasicFontShader fontShader) {
    this.boxShader = boxShader;
    this.fontShader = fontShader;
  }

  public void initialize() {
    boxShader.createShaderProgram();
    fontShader.createShaderProgram();
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
  }

  public void renderBoxes(List<UiRenderElement> boxes) {
    if (boxes != null) {
      boxShader.start();

      for (UiRenderElement renderElement : boxes) {
        UiBox box = ((UiBox) renderElement);
        boxShader.loadTranslation(box.bounds());
        boxShader.loadVisibility(box.visibility());
        if (box.useTexture()) {
          boxShader.loadTexture(box.texture());
        } else {
          boxShader.loadColor(box.color().rawColor());
        }
        VertexArrayObject textMesh = box.model();
        textMesh.bind();
        textMesh.enableVertexAttributes();
        textMesh.draw();
        textMesh.disableVertexAttributes();
        textMesh.unbind();
      }

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
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_BLEND);
  }

}
