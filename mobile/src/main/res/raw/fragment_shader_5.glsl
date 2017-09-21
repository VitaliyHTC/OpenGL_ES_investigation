precision mediump float;

varying mediump vec2 frag_TexCoord;

uniform sampler2D texture1;

void main() {
    gl_FragColor = texture2D(texture1, frag_TexCoord);
}