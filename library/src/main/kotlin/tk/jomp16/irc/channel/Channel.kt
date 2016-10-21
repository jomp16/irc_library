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

package tk.jomp16.irc.channel

import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.modes.Mode
import tk.jomp16.irc.user.User

class Channel(private val ircManager: IrcManager, val name: String) {
    val users: MutableList<String> = mutableListOf()
    private val userModes: MutableMap<String, MutableList<Mode>> = mutableMapOf()

    val usersObject: List<User>
        get() = ircManager.userList.users.filterKeys { key -> users.any { it == key } }.values.toList()

    fun addUser(nick: String) {
        if (!users.contains(nick)) users.add(nick)
        if (!userModes.containsKey(nick)) userModes.put(nick, mutableListOf())
    }

    fun addUser(user: User) {
        addUser(user.nick)
    }

    fun removeUser(nick: String) {
        if (!users.contains(nick)) throw Exception("No user $nick found on channel $name!")
        if (userModes.containsKey(nick)) userModes.remove(nick)

        users.remove(nick)
    }

    fun removeUser(user: User) {
        removeUser(user.nick)
    }

    fun changeModeUser(user: User, mode: Mode, newMode: Boolean) {
        getOrAddModesUser(user.nick).apply {
            if (newMode && !contains(mode)) add(mode)
            else if (contains(mode)) remove(mode)
        }
    }

    fun getOrAddModesUser(nick: String): MutableList<Mode> {
        if (!users.contains(nick)) throw Exception("No user $nick found on this channel!")
        if (!userModes.containsKey(nick)) throw Exception("No user modes for $nick found on this channel!")

        return userModes[nick]!!
    }

    override fun toString() = "Channel(name='$name', users=$users, userModes=$userModes)"
}