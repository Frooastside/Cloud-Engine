#version 400

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform sampler2D fontAtlas;
uniform float visibility;

void main() {
    vec4 textureColor = texture(fontAtlas, _textureCoordinates) * visibility;
    finalColor = textureColor + vec4(_textureCoordinates, 0, 0);
}