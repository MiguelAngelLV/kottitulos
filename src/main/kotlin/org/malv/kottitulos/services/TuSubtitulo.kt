package org.malv.kottitulos.services

import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.pad

class TuSubtitulo : SubtitleService {



    override fun find(episode: Episode): String? {


        val show = Jsoup.connect("https://www.tusubtitulo.com/series.php")
                .get()
                .select("[href^=/show/]")
                .firstOrNull { it.text().equals(episode.show, true) } ?: return null




        val showId = show.attr("href").substringAfterLast("/")

        val episodes = Jsoup.connect("https://www.tusubtitulo.com/ajax_loadShow.php")
                .data("show", showId)
                .data("season", "${episode.season}")
                .get()
                .select("table:contains(${episode.season}x${episode.episode.pad(2)})")


        if (episodes.isEmpty()) return null



        val versions = episodes.select("tr + tr")

        val version = versions.indexOfFirst { it.text().contains(episode.group, true) }

        if (version == -1) return null


        for (i in version+1 until versions.size) {
            if (versions[i].text().contains("Español") && versions[i].select("a").isNotEmpty())
                return Jsoup.connect(versions[i].select("a")
                        .first().absUrl("href"))
                        .execute()
                        .charset("ISO-8859-1")
                        .body()

            if (versions[i].text().contains("Versión"))
                return null
        }


        return null

    }



}