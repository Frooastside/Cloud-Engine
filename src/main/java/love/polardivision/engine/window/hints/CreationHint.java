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

package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public abstract class CreationHint {

  private final int key;
  private final int defaultValue;
  private final int value;

  private CreationHint(int key, int defaultValue, int value) {
    this.key = key;
    this.defaultValue = defaultValue;
    this.value = value;
  }

  public void apply() {
    GLFW.glfwWindowHint(key, value);
  }

  public void reset() {
    if (key != GLFW.GLFW_FALSE && key != GLFW.GLFW_DONT_CARE) {
      GLFW.glfwWindowHint(key, defaultValue);
    }
  }

  public static void resetAll() {
    GLFW.glfwDefaultWindowHints();
  }

  public int key() {
    return key;
  }

  public int defaultValue() {
    return defaultValue;
  }

  public int value() {
    return value;
  }

  public static class Resizeable extends CreationHint {
    public Resizeable(boolean value) {
      super(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class Visible extends CreationHint {
    public Visible(boolean value) {
      super(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class Decorated extends CreationHint {
    public Decorated(boolean value) {
      super(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class Focused extends CreationHint {
    public Focused(boolean value) {
      super(GLFW.GLFW_FOCUSED, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class AutoIconify extends CreationHint {
    public AutoIconify(boolean value) {
      super(GLFW.GLFW_AUTO_ICONIFY, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class Floating extends CreationHint {
    public Floating(boolean value) {
      super(GLFW.GLFW_FLOATING, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class Maximized extends CreationHint {
    public Maximized(boolean value) {
      super(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class CenterCursor extends CreationHint {
    public CenterCursor(boolean value) {
      super(GLFW.GLFW_CENTER_CURSOR, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class TransparentFramebuffer extends CreationHint {
    public TransparentFramebuffer(boolean value) {
      super(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class FocusOnShow extends CreationHint {
    public FocusOnShow(boolean value) {
      super(GLFW.GLFW_FOCUS_ON_SHOW, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class ScaleToMonitor extends CreationHint {
    public ScaleToMonitor(boolean value) {
      super(GLFW.GLFW_SCALE_TO_MONITOR, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class RedBits extends CreationHint {
    public RedBits(int value) {
      super(GLFW.GLFW_RED_BITS, 8, value);
    }
  }

  public static class GreenBits extends CreationHint {
    public GreenBits(int value) {
      super(GLFW.GLFW_GREEN_BITS, 8, value);
    }
  }

  public static class BlueBits extends CreationHint {
    public BlueBits(int value) {
      super(GLFW.GLFW_BLUE_BITS, 8, value);
    }
  }

  public static class AlphaBits extends CreationHint {
    public AlphaBits(int value) {
      super(GLFW.GLFW_ALPHA_BITS, 8, value);
    }
  }

  public static class DepthBits extends CreationHint {
    public DepthBits(int value) {
      super(GLFW.GLFW_DEPTH_BITS, 24, value);
    }
  }

  public static class StencilBits extends CreationHint {
    public StencilBits(int value) {
      super(GLFW.GLFW_STENCIL_BITS, 8, value);
    }
  }

  public static class AuxBuffers extends CreationHint {
    public AuxBuffers(int value) {
      super(GLFW.GLFW_AUX_BUFFERS, 0, value);
    }
  }

  public static class Samples extends CreationHint {
    public Samples(int value) {
      super(GLFW.GLFW_SAMPLES, 0, value);
    }
  }

  public static class RefreshRate extends CreationHint {
    public RefreshRate(int value) {
      super(GLFW.GLFW_REFRESH_RATE, GLFW.GLFW_DONT_CARE, value);
    }
  }

  public static class Stereo extends CreationHint {
    public Stereo(boolean value) {
      super(GLFW.GLFW_STEREO, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class SRGBCapable extends CreationHint {
    public SRGBCapable(boolean value) {
      super(GLFW.GLFW_SRGB_CAPABLE, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class DoubleBuffer extends CreationHint {
    public DoubleBuffer(boolean value) {
      super(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_TRUE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class ClientAPI extends CreationHint {
    public ClientAPI(love.polardivision.engine.window.hints.ClientAPI value) {
      super(GLFW.GLFW_CLIENT_API, love.polardivision.engine.window.hints.ClientAPI.OPENGL.value(), value.value());
    }
  }

  public static class ContextCreationAPI extends CreationHint {
    public ContextCreationAPI(love.polardivision.engine.window.hints.ContextCreationAPI value) {
      super(GLFW.GLFW_CONTEXT_CREATION_API, love.polardivision.engine.window.hints.ContextCreationAPI.NATIVE_CONTEXT_API.value(), value.value());
    }
  }

  public static class ContextVersionMajor extends CreationHint {
    public ContextVersionMajor(int value) {
      super(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 1, value);
    }
  }

  public static class ContextVersionMinor extends CreationHint {
    public ContextVersionMinor(int value) {
      super(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0, value);
    }
  }

  public static class ContextRobustness extends CreationHint {
    public ContextRobustness(love.polardivision.engine.window.hints.ContextCreationAPI value) {
      super(GLFW.GLFW_CONTEXT_ROBUSTNESS, love.polardivision.engine.window.hints.ContextRobustness.NO_ROBUSTNESS.value(), value.value());
    }
  }

  public static class ContextReleaseBehavior extends CreationHint {
    public ContextReleaseBehavior(love.polardivision.engine.window.hints.ContextCreationAPI value) {
      super(GLFW.GLFW_CONTEXT_RELEASE_BEHAVIOR, love.polardivision.engine.window.hints.ContextReleaseBehavior.ANY_RELEASE_BEHAVIOR.value(), value.value());
    }
  }

  public static class OpenGLForwardCompatibility extends CreationHint {
    public OpenGLForwardCompatibility(boolean value) {
      super(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class OpenGLDebugContext extends CreationHint {
    public OpenGLDebugContext(boolean value) {
      super(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_FALSE, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
  }

  public static class OpenGLProfile extends CreationHint {
    public OpenGLProfile(love.polardivision.engine.window.hints.ContextCreationAPI value) {
      super(GLFW.GLFW_OPENGL_PROFILE, love.polardivision.engine.window.hints.OpenGLProfile.OPENGL_ANY_PROFILE.value(), value.value());
    }
  }

}
