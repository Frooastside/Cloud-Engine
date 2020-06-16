package de.frooastside.engine.gui.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.shader.fontshader.FontShader;

public class FontRenderer {

	private FontShader fontShader;

	public FontRenderer() {
		fontShader = new FontShader();
	}
	
	public void render(Map<FontType, List<GuiText>> texts) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		fontShader.start();
		for(FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GuiText text : texts.get(font)) {
				if(text.isVisible()) {
					GL30.glBindVertexArray(text.getMesh());
					GL20.glEnableVertexAttribArray(0);
					GL20.glEnableVertexAttribArray(1);
					fontShader.loadColor(text.getRenderColor());
					fontShader.loadTranslation(text.getRenderPosition());
					GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
					GL20.glDisableVertexAttribArray(0);
					GL20.glDisableVertexAttribArray(1);
					GL30.glBindVertexArray(0);
				}
			}
		}
		fontShader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
