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
import tk.jomp16.irc.parser.IrcParserData
import tk.jomp16.irc.plugin.listeners.part.PartListener

class PartHandler : IHandler {
    override fun handle(ircManager: IrcManager, ircParserData: IrcParserData) {
        // :jomp16!~jomp16@fire.lord.jomp16 PART #jomp16-bot :Leaving
        // IrcParserData(raw=:jomp16!~jomp16@fire.lord.jomp16 PART #jomp16-bot :Leaving, user=User(raw='jomp16!~jomp16@fire.lord.jomp16', nick='jomp16', user='~jomp16', host='fire.lord.jomp16'), tags={}, command=PART, params=[#jomp16-bot, Leaving])

        val channel = ircManager.channelList.getOrAddChannel(ircManager, ircParserData.params[0])
        val reason = ircParserData.params[1]

        channel.removeUser(ircParserData.user)

        ircManager.eventBus.publishAsync(PartListener(ircManager, ircParserData.user, channel, reason))
    }
}