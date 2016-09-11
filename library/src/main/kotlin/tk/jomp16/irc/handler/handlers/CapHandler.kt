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
import tk.jomp16.irc.channel.ChannelList
import tk.jomp16.irc.handler.IHandler
import tk.jomp16.irc.parser.IrcParserData
import tk.jomp16.irc.plugin.listeners.cap.CapACKListener
import tk.jomp16.irc.plugin.listeners.cap.CapLSListener
import tk.jomp16.irc.plugin.listeners.cap.CapNAKListener
import tk.jomp16.irc.user.UserList

class CapHandler : IHandler {
    override fun handle(ircManager: IrcManager?, userList: UserList, channelList: ChannelList,
                        ircParserData: IrcParserData) {
        if (ircManager == null) throw Exception("IrcManager is null! I only work on non null IrcManager!")

        val capabilities = ircParserData.params[2].split(' ')

        when (ircParserData.params[1]) {
            "LS"  -> ircManager.eventBus.publish(CapLSListener(ircManager, capabilities))
            "ACK" -> ircManager.eventBus.publish(CapACKListener(ircManager, capabilities))
            "NAK" -> ircManager.eventBus.publish(CapNAKListener(ircManager, capabilities))
        }

        if (ircManager.outputCap.requestedCapabilities.isEmpty()) ircManager.outputCap.sendEnd()
    }
}