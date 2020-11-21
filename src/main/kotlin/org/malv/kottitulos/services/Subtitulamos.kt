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

        val id = show["id"] as Long


        val season = Jsoup.connect("https://www.subtitulamos.tv/shows/$id/season/${episode.season}")
                .get()


        val subtitles = season.select(".episode:contains(${episode.season}x${episode.episode.pad(2)})")


        val versions = subtitles.select(".subtitle-language:contains(Español) ~ .subtitle .sub")

        val groupVersions = versions.filter {
            it.select(".version-name").text().containsAny(episode.groups, true)  && it.select("a[href*=subtitles]").isNotEmpty()
        }

        if (groupVersions.isEmpty())
            return null


        val url = groupVersions.first().select("a[href*=subtitles]").first().absUrl("href")

        return Jsoup.connect(url).ignoreContentType(true).execute().charset("ISO-8859-1").body()
    }


}
