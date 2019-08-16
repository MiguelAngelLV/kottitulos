package org.malv.kottitulos

import org.malv.kottitulos.services.Subdivx
import org.malv.kottitulos.services.Subtitulamos
import org.malv.kottitulos.services.TuSubtitulo


fun main(args: Array<String>) {


    val downloader = SubtitleDownloader()

    downloader.addService(Subtitulamos())
    downloader.addService(TuSubtitulo())
    downloader.addService(Subdivx())


    var downloads = 0

    val options = args.filter { it.startsWith("-") }
    val files = args.filterNot { it.startsWith("-") }
    verbose = options.contains("-v")


    files.forEach { file ->

        val episode = Episode.create(file)
        println("Analyzing ${file.substringAfterLast("/")}: ")


        when {
            episode == null -> println("Invalid filename")
            downloader.find(episode) -> { println("Subtitle downloaded"); downloads++ }
            else -> println("Subtitle not found")
        }
    }

    println("Download $downloads/${files.size}")


}