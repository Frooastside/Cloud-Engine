package de.frooastside.engine;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import de.frooastside.UserService;
import de.frooastside.engine.camera.Camera;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.input.InputManager;
import de.frooastside.engine.language.I18n;
import de.frooastside.engine.model.VertexArrayObjectLoader;
import de.frooastside.engine.resource.LoadingQueue;

public class Engine {
	
	public static final String TITLE = "Wolken Eis Engine";
	public static final String VERSION = "0.1 Alpha";
	public static final String ASSETS_VERSION = "1.0";
	
	protected static Engine engine;
	
	protected String filePath;
	protected String osName;
	
	protected UserService userService;
	protected Session session;
	
	protected LoadingQueue loadingQueue;
	
	protected GLFWManager glfwManager;
	protected InputManager inputManager;
	protected FontType mainFont;
	
	protected double tickTime;
	
	public static float milliseconds = 0;
	public static int frames = 0;
	public static int currentFramesPerSecond = 0;
	
	protected void startEngine() {
		setFilePath();
		I18n.init(new File(getFilePath() + "/resources/lang"), "de_DE");
		
		userService = new UserService();
		
		glfwManager = new GLFWManager();
		glfwManager.createMainDisplay(TITLE + " " + VERSION, false);
		inputManager = new InputManager(glfwManager.getMainWindowID());
		Camera.createInstance();
		
		loadResources();
		
		loadingQueue.load();
		
		glfwManager.setMainWindowSize();
		GLFW.glfwShowWindow(glfwManager.getMainWindowID());
		
		boolean closeRequested = false;
		while(!closeRequested) {
			if(!loadingQueue.isDone()) {
				loadingQueue.processElement();
			}
			
			Camera.getInstance().update();
			update();
			
			if(tickTime >= 50.0) {
				while(tickTime >= 50.0) {
					tickTime -= 50.0;
					tick((float) (tickTime / 50f));
				}
			}
			tickTime += glfwManager.getDelta() * 1000.0;
			
			glfwManager.clearBuffers();
			render();
			
			glfwManager.updateDisplay(true);
			inputManager.updateInputs();
			closeRequested = GLFW.glfwWindowShouldClose(glfwManager.getMainWindowID());
			
			if(milliseconds >= 1) {
				currentFramesPerSecond = frames;
				GLFW.glfwSetWindowTitle(glfwManager.getMainWindowID(), TITLE + " " + VERSION + " " + currentFramesPerSecond + " FPS");
				frames = 0;
				milliseconds %= 1;
			}
			milliseconds += glfwManager.getDelta();
			frames++;
		}
		
		VertexArrayObjectLoader.cleanUp();
		cleanUp();
		glfwManager.terminate();
	}
	
	protected void cleanUp() {}

	protected void loadResources() {}

	protected void update() {}

	protected void tick(float tickProgression) {}
	
	protected void render() {}
	
	protected void setFilePath() {
		osName = System.getProperty("os.name");
		System.out.println(osName);
		if(osName.contains("Windows")) {
			filePath = System.getProperty("user.home") + "/documents/engine";
		}else if(osName.contains("Mac")) {
			filePath = System.getProperty("user.home") + "/engine";
		}else if(osName.contains("Linux")) {
			filePath = System.getProperty("user.home") + "/engine";
		}else {
			filePath = System.getProperty("user.home") + "/engine";
		}
	}

	public static Engine getEngine() {
		return engine;
	}

	public String getFilePath() {
		return filePath;
	}

	public GLFWManager getGlfwManager() {
		return glfwManager;
	}

	public LoadingQueue getLoadingQueue() {
		return loadingQueue;
	}

	public Session getSession() {
		return session;
	}

	public UserService getUserService() {
		return userService;
	}

	public void recalculateWindowSize(int width, int height) {
		glfwManager.setMainWindowWidth(width);
		glfwManager.setMainWindowHeight(height);
		glfwManager.setMainWindowSize();
		GL11.glViewport(0, 0, width, height);
		Camera.getInstance().recalculateProjectionMatrix();
		mainFont.recalculateAspectRatio();
	}
	
}
