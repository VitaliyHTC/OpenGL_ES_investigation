uniform highp mat4 u_ModelMatrix;
uniform highp mat4 u_ViewMatrix;
uniform highp mat4 u_ProjectionMatrix;

attribute vec4 a_Position;
attribute vec2 a_TexCoord;
attribute vec3 a_Normal;

//varying lowp vec4 frag_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

void main(void) {
    frag_TexCoord = a_TexCoord;
    frag_Normal = (u_ViewMatrix * u_ModelMatrix * vec4(a_Normal, 0.0)).xyz;
    frag_Position = (u_ViewMatrix * u_ModelMatrix * a_Position).xyz;

    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * a_Position;
}