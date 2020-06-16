package de.frooastside.engine.gui.meshcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.language.I18n;

public class FontType {

	private int textureAtlas;
	private TextMeshCreator loader;
	private File fontFile;
	
	public FontType(String path, String fontName) {
		this.textureAtlas = createFontTexture(path, fontName, ".png");
		this.fontFile = new File(path + "/" + fontName + ".fnt");
		this.loader = new TextMeshCreator(this.fontFile);
	}
	
	public int getTextureAtlas() {
		return textureAtlas;
	}
	
	public TextMeshData loadText(GuiText text) {
		return loader.createTextMesh(text);
	}
	
	public void recalculateAspectRatio() {
		if(loader != null)
			loader.reload(fontFile);
	}
	
	public int createFontTexture(String path, String file, String fileExtension) {
		int textureID = GL11.glGenTextures();
		loadImage(path + "/" + file + fileExtension, textureID);
		noFilter();
		return textureID;
	}
	
	private void noFilter() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	}
	
	private void loadImage(String file, int handle) {
		ByteBuffer imageBuffer;
		try {
			imageBuffer = ioResourceToByteBuffer(file, 128 * 128);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer channelBuffer = BufferUtils.createIntBuffer(1);
		if(!STBImage.stbi_info_from_memory(imageBuffer, widthBuffer, heightBuffer, channelBuffer)) {
			throw new RuntimeException(I18n.get("error.texture.loading.info", STBImage.stbi_failure_reason()));
		}
		
		ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer, channelBuffer, 0);
		if(image == null) {
			throw new RuntimeException(I18n.get("error.texture.loading", STBImage.stbi_failure_reason()));
		}
		
		int width = widthBuffer.get(0);
		int height = heightBuffer.get(0);
		int channel = channelBuffer.get(0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		
		if(channel == 3) {
			if((width & 3) != 0) {
				GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width & 1));
			}
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
		} else {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		}
		
		STBImage.stbi_image_free(image);
	}
	
	private ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;
		
		Path path = Paths.get(resource);
		if(Files.isReadable(path)) {
			try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int)seekableByteChannel.size() + 1);
				while (seekableByteChannel.read(buffer) != -1) {}
			}
		}else {
			try(InputStream source = new FileInputStream(new File(resource)); ReadableByteChannel readableByteChannel = Channels.newChannel(source)) {
				buffer = BufferUtils.createByteBuffer(bufferSize);
				while (true) {
					int bytes = readableByteChannel.read(buffer);
					if(bytes == -1) {
						break;
					}
					if(buffer.remaining() == 0) {
						buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				}
			}
		}
		buffer.flip();
		return buffer;
	}

	private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

}
