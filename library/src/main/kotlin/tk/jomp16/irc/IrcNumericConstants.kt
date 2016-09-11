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

package tk.jomp16.irc

enum class IrcNumericConstants(val numericCommand: String) {
    // Expected replies
    // `001 Welcome to the Internet Relay Network <raw>!<user>@<host>`
    RPL_WELCOME("001"),
    // `002 Your host is <servername>, running version <ver>`
    RPL_YOURHOST("002"),
    // `003 This server was created <date>`
    RPL_CREATED("003"),
    // `004 <servername> <version> <available user modes> available channel modes>`
    RPL_MYINFO("004"),
    // `005 Try server <server name>, port <port number>`
    RPL_BOUNCE("005"),
    // `302 :*1<reply> *( " " <reply> )`
    RPL_USERHOST("302"),
    // `303 :*1<raw> *( " " <raw> )`
    RPL_ISON("303"),
    // `301 <raw> :<away message>`
    RPL_AWAY("301"),
    // `305 :You are no longer marked as being away`
    RPL_UNAWAY("305"),
    // `306 :You have been marked as being away`
    RPL_NOWAWAY("306"),
    // `311 <raw> <user> <host> * :<real name>`
    RPL_WHOISUSER("311"),
    // `312 <raw> <server> :<server info>`
    RPL_WHOISSERVER("312"),
    // `313 <raw> :is an IRC operator`
    RPL_WHOISOPERATOR("313"),
    // `317 <raw> <integer> :seconds idle`
    RPL_WHOISIDLE("317"),
    // `318 <raw> :End of WHOIS list`
    RPL_ENDOFWHOIS("318"),
    // `319 <raw> :*( ( "@" / "+" ) <channel> " " )`
    RPL_WHOISCHANNELS("319"),
    // `314 <raw> <user> <host> * :<real name>`
    RPL_WHOWASUSER("314"),
    // `369 <raw> :End of WHOWAS`
    RPL_ENDOFWHOWAS("369"),
    // Obsolete. Not used.
    RPL_LISTSTART("321"),
    // `322 <channel> <# visible> :<topic>`
    RPL_LIST("322"),
    // `323 :End of LIST
    RPL_LISTEND("323"),
    // `325 <channel> <nickname>`
    RPL_UNIQOPIS("325"),
    // `324 <channel> <mode> <mode params>`
    RPL_CHANNELMODEIS("324"),
    // `331 <channel> :No topic is set`
    RPL_NOTOPIC("331"),
    // `332 <channel> :<topic>`
    RPL_TOPIC("332"),
    // `341 <channel> <raw>`
    RPL_INVITING("341"),
    // `342 <user> :Summoning user to IRC`
    RPL_SUMMONING("342"),
    // `346 <channel> <invitemask>`
    RPL_INVITELIST("346"),
    // `347 <channel> :End of channel invite list`
    RPL_ENDOFINVITELIST("347"),
    // `348 <channel> <exceptionmask>`
    RPL_EXCEPTLIST("348"),
    // `349 <channel> :End of channel exception list`
    RPL_ENDOFEXCEPTLIST("349"),
    // `351 <version>.<debuglevel> <server> :<comments>`
    RPL_VERSION("351"),
    /** `352 <channel> <user> <host> <server> <raw> ( "H" / "G" > ["*"] [ ( "@" / "+" ) ]
    :<hopcount> <real name>` **/
    RPL_WHOREPLY("352"),
    // `315 <name> :End of WHO list`
    RPL_ENDOFWHO("315"),
    // `353 ( "("")" / "*" / "@" ) <channel> :[ "@" / "+" ] <raw> *( " " [ "@" / "+" ] <raw> )`
    RPL_NAMREPLY("353"),
    // `366 <channel> :End of NAMES list`
    RPL_ENDOFNAMES("366"),
    // `364 <mask> <server> :<hopcount> <server info>`
    RPL_LINKS("364"),
    // `365 <mask> :End of LINKS list`
    RPL_ENDOFLINKS("365"),
    // `367 <channel> <banmask>`
    RPL_BANLIST("367"),
    // `368 <channel> :End of channel ban list`
    RPL_ENDOFBANLIST("368"),
    // `371 :<string>`
    RPL_INFO("371"),
    // `374 :End of INFO list`
    RPL_ENDOFINFO("374"),
    // `375 :- <server> Message of the day -`
    RPL_MOTDSTART("375"),
    // `372 :- <text>
    RPL_MOTD("372"),
    // `376 :End of MOTD command
    RPL_ENDOFMOTD("376"),
    // `381 :You are now an IRC operator
    RPL_YOUREOPER("381"),
    // `382 <config file> :Rehashing
    RPL_REHASHING("382"),
    // `383 You are service <servicename>
    RPL_YOURESERVICE("383"),
    // `391 <server> :<string showing server's local time>
    RPL_TIME("391"),
    // `392 :UserID   Terminal  Host
    RPL_USERSSTART("392"),
    // `393 :<username> <ttyline> <hostname>
    RPL_USERS("393"),
    // `394 :End of users
    RPL_ENDOFUSERS("394"),
    // `395 :Nobody logged in
    RPL_NOUSERS("395"),
    /** `200 Link <version & debug level> <destination> <next server> V<protocol version>
    <link uptime in seconds> <backstream sendq> <upstream sendq> **/
    RPL_TRACELINK("200"),
    // `201 Try. <class> <server>
    RPL_TRACECONNECTING("201"),
    // `202 H.S. <class> <server>
    RPL_TRACEHANDSHAKE("202"),
    // `203 ???? <class> [<client IP address in dot form>]
    RPL_TRACEUKNOWN("203"),
    // `204 Oper <class> <raw>
    RPL_TRACEOPERATOR("204"),
    // `205 User <class> <raw>
    RPL_TRACEUSER("205"),
    // `206 Serv <class> <int>S <int>C <server> <raw!user|*!*>@<host|server> V<protocol version>
    RPL_TRACESERVER("206"),
    // `207 Service <class> <name> <type> <active type>
    RPL_TRACESERVICE("207"),
    // `208 <newtype> 0 <client name>
    RPL_TRACENEWTYPE("208"),
    // `209 Class <class> <count>
    RPL_TRACECLASS("209"),
    // Unused.
    RPL_TRACERECONNECT("210"),
    // `261 File <logfile> <debug level>
    RPL_TRACELOG("261"),
    // `262 <server name> <version & debug level> :End of TRACE
    RPL_TRACEEND("262"),
    /** `211 <linkname> <sendq> <sent messages> <sent Kbytes> <received messages> <received Kbytes>
    <time open> **/
    RPL_STATSLINKINFO("211"),
    // `212 <command> <count> <byte count> <remote count>
    RPL_STATSCOMMANDS("212"),
    // `219 <stats letter> :End of STATS report
    RPL_ENDOFSTATS("219"),
    // `242 :Server Up %d days %d:%02d:%02d
    RPL_STATSUPTIME("242"),
    // `243 O <hostmask> * <name>
    RPL_STATSOLINE("243"),
    // `221 <user mode string>
    RPL_UMODEIS("221"),
    // `234 <name> <server> <mask> <type> <hopcount> <info>
    RPL_SERVLIST("234"),
    // `235 <mask> <type> :End of service listing
    RPL_SERVLISTEND("235"),
    // `251 :There are <integer> users and <integer> services on <integer> servers
    RPL_LUSERCLIENT("251"),
    // `252 <integer> :operator(s) online
    RPL_LUSEROP("252"),
    // `253 <integer> :unknown connection(s)
    RPL_LUSERUNKNOWN("253"),
    // `254 <integer> :channels formed
    RPL_LUSERCHANNELS("254"),
    // `255 :I have <integer> clients and <integer> servers
    RPL_LUSERME("255"),
    // `256 <server> :Administrative info
    RPL_ADMINME("256"),
    // `257 :<admin info>
    RPL_ADMINLOC1("257"),
    // `258 :<admin info>
    RPL_ADMINLOC2("258"),
    // `259 :<admin info>
    RPL_ADMINEMAIL("259"),
    // `263 <command> :Please wait a while and try again.
    RPL_TRYAGAIN("263"),
    // `730 <raw> :target[,target2]*
    RPL_MONONLINE("730"),
    // `731 <raw> :target[,target2]*
    RPL_MONOFFLINE("731"),
    // `732 <raw> :target[,target2]*
    RPL_MONLIST("732"),
    // `733 <raw> :End of MONITOR list
    RPL_ENDOFMONLIST("733"),
    // `760 <target> <key> <visibility> :<value>
    RPL_WHOISKEYVALUE("760"),
    // `761 <target> <key> <visibility> :[<value>]
    RPL_KEYVALUE("761"),
    // `762 :end of metadata
    RPL_METADATAEND("762"),
    // `900 <raw> <raw>!<ident>@<host> <account> :You are now logged in as <user>
    RPL_LOGGEDIN("900"),
    // `901 <raw> <raw>!<ident>@<host> :You are now logged out
    RPL_LOGGEDOUT("901"),
    // `903 <raw> :SASL authentication successful
    RPL_SASLSUCCESS("903"),
    // `908 <raw> <mechanisms> :are available SASL mechanisms
    RPL_SASLMECHS("908"),

    // Error replies
    // `401 <nickname> :No such raw/channel
    ERR_NOSUCHNICK("401"),
    // `402 <server name> :No such server
    ERR_NOSUCHSERVER("402"),
    // `403 <channel name> :No such channel
    ERR_NOSUCHCHANNEL("403"),
    // `404 <channel name> :Cannot send to channel
    ERR_CANNOTSENDTOCHAN("404"),
    // `405 <channel name> :You have joined too many channels
    ERR_TOOMANYCHANNELS("405"),
    // `406 <nickname> :There was no such nickname
    ERR_WASNOSUCHNICK("406"),
    // `407 <target> :<error code> recipients. <abort message>
    ERR_TOOMANYTARGETS("407"),
    // `408 <service name> :No such service
    ERR_NOSUCHSERVICE("408"),
    // `409 :No origin specified
    ERR_NOORIGIN("409"),
    // `411 :No recipient given (<command>)
    ERR_NORECIPIENT("411"),
    // `412 :No text to send
    ERR_NOTEXTTOSEND("412"),
    // `413 <mask> :No toplevel domain specified
    ERR_NOTOPLEVEL("413"),
    // `414 <mask> :Wildcard in toplevel domain
    ERR_WILDTOPLEVEL("414"),
    // `415 <mask> :Bad Server/host mask
    ERR_BADMASK("415"),
    // `421 <command> :Unknown command
    ERR_UNKNOWNCOMMAND("421"),
    // `422 :MOTD File is missing
    ERR_NOMOTD("422"),
    // `423 <server> :No administrative info available
    ERR_NOADMININFO("423"),
    // `424 :File error doing <file op> on <file>
    ERR_FILEERROR("424"),
    // `431 :No nickname given
    ERR_NONICKNAMEGIVEN("431"),
    // `432 <raw> :Erroneous nickname"
    ERR_ERRONEOUSNICKNAME("432"),
    // `433 <raw> :Nickname is already in use
    ERR_NICKNAMEINUSE("433"),
    // `436 <raw> :Nickname collision KILL from <user>@<host>
    ERR_NICKCOLLISION("436"),
    // `437 <raw/channel> :Nick/channel is temporarily unavailable
    ERR_UNAVAILRESOURCE("437"),
    // `441 <raw> <channel> :They aren't on that channel
    ERR_USERNOTINCHANNEL("441"),
    // `442 <channel> :You're not on that channel
    ERR_NOTONCHANNEL("442"),
    // `443 <user> <channel> :is already on channel
    ERR_USERONCHANNEL("443"),
    // `444 <user> :User not logged in
    ERR_NOLOGIN("444"),
    // `445 :SUMMON has been disabled
    ERR_SUMMONDISABLED("445"),
    // `446 :USERS has been disabled
    ERR_USERSDISABLED("446"),
    // `451 :You have not registered
    ERR_NOTREGISTERED("451"),
    // `461 <command> :Not enough parameters
    ERR_NEEDMOREPARAMS("461"),
    // `462 :Unauthorized command (already registered)
    ERR_ALREADYREGISTRED("462"),
    // `463 :Your host isn't among the privileged
    ERR_NOPERMFORHOST("463"),
    // `464 :Password incorrect
    ERR_PASSWDMISMATCH("464"),
    // `465 :You are banned from this server
    ERR_YOUREBANNEDCREEP("465"),
    // `466
    ERR_YOUWILLBEBANNED("466"),
    // `467 <channel> :Channel key already set
    ERR_KEYSET("467"),
    // `471 <channel> :Cannot join channel (+l)
    ERR_CHANNELISFULL("471"),
    // `472 <char> :is unknown mode char to me for <channel>
    ERR_UNKNOWNMODE("472"),
    // `473 <channel> :Cannot join channel (+i)
    ERR_INVITEONLYCHAN("473"),
    // `474 <channel> :Cannot join channel (+b)
    ERR_BANNEDFROMCHAN("474"),
    // `475 <channel> :Cannot join channel (+k)
    ERR_BADCHANNELKEY("475"),
    // `476 <channel> :Bad Channel Mask
    ERR_BADCHANMASK("476"),
    // `477 <channel> :Channel doesn't support modes
    ERR_NOCHANMODES("477"),
    // `478 <channel> <char> :Channel list is full
    ERR_BANLISTFULL("478"),
    // `481 :Permission Denied- You're not an IRC operator
    ERR_NOPRIVILEGES("481"),
    // `482 <channel> :You're not channel operator
    ERR_CHANOPRIVSNEEDED("482"),
    // `483 :You can't kill a server!
    ERR_CANTKILLSERVER("483"),
    // `484 :Your connection is restricted!
    ERR_RESTRICTED("484"),
    // `485 :You're not the original channel operator
    ERR_UNIQOPPRIVSNEEDED("485"),
    // `491 :No O-lines for your host
    ERR_NOOPERHOST("491"),
    // `501 :Unknown MODE flag
    ERR_UMODEUNKNOWNFLAG("501"),
    // `502 :Cannot change mode for other users
    ERR_USERSDONTMATCH("502"),
    // `734 <raw> <limit> <targets> :Monitor list is full.
    ERR_MONLISTFULL("734"),
    // `764 <target> :metadata limit reached
    ERR_METADATALIMIT("764"),
    // `765 <target> :invalid metadata target
    ERR_TARGETINVALID("765"),
    // `766 <key> :no matching key
    ERR_NOMATCHINGKEY("766"),
    // `767 <key> :invalid metadata key
    ERR_KEYINVALID("767"),
    // `768 <target> <key> :key not set
    ERR_KEYNOTSET("768"),
    // `769 <target> <key> :permission denied
    ERR_KEYNOPERMISSION("769"),
    // `902 <raw> :You must use a raw assigned to you.
    ERR_NICKLOCKED("902"),
    // `904 <raw> :SASL authentication failed
    ERR_SASLFAIL("904"),
    // `905 <raw> :SASL message too long
    ERR_SASLTOOLONG("905"),
    // `906 <raw> :SASL authentication aborted
    ERR_SASLABORT("906"),
    // `907 <raw> :You have already authenticated using SASL
    ERR_SASLALREADY("907")
}