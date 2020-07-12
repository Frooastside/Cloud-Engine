package de.frooastside.engine.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import de.frooastside.engine.Engine;
import de.frooastside.engine.GLFWManager;
import de.frooastside.engine.gui.GuiScreen;
import de.frooastside.engine.gui.LoadingScreen;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.language.I18n;
import de.frooastside.engine.model.RawModel;
import de.frooastside.engine.renderer.FontRenderer;
import de.frooastside.engine.renderer.GuiRenderer;
import de.frooastside.engine.texture.GLTexture;

public class LoadingQueue {
	
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;
	private LoadingScreen loadingScreen;
	private Class<? extends LoadingScreen> guiScreen;
	private String fontName;
	
	private Queue<ILoadingQueueElement> models;
	private Queue<ILoadingQueueElement> textures;
	
	protected int startTexturesLength;
	protected int startModelsLength;
	
	private boolean done = false;
	
	public LoadingQueue(Class<? extends LoadingScreen> guiScreen, String fontName) {
		models = new LinkedBlockingQueue<ILoadingQueueElement>();
		textures = new LinkedBlockingQueue<ILoadingQueueElement>();
		
		this.guiScreen = guiScreen;
		this.fontName = fontName;
	}
	
	public void addToLoadingQueue(ILoadingQueueElement element) {
		if(element instanceof RawModel) {
			models.offer(element);
		}else if(element instanceof GLTexture) {
			textures.offer(element);
		}else {
			System.err.println(I18n.get("error.lq.add"));
		}
	}
	
	public void processElement() {
		if(models.size() != 0) {
			ILoadingQueueElement model = models.poll();
			if(model != null) {
				model.process();
			}
		}else if(textures.size() != 0) {
			ILoadingQueueElement texture = textures.poll();
			if(texture != null) {
				texture.process();
			}
		}
	}
	
	protected void updateLoadingScreen() {}
	
	public void load() {
		startTexturesLength = textures.size();
		startModelsLength = models.size();
		Thread loadingScreenThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				GLFWManager glfwManager = Engine.getEngine().getGlfwManager();
				glfwManager.createLoadingDisplay();
				guiRenderer = new GuiRenderer();
				fontRenderer = new FontRenderer();
				try {
					loadingScreen = guiScreen.getConstructor(FontType.class).newInstance(new FontType(fontName));
					loadingScreen.setQueueLength(startModelsLength, startTexturesLength);
					loadingScreen.setLoadingQueue(LoadingQueue.this);
				}catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					switch (JOptionPane.showConfirmDialog(null, I18n.get("error.lq.confirmRetry"))) {
					case JOptionPane.OK_OPTION:
						try {
							loadingScreen = guiScreen.getConstructor(FontType.class).newInstance(new FontType(fontName));
							loadingScreen.setQueueLength(startModelsLength, startTexturesLength);
							loadingScreen.setLoadingQueue(LoadingQueue.this);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
							JOptionPane.showMessageDialog(null, I18n.get("error.lq.loadingScreen"));
							e1.printStackTrace();
						}
						break;
					default:
						e.printStackTrace();
					}
				}
				loadingScreen.open();
				while(!done) {
					glfwManager.clearBuffers();
					guiRenderer.render(loadingScreen);
					fontRenderer.render(loadingScreen.getTexts());
					loadingScreen.update(glfwManager.getDelta(), models.size(), textures.size());
					glfwManager.updateDisplay(false);
				}
				guiRenderer.cleanUp();
				fontRenderer.cleanUp();
				glfwManager.destroyLoadingWindow();
			}
			
		});
		loadingScreenThread.start();
		while(!done) {
			processElement();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public GuiScreen getLoadingScreen() {
		return loadingScreen;
	}
	
	public boolean isEmpty() {
		return models.isEmpty() && textures.isEmpty();
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
