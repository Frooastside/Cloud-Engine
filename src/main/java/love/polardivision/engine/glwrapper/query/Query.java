/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.glwrapper.query;

import love.polardivision.engine.glwrapper.GraphicalObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class Query extends GraphicalObject {

  private final int type;

  private boolean inUse = false;

  public Query(QueryType type) {
    this(type.value());
  }

  public Query(int type) {
    this.type = type;
  }

  @Override
  public void initialize() {
    setIdentifier(GL15.glGenQueries());
  }

  @Override
  public void bind() {
    GL15.glBeginQuery(type, identifier());
    inUse = true;
  }

  @Override
  public void unbind() {
    GL15.glEndQuery(type);
  }

  @Override
  public void delete() {
    GL15.glDeleteQueries(identifier());
  }

  public boolean ready() {
    return GL15.glGetQueryObjecti(identifier(), GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE;
  }

  public int result() {
    inUse = false;
    return GL15.glGetQueryObjecti(identifier(), GL15.GL_QUERY_RESULT);
  }

  public int type() {
    return type;
  }

  public boolean isInUse() {
    return inUse;
  }
}
