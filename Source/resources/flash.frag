uniform sampler2D sprite;

uniform vec4 result_colour;

void main() {
    vec4 pixel_colour = texture2D(sprite, gl_TexCoord[0].xy);

    vec4 colour_diff = vec4(0, 0, 0, 1);

    //vec4 white = vec4(1, 1, 1, 1);

    colour_diff.r = result_colour.r - pixel_colour.r;

    colour_diff.g = result_colour.g - pixel_colour.g;

    colour_diff.b = result_colour.b - pixel_colour.b;

    pixel_colour.r = pixel_colour.r + colour_diff.r;

    pixel_colour.g = pixel_colour.g + colour_diff.g;

    pixel_colour.b = pixel_colour.b + colour_diff.b;

    gl_FragColor = pixel_colour;
}