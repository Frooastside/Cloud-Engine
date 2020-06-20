package de.frooastside.engine.texture;

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
import org.lwjgl.opengl.EXTDirectStateAccess;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.EXTTextureMirrorClamp;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;
import org.lwjgl.stb.STBImage;

import de.frooastside.engine.Engine;
import de.frooastside.engine.language.I18n;
import de.frooastside.engine.resource.FileExtension;
import de.frooastside.engine.resource.ILoadingQueueElement;

public class GLTexture implements ILoadingQueueElement {
	
	public static final int NO_FILTER = 0x1;
	public static final int BILINEAR_FILTER = 0x2;
	public static final int TRILINEAR_FILTER = 0x4;
	public static final int ANISOTROPIC_FILTER = 0x8;
	
	private int textureID;
	private int filter;
	private ImageMetaData metaData;
	private String file;
	
	public GLTexture(String file, TextureType type, FileExtension fileExtension, boolean loadInstantly, int filter) {
		this.file = Engine.getEngine().getFilePath() + type.path + file + fileExtension.extension;
		this.filter = filter;
		if(loadInstantly) {
			generate();
			metaData = loadImage(this.file, textureID);
			applyFilter();
		}else {
			Engine.getEngine().getLoadingQueue().addToLoadingQueue(this);
		}
	}

	@Override
	public void process() {
		generate();
		metaData = loadImage(file, textureID);
		applyFilter();
	}
	
	private void applyFilter() {
		if((filter & NO_FILTER) == NO_FILTER) {
			noFilter();
		}
		if((filter & BILINEAR_FILTER) == BILINEAR_FILTER) {
			bilinearFilter();
		}
		if((filter & TRILINEAR_FILTER) == TRILINEAR_FILTER) {
			trilinearFilter();
		}
		if((filter & ANISOTROPIC_FILTER) == ANISOTROPIC_FILTER) {
			anisotropicFilter();
		}
	}
	
	private ImageMetaData loadImage(String file, int handle) {
		ByteBuffer imageBuffer;
		try {
			imageBuffer = ioResourceToByteBuffer(file, 128 * 128);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer c = BufferUtils.createIntBuffer(1);
		if(!STBImage.stbi_info_from_memory(imageBuffer, w, h, c)) {
			throw new RuntimeException(I18n.get("error.texture.loading.info") + STBImage.stbi_failure_reason());
		}
		
		ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
		if(image == null) {
			throw new RuntimeException(I18n.get("error.texture.loading") + STBImage.stbi_failure_reason());
		}
		
		int width = w.get(0);
		int height = h.get(0);
		int comp = c.get(0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		
		if(comp == 3) {
			if((width & 3) != 0) {
				GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width & 1));
			}
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
		} else {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		}
		
		STBImage.stbi_image_free(image);
		
		ImageMetaData metaData = new ImageMetaData(width, height, comp);
		
		return metaData;
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
	
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	}
	
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	public void generate() {
		textureID = GL11.glGenTextures();
	}
	
	public void delete() {
		GL11.glDeleteTextures(textureID);
	}
	
	private GLTexture noFilter() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		return this;
	}
	
	private GLTexture bilinearFilter() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		return this;
	}
	
	private GLTexture trilinearFilter() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		return this;
	}
	
	private GLTexture anisotropicFilter() {
		if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float maxfilterLevel = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxfilterLevel);
		}
		else{
			System.err.println(I18n.get("error.texture.anisotropic"));
		}
		return this;
	}
	
	public void clampToEdge() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE);
	}
	
	public void clampToBorder() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
	}
	
	public void repeat() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	}
	
	public void mirroredRepeat() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
	}
	
	public void mirrorRepeatEXT() {
		if(GL.getCapabilities().GL_EXT_texture_mirror_clamp) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, EXTTextureMirrorClamp.GL_MIRROR_CLAMP_TO_EDGE_EXT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, EXTTextureMirrorClamp.GL_MIRROR_CLAMP_TO_EDGE_EXT);
		}
		else{
			System.err.println(I18n.get("error.texture.mirror"));
		}
	}
	
	public void clampToEdgeDirectAccessEXT() {
		EXTDirectStateAccess.glTextureParameteriEXT(textureID, GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE);
		EXTDirectStateAccess.glTextureParameteriEXT(textureID, GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE);
	}
	
	public void mirrorRepeatDirectAccessEXT() {
		EXTDirectStateAccess.glTextureParameteriEXT(textureID, GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, EXTTextureMirrorClamp.GL_MIRROR_CLAMP_TO_EDGE_EXT);
		EXTDirectStateAccess.glTextureParameteriEXT(textureID, GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, EXTTextureMirrorClamp.GL_MIRROR_CLAMP_TO_EDGE_EXT);
	}
	
	public void activeTextureUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
	}
	
	public void allocateImage2D(int internalFormat, int format, int type, ByteBuffer data) {
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, metaData.getWidth(), metaData.getHeight(), 0, format, type, data);
	}
	
	public void allocateImage2D(int internalFormat, int format, int type) {
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, metaData.getWidth(), metaData.getHeight(), 0, format, type, (ByteBuffer) null);
	}
	
	public void allocateImage2DMultisample(int samples, int internalFormat) {
		GL32.glTexImage2DMultisample(GL11.GL_TEXTURE_2D, samples, internalFormat, metaData.getWidth(), metaData.getHeight(), true);
	}
	
	public void allocateStorage2D(int levels, int internalFormat) {
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, levels, internalFormat, metaData.getWidth(), metaData.getHeight());
	}
	
	public void allocateStorage3D(int levels, int layers, int internalFormat) {
		GL42.glTexStorage3D(GL11.GL_TEXTURE_2D, levels, internalFormat, metaData.getWidth(), metaData.getHeight(), layers);
	}

	public int getTextureID() {
		return textureID;
	}

}
