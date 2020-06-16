package de.frooastside.engine.gui.renderer;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.frooastside.engine.gui.GuiScreen;
import de.frooastside.engine.gui.VaoData;
import de.frooastside.engine.gui.VaoLoader;
import de.frooastside.engine.gui.guielements.GuiRenderElement;
import de.frooastside.engine.shader.guishader.GuiShader;

public class GuiRenderer {
	
	private GuiShader guiShader;
	private VaoData guiQuad;
	
	public GuiRenderer() {
		guiShader = new GuiShader();
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		guiQuad = VaoLoader.loadToVao(positions);
	}
	
	public void render(GuiScreen guiScreen) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		guiShader.start();
		guiShader.loadBrightness(1.0f);
		GL30.glBindVertexArray(guiQuad.getID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GuiRenderElement element : guiScreen.getTiles()) {
			if(element.isVisible()) {
				guiShader.loadTransformation(new Vector4f(element.getRenderPosition().x, element.getRenderPosition().y, element.getRenderScale().x, element.getRenderScale().y));
				if(element.isUseColor()) {
					guiShader.loadColor(element.getRenderColor());
				}else {
					guiShader.loadTexture();
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getTexture());
				}
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, guiQuad.getVertexCount());
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		guiShader.stop();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
