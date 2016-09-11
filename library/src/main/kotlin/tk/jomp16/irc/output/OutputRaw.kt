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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.IrcManager
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class OutputRaw(private val ircManager: IrcManager) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val writeLock: ReentrantLock = ReentrantLock(true)
    private val writeNowCondition: Condition = writeLock.newCondition()

    private var lastSentLine: Long = 0

    private val delayNanos: Long
        get() = ircManager.ircConfig.delay * 1000000L

    fun writeRaw(raw: String, now: Boolean = false) {
        if (raw.isBlank()) {
            log.error("Can't write blank or empty line! Line: {}", raw)

            return
        }

        if (!ircManager.connected) {
            log.error("Can't send line because client isn't connected!")

            return
        }

        try {
            writeLock.lock()

            if (!now) {
                var curNanos = System.nanoTime()

                while (lastSentLine + delayNanos > curNanos) {
                    writeNowCondition.awaitNanos(lastSentLine + delayNanos - curNanos)

                    curNanos = System.nanoTime()
                }
            }

            lastSentLine = System.nanoTime()

            // log this raw line
            log.debug(raw)

            // Write teh fucking raw line!
            ircManager.ircWriter.write("$raw\r\n")
            ircManager.ircWriter.flush()
        } finally {
            writeLock.unlock()
        }
    }

    fun writeRaw(prefix: String, message: String, suffix: String, now: Boolean = false) {
        val finalMessage = "$prefix$message$suffix"

        val realMaxLineLength = 512 - 2

        if (finalMessage.length < realMaxLineLength) {
            // Length is good (or auto split message is false), just go ahead and send it
            writeRaw(finalMessage, now)

            return
        }

        // Too long, split it up
        val maxMessageLength = realMaxLineLength - "$prefix$suffix".length

        // Oh look, no function to split every nth char. Since regex is expensive, use this nonsense
        val iterations = Math.ceil(message.length / maxMessageLength.toDouble()).toInt()

        (0..iterations - 1).forEach { i ->
            val endPoint = if (i != iterations - 1) (i + 1) * maxMessageLength else message.length
            val curMessagePart = "$prefix${message.substring(i * maxMessageLength, endPoint)}$suffix"

            writeRaw(curMessagePart, now)
        }
    }
}