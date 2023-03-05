in vec2 position;
in vec2 textureCoordinates;

out vec2 _textureCoordinates;

uniform vec4 translation;

const vec2 screenSpaceTransformation = vec2(1.0, -1.0);

void main() {
    vec2 screenSpacePosition = translation.xy * screenSpaceTransformation * 2;
    vec2 screenSpaceScale = translation.zw * screenSpaceTransformation;
    gl_Position = vec4(position * screenSpaceScale + screenSpaceScale + screenSpacePosition - screenSpaceTransformation, 0, 1.0);
    _textureCoordinates = textureCoordinates;
}