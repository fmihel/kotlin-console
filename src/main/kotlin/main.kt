import fmihel.console.*

fun main() {
    console.configure { group_mark_end = true }
    console.group("test color") {
        console("color: " + console.color("green", "green"))
        console("color: " + console.color("red", "red"))
    }
    console.group("debug ") { console.debug("draw debug info") }

    for (i in 0..100) {
        console.progress(i)
        Thread.sleep(100)
    }
}
