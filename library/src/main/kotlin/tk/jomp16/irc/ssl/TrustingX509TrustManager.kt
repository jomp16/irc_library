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

package tk.jomp16.irc.ssl

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class TrustingX509TrustManager : X509TrustManager {
    override fun checkClientTrusted(cert: Array<out X509Certificate>, authType: String) {
    }

    override fun checkServerTrusted(cert: Array<out X509Certificate>, authType: String) {
    }

    override fun getAcceptedIssuers(): Array<out X509Certificate> = arrayOf()
}