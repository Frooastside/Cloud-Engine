#version 400

in vec2 position;
in vec2 textureCoords;

out vec2 _textureCoords;

uniform vec2 translation;

void main(void){

	gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
	_textureCoords = textureCoords;

}
