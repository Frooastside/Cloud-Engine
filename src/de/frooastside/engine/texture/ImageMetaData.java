package de.frooastside.engine.texture;

public class ImageMetaData {
	
	private int width;
	private int height;
	private int channels;
	
	public ImageMetaData(int width, int height, int channels) {
		this.width = width;
		this.height = height;
		this.channels = channels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getChannels() {
		return channels;
	}
	
	public void setChannels(int channels) {
		this.channels = channels;
	}
	
}
