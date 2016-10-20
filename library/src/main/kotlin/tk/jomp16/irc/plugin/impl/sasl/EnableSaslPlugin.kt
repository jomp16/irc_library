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

package tk.jomp16.irc.plugin.impl.sasl

import net.engio.mbassy.listener.Handler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcNumericConstants
import tk.jomp16.irc.plugin.impl.cap.EnableCapPlugin
import tk.jomp16.irc.plugin.listeners.cap.CapACKListener
import tk.jomp16.irc.plugin.listeners.unknown.UnknownListener
import javax.xml.bind.DatatypeConverter

class EnableSaslPlugin : EnableCapPlugin("sasl") {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Handler
    fun handleCapACKSASL(capACKListener: CapACKListener) {
        if (capACKListener.capabilities.contains("sasl") && capACKListener.ircManager.ircConfig.sasl && !capACKListener.ircManager.authenticated) {
            log.info("Requesting PLAIN authentication algorithm...")

            capACKListener.ircManager.outputCap.requestedCapabilities.add("sasl")

            capACKListener.ircManager.outputRaw.writeRaw("AUTHENTICATE PLAIN", true)
        }
    }

    @Handler
    fun handleUnknown(unknownListener: UnknownListener) {
        if (unknownListener.ircManager.ircConfig.sasl && !unknownListener.ircManager.authenticated) {
            when (unknownListener.ircParserData.command) {
                "AUTHENTICATE"                                                -> {
                    if (!unknownListener.ircManager.authenticated) {

                        if (unknownListener.ircManager.ircConfig.saslPassword.isBlank()) {
                            log.error("No password set for SASL!")
                        } else if (unknownListener.ircManager.ircConfig.sasl) {
                            log.info("Authenticating as {} with SASL", unknownListener.ircManager.ircConfig.saslUser)

                            unknownListener.ircManager.outputRaw.writeRaw(
                                    "AUTHENTICATE ${DatatypeConverter.printBase64Binary(
                                            (unknownListener.ircManager.ircConfig.saslUser +
                                                    "\u0000${unknownListener.ircManager.ircConfig.saslUser}" +
                                                    "\u0000${unknownListener.ircManager.ircConfig.saslPassword}")
                                                    .toByteArray(Charsets.UTF_8)
                                    )}", true)
                        }
                    }
                }
                IrcNumericConstants.RPL_LOGGEDIN.numericCommand.toString(),
                IrcNumericConstants.RPL_SASLSUCCESS.numericCommand.toString() -> {
                    if (!unknownListener.ircManager.authenticated && unknownListener.ircManager.ircConfig.sasl) {
                        unknownListener.ircManager.authenticated = true

                        log.info("Authenticated as {} with SASL!", unknownListener.ircManager.ircConfig.saslUser)

                        unknownListener.ircManager.outputCap.requestedCapabilities.remove("sasl")

                        unknownListener.ircManager.outputCap.sendEnd()
                    }
                }
                IrcNumericConstants.ERR_SASLFAIL.numericCommand.toString()    -> {
                    if (!unknownListener.ircManager.authenticated && unknownListener.ircManager.ircConfig.sasl) {
                        log.info("Couldn't authenticate as {} with SASL!", unknownListener.ircManager.ircConfig.saslUser)

                        unknownListener.ircManager.outputCap.requestedCapabilities.remove("sasl")

                        unknownListener.ircManager.outputCap.sendEnd()
                    }
                }
            }
        }
    }
}