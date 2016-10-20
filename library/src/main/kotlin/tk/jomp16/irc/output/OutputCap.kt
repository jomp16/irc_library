/*
 * Copyright (C) 2016 jomp16
 *
 * This file is part of irc_library.
 *
 * irc_library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * irc_library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with irc_library. If not, see <http://www.gnu.org/licenses/>.
 */

package tk.jomp16.irc.output

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcManager
import java.util.*

class OutputCap(private val ircManager: IrcManager) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private var alreadySentEnd: Boolean = true

    val capabilities: MutableList<String> = mutableListOf()
    val requestedCapabilities: MutableList<String> = LinkedList()

    fun sendList() {
        ircManager.outputRaw.writeRaw("CAP LS", true)

        alreadySentEnd = false
    }

    fun sendRequest(capRequested: String) {
        if (requestedCapabilities.contains(capRequested)) return

        requestedCapabilities.add(capRequested)

        ircManager.outputRaw.writeRaw("CAP REQ :$capRequested")

        alreadySentEnd = false
    }

    fun sendEnd() {
        if (requestedCapabilities.isNotEmpty()) {
            log.error("I must wait to remove all requested capabilities, remaining ones is {}", requestedCapabilities)

            return
        }

        if (!alreadySentEnd) {
            ircManager.outputRaw.writeRaw("CAP END", true)

            alreadySentEnd = true
        }
    }
}