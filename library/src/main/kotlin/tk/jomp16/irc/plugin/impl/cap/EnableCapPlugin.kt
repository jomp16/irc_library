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

package tk.jomp16.irc.plugin.impl.cap

import net.engio.mbassy.listener.Handler
import org.slf4j.LoggerFactory
import tk.jomp16.irc.plugin.AbstractPlugin
import tk.jomp16.irc.plugin.listeners.cap.CapACKListener
import tk.jomp16.irc.plugin.listeners.cap.CapLSListener
import tk.jomp16.irc.plugin.listeners.cap.CapNAKListener

// Open to SASL Plugin
open class EnableCapPlugin(val capability: String) : AbstractPlugin() {
    private val log = LoggerFactory.getLogger(javaClass)

    @Handler(priority = Int.MAX_VALUE)
    fun handleCapLS(capLSListener: CapLSListener) {
        if (!capLSListener.capabilities.contains(capability)) return

        log.info("Requesting {}...", capability)

        capLSListener.ircManager.outputCap.sendRequest(capability)
    }

    @Handler(priority = Int.MAX_VALUE)
    fun handleCapACK(capACKListener: CapACKListener) {
        if (!capACKListener.capabilities.contains(capability)) return

        log.info("Requested capability {} successfully!", capability)

        capACKListener.ircManager.outputCap.capabilities.add(capability)
        capACKListener.ircManager.outputCap.requestedCapabilities.remove(capability)
    }

    @Handler(priority = Int.MAX_VALUE)
    fun handleCapNAK(capNAKListener: CapNAKListener) {
        if (!capNAKListener.capabilities.contains(capability)) return

        log.info("Removed capability {} successfully!", capability)

        capNAKListener.ircManager.outputCap.capabilities.remove(capability)
        capNAKListener.ircManager.outputCap.requestedCapabilities.remove(capability)
    }
}