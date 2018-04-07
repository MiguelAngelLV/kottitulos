package org.malv.kottitulos.tests

import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import org.junit.Test
import org.malv.kottitulos.Episode
import org.malv.kottitulos.services.TuSubtitulo

class TuSubtituloTest {


    @Test
    fun `Find subtitles`() {

        val tusubtitulo = TuSubtitulo()

        val episode1 = Episode("the big bang theory", 3, 11, "dimension", "")

        val subtitle1 = tusubtitulo.find(episode1) ?: return fail()


        subtitle1.contains('¿')
        subtitle1.contains('á')

        val episode2 = Episode("the big bang theory", 3, 80, "dimension", "")

        val subtitle2 = tusubtitulo.find(episode2)

        assertNull(subtitle2)


    }


}