package org.malv.kottitulos

import org.malv.kottitulos.services.SubtitleService
import java.io.FileWriter


class SubtitleDownloader {

    private val services = ArrayList<SubtitleService>()


    fun addService(service: SubtitleService) {
        services.add(service)
    }


    fun find(episode: Episode): Boolean {

        for (service in services) {

            val subtitle = service.find(episode)

            if (subtitle != null) {
                download(subtitle, episode.filename)
                return true
            }

        }

        return false

    }


    private fun download(subtitle: String, filename: String) {
        val file = FileWriter("$filename.srt")
        file.write(subtitle)
        file.close()
    }




}