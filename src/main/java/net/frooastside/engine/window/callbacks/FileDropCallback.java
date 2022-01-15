package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;

import java.io.File;

public interface FileDropCallback {

  void invokeFileDropCallback(Window window, File[] files);

}
