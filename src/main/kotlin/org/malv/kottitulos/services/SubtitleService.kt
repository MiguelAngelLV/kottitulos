package org.malv.kottitulos.services

import org.malv.kottitulos.Episode

interface SubtitleService {


    fun find(episode: Episode): String?

}