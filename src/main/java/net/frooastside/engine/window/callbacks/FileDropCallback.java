package net.frooastside.engine.window.callbacks;

import java.io.File;
import net.frooastside.engine.window.Window;

public interface FileDropCallback {

  void invokeFileDropCallback(Window window, File[] files);

}
