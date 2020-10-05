package net.frooastside.engine.postprocessing;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.shader.Shader;
import net.frooastside.engine.shader.ShaderProgram;
import net.frooastside.engine.shader.uniforms.UniformTexture;

public class FullscreenQuadRenderer extends PostProcessingEffect {

  private final FullscreenQuadShader fullscreenQuadShader = new FullscreenQuadShader();

  public void initialize() {
    fullscreenQuadShader.initialize();
  }

  public void drawTexture(Texture texture) {
    fullscreenQuadShader.start();
    prepare();
    fullscreenQuadShader.loadTexture(texture);
    draw();
    stop();
    fullscreenQuadShader.stop();
  }

  @Override
  public void delete() {
    fullscreenQuadShader.delete();
    fullscreenQuad().delete();
  }

  public static class FullscreenQuadShader extends ShaderProgram {

    private final UniformTexture textureSamplerUniform = new UniformTexture("textureSampler");

    @Override
    protected void addShaders() {
      addShader(Shader.createShader("/net/frooastside/engine/postprocessing/fullscreenquad/vertexshader.glsl", true, Shader.VERTEX_SHADER));
      addShader(Shader.createShader("/net/frooastside/engine/postprocessing/fullscreenquad/fragmentshader.glsl", true, Shader.FRAGMENT_SHADER));
    }

    @Override
    protected void bindAttributes() {
      bindAttribute(0, "position");
    }

    @Override
    protected void storeUniformLocations() {
    }

    @Override
    protected void setDefaults() {
      textureSamplerUniform.loadTextureUnit(0);
    }

    public void loadTexture(Texture texture) {
      textureSamplerUniform.activeTextureUnit();
      texture.bind();
    }
  }

}
