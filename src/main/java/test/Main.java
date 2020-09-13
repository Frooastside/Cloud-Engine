package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.gui.GuiText;
import net.frooastside.engine.resource.Font;
import net.frooastside.engine.gui.BasicGuiShader;
import net.frooastside.engine.resource.ResourceItem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

public class Main {

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
    //font.loadFont(ioResourceToByteBuffer("C:\\Users\\Simon\\Documents\\Engine\\resources\\font\\Aretha - Light.ttf", 0), 1.7f, 2048, 128, 128, 0);
    Font font = new Font(ResourceItem.readFile(new File("C:\\Windows\\Fonts\\JetBrainsMonoNL-Regular.ttf")));
    //Font font = Font.createFont(ioResourceToByteBuffer("C:\\Windows\\Fonts\\CascadiaMono.ttf", 0), 2048, 0, 512, 128);
    font.getThreadUnspecificLoader().run();
    font.getThreadSpecificLoader().run();
    GuiText text = new GuiText(font);
    text.recalculate(1.7f);
    BasicGuiShader triangleShaderProgram = new BasicGuiShader();
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

}