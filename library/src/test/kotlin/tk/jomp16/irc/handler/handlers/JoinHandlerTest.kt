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

package tk.jomp16.irc.handler.handlers

import org.junit.Assert
import org.junit.Test
import tk.jomp16.irc.dom.ircManager
import tk.jomp16.irc.parser.IrcParser

class JoinHandlerTest {
    @Test
    fun testJoin() {
        val raw = ":Shinpachi-kun!~shinpachi@A92D18A2.267422C7.234FB82F.IP JOIN :#jomp16-bot"
        val ircParserData = IrcParser.parse(raw)

        val ircManager = ircManager { test = true }
        val joinHandler = JoinHandler()

        joinHandler.handle(ircManager, ircParserData)

        Assert.assertEquals(mutableListOf("#jomp16-bot"), ircManager.channelList.channels.keys.toList())
        Assert.assertEquals(mutableListOf("Shinpachi-kun"), ircManager.channelList.getOrAddChannel("#jomp16-bot").users)
    }
}