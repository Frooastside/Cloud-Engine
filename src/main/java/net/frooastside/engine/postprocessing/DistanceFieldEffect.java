package net.frooastside.engine.postprocessing;

import net.frooastside.engine.Window;
import net.frooastside.engine.graphicobjects.framebuffer.FrameBufferObject;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.shader.Shader;
import net.frooastside.engine.shader.ShaderProgram;
import net.frooastside.engine.shader.uniforms.UniformInt;
import net.frooastside.engine.shader.uniforms.UniformTexture;
import net.frooastside.engine.shader.uniforms.UniformVector2f;
import org.lwjgl.opengl.GL11;

public class DistanceFieldEffect extends PostProcessingEffect {

  public static DistanceFieldShader distanceFieldShader = new DistanceFieldShader();

  public static void init() {
    distanceFieldShader.createShaderProgram();
  }

  public static Texture generateDistanceField(Texture input, int passes, int distance) {
    int[] viewportDimensions = new int[4];
    GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewportDimensions);

    FrameBufferObject aBuffer = FrameBufferObject.createDefaultFrameBuffer(input.width(), input.height(), 0);
    FrameBufferObject bBuffer = FrameBufferObject.createDefaultFrameBuffer(input.width(), input.height(), 0);

    aBuffer.resetViewport();

    prepare();
    distanceFieldShader.start();
    distanceFieldShader.loadTexture(input);

    int beta;

    distanceFieldShader.loadOffset(1f / input.width(), 0);
    for (int verticalBeta = 0; verticalBeta < passes; verticalBeta++) {
      beta = (verticalBeta * distance * 2) + 1;
      distanceFieldShader.loadBeta(beta);
      aBuffer.bind();
      Window.clearBuffers();
      draw();
      aBuffer.unbind();
      distanceFieldShader.loadTexture((Texture) aBuffer.attachments().get(0));

      beta = (verticalBeta * distance * 2) + distance + 1;
      distanceFieldShader.loadBeta(beta);
      bBuffer.bind();
      Window.clearBuffers();
      draw();
      bBuffer.unbind();
      distanceFieldShader.loadTexture((Texture) bBuffer.attachments().get(0));
    }

    distanceFieldShader.loadOffset(0, 1f / input.height());
    for (int horizontalBeta = 0; horizontalBeta < passes; horizontalBeta++) {
      beta = (horizontalBeta * distance * 2) + 1;
      distanceFieldShader.loadBeta(beta);
      aBuffer.bind();
      Window.clearBuffers();
      draw();
      aBuffer.unbind();
      distanceFieldShader.loadTexture((Texture) aBuffer.attachments().get(0));

      beta = (horizontalBeta * distance * 2) + distance + 1;
      distanceFieldShader.loadBeta(beta);
      bBuffer.bind();
      Window.clearBuffers();
      draw();
      bBuffer.unbind();
      distanceFieldShader.loadTexture((Texture) bBuffer.attachments().get(0));
    }

    distanceFieldShader.stop();
    stop();

    GL11.glViewport(viewportDimensions[0], viewportDimensions[1], viewportDimensions[2], viewportDimensions[3]);
    return (Texture) bBuffer.attachments().get(0);
  }

  public static class DistanceFieldShader extends ShaderProgram {

    private final UniformTexture textureSamplerUniform = new UniformTexture("textureSampler");
    private final UniformInt betaUniform = new UniformInt("beta");
    private final UniformVector2f offsetUniform = new UniformVector2f("offset");

    @Override
    protected void addShaders() {
      addShader(Shader.createShader("/net/frooastside/engine/postprocessing/distancefield/vertexshader.glsl", true, Shader.VERTEX_SHADER));
      addShader(Shader.createShader("/net/frooastside/engine/postprocessing/distancefield/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
    }

    @Override
    protected void bindAttributes() {
      bindAttribute(0, "position");
    }

    @Override
    protected void storeUniformLocations() {
      storeUniformLocation(betaUniform);
      storeUniformLocation(offsetUniform);
    }

    @Override
    protected void setDefaults() {
      textureSamplerUniform.loadTextureUnit(0);
    }

    public void loadTexture(Texture texture) {
      textureSamplerUniform.activeTextureUnit();
      texture.bind();
    }

    public void loadBeta(int beta) {
      System.out.println("Beta: " + beta);
      betaUniform.loadInt(beta);
    }

    public void loadOffset(float x, float y) {
      System.out.println("Offset: " + x + ", " + y);
      offsetUniform.loadVector2f(x, y);
    }
  }
}
