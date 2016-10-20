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
import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.config.IrcConfig
import tk.jomp16.irc.modes.Mode
import tk.jomp16.irc.parser.IrcParser

class ModeHandlerTest {
    @Test
    fun handle() {
        val raw1 = ":ChanServ!service@rizon.net MODE #jomp16-bot +qo Shinpachi-kun Shinpachi-kun"
        val raw2 = ":ChanServ!service@rizon.net MODE #jomp16-bot -o Shinpachi-kun"
        val raw3 = ":Shinpachi-kun!~shinpachi@A628EC77.57E8531E.F6BF86DB.IP MODE Shinpachi-kun :+r"

        val ircParserData1 = IrcParser.parse(raw1)
        val ircParserData2 = IrcParser.parse(raw2)
        val ircParserData3 = IrcParser.parse(raw3)

        Assert.assertNotNull(ircParserData1)
        Assert.assertNotNull(ircParserData2)
        Assert.assertNotNull(ircParserData3)

        val ircManager = IrcManager(IrcConfig().apply { test = true })
        val modeHandler = ModeHandler()

        val channel1 = ircManager.channelList.getOrAddChannel("#jomp16-bot")
        val channel2 = ircManager.channelList.getOrAddChannel("Shinpachi-kun")

        channel1.addUser("Shinpachi-kun")
        channel2.addUser("Shinpachi-kun")

        modeHandler.handle(ircManager, ircParserData1)
        Assert.assertEquals(listOf(Mode.CHANNEL_OWNER, Mode.CHANNEL_FULL_OP), channel1.getOrAddModesUser("Shinpachi-kun"))

        modeHandler.handle(ircManager, ircParserData2)
        Assert.assertEquals(listOf(Mode.CHANNEL_OWNER), channel1.getOrAddModesUser("Shinpachi-kun"))

        modeHandler.handle(ircManager, ircParserData3)
        Assert.assertEquals(listOf(Mode.USER_RESTRICTED), channel2.getOrAddModesUser("Shinpachi-kun"))
    }
}