package org.malv.kottitulos.services

import org.jsoup.Jsoup
import org.malv.kottitulos.Episode
import org.malv.kottitulos.containsAny
import org.malv.kottitulos.pad
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class TuSubtitulo : SubtitleService {

    var cacheUpdated = false

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

        val statement = database.prepareStatement("SELECT id FROM tusubtitulo WHERE name LIKE ?")
        statement.setString(1, name)

        val resultSet = statement.executeQuery()

        if (resultSet.next())
            return resultSet.getInt("id")

        return null

    }

    private fun updateCache() {

        if (cacheUpdated) return

        cacheUpdated = true

        val query = database.prepareStatement("SELECT id FROM tusubtitulo ORDER BY id DESC LIMIT 1")

        val resultSet = query.executeQuery()

        val max = if (resultSet.next()) resultSet.getInt("id") else 0


        val statement = database.prepareStatement("INSERT INTO tusubtitulo (name, id) VALUES (?, ?)")

        Jsoup.connect("https://www.tusubtitulo.com/series.php")
                .get()
                .select("[href^=/show/]")
                .forEach {

                    statement.setString(1, it.text()
                            .replace("(", "")
                            .replace(")", "")
                            .trim().toLowerCase())

                    val id = it.attr("href").substringAfterLast("/").toInt()
                    statement.setInt(2, id)

                    if (id > max)
                        statement.addBatch()
                }

        statement.executeBatch()
    }


    private fun createConnection() {

        Class.forName("org.sqlite.JDBC")

        val directory = File("${System.getProperty("user.home")}${File.separator}.kottitulos")
        directory.mkdirs()

        val file = directory.absolutePath + File.separator + "tusubtitulo.db"
        database = DriverManager.getConnection("jdbc:sqlite:$file")

        val statement = database.prepareStatement("CREATE TABLE IF NOT EXISTS tusubtitulo (name TEXT, id INTEGER)")
        statement.execute()


    }

    override fun find(episode: Episode): String? {


        val show = findShow(episode.show) ?: return null

        val response = Jsoup.connect("https://www.tusubtitulo.com/season/$show/${episode.season}")
                .execute()

        val cookies = response.cookies()
        val fragment = response.parse().selectFirst("table:contains(${episode.season}x${episode.episode.pad(2)})")
                ?: return null
        val versions = fragment.select("tr + tr")

        val indexVersions = ArrayList<Int>()
        versions.forEachIndexed { index, it ->
            if (it.text().containsAny(episode.groups, true))
                indexVersions.add(index)
        }

        if (indexVersions.isEmpty()) return null


        val code = fragment.selectFirst(".NewsTitle a").absUrl("href").substringAfter("episodes/").substringBefore("/")


        return null

       /* val url = "https://www.tusubtitulo.com/updated/5/${code}/${version}"

        return Jsoup.connect(url)
                .referrer("https://www.tusubtitulo.com/show/$show")
                .cookies(cookies)
                .execute()
                .charset("ISO-8859-1")
                .body()*/
    }


}
