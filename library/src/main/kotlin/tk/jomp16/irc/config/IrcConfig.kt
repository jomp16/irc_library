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

package tk.jomp16.irc.config

import java.util.concurrent.TimeUnit

class IrcConfig {
    /**
     * Milliseconds to wait with no data from the IRC server before sending
     * a PING request to check if the socket is still alive, default 1
     * minutes (1000x60x1=60,000 milliseconds)
     */
    var socketTimeout = TimeUnit.MINUTES.toMillis(1)

    // User
    var name: String = ""
    var ident: String = ""
    var realName: String = ""

    // NickServ
    var nickServPassword: String = ""

    // IRC
    var server: String = ""
    var port: Int = 6667
    var serverPassword: String = ""

    // SASL
    var sasl: Boolean = false
    var saslUser: String = ""
    var saslPassword: String = ""

    // SSL
    var ssl: Boolean = false
    var trustAllSsl: Boolean = false

    var delay: Int = 1000

    var joinChannels: Array<String> = arrayOf()
}