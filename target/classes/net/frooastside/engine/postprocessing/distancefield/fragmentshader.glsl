#version 400

in vec2 textureCoordinates;

out vec4 finalColor;

uniform sampler2D textureSampler;
uniform int beta;
uniform vec2 offset;

void main() {
    vec4 this_pixel = texture(textureSampler, textureCoordinates);
    vec4 before_pixel = texture(textureSampler, textureCoordinates - offset);
    vec4 after_pixel = texture(textureSampler, textureCoordinates + offset);

    float A = this_pixel.r;
    float floatBeta = beta / 255.0;
    float after = floatBeta + after_pixel.r;
    float before = floatBeta + before_pixel.r;
    float B = min(min(A, after), before);

    finalColor = vec4(B, B, B, 1);
}