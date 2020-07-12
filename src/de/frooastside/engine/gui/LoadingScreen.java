package de.frooastside.engine.gui;

import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.language.I18n;
import de.frooastside.engine.resource.LoadingQueue;

public abstract class LoadingScreen extends GuiScreen {
	
	protected LoadingQueue loadingQueue;
	
	protected FontType font;
	
	protected GuiText loadingScreenText;
	protected float progress;
	protected float timer;
	protected int startTexturesLength;
	protected int startModelsLength;
	
	public LoadingScreen(FontType font) {
		this.font = font;
	}
	
	public void update(float delta, int modelQueueSize, int textureQueueSize) {
		if(loadingScreenText != null) {
			if(modelQueueSize != 0) {
				loadingScreenText.setText("Models " + (startModelsLength - modelQueueSize) + " / " + startModelsLength);
				loadingScreenText.reload();
				progress = 1f - (float) modelQueueSize / (float) startModelsLength;
			}else if(textureQueueSize != 0) {
				loadingScreenText.setText("Textures " + (startTexturesLength - textureQueueSize) + " / " + startTexturesLength);
				loadingScreenText.reload();
				progress = 1f - (float) textureQueueSize / (float) startTexturesLength;
			}else {
				if(!loadingScreenText.getText().equalsIgnoreCase(I18n.get("lq.done"))) {
					loadingScreenText.setText(I18n.get("lq.done"));
					loadingScreenText.reload();
				}
				if(timer < 0.5f) {
					timer += delta;
					progress = timer / 0.5f;
				}else {
					loadingQueue.setDone(true);
				}
			}
		}
		super.update(delta);
	}
	
	public void setQueueLength(int startModelsLength, int startTexturesLength) {
		this.startTexturesLength = startTexturesLength;
		this.startModelsLength = startModelsLength;
	}

	public void setLoadingQueue(LoadingQueue loadingQueue) {
		this.loadingQueue = loadingQueue;
	}

}
