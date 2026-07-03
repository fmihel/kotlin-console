package fmihel.console

fun translate(y: Int, y1: Int, y2: Int, x1: Int, x2: Int): Int {
    if (y2 - y1 == 0) {
        return 0
    }
    return (x2 * (y - y1) + x1 * (y2 - y)) / (y2 - y1)
}
