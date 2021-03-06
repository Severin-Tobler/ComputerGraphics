package jrtr.swrenderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jrtr.Texture;

/**
 * Manages textures for the software renderer. Not implemented here.
 */
public class SWTexture implements Texture {

	private BufferedImage texture;
	
	public void load(String fileName) throws IOException {
		File f = new File(fileName);
		texture = ImageIO.read(f);
	}
	
	public int getColorNN(float x, float y){
		int u = Math.round(x*(texture.getWidth()-1));
		int v = Math.round((texture.getHeight()-1)-y*(texture.getHeight()-1));
		return texture.getRGB(u, v);
	}
	
	public int getColorBL(float x, float y){
		float u = x*(texture.getWidth()-1);
		float v = (texture.getHeight()-1)- y*(texture.getHeight()-1);
		int u0 = (int) Math.floor(u);
		int u1 = (int) Math.ceil(u);
		int v0 = (int) Math.floor(v);
		int v1 = (int) Math.ceil(v);
		
		float wu;
		float wv;
		if(u0 != u1)
			wu = (u-u0)/(u1-u0);
		else
			wu = 0;
		if(v0 != v1)
			wv = (v-v0)/(v1-v0);
		else
			wv = 0;

		Color u0v0 = new Color(texture.getRGB(u0, v0));
		Color u0v1 = new Color(texture.getRGB(u0, v1));
		Color u1v0 = new Color(texture.getRGB(u1, v0));
		Color u1v1 = new Color(texture.getRGB(u1, v1));
		int r = interpolate(u0v0.getRed(), u0v1.getRed(), u1v0.getRed(), u1v1.getRed(), wu, wv);
		int g = interpolate(u0v0.getGreen(), u0v1.getGreen(), u1v0.getGreen(), u1v1.getGreen(), wu, wv);
		int b = interpolate(u0v0.getBlue(), u0v1.getBlue(), u1v0.getBlue(), u1v1.getBlue(), wu, wv);
		Color c = new Color(r,g,b);
		return c.getRGB();
	}
	
	private int interpolate(int u0v0, int u0v1, int u1v0, int u1v1, float wu, float wv){
		float cb = u0v0*(1-wu) + u1v0*wu;
		float ct = u0v1*(1-wu) + u1v1*wu;
		float c = cb*(1-wv) + ct*wv;
		return Math.round(c);
	}

}
