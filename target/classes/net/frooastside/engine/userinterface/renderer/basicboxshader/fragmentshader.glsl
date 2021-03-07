#version 130

in vec2 _textureCoordinates;

out vec4 finalColor;

uniform float alpha;

uniform float useColor;
uniform vec4 color;

uniform float useTexture;
uniform sampler2D guiTexture;

void main() {
    finalColor = vec4(1.0);

    if (useTexture > 0.5) {
        finalColor = texture(guiTexture, _textureCoordinates);
    }

    finalColor = mix(finalColor, finalColor * color, useColor);

    finalColor.a *= alpha;
}