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
import tk.jomp16.irc.plugin.listeners.join.JoinListener

class JoinHandler : IHandler {
    override fun handle(ircManager: IrcManager, ircParserData: IrcParserData) {
        // :Shinpachi-kun!~shinpachi@A92D18A2.267422C7.234FB82F.IP JOIN :#jomp16-bot
        // IrcParserData(server=false, raw=Shinpachi-kun, user=~shinpachi, host=A92D18A2.267422C7.234FB82F.IP, tags={}, command=JOIN, params=[#jomp16-bot])

        val channel = ircManager.channelList.getOrAddChannel(ircManager, ircParserData.params[0])

        channel.addUser(ircParserData.user)

        ircManager.eventBus.publishAsync(JoinListener(ircManager, ircParserData.user, channel))
    }
}