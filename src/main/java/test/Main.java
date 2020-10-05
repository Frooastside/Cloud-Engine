package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.userinterface.renderer.BasicBoxShader;
import net.frooastside.engine.userinterface.renderer.BasicFontShader;
import net.frooastside.engine.postprocessing.FullscreenQuadRenderer;
import net.frooastside.engine.resource.ResourceContainer;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.UiScreen;
import net.frooastside.engine.userinterface.renderer.UiRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.File;
import java.io.IOException;

public class Main {

  public static final String TEXT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    GLFWErrorCallback.createPrint(System.err).set();
    GLFW.glfwInit();
    Window window = Window.createWindow("Game Window", false, (int) (960f), (int) (540f));
    window.initGLContext(false);

    ResourceContainer container = new ResourceContainer();
    container.load(new File("C:/Users/Simon/Documents/Font.pak"));
    ResourceFont font = (ResourceFont) container.get("JetBrainsMonoNL-Regular.ttf");
    //ResourceFont font = new ResourceFont(BufferUtils.readFile(new File("C:\\Users\\Simon\\Documents\\JetBrainsMonoNL-Regular.ttf")));
    font.unspecificLoader().run();
    font.contextSpecificLoader().run();
    //font.texture().saveToFile(new File("C:\\Users\\Simon\\Documents\\JetBrainsMonoNL-ExtraBold.png"));

    //ResourceTexture texture1 = new ResourceTexture(BufferUtils.readFile(new File("C:\\Users\\Simon\\Documents\\Engine\\resources\\textures\\font\\consolas.png")));
    //texture1.getThreadUnspecificLoader().run();
    //texture1.getThreadSpecificLoader().run();

    //UiLabel text = new UiLabel(font, TEXT, false);
    //text.recalculate(new Vector2f(1f / 1920f, 1f / 1080f));
    UiScreen uiScreen = new UiScreenTest(window, font);
    uiScreen.initialize();
    uiScreen.recalculate();

    UiRenderer renderer = new UiRenderer(new BasicBoxShader(), new BasicFontShader());
    renderer.initialize();
    //FrameBufferObject frameBufferObject = FrameBufferObject.createDefaultFrameBuffer(window.windowWidth(), window.windowHeight(), 0);
    FullscreenQuadRenderer fullscreenQuadRenderer = new FullscreenQuadRenderer();
    fullscreenQuadRenderer.initialize();

    while (!window.shouldWindowClose()) {
      Window.clearBuffers();
      fullscreenQuadRenderer.drawTexture(font.texture());

      renderer.render(uiScreen);

      /*GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      fontShader.start();
      fontShader.loadTexture(font.texture());
      fontShader.loadColor(1, 1, 1);
      fontShader.loadWidth(0.5f);
      fontShader.loadEdge(0.325f);
      //fontShader.loadTexture(font.texture());
      ((UiText) uiScreen.children().get(0)).model().bind();
      ((UiText) uiScreen.children().get(0)).model().enableVertexAttributes();
      ((UiText) uiScreen.children().get(0)).model().draw();
      ((UiText) uiScreen.children().get(0)).model().disableVertexAttributes();
      ((UiText) uiScreen.children().get(0)).model().unbind();
      fontShader.stop();
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDisable(GL11.GL_BLEND);*/

      window.updateWindow();
    }

    window.closeWindow();
    GLFW.glfwTerminate();
  }

}