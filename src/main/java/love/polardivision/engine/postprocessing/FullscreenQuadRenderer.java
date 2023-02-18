/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.postprocessing;

import love.polardivision.engine.shader.Shader;
import love.polardivision.engine.shader.ShaderProgram;
import love.polardivision.engine.shader.ShaderType;
import love.polardivision.engine.shader.uniforms.UniformTexture;
import love.polardivision.engine.wrappers.gl.texture.Texture;

public class FullscreenQuadRenderer extends PostProcessingEffect {

  private final FullscreenQuadShader fullscreenQuadShader = new FullscreenQuadShader();

  public void initialize() {
    fullscreenQuadShader.initialize();
  }

  public void drawTexture(Texture texture) {
    fullscreenQuadShader.bind();
    bind();
    fullscreenQuadShader.loadTexture(texture);
    draw();
    unbind();
    fullscreenQuadShader.unbind();
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
      addShader(Shader.createShader(FullscreenQuadRenderer.class.getResourceAsStream("fullscreenquad/vertexshader.glsl"), ShaderType.VERTEX_SHADER));
      addShader(Shader.createShader(FullscreenQuadRenderer.class.getResourceAsStream("fullscreenquad/fragmentshader.glsl"), ShaderType.FRAGMENT_SHADER));
    }

    @Override
    protected void bindAttributes() {
      bindAttribute(0, "position");
    }

    @Override
    protected void storeUniformLocations() {
    }

    @Override
    protected void loadTextureUnits() {
      textureSamplerUniform.loadTextureUnit(0);
    }

    public void loadTexture(Texture texture) {
      textureSamplerUniform.activeTextureUnit();
      texture.bind();
    }
  }

}
