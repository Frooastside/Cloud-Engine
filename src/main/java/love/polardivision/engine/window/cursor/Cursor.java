package love.polardivision.engine.window.cursor;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

public record Cursor(long address) {

  public static Cursor createCursor(GLFWImage image, Vector2i hotSpot) {
    return new Cursor(GLFW.glfwCreateCursor(image, hotSpot.x, hotSpot.y));
  }

  public static Cursor createStandardCursor(CursorShape shape) {
    return new Cursor(GLFW.glfwCreateStandardCursor(shape.value()));
  }

  public void destroy() {
    GLFW.glfwDestroyCursor(address());
  }

}
