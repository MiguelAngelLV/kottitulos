package org.malv.kottitulos

data class Episode(val show: String, val episode: Int, val season: Int, val group: String, val filename: String) {


    companion object {
        val ID = Regex("""s(\d{2})e(\d{2})""")



        fun create(filename: String) : Episode? {


            val name = filename.substringAfterLast('/')
                    .substringBeforeLast('.')
                    .replace('.', ' ')
                    .toLowerCase()

            val result = ID.find(name) ?: return null

            val id = result.groupValues[0]
            val season = result.groupValues[1].toInt()
            val episode = result.groupValues[2].toInt()

            val show = name.substringBefore(id)
                    .trim()



            val group = name.substringAfterLast('-')


            return Episode(show = show,
                    season = season,
                    episode =  episode,
                    group = group,
                    filename = filename.substringBeforeLast("."))

        }


    }

}