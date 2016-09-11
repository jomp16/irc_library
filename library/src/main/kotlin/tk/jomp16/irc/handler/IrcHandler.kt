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

package tk.jomp16.irc.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.IrcNumericConstants
import tk.jomp16.irc.handler.handlers.*
import tk.jomp16.irc.parser.IrcParser
import tk.jomp16.irc.plugin.listeners.unknown.UnknownListener

class IrcHandler(val ircManager: IrcManager) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val instances: MutableMap<Class<*>, IHandler> = mutableMapOf()

    private val handlers: Map<String, IHandler> = mapOf(
            "PING" to getInstance(PingHandler::class.java),
            "CAP" to getInstance(CapHandler::class.java),
            "NOTICE" to getInstance(NoticeHandler::class.java),
            "MODE" to getInstance(ModeHandler::class.java),
            "PRIVMSG" to getInstance(PrivateMessageHandler::class.java),
            IrcNumericConstants.RPL_NAMREPLY.numericCommand to getInstance(NamesHandler::class.java)
    )

    fun handle(raw: String) {
        log.debug(raw)

        val ircParserData = IrcParser.parse(raw)

        log.debug("{}", ircParserData)

        if (handlers.containsKey(ircParserData.command)) {
            val handler = handlers[ircParserData.command] ?: return

            handler.handle(ircManager, ircManager.userList, ircManager.channelList, ircParserData)
        } else {
            ircManager.eventBus.publishAsync(UnknownListener(ircManager, ircParserData))
        }
    }

    private fun getInstance(clazz: Class<out IHandler>): IHandler {
        val clazz1: IHandler

        if (!instances.containsKey(clazz)) {
            clazz1 = clazz.newInstance()

            instances.put(clazz, clazz1)
        } else {
            clazz1 = instances[clazz]!!
        }

        return clazz1
    }
}