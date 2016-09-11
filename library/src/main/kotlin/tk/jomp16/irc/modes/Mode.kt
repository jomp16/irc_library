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

package tk.jomp16.irc.modes

enum class Mode(val modeSymbol: String, val mode: String) {
    // channel modes
    CHANNEL_OWNER("~", "q"),
    CHANNEL_ADMIN("&", "a"),
    CHANNEL_FULL_OP("@", "o"),
    CHANNEL_HALF_OP("%", "h"),
    CHANNEL_VOICE("+", "v"),

    // user modes
    USER_RESTRICTED("", "r"),
    USER_SECURE_CONN("", "Z"),
    USER_INVISIBLE("", "i");

    companion object {
        fun getModes(raw: String, fromMode: Boolean = true, userMode: Boolean = false): List<Mode> {
            val modes: MutableList<Mode> = mutableListOf()

            // such code, so clever, I liek it!
            while (values()
                    .filter { if (userMode) it.name.startsWith("USER_") else it.name.startsWith("CHANNEL_") }
                    .any { modes.size < raw.length && raw[modes.size].toString() == if (fromMode) it.mode else it.modeSymbol }) {
                modes += values()
                        .filter { if (userMode) it.name.startsWith("USER_") else it.name.startsWith("CHANNEL_") }
                        .filter { raw[modes.size].toString() == if (fromMode) it.mode else it.modeSymbol }
            }

            return modes
        }
    }
}