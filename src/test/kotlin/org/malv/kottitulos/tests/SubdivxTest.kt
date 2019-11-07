package org.malv.kottitulos.tests

import junit.framework.Assert
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import org.junit.Test
import org.malv.kottitulos.Episode
import org.malv.kottitulos.services.Subdivx

class SubdivxTest {


    @Test
    fun `Find subtitles`() {

        val subdivx = Subdivx()

        val episode1 = Episode("the big bang theory", 3, 11, "dimension")

        val subtitle1 = subdivx.find(episode1) ?: return fail()

        assert(subtitle1.startsWith("1"))
        assert(subtitle1.contains('¿'))
        assert(subtitle1.contains('á'))


        val episode2 = Episode("the big bang theory", 3, 80, "dimension")

        val subtitle2 = subdivx.find(episode2)

        assertNull(subtitle2)


    }


    @Test
    fun `Find subtitles in zip`() {

        val subdivx = Subdivx()

        val episode1 = Episode("silicon valley", 1, 5, "tbs")

        val subtitle1 = subdivx.find(episode1) ?: return fail()

        assert(subtitle1.startsWith("1"))
        assert(subtitle1.contains('¿'))
        assert(subtitle1.contains('á'))


        val episode2 = Episode.create("silicon.valley.s06e01.proper.1080p.web.h264-tbs.mkv") ?: return fail()


        val subtitle2 = subdivx.find(episode2) ?: return fail()

        assert(subtitle1.startsWith("1"))
        assert(subtitle1.contains('¿'))
        assert(subtitle1.contains('á'))


    }



}