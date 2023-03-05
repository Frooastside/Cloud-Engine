in vec2 textureCoordinates;

out vec4 finalColor;

uniform sampler2D textureSampler;

void main() {
    finalColor = texture(textureSampler, textureCoordinates);
}