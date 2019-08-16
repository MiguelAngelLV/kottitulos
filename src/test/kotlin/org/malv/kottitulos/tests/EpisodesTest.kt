package org.malv.kottitulos.tests

import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Test
import org.malv.kottitulos.Episode


class EpisodesTest {



    @Test
    fun `Analyze episode name`() {

        val ep1 = Episode.create("silicon.valley.s05e01.1080p.web.h264-tbs.mkv") ?: return fail()


        assertEquals("silicon valley", ep1.show)
        assertEquals(5, ep1.season)
        assertEquals(1, ep1.episode)
        assert(ep1.groups.contains("tbs"))



        val ep2 = Episode.create("the.last.man.on.earth.s04e09.1080p.web.x264-tbs.mkv") ?: return fail()


        assertEquals("the last man on earth", ep2.show)
        assertEquals(4, ep2.season)
        assertEquals(9, ep2.episode)
        assert(ep2.groups.contains("tbs"))



        val ep3 = Episode.create("Final.Space.S01E06.Chapter.Six.1080p.AMZN.WEB-DL.DDP5.1.H.264-NTb.mkv") ?: return fail()

        assertEquals("final space", ep3.show)
        assertEquals(1, ep3.season)
        assertEquals(6, ep3.episode)
        assert(ep3.groups.contains("amzn"))
        assert(ep3.groups.contains("ntb"))



        val ep4 = Episode.create("future.man.s01e03.multi.1080p.hdtv.x264-hybris.mkv") ?: return fail()

        assertEquals("future man", ep4.show)
        assertEquals(1, ep4.season)
        assertEquals(3, ep4.episode)
        assert(ep4.groups.contains("hybris"))



        val ep5 = Episode.create("The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM.mkv") ?: return fail()

        assertEquals("the big bang theory", ep5.show)
        assertEquals(11, ep5.season)
        assertEquals(19, ep5.episode)
        assert(ep5.groups.contains("plutonium"))




        val ep6 = Episode.create("/path/to/file/The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM.mkv") ?: return fail()

        assertEquals("the big bang theory", ep6.show)
        assertEquals(11, ep6.season)
        assertEquals(19, ep6.episode)
        assert(ep6.groups.contains("plutonium"))
        assertEquals("/path/to/file/The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM", ep6.filename)




        val ep7 = Episode.create("iZombie.S04E10.1080p.WEB.x264-METCON.mkv") ?: return fail()

        assertEquals("izombie", ep7.show)
        assertEquals(4, ep7.season)
        assertEquals(10, ep7.episode)
        assert(ep7.groups.contains("metcon"))
    }


}