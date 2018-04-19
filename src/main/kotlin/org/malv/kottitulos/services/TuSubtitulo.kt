package org.malv.kottitulos.services

import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.pad
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class TuSubtitulo : SubtitleService {

    lateinit var database: Connection

    init {


        createConnection()
    }


    private fun findShow(name: String): Int? {

        val id = findCache(name)

        if (id == null)
            updateCache()

        return id ?: findCache(name)

    }


    private fun findCache(name: String): Int? {

        val statement = database.prepareStatement("SELECT id FROM tusubtitulo WHERE name = ?")
        statement.setString(1, name)

        val resultSet = statement.executeQuery()

        if (resultSet.next())
            return resultSet.getInt("id")

        return null

    }

    private fun updateCache() {

        database.prepareStatement("DELETE FROM tusubtitulo").execute()
        val statement = database.prepareStatement("INSERT INTO tusubtitulo (name, id) VALUES (?, ?)")

        Jsoup.connect("https://www.tusubtitulo.com/series.php")
                .get()
                .select("[href^=/show/]")
                .forEach {
                    statement.setString(1, it.text().toLowerCase())
                    statement.setString(2, it.attr("href").substringAfterLast("/"))
                    statement.addBatch()
                }

        statement.executeBatch()
    }


    private fun createConnection() {

        Class.forName("org.sqlite.JDBC")

        val directory = File("${System.getProperty("user.home")}${File.separator}.kottitulos")
        directory.mkdirs()

        val file = directory.absolutePath  + File.separator + "tusubtitulo.db"
        database = DriverManager.getConnection("jdbc:sqlite:$file")

        val statement = database.prepareStatement("CREATE TABLE IF NOT EXISTS tusubtitulo (name TEXT, id INTEGER)")
        statement.execute()
    }

    override fun find(episode: Episode): String? {


        val show = findShow(episode.show) ?: return null

        val response = Jsoup.connect("https://www.tusubtitulo.com/ajax_loadShow.php")
                .data("show", "$show")
                .data("season", "${episode.season}")
                .header("X-Requested-With", "XMLHttpRequest")
                .execute()

        val cookies = response.cookies()
        val episodes = response
                .parse().select("table:contains(${episode.season}x${episode.episode.pad(2)})")


        if (episodes.isEmpty()) return null


        val versions = episodes.select("tr + tr")

        val version = versions.indexOfFirst { it.text().contains(episode.group, true) }

        if (version == -1) return null


        for (i in version + 1 until versions.size) {
            if (versions[i].text().contains("Español") && versions[i].select("a").isNotEmpty()) {
                val url = versions[i].select("a").first().absUrl("href")

                return Jsoup.connect(url)
                        .referrer("https://www.tusubtitulo.com/show/$show")
                        .cookies(cookies)
                        .execute()
                        .charset("ISO-8859-1")
                        .body()

            }

            if (versions[i].text().contains("Versión"))
                return null
        }


        return null

    }


}