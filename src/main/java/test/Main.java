package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.postprocessing.DistanceFieldEffect;
import net.frooastside.engine.postprocessing.FullscreenQuadRenderer;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.resource.ResourceFont;
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
    Window window = Window.createWindow("Game Window", false, (int) (960 * 1.8f), (int) (540 * 1.8f));
    window.initGLContext(false);

    //ResourceContainer container = new ResourceContainer();
    //container.load(new File("C:/Users/Simon/Documents/Font.pak"));
    //ResourceFont font = (ResourceFont) container.get("JetBrainsMonoNL-Regular.ttf");
    ResourceFont font = new ResourceFont(BufferUtils.readFile(new File("C:\\Users\\Simon\\Documents\\JetBrainsMonoNL-Regular.ttf")));
    font.getThreadUnspecificLoader().run();
    font.getThreadSpecificLoader().run();

    //GuiText text = new GuiText(font, TEXT, false);
    //text.recalculate(1.7f);
    //BasicFontShader fontShader = new BasicFontShader();
    //fontShader.createShaderProgram();
    //FrameBufferObject frameBufferObject = FrameBufferObject.createDefaultFrameBuffer(window.windowWidth(), window.windowHeight(), 0);
    FullscreenQuadRenderer.init();
    DistanceFieldEffect.init();

    Texture texture = DistanceFieldEffect.generateDistanceField(font.texture(), 16);
    while (!window.shouldWindowClose()) {
      GL11.glDisable(GL11.GL_CULL_FACE);
      Window.clearBuffers();

      if(GLFW.glfwGetKey(window.windowId(), GLFW.GLFW_KEY_0) == GLFW.GLFW_PRESS) {
        FullscreenQuadRenderer.drawTexture(texture);
      }else {
        FullscreenQuadRenderer.drawTexture(font.texture());
      }


      //frameBufferObject.bind();
      //GL11.glClearColor(0, 0, 1, 1);
      //Window.clearBuffers();
      //frameBufferObject.unbind();

      //frameBufferObject.unbind(GL30.GL_DRAW_FRAMEBUFFER);
      //frameBufferObject.bind(GL30.GL_READ_FRAMEBUFFER);
      //frameBufferObject.selectDrawOutput(GL11.GL_BACK);
      //frameBufferObject.blitFrameBuffer(0, 0, window.windowWidth(), window.windowHeight(), GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
      //frameBufferObject.unbind();

      //fontShader.start();
      //fontShader.loadTexture((Texture) frameBufferObject.attachments().get(0));
      //fontShader.loadTexture(font.texture());
      //text.model().bind();
      //text.model().enableVertexAttributes();
      //text.model().draw();
      //text.model().disableVertexAttributes();
      //text.model().unbind();
      //fontShader.stop();

      window.updateWindow();
    }
    //frameBufferObject.delete();
    //text.model().delete();
    //fontShader.delete();

    window.closeWindow();
    GLFW.glfwTerminate();
  }

}