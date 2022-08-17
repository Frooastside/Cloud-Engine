package love.polardivision.engine.window.callbacks;

import java.io.File;
import love.polardivision.engine.window.Window;

public interface FileDropCallback {

  void invokeFileDropCallback(Window window, File[] files);

}
