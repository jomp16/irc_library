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

package tk.jomp16.irc.parser

import tk.jomp16.irc.user.User

object IrcParser {
    fun parse(raw: String): IrcParserData {
        var position: Int = 0

        val messageTags: MutableMap<String, String> = mutableMapOf()
        var user: User = User("")
        var command: String = ""
        val params: MutableList<String> = mutableListOf()

        // First thing is check for message tags
        if (raw[position] == '@') {
            // message tag found, pick them up and parse it
            position += 1

            val indexOfSpace = raw.indexOf(' ', startIndex = position)

            if (indexOfSpace != -1) {
                val messageTagsRaw = raw.substring(position, indexOfSpace)
                messageTags += messageTagsRaw.split(';').associate {
                    val split = it.split('=')

                    split[0] to if (split.size > 1) split[1] else ""
                }

                position = indexOfSpace + 1
            }
        }

        // And now check if raw contains any prefixes
        if (raw[position] == ':') {
            // prefix found, pick them up and parse it
            position += 1

            val indexOfSpace = raw.indexOf(' ', startIndex = position)

            if (indexOfSpace != -1) {
                val prefixRaw = raw.substring(position, indexOfSpace)

                user = User(prefixRaw, failToHost = true)

                position = indexOfSpace + 1
            }
        }

        // Now extract the command
        val indexOfSpace1 = raw.indexOf(' ', startIndex = position)

        if (indexOfSpace1 != -1) {
            command = raw.substring(position, indexOfSpace1)

            position = indexOfSpace1 + 1
        }

        // If there's something... It's the params
        while (position < raw.length) {
            val indexOfSpace = raw.indexOf(' ', startIndex = position)

            if (raw[position] == ':' || indexOfSpace == -1) {
                // trailing parameter or last string
                if (raw[position] == ':') position += 1

                params += raw.substring(position).trim()

                position = raw.length
            } else {
                params += raw.substring(position, indexOfSpace)

                position = indexOfSpace + 1
            }
        }

        return IrcParserData(raw, user, messageTags, command, params)
    }
}