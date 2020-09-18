#version 400

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform sampler2D guiTexture;
uniform vec3 color;
uniform float visibility;
uniform float width;
uniform float height;

void main() {
    vec4 textureColor = texture(guiTexture, _textureCoordinates) * visibility;
    vec4 basicColor = vec4(color, visibility);
    finalColor = mix(basicColor, textureColor, useTexture);
}