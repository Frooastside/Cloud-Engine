package net.frooastside.engine.graphicobjects.framebuffer;

public interface FrameBufferAttachment {

  void resize(int width, int height);

  void bind();

  void unbind();

  int attachment();
}
