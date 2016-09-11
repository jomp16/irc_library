/*
 * Copyright (C) 2016 jomp16
 *
 * This file is part of irc_library.
 *
 * irc_library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * irc_library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with irc_library. If not, see <http://www.gnu.org/licenses/>.
 */

package tk.jomp16.irc.handler.handlers

import org.junit.Assert
import org.junit.Test
import tk.jomp16.irc.channel.ChannelList
import tk.jomp16.irc.modes.Mode
import tk.jomp16.irc.parser.IrcParser
import tk.jomp16.irc.user.UserList

class NamesHandlerTest {
    @Test
    fun handle() {
        val string = ":weber.freenode.net 353 Shinpachi-kun = #jomp16-bot :Shinpachi-kun @+jomp16 @ChanServ"

        val ircParserData = IrcParser.parse(string)

        Assert.assertNotNull(ircParserData)

        val userList = UserList()
        val channelList = ChannelList()
        val namesHandler = NamesHandler()

        namesHandler.handle(null, userList, channelList, ircParserData)

        channelList.channels.values.forEach { channel ->
            userList.users.entries.forEach { userEntry ->
                when (userEntry.key) {
                    "Shinpachi-kun" -> {
                        Assert.assertEquals("Shinpachi-kun", userEntry.value.nick)
                        Assert.assertEquals(channel.getOrAddModesUser(userEntry.value.nick), listOf<Mode>())
                    }
                    "@+jomp16"      -> {
                        Assert.assertEquals("jomp16", userEntry.value.nick)
                        Assert.assertEquals(channel.getOrAddModesUser(userEntry.value.nick), listOf(Mode.CHANNEL_FULL_OP, Mode.CHANNEL_VOICE))
                    }
                    "@ChanServ"     -> {
                        Assert.assertEquals("ChanServ", userEntry.value.nick)
                        Assert.assertEquals(channel.getOrAddModesUser(userEntry.value.nick), listOf(Mode.CHANNEL_FULL_OP))
                    }
                }
            }
        }
    }
}