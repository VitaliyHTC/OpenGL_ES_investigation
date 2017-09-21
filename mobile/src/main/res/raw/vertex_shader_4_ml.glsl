uniform highp mat4 u_ModelMatrix;
uniform highp mat4 u_ViewMatrix;
uniform highp mat4 u_ProjectionMatrix;
//uniform highp mat4 u_TransposeInverseModelMatrix;

attribute vec4 a_Position;
attribute vec2 a_TexCoord;
attribute vec3 a_Normal;

//varying lowp vec4 frag_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

void main(void) {
    frag_TexCoord = a_TexCoord;

    //frag_Normal = mat3(transpose(inverse(u_ModelMatrix))) * a_Normal;
    frag_Normal = (u_ModelMatrix * vec4(a_Normal, 0.0)).xyz;
    //frag_Normal = mat3(u_TransposeInverseModelMatrix) * a_Normal;

    frag_Position = (u_ModelMatrix * a_Position).xyz;

    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * a_Position;
}