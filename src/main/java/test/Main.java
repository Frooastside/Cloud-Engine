package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.gui.GuiText;
import net.frooastside.engine.gui.font.Font;
import net.frooastside.engine.shader.guishader.GuiShader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  private static GuiShader triangleShaderProgram;

  public static void main(String[] args) throws IOException {
    GLFWErrorCallback.createPrint(System.err).set();
    GLFW.glfwInit();
    Window window = Window.createWindow("Game Window", false, 960, 540);
    window.initGLContext(false);
    /*VertexArrayObject vertexArrayObject = VertexArrayObject.create2DTFor(new float[]{
      -0.5f, 0.5f,
      0.5f, 0.5f,
      -0.5f, -0.5f,
    },new float[]{
      1, 0.5f, 0, 1,
      0, 1, 0.5f, 1,
      0.5f, 1, 0, 1,
      0, 0.5f, 1, 1
    });*/
    Font font = new Font();

    font.loadFont(ioResourceToByteBuffer("C:\\Users\\Simon\\Documents\\Engine\\resources\\font\\Aretha - Light.ttf", 0), 1.7f, 2048, 128, 128, 0);
    GuiText text = new GuiText(font);
    text.createMesh();
    triangleShaderProgram = new GuiShader();
    triangleShaderProgram.createShaderProgram();
    while (!window.shouldWindowClose()) {
      window.clearBuffers();
      GL11.glDisable(GL11.GL_CULL_FACE);
      triangleShaderProgram.start();
      text.model().bind();
      triangleShaderProgram.loadTexture(font.texture());
      text.model().enableVertexAttributes();
      text.model().draw();
      text.model().disableVertexAttributes();
      text.model().unbind();
      triangleShaderProgram.stop();
      window.updateWindow();
    }

    text.model().delete();
    triangleShaderProgram.delete();

    window.closeWindow();
    GLFW.glfwTerminate();
  }

  private static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
    ByteBuffer buffer;

    Path path = Paths.get(resource);
    if(Files.isReadable(path)) {
      try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
        buffer = BufferUtils.createByteBuffer((int)seekableByteChannel.size() + 1);
        while (seekableByteChannel.read(buffer) != -1) {}
      }
    }else {
      System.out.println(1);
      try(InputStream source = new FileInputStream(new File(resource)); ReadableByteChannel readableByteChannel = Channels.newChannel(source)) {
        buffer = BufferUtils.createByteBuffer(bufferSize);
        while (true) {
          int bytes = readableByteChannel.read(buffer);
          if(bytes == -1) {
            break;
          }
          if(buffer.remaining() == 0) {
            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
          }
        }
      }
    }
    buffer.flip();
    return buffer;
  }

  private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
    ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
  }

}