package org.malv.kottitulos.tests

import junit.framework.TestCase.*
import org.junit.Test
import org.malv.kottitulos.Episode
import org.malv.kottitulos.services.Subtitulamos

class SubtitulamosTest {


    @Test
    fun `Find subtitles`() {

        val subtitulamos = Subtitulamos()

        val episode1 = Episode("the big bang theory", 3, 11, "dimension")

        val subtitle1 = subtitulamos.find(episode1) ?: return fail()

        assert(subtitle1.startsWith("1"))
        assert(subtitle1.contains('¿'))
        assert(subtitle1.contains('á'))


        val episode2 = Episode("the big bang theory", 3, 80, "dimension")

        val subtitle2 = subtitulamos.find(episode2)

        assertNull(subtitle2)



        val episode3 = Episode.create("Superstore.S05E12.720p.HDTV.x264-AVS.mkv") ?: return fail()
        val subtitle3 = subtitulamos.find(episode3)

        assertNotNull(subtitle3)


    }


}
