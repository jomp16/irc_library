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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.channel.ChannelList
import tk.jomp16.irc.handler.IHandler
import tk.jomp16.irc.parser.IrcParserData
import tk.jomp16.irc.user.UserList

class NoticeHandler : IHandler {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handle(ircManager: IrcManager?, userList: UserList, channelList: ChannelList,
                        ircParserData: IrcParserData) {
        if (ircManager == null) throw Exception("IrcManager is null! I only work on non null IrcManager!")

        if (ircParserData.user.nick.contains("NickServ", ignoreCase = true)) {
            if (ircParserData.params[1].contains("/msg NickServ identify", ignoreCase = true) && !ircManager.authenticated) {
                // authenticate with good ol' NickServ

                if (ircManager.ircConfig.nickServPassword.isBlank()) log.error("No password set for NickServ!")
                else ircManager.outputIrc.sendPrivateMessage("NickServ", "identify ${ircManager.ircConfig.nickServPassword}")
            } else if (ircParserData.params[1].contains("you are now", ignoreCase = true) && !ircManager.authenticated) {
                log.info("Authenticated as {}", ircManager.ircConfig.name)

                ircManager.authenticated = true
            } else if (ircParserData.params[1].contains("password incorrect", ignoreCase = true) && !ircManager.authenticated) {
                log.error("Couldn't authenticate as {}", ircManager.ircConfig.name)
            }

            return
        }
    }
}