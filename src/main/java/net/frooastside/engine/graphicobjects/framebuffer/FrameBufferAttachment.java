package net.frooastside.engine.graphicobjects.framebuffer;

public interface FrameBufferAttachment {

  void resize(int width, int height);

  void appendToFrameBuffer();

  int attachment();

  void setAttachment(int attachment);
}
