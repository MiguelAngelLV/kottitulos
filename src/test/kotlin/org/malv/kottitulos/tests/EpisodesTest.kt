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
        assertEquals("tbs", ep1.group)



        val ep2 = Episode.create("the.last.man.on.earth.s04e09.1080p.web.x264-tbs.mkv") ?: return fail()


        assertEquals("the last man on earth", ep2.show)
        assertEquals(4, ep2.season)
        assertEquals(9, ep2.episode)
        assertEquals("tbs", ep2.group)



        val ep3 = Episode.create("Final.Space.S01E06.Chapter.Six.1080p.AMZN.WEB-DL.DDP5.1.H.264-NTb.mkv") ?: return fail()

        assertEquals("final space", ep3.show)
        assertEquals(1, ep3.season)
        assertEquals(6, ep3.episode)
        assertEquals("amzn", ep3.group)



        val ep4 = Episode.create("future.man.s01e03.multi.1080p.hdtv.x264-hybris.mkv") ?: return fail()

        assertEquals("future man", ep4.show)
        assertEquals(1, ep4.season)
        assertEquals(3, ep4.episode)
        assertEquals("hybris", ep4.group)



        val ep5 = Episode.create("The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM.mkv") ?: return fail()

        assertEquals("the big bang theory", ep5.show)
        assertEquals(11, ep5.season)
        assertEquals(19, ep5.episode)
        assertEquals("plutonium", ep5.group)




        val ep6 = Episode.create("/path/to/file/The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM.mkv") ?: return fail()

        assertEquals("the big bang theory", ep6.show)
        assertEquals(11, ep6.season)
        assertEquals(19, ep6.episode)
        assertEquals("plutonium", ep6.group)
        assertEquals("/path/to/file/The.Big.Bang.Theory.S11E19.1080p.HDTV.x264-PLUTONiUM", ep6.filename)
    }


}