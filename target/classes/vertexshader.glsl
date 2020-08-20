#version 400

in vec3 position;
in vec4 color;

out vec4 _color;

void main() {
    _color = color;
    gl_Position = vec4(position, 1.0);
}