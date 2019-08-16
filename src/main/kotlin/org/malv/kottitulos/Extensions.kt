package org.malv.kottitulos

var verbose: Boolean = false

fun Int.pad(length: Int): String {

    return "${this}".padStart(length, '0')

}

fun Any.log(text: String) {

    if (verbose)
        println(text)

}


fun String.containsAny(list: List<String>, ignoreCase: Boolean = false): Boolean {

    return list.any { this.contains(it, ignoreCase) }
}