#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    vec2 uv = texCoord;
    uv -= 0.5; uv *= 0.1; uv.y += 0.5;
    float bottom_width = 1.0;
    float top_width = 1.0;
    uv.x /= mix(bottom_width, top_width, uv.y);
    uv.x += 0.5;
    vec3 col = texture2D(DiffuseSampler, clamp(uv, vec2(0), vec2(1))).rgb;
    fragColor = vec4(col, 1.0);
}
