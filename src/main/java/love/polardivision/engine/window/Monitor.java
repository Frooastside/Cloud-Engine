/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
