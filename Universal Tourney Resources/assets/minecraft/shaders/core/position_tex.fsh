#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a == 0.0) {
        discard;
    }
    fragColor = color * ColorModulator;

    // Replaces any () colored pixel in the tablist
    if(fragColor.xyz == vec3(94/255.0, 102/255.0, 83/255.0)) {
        fragColor.a = 0.0;
    }
}
