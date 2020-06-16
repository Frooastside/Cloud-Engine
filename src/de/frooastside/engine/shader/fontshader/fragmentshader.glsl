#version 400

in vec2 _textureCoords;

out vec4 out_color;

uniform vec4 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0.5;
const float borderEdge = 0.4;

const vec2 offset = vec2(0.0028, 0.0028);

const vec4 outlineColor = vec4(0.0, 0.0, 0.0, 1.0);

float smoothlyStep(float edge0, float edge1, float x){
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

void main(void){

	float distance = 1.0 - texture(fontAtlas, _textureCoords).a;
	float alpha = 1.0 - smoothlyStep(width, width + edge, distance);

	float distance2 = 1.0 - texture(fontAtlas, _textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothlyStep(borderWidth, borderWidth + borderEdge, distance2);

	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec4 overallColor = mix(outlineColor, color, alpha / overallAlpha);

	overallColor.w = overallColor.w * overallAlpha;
	out_color = overallColor;

}
