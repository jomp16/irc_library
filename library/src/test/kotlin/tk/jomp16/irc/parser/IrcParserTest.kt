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

package tk.jomp16.irc.parser

import org.junit.Assert
import org.junit.Test

class IrcParserTest {
    @Test
    fun parseNormal() {
        val raw = ":weber.freenode.net 332 Shinpachi-kun #jomp16-bot :Canal para teste de bots | ( ͡° ͜ʖ ͡°) | Kotlin ftw!"
        val parsed = IrcParser.parse(raw)

        Assert.assertNotNull(parsed)
        Assert.assertEquals("weber.freenode.net", parsed.user.host)
        Assert.assertEquals("332", parsed.command)
        Assert.assertEquals(mapOf<String, String>(), parsed.tags)
        Assert.assertEquals(listOf("Shinpachi-kun", "#jomp16-bot", "Canal para teste de bots | ( ͡° ͜ʖ ͡°) | Kotlin ftw!"), parsed.params)
    }

    @Test
    fun parseTags() {
        val raw = "@aaa=bbb;ccc;example.com/ddd=eee :raw!ident@host.com PRIVMSG me :Hello"
        val parsed = IrcParser.parse(raw)

        Assert.assertNotNull(parsed)
        Assert.assertEquals(mapOf("aaa" to "bbb", "ccc" to "", "example.com/ddd" to "eee"), parsed.tags)
        Assert.assertEquals("raw", parsed.user.nick)
        Assert.assertEquals("ident", parsed.user.user)
        Assert.assertEquals("host.com", parsed.user.host)
        Assert.assertEquals("PRIVMSG", parsed.command)
        Assert.assertEquals(listOf("me", "Hello"), parsed.params)
    }

    @Test
    fun parseLongRaw() {
        val raw = ":irc.rizon.sexy 004 Shinpachi-kun irc.rizon.sexy plexus-4(hybrid-8.1.20) CDGNRSUWagilopqrswxyz BCIMNORSabcehiklmnopqstvz Iabehkloqv"
        val parsed = IrcParser.parse(raw)

        Assert.assertNotNull(parsed)
    }
}