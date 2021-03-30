package org.malv.kottitulos.services

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.containsAny
import org.malv.kottitulos.pad


class Subtitulamos : SubtitleService {


    override fun find(episode: Episode): String? {

        val query = Jsoup.connect("https://www.subtitulamos.tv/search/query")
                .data("q", episode.show)
                .ignoreContentType(true)
                .execute().body()


        val shows = JSONParser().parse(query) as JSONArray

        if (shows.isEmpty())
            return null

        val show = shows.first() as JSONObject

        val id = show["show_id"] as Long


        val season = Jsoup.connect("https://www.subtitulamos.tv/shows/$id")
            .followRedirects(true)
            .get().select("#season-choices a")
            .firstOrNull { it.text() == "${episode.season}" } ?: return null



        val subtitles = Jsoup.connect(season.absUrl("href"))
            .get().select("#episode-choices a")
            .firstOrNull { it.text() == "${episode.episode}" } ?: return null


        val versions = Jsoup.connect(subtitles.absUrl("href"))
            .get().select(".language-container + .language-container .version-container")


        val groupVersions = versions.filter {
            it.select("p.bold").text().containsAny(episode.groups, true)
        }.flatMap {
            it.select(".version-buttons a[href$=download]")
        }


        val url = groupVersions.firstOrNull()?.absUrl("href") ?: return null

        return Jsoup.connect(url).ignoreContentType(true).execute().charset("ISO-8859-1").body()
    }


}
