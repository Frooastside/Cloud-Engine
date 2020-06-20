package de.frooastside.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class GLFWManager {
	
	private long loadingWindowID;
	
	private long mainWindowID;
	private int mainWindowWidth;
	private int mainWindowHeight;
	
	private int currentWindowWidth;
	private int currentWindowHeight;
	
	private long lastFrameTime;
	private double delta;
	
	public void createMainDisplay(String title, boolean fullscreen) {
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW.glfwInit();
		if(fullscreen) {
			long primary = GLFW.glfwGetPrimaryMonitor();
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			GLFWVidMode videoMode = GLFW.glfwGetVideoMode(primary);
			int width = videoMode.width();
			int height = videoMode.height();
			mainWindowWidth = width;
			mainWindowHeight = height;
			mainWindowID = GLFW.glfwCreateWindow(width, height, title, primary, 0);
			GLFW.glfwSetWindowPos(mainWindowID, videoMode.width() / 2 - width / 2, videoMode.height() / 2 - height / 2);
		}else {
			long primary = GLFW.glfwGetPrimaryMonitor();
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			GLFWVidMode videoMode = GLFW.glfwGetVideoMode(primary);
			int width = (int) (videoMode.width() / 1.2f);
			int height = (int) (videoMode.height() / 1.2f);
			mainWindowWidth = width;
			mainWindowHeight = height;
			mainWindowID = GLFW.glfwCreateWindow(width, height, title, 0, 0);
			GLFW.glfwSetWindowPos(mainWindowID, videoMode.width() / 2 - width / 2, videoMode.height() / 2 - height / 2);
		}
		GLFW.glfwSetInputMode(mainWindowID, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
		GLFW.glfwMakeContextCurrent(mainWindowID);
		GLFW.glfwSwapInterval(0);
		initOpenGL();
	}
	
	public void createLoadingDisplay() {
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW.glfwInit();
		long primary = GLFW.glfwGetPrimaryMonitor();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(primary);
		int width = videoMode.width() / 4;
		int height = videoMode.height() / 4;
		currentWindowWidth = width;
		currentWindowHeight = height;
		loadingWindowID = GLFW.glfwCreateWindow(width, height, "Engine Loader", 0, 0);
		GLFW.glfwSetWindowPos(loadingWindowID, videoMode.width() / 2 - width / 2, videoMode.height() / 2 - height / 2);
		GLFW.glfwMakeContextCurrent(loadingWindowID);
		GLFW.glfwSwapInterval(0);
		initOpenGL();
	}
	
	private void initOpenGL() {
		GL.createCapabilities();
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void updateDisplay(boolean mainWindow) {
		GLFW.glfwSwapBuffers(mainWindow ? getMainWindowID() : getLoadingWindowID());
		GLFW.glfwPollEvents();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000.0f;
		delta = delta >= 0.5f ? 0 : delta;
		lastFrameTime = currentFrameTime;
	}
	
	public void destroyLoadingWindow() {
		GLFW.glfwDestroyWindow(loadingWindowID);
	}
	
	public void terminate() {
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public void clearBuffers() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private static long getCurrentTime() {
		return (System.nanoTime() / 1000000);
	}

	public float getDelta() {
		return (float) delta;
	}
	
	public double getPreciseDelta() {
		return delta;
	}
	
	public void setMainWindowSize() {
		this.currentWindowWidth = this.mainWindowWidth;
		this.currentWindowHeight = this.mainWindowHeight;
	}
	
	public void setWindowOpacity(float opacity) {
		GLFW.glfwSetWindowOpacity(loadingWindowID, opacity);
	}

	public long getMainWindowID() {
		return mainWindowID;
	}

	public long getLoadingWindowID() {
		return loadingWindowID;
	}

	public int getCurrentWindowWidth() {
		return currentWindowWidth;
	}

	public int getCurrentWindowHeight() {
		return currentWindowHeight;
	}

	public int getMainWindowWidth() {
		return mainWindowWidth;
	}

	public void setMainWindowWidth(int mainWindowWidth) {
		this.mainWindowWidth = mainWindowWidth;
	}

	public int getMainWindowHeight() {
		return mainWindowHeight;
	}

	public void setMainWindowHeight(int mainWindowHeight) {
		this.mainWindowHeight = mainWindowHeight;
	}

}
