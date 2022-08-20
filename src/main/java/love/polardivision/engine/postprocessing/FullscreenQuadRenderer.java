/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.postprocessing;

import love.polardivision.engine.glwrapper.texture.Texture;
import love.polardivision.engine.shader.Shader;
import love.polardivision.engine.shader.ShaderProgram;
import love.polardivision.engine.shader.ShaderType;
import love.polardivision.engine.shader.uniforms.UniformTexture;

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
