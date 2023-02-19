#version 130

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform float alpha;

uniform float useColor;
uniform vec4 color;

uniform float useTexture;
uniform sampler2D guiTexture;

void main() {
    finalColor = mix(vec4(1.0), texture(guiTexture, _textureCoordinates), useTexture);
    finalColor = mix(finalColor, finalColor * color, useColor);
    finalColor.a *= alpha;
}