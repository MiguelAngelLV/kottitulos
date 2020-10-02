package org.malv.kottitulos.tests

import junit.framework.TestCase.*
import org.junit.Test
import org.malv.kottitulos.Episode
import org.malv.kottitulos.services.TuSubtitulo

class TuSubtituloTest {


    @Test
    fun `Find subtitles`() {

        val tusubtitulo = TuSubtitulo()

        val episode1 = Episode.create("shameless.us.s10e04.1080p.web.h264-tbs.mkv")!!

        val subtitle1 = tusubtitulo.find(episode1) ?: return fail()


        assert(subtitle1.startsWith("1"))
        assert(subtitle1.contains('¿'))
        assert(subtitle1.contains('á'))

        val episode2 = Episode("the big bang theory", 3, 80, "dimension")

        val subtitle2 = tusubtitulo.find(episode2)

        assertNull(subtitle2)


        val episode3 = Episode.create("The.Middle.S09E22.Split.Decision.1080p.AMZN.WEB-DL.DDP5.1.H264-NTb.mkv") ?: throw Exception()


        val subtitle3 = tusubtitulo.find(episode3)

        assertNotNull(subtitle3)
    }


}