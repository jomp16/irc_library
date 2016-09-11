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

package tk.jomp16.irc.output

import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.channel.Channel

class OutputIrc(val ircManager: IrcManager) {
    fun sendNotice(target: String, message: String, ctcp: Boolean = false) = ircManager.outputRaw.writeRaw("NOTICE $target :${if (ctcp) "\u0001" else ""}", message, if (ctcp) "\u0001" else "")

    fun sendPrivateMessage(target: Channel, message: String, ctcp: Boolean = false) = sendPrivateMessage(target.name, message, ctcp)

    fun sendPrivateMessage(target: String, message: String, ctcp: Boolean = false) = ircManager.outputRaw.writeRaw("PRIVMSG $target :${if (ctcp) "\u0001" else ""}", message, if (ctcp) "\u0001" else "")

    fun joinChannel(channel: String) = ircManager.outputRaw.writeRaw("JOIN $channel")
}