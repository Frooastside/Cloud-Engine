/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.elements.render;


import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.vertexarray.Primitive;
import love.polardivision.engine.glwrapper.vertexarray.VertexArrayObject;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferTarget;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferUsage;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.VertexBufferObject;
import love.polardivision.engine.userinterface.Color;
import love.polardivision.engine.userinterface.Font;
import love.polardivision.engine.userinterface.elements.RenderElement;
import love.polardivision.engine.utils.BufferUtils;
import love.polardivision.engine.utils.NativeObject;

public class Text extends RenderElement implements NativeObject {

  private final VertexArrayObject model = new VertexArrayObject(Primitive.TRIANGLES, 0);

  private final Font font;
  private final boolean centered;

  private String text;

  private float aspectRatio;

  public Text(Font font, String text, Color color, boolean centered) {
    super.setColor(color);
    this.font = font;
    this.text = text;
    this.centered = centered;
  }

  @Override
  public void initialize() {
    model.initialize();
    model.bind();
    VertexBufferObject positionBuffer = new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.DYNAMIC_DRAW);
    positionBuffer.initialize();
    model.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);
    VertexBufferObject textureCoordinateBuffer = new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.DYNAMIC_DRAW);
    textureCoordinateBuffer.initialize();
    model.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);
    model.unbind();
  }

  @Override
  public void delete() {
    model().delete();
  }

  @Override
  public void recalculate() {
    this.aspectRatio = pixelSize().x / pixelSize().y;
    float maxLineLength = bounds().z;
    float fontHeight = bounds().w;

    double fontWidth = fontHeight * aspectRatio;

    double lineLength = 0;
    int characterCount = 0;
    for (char codepoint : text.toCharArray()) {
      Font.Character character = font.character(codepoint);
      lineLength += character.xAdvance() * (1f / font.characterHeight()) * fontWidth;
      characterCount += codepoint != Font.SPACE_CODEPOINT ? 1 : 0;
    }

    double cursorX = centered ? (maxLineLength - lineLength) / 2 : 0.0;
    double yLineOffset = fontHeight / 2;
    float[] positions = new float[characterCount * 12];
    float[] textureCoordinates = new float[characterCount * 12];

    int index = 0;
    for (char codepoint : text.toCharArray()) {
      Font.Character character = font.character(codepoint);
      if (codepoint != Font.SPACE_CODEPOINT) {
        addVerticesFor(positions, textureCoordinates, character, index, fontHeight, fontWidth, cursorX, yLineOffset);
        index++;
      }
      cursorX += character.xAdvance() * (1f / font.characterHeight()) * fontWidth;
    }

    if (positions.length / 2 == model.length()) {
      bufferSubData(positions, textureCoordinates);
    } else {
      bufferData(positions, textureCoordinates);
    }
  }

  protected void addVerticesFor(float[] positions, float[] textureCoordinates, Font.Character character, int characterIndex, double fontHeight, double fontWidth, double cursorX, double cursorY) {
    double x = cursorX + (character.xOffset() * (1f / font.characterHeight()) * fontWidth);
    double y = cursorY + (character.yOffset() * (1f / font.characterHeight()) * fontHeight);
    double xMax = x + (character.xSize() * (1f / font.characterHeight()) * fontWidth);
    double yMax = y + (character.ySize() * (1f / font.characterHeight()) * fontHeight);
    addPoints(positions,
      characterIndex,
      x * 2 - 1,
      y * -2 + 1,
      xMax * 2 - 1,
      yMax * -2 + 1);
    addPoints(textureCoordinates,
      characterIndex,
      character.xTextureCoordinate(),
      character.yTextureCoordinate(),
      character.xMaxTextureCoordinate(),
      character.yMaxTextureCoordinate());
  }

  private void addPoints(float[] array, int characterIndex, double x, double y, double xMax, double yMax) {
    array[characterIndex * 12] = (float) x;
    array[characterIndex * 12 + 1] = (float) y;
    array[characterIndex * 12 + 2] = (float) x;
    array[characterIndex * 12 + 3] = (float) yMax;
    array[characterIndex * 12 + 4] = (float) xMax;
    array[characterIndex * 12 + 5] = (float) yMax;

    array[characterIndex * 12 + 6] = (float) x;
    array[characterIndex * 12 + 7] = (float) y;
    array[characterIndex * 12 + 8] = (float) xMax;
    array[characterIndex * 12 + 9] = (float) yMax;
    array[characterIndex * 12 + 10] = (float) xMax;
    array[characterIndex * 12 + 11] = (float) y;
  }

  private void bufferData(float[] positions, float[] textureCoordinates) {
    model.bind();
    model.setLength(positions.length / 2);
    VertexBufferObject positionBuffer = model.vertexBufferObject(0);
    positionBuffer.storeFloatData(BufferUtils.store(positions));
    VertexBufferObject textureCoordinateBuffer = model.vertexBufferObject(1);
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(textureCoordinates));
    model.unbind();
  }

  private void bufferSubData(float[] positions, float[] textureCoordinates) {
    model.bind();
    VertexBufferObject positionBuffer = model.vertexBufferObject(0);
    positionBuffer.storeFloatSubData(BufferUtils.store(positions), 0);
    VertexBufferObject textureCoordinateBuffer = model.vertexBufferObject(1);
    textureCoordinateBuffer.storeFloatSubData(BufferUtils.store(textureCoordinates), 0);
    model.unbind();
  }

  public double lineLength(int beginIndex, int endIndex) {
    this.aspectRatio = pixelSize().x / pixelSize().y;
    float fontHeight = bounds().w;
    double fontWidth = fontHeight * aspectRatio;
    double lineLength = 0;
    char[] chars = text.toCharArray();
    for (int i = beginIndex; i < endIndex; i++) {
      Font.Character character = font.character(chars[i]);
      lineLength += (character.xAdvance() * (1f / font.characterHeight()) * fontWidth);
    }
    if (endIndex < text.length()) {
      lineLength += (font.character(chars[endIndex]).xOffset() * (1f / font.characterHeight()) * fontWidth) / 2;
    }
    return lineLength;
  }

  public void updateText(String text) {
    setText(text);
    recalculate();
  }

  @Override
  public RenderType renderType() {
    return RenderType.TEXT;
  }

  public VertexArrayObject model() {
    return model;
  }

  public Font font() {
    return font;
  }

  public float aspectRatio() {
    return aspectRatio;
  }

  public String text() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
