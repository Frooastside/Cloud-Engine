#version 130

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform float useTexture;
uniform float visibility;
uniform sampler2D guiTexture;
uniform vec3 color;

void main() {
    vec4 textureColor = texture(guiTexture, _textureCoordinates) * visibility;
    vec4 basicColor = vec4(color, visibility);
    finalColor = mix(basicColor, textureColor, useTexture);
}