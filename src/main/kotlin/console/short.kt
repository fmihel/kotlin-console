package fmihel.console

fun short(a: Array<Int>): String {
    return "{${a.count()}} " + a.joinToString(",", "[", "]", 5, "..")
}

fun short(a: IntArray): String {
    return "{${a.count()}} " + a.joinToString(",", "[", "]", 5, "..")
}

fun short(a: CharArray): String {
    return "{${a.count()}} " + a.joinToString(",", "[", "]", 5, "..")
}

fun short(a: String): String {
    return if (a.length > 30) a.take(28) + "..{" + a.length.toString() + "}" else a
}
