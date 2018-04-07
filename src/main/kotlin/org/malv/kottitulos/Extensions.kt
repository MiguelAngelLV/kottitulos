package org.malv.kottitulos

fun Int.pad(length: Int): String {

    return "${this}".padStart(length, '0')

}