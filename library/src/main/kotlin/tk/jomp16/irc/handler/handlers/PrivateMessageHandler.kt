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

package tk.jomp16.irc.handler.handlers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcManager
import tk.jomp16.irc.ctcp.CtcpCommand
import tk.jomp16.irc.handler.IHandler
import tk.jomp16.irc.parser.IrcParserData
import tk.jomp16.irc.plugin.listeners.privmsg.PrivateMessageListener

class PrivateMessageHandler : IHandler {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handle(ircManager: IrcManager, ircParserData: IrcParserData) {
        // channel:
        // :jomp16!~jomp16@unaffiliated/jomp16 PRIVMSG #jomp16-bot :all_registered_modes
        // IrcParserData(raw=:jomp16!~jomp16@unaffiliated/jomp16 PRIVMSG #jomp16-bot :all_registered_modes, user=User(raw='jomp16!~jomp16@unaffiliated/jomp16', nick='jomp16', user='~jomp16', host='unaffiliated/jomp16'), tags={}, command=PRIVMSG, params=[#jomp16-bot, all_registered_modes])

        // private chat
        // :jomp16!~jomp16@unaffiliated/jomp16 PRIVMSG jomp16-bot :lolwut
        // IrcParserData(raw=:jomp16!~jomp16@unaffiliated/jomp16 PRIVMSG jomp16-bot :lolwut, user=User(raw='jomp16!~jomp16@unaffiliated/jomp16', nick='jomp16', user='~jomp16', host='unaffiliated/jomp16'), tags={}, command=PRIVMSG, params=[jomp16-bot, lolwut])

        val channel = ircManager.channelList.getOrAddChannel(ircParserData.params[0])
        val message = ircParserData.params[1]

        if (message.startsWith("\u0001") && message.endsWith("\u0001")) {
            // CTCP message
            val strippedMessage = message.removeSurrounding("\u0001")
            val splitMessage = strippedMessage.split(' ')
            val ctcpCommand = CtcpCommand.getCtcpCommand(splitMessage[0].toUpperCase()) ?: return

            when (ctcpCommand) {
                CtcpCommand.VERSION -> ircManager.outputIrc.sendNotice(ircParserData.user.nick, "VERSION irc_library 1.0-SNAPSHOT", true)
                else                -> log.debug("I don't know how to handle that CTCP {} command!", ctcpCommand)
            }
        } else if (message == "ctcp_version") {
            ircManager.outputIrc.sendPrivateMessage(ircParserData.user.nick, "VERSION", true)
        } else if (message == "all_registered_modes") {
            ircManager.outputIrc.sendPrivateMessage(channel, ircManager.channelList.channels.values.joinToString())
        } else {
            ircManager.eventBus.publishAsync(PrivateMessageListener(ircManager, message))
        }
    }
}