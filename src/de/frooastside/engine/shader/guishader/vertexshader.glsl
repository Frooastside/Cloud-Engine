#version 400

in vec2 position;

out vec2 textureCoords;

uniform vec4 transform;

void main(void){
	
	vec2 screenPosition = position * (transform.zw) + vec2(transform.z, -(transform.w)) - vec2(1, -1) + vec2(transform.x * 2, -(transform.y * 2));
	gl_Position = vec4(screenPosition, 0.999, 1.0);
	textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
	
}
