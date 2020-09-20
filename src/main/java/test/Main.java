package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.gui.GuiText;
import net.frooastside.engine.resource.Font;
import net.frooastside.engine.gui.BasicGuiShader;
import net.frooastside.engine.resource.ResourceContainer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

public class Main {

  private static final String TEXT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    GLFWErrorCallback.createPrint(System.err).set();
    GLFW.glfwInit();
    Window window = Window.createWindow("Game Window", false, 960, 540);
    window.initGLContext(false);

    ResourceContainer container = new ResourceContainer();
    container.load(new File("C:/Users/Simon/Documents/Font.pak"));
    Font font = (Font) container.get("JetBrainsMonoNL-Regular.ttf");
    font.getThreadUnspecificLoader().run();
    font.getThreadSpecificLoader().run();

    GuiText text = new GuiText(font, TEXT, false);
    text.recalculate(1.7f);
    BasicGuiShader triangleShaderProgram = new BasicGuiShader();
    triangleShaderProgram.createShaderProgram();
    while (!window.shouldWindowClose()) {
      Window.clearBuffers();
      GL11.glDisable(GL11.GL_CULL_FACE);
      triangleShaderProgram.start();
      triangleShaderProgram.loadTexture(font.texture());
      text.model().bind();
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