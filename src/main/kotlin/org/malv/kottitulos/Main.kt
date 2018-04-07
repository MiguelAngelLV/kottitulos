package org.malv.kottitulos

import org.malv.kottitulos.services.Subtitulamos
import org.malv.kottitulos.services.TuSubtitulo


fun main(args: Array<String>) {


    val downloader = SubtitleDownloader()

    downloader.addService(Subtitulamos())
    downloader.addService(TuSubtitulo())




    args.forEach { file ->
        println("Analyzing $file")

        val episode = Episode.create(file)

        when {
            episode == null -> println("Invalid filename")
            downloader.find(episode) -> println("Subtitle downloaded")
            else -> println("Subtitle not found")
        }
    }


}