package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Window;

import java.io.File;

public interface FileDropCallback {

  void invokeFileDropCallback(Window window, File[] files);

}
