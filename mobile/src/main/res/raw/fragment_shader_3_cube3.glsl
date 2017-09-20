precision mediump float;

uniform sampler2D u_Texture;

varying lowp vec4 frag_Color;
varying lowp vec2 frag_TexCoord;
varying lowp vec3 frag_Normal;
varying lowp vec3 frag_Position;

struct Material {
    vec3 AmbientIntensity;
    vec3 DiffuseIntensity;
    vec3 SpecularIntensity;
    float Shininess;
};

uniform Material u_Material;

struct Light {
    vec3 Color;
    vec3 Direction;
};

uniform Light u_Light;

void main() {

    // Ambient
    lowp vec3 AmbientColor = u_Light.Color * u_Material.AmbientIntensity;

    // Diffuse
    lowp vec3 Normal = normalize(frag_Normal);
    lowp float DiffuseFactor = max(-dot(Normal, u_Light.Direction), 0.0);
    lowp vec3 DiffuseColor = u_Light.Color * u_Material.DiffuseIntensity * DiffuseFactor;

    // Specular
    lowp vec3 Eye = normalize(frag_Position);
    lowp vec3 Reflection = reflect(u_Light.Direction, Normal);
    lowp float SpecularFactor = pow(max(0.0, -dot(Reflection, Eye)), u_Material.Shininess);
    lowp vec3 SpecularColor = u_Light.Color * u_Material.SpecularIntensity * SpecularFactor;

    gl_FragColor = texture2D(u_Texture, frag_TexCoord) * vec4((AmbientColor + DiffuseColor + SpecularColor), 1.0);
}