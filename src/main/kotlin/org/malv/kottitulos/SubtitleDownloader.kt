package org.malv.kottitulos

import org.malv.kottitulos.services.SubtitleService
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter




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
        val file = BufferedWriter(OutputStreamWriter(FileOutputStream("$filename.srt"), "ISO-8859-1"))
        file.write(subtitle)
        file.close()
    }




}