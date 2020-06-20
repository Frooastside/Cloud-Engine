package de.frooastside.engine.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import de.frooastside.engine.Engine;

public class InputManager {
	
	private long mainWindowID;
	
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWDropCallback fileDropCallback;
	private GLFWJoystickCallback joystickCallback;
	
	private List<ICameraMovementCallback> cameraMovementCallbacks = new ArrayList<ICameraMovementCallback>();
	private List<IMovementCallback> movementCallbacks = new ArrayList<IMovementCallback>();
	private List<ICursorPositionCallback> cursorPositionCallbacks = new ArrayList<ICursorPositionCallback>();
	private List<IButtonCallback> buttonCallbacks = new ArrayList<IButtonCallback>();
	
	private GLFWGamepadState gamePadState;
	private List<Integer> gamePadButtonList = new ArrayList<Integer>();
	private List<Integer> keyboardButtonList = new ArrayList<Integer>();

	private boolean cursorDisabled;
	private boolean useControllerInput;
	
	private int gamePadID = -1;
	
	private float mouseX;
	private float mouseY;
	private float lastX;
	private float lastY;
	private float relativeX;
	private float relativeY;
	
	private float leftX;
	private float leftY;
	private float rightX;
	private float rightY;
	private float leftTrigger;
	private float rightTrigger;
	
	public InputManager(long mainWindowID) {
		this.mainWindowID = mainWindowID;
		setCallbacks();
		
		for (int i = 0; i <= GLFW.GLFW_JOYSTICK_LAST; i++) {
			if(GLFW.glfwJoystickIsGamepad(i)) {
				gamePadID = i;
				System.out.println(gamePadID);
				System.out.println(GLFW.glfwGetGamepadName(i));
			}
		}
		gamePadState = GLFWGamepadState.create();
	}

	public void updateInputs() {
		if(gamePadID != -1) {
			boolean state = GLFW.glfwGetGamepadState(gamePadID, gamePadState);
			if(state) {
				gamePadButtonList.clear();
				for(int i = 0; i <= GLFW.GLFW_GAMEPAD_BUTTON_LAST; i++) {
					if(gamePadState.buttons(i) == GLFW.GLFW_PRESS) {
						gamePadButtonList.add(i);
					}
				}
				leftX = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
				leftY = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
				rightX = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
				rightY = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
				leftTrigger = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
				rightTrigger = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
				boolean axesMoved = false;
				if(leftX >= 0.1f || leftX <= -0.15f) {
					axesMoved = true;
				}else {
					leftX = 0;
				}
				if(leftY >= 0.1f || leftY <= -0.15f) {
					axesMoved = true;
				}else {
					leftY = 0;
				}
				if(rightX >= 0.1f || rightX <= -0.15f) {
					axesMoved = true;
				}else {
					rightX = 0;
				}
				if(rightY >= 0.1f || rightY <= -0.15f) {
					axesMoved = true;
				}else {
					rightY = 0;
				}
				if(leftTrigger >= 0.2f) {
					axesMoved = true;
				}else {
					leftTrigger = 0;
				}
				if(rightTrigger >= 0.2f) {
					axesMoved = true;
				}else {
					rightTrigger = 0;
				}
				if(!useControllerInput) {
					if(axesMoved || !gamePadButtonList.isEmpty()) {
						useControllerInput = true;
					}
				}
			}
		}else {
			useControllerInput = false;
		}
		
		if(cursorDisabled) {
			relativeX = mouseX - lastX;
			relativeY = mouseY - lastY;
		}else {
			relativeX = 0;
			relativeY = 0;
		}
		lastX = mouseX;
		lastY = mouseY;
		
		if(cursorDisabled) {
			if(useControllerInput) {
				useControllerInput = relativeX == 0 && relativeY == 0;
			}
		}
		if(!useControllerInput) {
			if(cursorDisabled) {
				for(ICameraMovementCallback callback : cameraMovementCallbacks) {
					callback.invokeRotation(relativeX * 0.112f, relativeY * 0.112f);
				}
			}else {
				for (ICursorPositionCallback callback : cursorPositionCallbacks) {
					callback.invokePosition(mouseX, mouseY);
				}
			}
			int vertical = 0;
			int horizontal = 0;
			if(keyboardButtonList.contains(GLFW.GLFW_KEY_S)) {
				vertical = -1;
			}
			if(keyboardButtonList.contains(GLFW.GLFW_KEY_W)) {
				vertical = 1;
			}
			if(keyboardButtonList.contains(GLFW.GLFW_KEY_A)) {
				horizontal = 1;
			}
			if(keyboardButtonList.contains(GLFW.GLFW_KEY_D)) {
				horizontal = -1;
			}
			if(vertical != 0 || horizontal != 0) {
				for(IMovementCallback callback : movementCallbacks) {
					callback.invokeMovement(vertical, horizontal);
				}
			}
		}else {
			for(ICameraMovementCallback callback : cameraMovementCallbacks) {
				callback.invokeRotation(rightX * 0.112f, rightY * 0.112f);
			}
			for(IMovementCallback callback : movementCallbacks) {
				callback.invokeMovement(-leftY, -leftX);
			}
		}
	}

	public void invokeMouseButtonPress(long window, int button, int action, int mods) {
		
	}

	public void invokeKeyPress(long window, int key, int scancode, int action, int mods) {
		switch (action) {
			case GLFW.GLFW_PRESS:
				keyboardButtonList.add(key);
				break;
			case GLFW.GLFW_REPEAT:
				
				break;
			case GLFW.GLFW_RELEASE:
				keyboardButtonList.remove((Object) key);
				break;
			default:
				break;
		}
		for (IButtonCallback callback : buttonCallbacks) {
			callback.invokeButtonPress(key);
		}
	}
	
	public String getClipboard() {
		String clipboard = GLFW.glfwGetClipboardString(mainWindowID);
		if(clipboard != null) {
			return clipboard;
		}else {
			return "";
		}
	}
	
	public void setClipboard(String text) {
		GLFW.glfwSetClipboardString(mainWindowID, text);
	}
	
	public void hideCursor() {
		cursorDisabled = false;
		GLFW.glfwSetInputMode(mainWindowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
	}
	
	public void disableCursor() {
		cursorDisabled = true;
		GLFW.glfwSetInputMode(mainWindowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public void showCursor() {
		cursorDisabled = false;
		GLFW.glfwSetInputMode(mainWindowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		int[] width = new int[1];
		int[] height = new int[1];
		GLFW.glfwGetFramebufferSize(mainWindowID, width, height);
		GLFW.glfwSetCursorPos(mainWindowID, width[0] / 2f, height[0] / 2f);
	}
	
	private void setCallbacks() {
		keyCallback = new GLFWKeyCallback() {
			
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(action == GLFW.GLFW_PRESS) {
					if(key == GLFW.GLFW_KEY_ESCAPE) {
						if(isCursorDisabled()) {
							showCursor();
						}else {
							disableCursor();
						}
					}else if(key == GLFW.GLFW_KEY_6) {
						System.gc();
					}
				}
				invokeKeyPress(window, key, scancode, action, mods);
			}
			
		};
		cursorPosCallback = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseX = (float) xpos;
				mouseY = (float) ypos;
			}
			
		};
		mouseButtonCallback = new GLFWMouseButtonCallback() {
			
			@Override
			public void invoke(long window, int button, int action, int mods) {
				invokeMouseButtonPress(window, button, action, mods);
			}
			
		};
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			
			@Override
			public void invoke(long window, int width, int height) {
				Engine.getEngine().recalculateWindowSize(width, height);
			}
			
		};
		fileDropCallback = new GLFWDropCallback() {
			
			@Override
			public void invoke(long window, int count, long names) {
				
			}
			
		};
		joystickCallback = new GLFWJoystickCallback() {
			
			@Override
			public void invoke(int jid, int event) {
				System.out.println(jid);
				System.out.println(event);
				System.out.println(GLFW.glfwJoystickIsGamepad(jid));
				if(event == GLFW.GLFW_CONNECTED) {
					if(GLFW.glfwJoystickIsGamepad(jid)) {
						gamePadID = jid;
					}
				}else {
					gamePadID = -1;
				}
			}
			
		};
		
		GLFW.glfwSetKeyCallback(mainWindowID, keyCallback);
		GLFW.glfwSetCursorPosCallback(mainWindowID, cursorPosCallback);
		GLFW.glfwSetMouseButtonCallback(mainWindowID, mouseButtonCallback);
		GLFW.glfwSetFramebufferSizeCallback(mainWindowID, framebufferSizeCallback);
		GLFW.glfwSetDropCallback(mainWindowID, fileDropCallback);
		GLFW.glfwSetJoystickCallback(joystickCallback);
	}

	public List<ICameraMovementCallback> getCameraMovementCallbacks() {
		return cameraMovementCallbacks;
	}

	public List<IMovementCallback> getButtonMovementCallbacks() {
		return movementCallbacks;
	}

	public boolean isCursorDisabled() {
		return cursorDisabled;
	}

	public int getGamePadID() {
		return gamePadID;
	}

	public void setGamePadID(int gamePadID) {
		this.gamePadID = gamePadID;
	}

	public List<Integer> getKeyboardButtonList() {
		return keyboardButtonList;
	}

}
