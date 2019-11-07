package org.malv.kottitulos.services

import com.github.junrar.Archive
import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.containsAny
import org.malv.kottitulos.pad
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.zip.ZipInputStream


class Subdivx : SubtitleService {


    override fun find(episode: Episode): String? {

        val document = Jsoup.connect("https://www.subdivx.com/index.php")
                .data("buscar", "${episode.show} S${episode.season.pad(2)}E${episode.episode.pad(2)}")
                .data("accion", "5")
                .data("masdesc", "")
                .data("subtitulos", "1")
                .data("realiza_b", "1")
                .get()


        document.select("#buscador_detalle")
                .filter { it.text().containsAny(episode.groups, ignoreCase = true) }

                .forEach {

                    val downloads = it.previousElementSibling().selectFirst("a")
                    val subtitule = download(downloads.absUrl("href"))


                    if (subtitule != null) return subtitule

                }


        return null
    }


    fun download(download: String): String? {

        val url = Jsoup.connect(download).get()
                .selectFirst("a[href^=bajar]")
                .absUrl("href")


        val response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute()

        val data = response.bodyAsBytes()



        return when (response.contentType()) {
            "application/x-rar-compressed" -> unrar(data)
            "application/zip" -> unzip(data)
            else -> null
        }


    }


    fun unrar(data: ByteArray): String? {

        val temp = File.createTempFile("subtitle", ".rar")
        temp.writeBytes(data)

        val unrar = Archive(temp)

        val subtitle = unrar.fileHeaders.firstOrNull { it.fileNameString.endsWith(".srt") } ?: return null


        val uncompressed = ByteArrayOutputStream()

        unrar.extractFile(subtitle, uncompressed)

        return uncompressed.toString("iso-8859-1")


    }

    fun unzip(data: ByteArray): String? {
        val charset = Charset.forName("iso-8859-1")

        val temp = File.createTempFile("subtitle", ".zip")
        temp.writeBytes(data)

        val unzip = ZipInputStream(FileInputStream(temp), charset)

        var entry = unzip.nextEntry ?: return null

        while(!entry.name.endsWith(".srt"))
            entry = unzip.nextEntry ?: return null



        val bytes = ByteArray(1024)
        val uncompressed = ByteArrayOutputStream()

        var read = unzip.read(bytes)
        while (read > -1) {
            uncompressed.write(bytes, 0, read)
            read = unzip.read(bytes)
        }

        return uncompressed.toString("iso-8859-1")

    }
}