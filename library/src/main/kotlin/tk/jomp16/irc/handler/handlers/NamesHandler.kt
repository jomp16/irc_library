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

import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.channel.ChannelList
import tk.jomp16.irc.handler.IHandler
import tk.jomp16.irc.modes.Mode
import tk.jomp16.irc.parser.IrcParserData
import tk.jomp16.irc.user.UserList

class NamesHandler : IHandler {
    override fun handle(ircManager: IrcManager?, userList: UserList, channelList: ChannelList,
                        ircParserData: IrcParserData) {
        val channel = channelList.getOrAddChannel(ircParserData.params[2])
        val users = ircParserData.params[3].split(' ')

        users.forEach { userString ->
            val modes = Mode.getModes(userString, false)
            val user = userList.getOrAddUser(userString.substring(modes.size))

            channel.addUser(user)

            modes.forEach { channel.changeModeUser(user, it, true) }
        }
    }
}