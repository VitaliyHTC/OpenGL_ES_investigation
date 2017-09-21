uniform highp mat4 u_ModelMatrix;
uniform highp mat4 u_ViewMatrix;
uniform highp mat4 u_ProjectionMatrix;

attribute vec3 a_Position;
attribute vec2 a_TexCoord;

varying lowp vec2 frag_TexCoord;

void main() {
    frag_TexCoord = a_TexCoord;
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}