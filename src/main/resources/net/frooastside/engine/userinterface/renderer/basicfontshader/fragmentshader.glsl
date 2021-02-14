#version 130

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform sampler2D fontAtlas;
uniform vec3 color;
uniform float visibility;
uniform float width;
uniform float edge;

void main() {
    float distance = 1.0 - texture(fontAtlas, _textureCoordinates).r;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);
    finalColor = vec4(color, alpha * visibility);
}