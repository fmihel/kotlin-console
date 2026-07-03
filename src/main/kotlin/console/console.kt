package fmihel.console

data class ConsoleOutProp(
        val indent: Int = 1,
        val color: String = "common",
        val pref: String = "out: ",
        val colorPref: String = "yellow",
)

object console {
    private const val RESET = "\u001B[0m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val RED = "\u001B[31m"
    private const val WHITE = "\u001B[97m"
    private const val PURPLE = "\u001B[35m"
    var tab = "    "
    var line = "-"
    var line_len = 30
    var group_mark_end = false

    fun configure(block: console.() -> Unit) {
        this.block()
    }

    // Используем простой String вместо data class, так как имя — это единственное свойство
    private val _begin = mutableListOf<String>()

    // Вычисляемое свойство вместо функции off()
    private val indent: String
        get() = tab.repeat(_begin.size)

    fun log(vararg args: Any?) {
        // joinToString объединяет элементы через пробел, как в JS
        println("$indent${args.joinToString(" ")}")
    }

    operator fun invoke(vararg args: Any?) {
        log(*args)
    }

    fun debug(vararg args: Any?) {
        println("$GREEN$indent${args.joinToString(" ")}$RESET")
    }

    // Перегрузка для Exception (выводим и сообщение, и стек, если нужно)
    fun error(e: Throwable) {
        println("$RED$indent${e.localizedMessage ?: e.message}$RESET")
    }

    fun error(message: String) {
        println("$RED$indent$message$RESET")
    }

    fun warn(message: String) {
        println("$YELLOW$indent$message$RESET")
    }

    fun line() {
        val add: String = indent
        println(add + line.repeat(line_len - add.length))
    }

    fun head(caption: String) {
        val out = "$caption "
        val currentIndent = indent
        val remainingLen = (line_len - out.length - currentIndent.length).coerceAtLeast(0)
        println("$WHITE$currentIndent$out${line.repeat(remainingLen)}$RESET")
    }

    fun begin(name: String) {
        if (_begin.contains(name)) {
            // В Kotlin принято использовать стандартные исключения, например
            // IllegalArgumentException
            throw IllegalArgumentException("console.begin($name) already exists")
        }
        head(name)
        _begin.add(name)
    }

    fun end(name: String = "", markEnd: Boolean? = null) {
        // 1. Проверяем, есть ли вообще что закрывать
        if (_begin.isEmpty()) {
            throw IllegalStateException(
                    "Structure violation: console.end($name) called, but no console.begin() is active"
            )
        }

        val lastOpen = _begin.last()

        // 2. Если имя передано, проверяем, совпадает ли оно с последним открытым begin
        if (name.isNotEmpty() && lastOpen != name) {
            throw IllegalStateException(
                    "Structure violation: expected console.end('$lastOpen'), but got console.end('$name')"
            )
        }

        // 3. Если всё ок — удаляем из стека
        _begin.removeAt(_begin.lastIndex)
        if (markEnd ?: group_mark_end) {
            head("end: $name")
        }
    }

    inline fun group(name: String, markEnd: Boolean? = null, block: () -> Unit) {
        begin(name)
        try {
            block() // Выполняем код внутри блока
        } finally {
            // end() вызовется гарантированно, даже если код внутри block() упадет с ошибкой
            end(name, markEnd)
        }
    }

    fun outProp(
            indent: Int = 1,
            color: String = "",
            pref: String = "out: ",
            colorPref: String = "yellow"
    ): ConsoleOutProp {
        return ConsoleOutProp(indent, color, pref, colorPref)
    }

    fun out(text: String, prop: ConsoleOutProp) {
        var pref: String
        if (prop.pref.length > 0) {
            pref = "${getColor(prop.colorPref)}${tab.repeat(prop.indent)}${prop.pref} $RESET"
        } else {
            pref = tab.repeat(prop.indent)
        }

        println("$pref${getColor(prop.color)}${text}$RESET")
    }

    private fun getColor(color: String = ""): String {
        when (color) {
            "red" -> return RED
            "white" -> return WHITE
            "yellow" -> return YELLOW
            "green" -> return GREEN
            "purple" -> return PURPLE
            "common" -> return RESET
        }
        return RESET
    }

    fun color(text: String, color: String): String {
        return getColor(color) + text + getColor("common")
    }

    fun progress(
            percent: Int = 100,
            passedChar: String = "|",
            remainedChar: String = "_",
            len: Int = 20
    ) {
        val count = translate(percent, 0, 100, 0, len)

        print("\r" + passedChar.repeat(count) + remainedChar.repeat(len - count))
    }
}
