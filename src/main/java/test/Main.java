package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.gui.BasicFontShader;
import net.frooastside.engine.gui.GuiText;
import net.frooastside.engine.postprocessing.DistanceFieldEffect;
import net.frooastside.engine.postprocessing.FullscreenQuadRenderer;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.resource.ResourceTexture;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

public class Main {

  private static final String TEXT = "LMM%orem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

  public static void main(String[] args) throws IOException {
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
    font.texture().saveToFile(new File("C:\\Users\\Simon\\Documents\\JetBrainsMonoNL-ExtraBold.png"));

    ResourceTexture texture1 = new ResourceTexture(BufferUtils.readFile(new File("C:\\Users\\Simon\\Documents\\Engine\\resources\\textures\\font\\consolas.png")));
    texture1.getThreadUnspecificLoader().run();
    texture1.getThreadSpecificLoader().run();

    GuiText text = new GuiText(font, TEXT, false);
    text.recalculate(1.7f);
    BasicFontShader fontShader = new BasicFontShader();
    fontShader.createShaderProgram();
    //FrameBufferObject frameBufferObject = FrameBufferObject.createDefaultFrameBuffer(window.windowWidth(), window.windowHeight(), 0);
    FullscreenQuadRenderer.init();
    DistanceFieldEffect.init();

    //Texture texture = DistanceFieldEffect.generateDistanceField(font.texture(), 32, 1);
    while (!window.shouldWindowClose()) {
      GL11.glDisable(GL11.GL_CULL_FACE);
      Window.clearBuffers();
      FullscreenQuadRenderer.drawTexture(font.texture());


      //frameBufferObject.bind();
      //GL11.glClearColor(0, 0, 1, 1);
      //Window.clearBuffers();
      //frameBufferObject.unbind();

      //frameBufferObject.unbind(GL30.GL_DRAW_FRAMEBUFFER);
      //frameBufferObject.bind(GL30.GL_READ_FRAMEBUFFER);
      //frameBufferObject.selectDrawOutput(GL11.GL_BACK);
      //frameBufferObject.blitFrameBuffer(0, 0, window.windowWidth(), window.windowHeight(), GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
      //frameBufferObject.unbind();

      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      fontShader.start();
      fontShader.loadTexture(font.texture());
      fontShader.loadColor(1, 1, 1);
      fontShader.loadWidth(0.5f);
      fontShader.loadEdge(0.325f);
      //fontShader.loadTexture(font.texture());
      text.model().bind();
      text.model().enableVertexAttributes();
      text.model().draw();
      text.model().disableVertexAttributes();
      text.model().unbind();
      fontShader.stop();
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDisable(GL11.GL_BLEND);

      window.updateWindow();
    }
    //frameBufferObject.delete();
    //text.model().delete();
    //fontShader.delete();

    window.closeWindow();
    GLFW.glfwTerminate();
  }

}