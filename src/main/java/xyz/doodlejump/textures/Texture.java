package xyz.doodlejump.textures;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL46;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Texture {

    private int textureID;

    public Texture() {
        textureID = GL11.glGenTextures();
    }
    public Texture(BufferedImage img) {
        textureID = GL11.glGenTextures();
        loadTexture(img);
    }

    public void loadTexture(BufferedImage img) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int bert = img.getRGB(x, y);
                int red, blue, green, alpha;
                blue = (bert >>> 0) & 0xFF;
                green = (bert >>> 8) & 0xFF;
                red = (bert >>> 16) & 0xFF;
                alpha = (bert >>> 24) & 0xFF;
                buffer.put((byte) (red));
                buffer.put((byte) (green));
                buffer.put((byte) (blue));
                buffer.put((byte) alpha);

            }
        }
        buffer.flip();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL15.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_EDGE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


    public void draw(double x, double y, double width, double height) {

        glColor4d(1,1,1,1);
        glEnable(GL_TEXTURE_2D);
        bind();
        glBegin(GL_QUADS);


        glTexCoord2d(0,0);
        glVertex2d(x, y);

        glTexCoord2d(1,0);
        glVertex2d(width + x, y);

        glTexCoord2d(1,1);
        glVertex2d(width + x,  height + y);

        glTexCoord2d(0,1);
        glVertex2d(x, height + y);

        glEnd();
        glDisable(GL_TEXTURE_2D);

    }
}
