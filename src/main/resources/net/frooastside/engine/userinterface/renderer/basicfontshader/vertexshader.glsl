#version 400

in vec2 position;
in vec2 textureCoordinates;

out vec2 _textureCoordinates;

uniform vec2 offset;

void main() {
    gl_Position = vec4(position + offset, 0, 1.0);
    _textureCoordinates = textureCoordinates;
}