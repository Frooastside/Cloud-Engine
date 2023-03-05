in vec2 _textureCoordinates;

out vec4 finalColor;

uniform sampler2D fontAtlas;
uniform vec4 color;
uniform float alpha;
uniform float width;
uniform float edge;

void main() {
    float distance = 1.0 - texture(fontAtlas, _textureCoordinates).r;
    float distanceBasedAlpha = 1.0 - smoothstep(width, width + edge, distance);
    finalColor = color;
    finalColor.a *= distanceBasedAlpha * alpha;
}