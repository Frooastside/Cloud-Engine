/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public record Monitor(long address) {

  public static Monitor DEFAULT = new Monitor(GLFW.glfwGetPrimaryMonitor());

  private static final List<Monitor> AVAILABLE_MONITORS = new ArrayList<>();

  static void updateAvailableMonitors() {
    AVAILABLE_MONITORS.clear();
    PointerBuffer monitorPointerBuffer = GLFW.glfwGetMonitors();
    if (monitorPointerBuffer == null) {
      throw new IllegalStateException(new NullPointerException());
    }
    long[] monitorPointer = new long[monitorPointerBuffer.remaining()];
    monitorPointerBuffer.get(monitorPointer);
    long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
    for (long monitorAddress : monitorPointer) {
      Monitor monitor = new Monitor(monitorAddress);
      if (monitorAddress == primaryMonitor) {
        DEFAULT = monitor;
      }
      AVAILABLE_MONITORS.add(monitor);
    }
  }

  public String name() {
    return GLFW.glfwGetMonitorName(address);
  }

  public List<GLFWVidMode> videoModes() {
    GLFWVidMode.Buffer videoModes = GLFW.glfwGetVideoModes(address);
    return videoModes != null ? videoModes.stream().toList() : new ArrayList<>();
  }

  public Vector2f physicalSize() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      IntBuffer width = memoryStack.mallocInt(1);
      IntBuffer height = memoryStack.mallocInt(1);
      GLFW.glfwGetMonitorPhysicalSize(address, width, height);
      return new Vector2f(width.get(), height.get());
    }
  }

  public Vector2f contentScale() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      FloatBuffer scaleX = memoryStack.mallocFloat(1);
      FloatBuffer scaleY = memoryStack.mallocFloat(1);
      GLFW.glfwGetMonitorContentScale(address, scaleX, scaleY);
      return new Vector2f(scaleX.get(), scaleY.get());
    }
  }

  public Vector2i virtualPosition() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      IntBuffer x = memoryStack.mallocInt(1);
      IntBuffer y = memoryStack.mallocInt(1);
      GLFW.glfwGetMonitorPos(address, x, y);
      return new Vector2i(x.get(), y.get());
    }
  }

  public Vector4i workArea() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      IntBuffer x = memoryStack.mallocInt(1);
      IntBuffer y = memoryStack.mallocInt(1);
      IntBuffer width = memoryStack.mallocInt(1);
      IntBuffer height = memoryStack.mallocInt(1);
      GLFW.glfwGetMonitorWorkarea(address, x, y, width, height);
      return new Vector4i(x.get(), y.get(), width.get(), height.get());
    }
  }

  public static List<Monitor> availableMonitors() {
    return AVAILABLE_MONITORS;
  }
}
