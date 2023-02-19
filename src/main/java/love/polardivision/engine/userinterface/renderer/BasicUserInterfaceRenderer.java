/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.renderer;

import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.userinterface.elements.Element;
import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.elements.RenderElement;
import love.polardivision.engine.userinterface.elements.render.Box;
import love.polardivision.engine.userinterface.elements.render.Text;
import love.polardivision.engine.utils.BufferUtils;
import love.polardivision.engine.wrappers.gl.DataType;
import love.polardivision.engine.wrappers.gl.framebuffer.FrameBufferObject;
import love.polardivision.engine.wrappers.gl.vertexarray.Primitive;
import love.polardivision.engine.wrappers.gl.vertexarray.VertexArrayObject;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.BufferTarget;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.BufferUsage;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.VertexBufferObject;
import love.polardivision.engine.wrappers.yoga.Overflow;
import org.lwjgl.opengl.GL11;

public class BasicUserInterfaceRenderer extends UserInterfaceRenderer {

  private static final float[] DEFAULT_BOX_POSITIONS =
      new float[] {-1, 1, -1, -1, 1, 1, 1, -1, 1, 1, -1, -1};
  private static final float[] DEFAULT_BOX_TEXTURE_COORDINATES =
      new float[] {0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1};

  private final VertexArrayObject defaultBox =
      new VertexArrayObject(Primitive.TRIANGLES, DEFAULT_BOX_POSITIONS.length / 2);

  private final BoxShader boxShader;
  private final FontShader fontShader;

  private RenderElement.RenderType currentRenderType;
  private FunctionalElement hideOverflowElement;

  public BasicUserInterfaceRenderer(BoxShader boxShader, FontShader fontShader) {
    this.boxShader = boxShader;
    this.fontShader = fontShader;
  }

  @Override
  public void initialize() {
    defaultBox.initialize();
    defaultBox.bind();
    VertexBufferObject positionBuffer =
        new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.initialize();
    positionBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_POSITIONS));
    defaultBox.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);
    VertexBufferObject textureCoordinateBuffer =
        new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    textureCoordinateBuffer.initialize();
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(DEFAULT_BOX_TEXTURE_COORDINATES));
    defaultBox.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);
    defaultBox.unbind();

    boxShader.initialize();
    fontShader.initialize();
  }

  @Override
  public void delete() {
    boxShader.delete();
    fontShader.delete();
  }

  @Override
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
    FrameBufferObject.clearStencilBuffer();
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_CULL_FACE);
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
      boolean hideOverflow =
          functionalElement.layoutNode().overflow() == Overflow.HIDDEN
              || functionalElement.layoutNode().overflow() == Overflow.SCROLL;
      RenderElement background = functionalElement.background();
      if (background != null) {
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

  private void enableStencilRendering() {
    GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
    GL11.glStencilMask(0xFF);
  }

  private void renderStencil(FunctionalElement containerElement) {
    hideOverflowElement = containerElement;
    if (containerElement != null) {
      FrameBufferObject.clearStencilBuffer();
      enableStencilRendering();
      containerElement.children().stream()
          .filter(element -> element instanceof RenderElement)
          .forEach(element -> renderElements(element, 1));
      disableStencilRendering();
    }
  }

  private void disableStencilRendering() {
    GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);
    GL11.glStencilMask(0x00);
  }

  private void prepareRendering(RenderElement renderElement) {
    RenderElement.RenderType renderType = renderElement.renderType();
    if (currentRenderType != renderType) {
      endShader(currentRenderType);
      prepareShader(renderType);
      currentRenderType = renderType;
    }
  }

  private void prepareShader(RenderElement.RenderType renderType) {
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

  private void endShader(RenderElement.RenderType renderType) {
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

  private void renderElement(RenderElement renderElement, float alpha) {
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
}
