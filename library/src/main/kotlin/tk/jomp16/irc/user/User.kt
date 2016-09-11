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

package tk.jomp16.irc.user

class User {
    val nick: String
    val user: String
    val host: String

    val raw: String
        get() = "$nick${if (user.isNotEmpty()) "!$user" else ""}${if (host.isNotEmpty()) "@$host" else ""}"

    constructor(nick1: String, user1: String, host1: String) {
        nick = nick1
        user = user1
        host = host1
    }

    constructor(raw: String, failToHost: Boolean = false) {
        val nickIndex = raw.indexOf('!')
        val userIndex = raw.indexOf('@')

        nick = if (nickIndex == -1) if (failToHost) "" else raw else raw.substring(0, nickIndex)
        user = if (userIndex == -1) "" else raw.substring(nickIndex + 1, userIndex)
        host = if (userIndex == -1) if (!failToHost) "" else raw else raw.substring(userIndex + 1)
    }

    override fun toString(): String {
        return "User(raw='$raw', nick='$nick', user='$user', host='$host')"
    }
}