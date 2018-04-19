package org.malv.kottitulos.services

import com.github.junrar.Archive
import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.pad
import java.io.ByteArrayOutputStream
import java.io.File

class Subdivx : SubtitleService{



    override fun find(episode: Episode): String? {

        val document = Jsoup.connect("https://www.subdivx.com/index.php")
                .data("buscar", "${episode.show} S${episode.season.pad(2)}E${episode.episode.pad(2)}")
                .data("accion", "5")
                .data("masdesc", "")
                .data("subtitulos", "1")
                .data("realiza_b", "1")
                .get()


        document.select("#buscador_detalle")
                .filter { it.text().contains(episode.group, ignoreCase = true) }

                .forEach {

                    val downloads = it.select("a[target]")
                    val subtitule = unrar(downloads.first().absUrl("href"))

                    if (subtitule != null) return subtitule

        }


        return null
    }



    fun unrar(download: String) : String? {
        val temp = File.createTempFile("subtitle", ".rar")
        val rar = Jsoup.connect(download)
                .ignoreContentType(true)
                .execute()
                .bodyAsBytes()

        temp.writeBytes(rar)

        val unrar = Archive(temp)

        val subtitle = unrar.fileHeaders.firstOrNull { it.fileNameString.endsWith(".srt") } ?: return null


        val uncompressed = ByteArrayOutputStream()

        unrar.extractFile(subtitle, uncompressed)

        return uncompressed.toString("iso-8859-1")


    }

}