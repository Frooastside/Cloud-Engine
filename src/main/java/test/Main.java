package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.model.VertexArrayObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Main {

  private static TriangleShaderProgram triangleShaderProgram;

  public static void main(String[] args) {
    GLFWErrorCallback.createPrint(System.err).set();
    GLFW.glfwInit();
    Window window = Window.createWindow("Game Window", false, 960, 540);
    window.initGLContext(false);
    VertexArrayObject vertexArrayObject = VertexArrayObject.create2DCIFor(new float[]{
      -0.5f, 0.5f,
      0.5f, 0.5f,
      -0.5f, -0.5f,
      0.5f, -0.5f
    },new float[]{
      1, 0.5f, 0, 1,
      0, 1, 0.5f, 1,
      0.5f, 1, 0, 1,
      0, 0.5f, 1, 1
    }, new short[] {
      0, 2, 1,
      1, 2, 3
    });
    triangleShaderProgram = new TriangleShaderProgram();
    triangleShaderProgram.createShaderProgram();
    while (!window.shouldWindowClose()) {
      window.clearBuffers();
      triangleShaderProgram.start();
      vertexArrayObject.bind();
      vertexArrayObject.enableVertexAttributes();
      vertexArrayObject.draw();
      vertexArrayObject.disableVertexAttributes();
      vertexArrayObject.unbind();
      triangleShaderProgram.stop();
      window.updateWindow();
    }

    vertexArrayObject.delete();
    triangleShaderProgram.delete();

    window.closeWindow();
    GLFW.glfwTerminate();
  }

}