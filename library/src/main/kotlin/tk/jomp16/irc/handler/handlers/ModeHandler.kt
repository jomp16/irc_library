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

import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.handler.IHandler
import tk.jomp16.irc.modes.Mode
import tk.jomp16.irc.parser.IrcParserData

class ModeHandler : IHandler {
    private var firstMode: Boolean = true

    override fun handle(ircManager: IrcManager, ircParserData: IrcParserData) {
        // :Shinpachi-kun!~shinpachi@A628EC77.57E8531E.F6BF86DB.IP MODE Shinpachi-kun :+r
        // IrcParserData(raw=:Shinpachi-kun!~shinpachi@A628EC77.57E8531E.F6BF86DB.IP MODE Shinpachi-kun :+r, user=User(nick='Shinpachi-kun', user='~shinpachi', host='A628EC77.57E8531E.F6BF86DB.IP'), tags={}, command=MODE, params=[Shinpachi-kun, +r])

        // :ChanServ!service@rizon.net MODE #jomp16-bot +qo Shinpachi-kun Shinpachi-kun
        // IrcParserData(raw=:ChanServ!service@rizon.net MODE #jomp16-bot +qo Shinpachi-kun Shinpachi-kun, user=User(raw='ChanServ', user='service', host='rizon.net'), tags={}, command=MODE, params=[#jomp16-bot, +qo, Shinpachi-kun, Shinpachi-kun])

        // :jomp16!~jomp16@fire.lord.jomp16 MODE #jomp16-bot -oo jomp16 Shinpachi-kun
        // IrcParserData(raw=:jomp16!~jomp16@fire.lord.jomp16 MODE #jomp16-bot -oo jomp16 Shinpachi-kun, user=User(nick='jomp16', user='~jomp16', host='fire.lord.jomp16'), tags={}, command=MODE, params=[#jomp16-bot, -oo, jomp16, Shinpachi-kun])

        if (firstMode) {
            // join after first IRC mode, which is always given by IRC server (probably?)

            firstMode = false

            ircManager.ircConfig.joinChannels.forEach { ircManager.outputIrc.joinChannel(it) }
        }

        val channel = ircManager.channelList.getOrAddChannel(ircManager, ircParserData.params[0])
        val modesRaw = ircParserData.params[1]
        val modes = Mode.getModes(modesRaw.substring(1), userMode = ircParserData.params.size <= 2)
        val newMode = modesRaw.startsWith('+')

        if (ircParserData.params.size > 2) {
            modes.forEachIndexed { i, mode ->
                val user = ircManager.userList.getOrAddUser(ircParserData.params[2 + i])

                channel.addUser(user)
                channel.changeModeUser(user, mode, newMode)
            }
        } else {
            modes.forEach {
                val user = ircManager.userList.getOrAddUser(ircParserData.params[0])

                channel.addUser(user)
                channel.changeModeUser(user, it, newMode)
            }
        }
    }
}