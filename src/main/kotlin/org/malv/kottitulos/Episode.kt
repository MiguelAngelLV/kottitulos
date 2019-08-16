package org.malv.kottitulos

class Episode(val show: String, val episode: Int, val season: Int, group: String? = null, val filename: String = "") {


    val groups = ArrayList<String>()

    init {
        if (group != null)
            groups.add(group)
    }


    companion object {
        val ID = Regex("""s(\d{2})e(\d{2})""")


        fun create(filename: String): Episode? {


            val name = filename.substringAfterLast('/')
                    .substringBeforeLast('.')
                    .replace('.', ' ')
                    .toLowerCase()

            val result = ID.find(name) ?: return null

            val id = result.groupValues[0]
            val season = result.groupValues[1].toInt()
            val episode = result.groupValues[2].toInt()

            val show = name.substringBefore(id).trim()




            val e = Episode(show = show,
                    season = season,
                    episode = episode,
                    filename = filename.substringBeforeLast("."))

            val group =  name.substringAfterLast('-')

            e.groups.add(group)


            if (name.contains("amzn"))
                e.groups.add("amzn")


            if (group == "strife")
                e.groups.add("ion10")


            return e

        }


    }

}