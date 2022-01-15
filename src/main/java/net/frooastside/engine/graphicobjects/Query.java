package net.frooastside.engine.graphicobjects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class Query extends GraphicObject {

  private final int type;

  private boolean inUse = false;

  public Query(int type){
    this.type = type;
  }

  public boolean ready() {
    return GL15.glGetQueryObjecti(identifier(), GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE;
  }

  public int result() {
    inUse = false;
    return GL15.glGetQueryObjecti(identifier(), GL15.GL_QUERY_RESULT);
  }

  @Override
  public void generateIdentifier() {
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

  public int type() {
    return type;
  }

  public boolean isInUse() {
    return inUse;
  }
}
