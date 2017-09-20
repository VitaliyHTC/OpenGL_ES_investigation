precision mediump float;

uniform sampler2D u_Texture;

varying lowp vec4 frag_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

struct Material {
    sampler2D DiffuseMap;
    sampler2D SpecularMap;
    float Shininess;
};

uniform Material u_Material;

struct Light {
    vec3 Color;
    vec3 Direction;
};

uniform Light u_Light;

uniform float u12;
uniform float u13;
uniform float u14;
uniform float u15;
uniform float u16;
uniform float u17;
uniform float u18;
uniform float u19;
uniform float u20;
uniform float u21;
uniform float u22;

void main() {

    // Ambient
    lowp vec3 AmbientColor = u_Light.Color * vec3(texture2D(u_Material.DiffuseMap, frag_TexCoord));

    // Diffuse
    lowp vec3 Normal = normalize(frag_Normal);
    lowp float DiffuseFactor = max(-dot(Normal, u_Light.Direction), 0.0);
    lowp vec3 DiffuseColor = u_Light.Color * DiffuseFactor * vec3(texture2D(u_Material.DiffuseMap, frag_TexCoord));

    // Specular
    lowp vec3 Eye = normalize(frag_Position);
    lowp vec3 Reflection = reflect(u_Light.Direction, Normal);
    lowp float SpecularFactor = pow(max(0.0, -dot(Reflection, Eye)), u_Material.Shininess);
    lowp vec3 SpecularColor = u_Light.Color * SpecularFactor * vec3(texture2D(u_Material.SpecularMap, frag_TexCoord));

    gl_FragColor = vec4((AmbientColor + DiffuseColor + SpecularColor), 1.0);
}