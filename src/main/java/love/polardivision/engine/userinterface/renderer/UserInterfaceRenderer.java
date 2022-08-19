/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.userinterface.renderer;

import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.vertexarray.Primitive;
import love.polardivision.engine.glwrapper.vertexarray.VertexArrayObject;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferTarget;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferUsage;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.VertexBufferObject;
import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.userinterface.elements.Element;
import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.elements.RenderElement;
import love.polardivision.engine.userinterface.elements.render.Box;
import love.polardivision.engine.userinterface.elements.render.Text;
import love.polardivision.engine.utils.BufferUtils;
import org.lwjgl.opengl.GL11;

public class UserInterfaceRenderer {

  private static final float[] DEFAULT_BOX_POSITIONS = new float[]{-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};
  private static final float[] DEFAULT_BOX_TEXTURE_COORDINATES = new float[]{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1};

  private final VertexArrayObject defaultBox = generateDefaultBox();

  private final BasicBoxShader boxShader;
  private final BasicFontShader fontShader;

  private RenderElement.RenderType currentRenderType;
  private FunctionalElement hideOverflowElement;

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
    renderElements(screen);
    endShader(currentRenderType);
    currentRenderType = null;
    end();
  }

  private void prepare() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_STENCIL_TEST);
    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
    clearStencilBuffer();
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_CULL_FACE);
  }

  private void clearStencilBuffer() {
    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
  }

  private void renderElements(Screen screen) {
    for (FunctionalElement element : screen.children()) {
      if (element != null) {
        renderElements(element, 1);
      }
    }
  }

  private void renderElements(Element element, float alpha) {
    if (element instanceof RenderElement renderElement) {
      if (renderElement.visible()) {
        prepareRendering(renderElement);
        renderElement(renderElement, alpha);
      }
    } else if (element instanceof FunctionalElement functionalElement) {
      boolean hideOverflow = functionalElement.hideOverflow();
      RenderElement background = functionalElement.background();
      if(background != null) {
        renderElement(background, alpha * background.alpha());
      }
      if (!hideOverflow) {
        functionalElement.children().forEach(child -> renderElements(child, alpha * child.alpha()));
      } else {
        FunctionalElement initialHideOverflowElement = hideOverflowElement;
        renderStencil(functionalElement);

        functionalElement.children().forEach(child -> renderElements(child, alpha * child.alpha()));

        renderStencil(initialHideOverflowElement);
      }
    }
  }

  public void enableStencilRendering() {
    GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
    GL11.glStencilMask(0xFF);
  }

  public void renderStencil(FunctionalElement containerElement) {
    hideOverflowElement = containerElement;
    if (containerElement != null) {
      clearStencilBuffer();
      enableStencilRendering();
      containerElement.children().stream()
        .filter(element -> element instanceof RenderElement)
        .forEach(element -> renderElements(element, 1));
      disableStencilRendering();
    }
  }

  public void disableStencilRendering() {
    GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);
    GL11.glStencilMask(0x00);
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
      boxShader.bind();
      defaultBox.bind();
      defaultBox.enableVertexAttributes();
      return;
    }
    if (renderType == RenderElement.RenderType.TEXT) {
      fontShader.bind();
    }
  }

  public void endShader(RenderElement.RenderType renderType) {
    if (renderType == RenderElement.RenderType.BOX) {
      defaultBox.disableVertexAttributes();
      defaultBox.unbind();
      boxShader.unbind();
      return;
    }
    if (renderType == RenderElement.RenderType.TEXT) {
      fontShader.unbind();
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
      fontShader.loadEdge(0.5f);
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
    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
    GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
    GL11.glStencilMask(0x00);
    GL11.glDisable(GL11.GL_STENCIL_TEST);
    GL11.glDisable(GL11.GL_BLEND);
  }

  public void delete() {
    boxShader.delete();
    fontShader.delete();
  }

  private static VertexArrayObject generateDefaultBox() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(Primitive.TRIANGLES, DEFAULT_BOX_POSITIONS.length / 2);
    vertexArrayObject.initialize();
    vertexArrayObject.bind();

    VertexBufferObject positionBuffer = new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.initialize();
    positionBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_POSITIONS));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    VertexBufferObject textureCoordinateBuffer = new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    textureCoordinateBuffer.initialize();
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_TEXTURE_COORDINATES));
    vertexArrayObject.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

}
