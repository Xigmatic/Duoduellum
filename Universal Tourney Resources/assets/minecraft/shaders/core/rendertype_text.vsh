#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform int FogShape;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;

    // Removes color from no-shadow text
    if(Color == vec4(227/255.0, 255/255.0, 234/255.0, Color.a) && (
        Position.z == 0.03 ||
        Position.z == 100.03 ||
        Position.z == 200.03 ||
        Position.z == 300.03 ||
        Position.z == 400.03
    )) {
        vertexColor = texelFetch(Sampler2, UV2 / 16, 0);
    
    // Removes shadow by removing pixels with a darker color of the no-shad0w color
    } else if((Color == vec4(56/255.0, 63/255.0, 58/255.0, Color.a)) && (
        Position.z == 0 ||
        Position.z == 100 ||
        Position.z == 200 ||
        Position.z == 300 ||
        Position.z == 400
    )) {
        vertexColor.a = 0.0;
    // Transparent (GRAY DEFAULT TEXT COLOR)
    } else if((Color == vec4(170/255.0, 170/255.0, 170/255.0, Color.a) &&
        Position.z == 0.03 && Position.y < 300.0) || (Color == vec4(42/255.0, 42/255.0, 42/255.0, Color.a) && Position.z == 0 && Position.y < 300.0)) {
        vertexColor.a = 0.0;
    }
}
