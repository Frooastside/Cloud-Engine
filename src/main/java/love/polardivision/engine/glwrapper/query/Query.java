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
