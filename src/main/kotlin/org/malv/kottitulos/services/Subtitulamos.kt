package org.malv.kottitulos.services

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.containsAny


class Subtitulamos : SubtitleService {



    override fun find(episode: Episode): String? {

        val query = Jsoup.connect("https://www.subtitulamos.tv/search/query")
                .data("q", "${episode.show} ${episode.season}x${episode.episode}")
                .ignoreContentType(true)
                .execute().body()


        val shows = JSONParser().parse(query) as JSONArray

        if (shows.isEmpty())
            return null

        val show = shows.first() as JSONObject
        val episodes = show["episodes"] as JSONArray

        if (episodes.isEmpty())
            return null

        val result = episodes.first() as JSONObject

        val id = result["id"] as Long


        val details = Jsoup.connect("https://www.subtitulamos.tv/episodes/$id")
                .get()
                .select("#subtitle_details > div")

        val spanish = details.indexOfFirst { it.text().contains("Español") }

        if (spanish == -1)
            return null

        val versions = details.subList(spanish, details.size - 1)

        val groupVersions = versions.filter {
            it.select(".version_name").text().containsAny(episode.groups, true)
            && it.select("a[href$=download]").isNotEmpty()
        }

        if (groupVersions.isEmpty())
            return null


        val url = groupVersions.first().select("a[href$=download]").first().absUrl("href")

        return Jsoup.connect(url).ignoreContentType(true).execute().charset("ISO-8859-1").body()
    }



}