package net.frooastside.engine.graphicobjects.framebuffer;

import net.frooastside.engine.graphicobjects.GraphicObject;
import net.frooastside.engine.graphicobjects.texture.Texture;
import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.List;

public class FrameBufferObject extends GraphicObject {

  private int width;
  private int height;

  private final List<FrameBufferAttachment> attachments = new ArrayList<>();

  public FrameBufferObject(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void appendFrameBufferAttachment(FrameBufferAttachment attachment) {
    attachment.generateIdentifier();
    attachment.bind();
    attachment.store();
    attachment.appendToFrameBuffer();
    attachment.unbind();
    attachments.add(attachment);
  }

  public void resize(int width, int height) {
    this.width = width;
    this.height = height;
    attachments.forEach(attachment -> attachment.resize(width, height));
  }

  public void resetViewport() {
    GL11.glViewport(0, 0, width, height);
  }

  public void selectDrawOutput(int attachment) {
    GL11.glDrawBuffer(attachment);
  }

  public void selectDrawOutputs(int... attachments) {
    GL30.glDrawBuffers(attachments);
  }

  public void copyToFBO(FrameBufferObject output) {
    output.bind(GL30.GL_DRAW_FRAMEBUFFER);
    bind(GL30.GL_READ_FRAMEBUFFER);
    blitFrameBuffer(0, 0, output.width, output.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
  }

  public void blitFrameBuffer(int outputX, int outputY, int outputWidth, int outputHeight, int mask, int filter) {
    blitFrameBuffer(0, 0, width, height, outputX, outputY, outputWidth, outputHeight, mask, filter);
  }

  public void blitFrameBuffer(int x, int y, int width, int height, int outputX, int outputY, int outputWidth, int outputHeight, int mask, int filter) {
    GL30.glBlitFramebuffer(x, y, width, height, outputX, outputY, outputWidth, outputHeight, mask, filter);
  }

  @Override
  public void generateIdentifier() {
    identifier = GL30.glGenFramebuffers();
  }

  @Override
  public void bind() {
    bind(GL30.GL_FRAMEBUFFER);
  }

  public void bind(int target) {
    GL30.glBindFramebuffer(target, identifier);
  }

  @Override
  public void unbind() {
    unbind(GL30.GL_FRAMEBUFFER);
  }

  public void unbind(int target) {
    GL30.glBindFramebuffer(target, 0);
  }

  @Override
  public void delete() {
    GL30.glDeleteFramebuffers(identifier);
  }

  public static FrameBufferObject createDefaultFrameBuffer(int width, int height, int samples) {
    FrameBufferObject frameBufferObject = new FrameBufferObject(width, height);
    frameBufferObject.generateIdentifier();
    frameBufferObject.bind();

    //if (samples == 0) {
      Texture colorTexture = new Texture(null, Texture.BILINEAR_FILTER, width, height, GL11.GL_RGBA, GL11.GL_RGBA);
      colorTexture.setAttachment(GL30.GL_COLOR_ATTACHMENT0);
      frameBufferObject.appendFrameBufferAttachment(colorTexture);
      frameBufferObject.selectDrawOutput(GL30.GL_COLOR_ATTACHMENT0);
    //} else {
    //  BufferAttachment colorBuffer = new BufferAttachment(samples, GL11.GL_RGBA8);
    //  colorBuffer.setAttachment(GL30.GL_COLOR_ATTACHMENT0);
    //  frameBufferObject.appendFrameBufferAttachment(colorBuffer);
    //}
    //BufferAttachment depthBuffer = new BufferAttachment(samples, GL14.GL_DEPTH_COMPONENT24);
    //depthBuffer.setAttachment(GL30.GL_DEPTH_ATTACHMENT);
    //frameBufferObject.appendFrameBufferAttachment(depthBuffer);

    frameBufferObject.unbind();
    return frameBufferObject;
  }

  public List<FrameBufferAttachment> attachments() {
    return attachments;
  }
}
