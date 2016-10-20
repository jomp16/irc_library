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

package tk.jomp16.irc

import net.engio.mbassy.bus.MBassador
import net.engio.mbassy.bus.error.IPublicationErrorHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tk.jomp16.irc.channel.ChannelList
import tk.jomp16.irc.config.IrcConfig
import tk.jomp16.irc.handler.IrcHandler
import tk.jomp16.irc.output.OutputCap
import tk.jomp16.irc.output.OutputIrc
import tk.jomp16.irc.output.OutputRaw
import tk.jomp16.irc.plugin.AbstractPlugin
import tk.jomp16.irc.plugin.impl.cap.EnableCapPlugin
import tk.jomp16.irc.plugin.impl.sasl.EnableSaslPlugin
import tk.jomp16.irc.ssl.TrustingX509TrustManager
import tk.jomp16.irc.user.UserList
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket
import java.net.SocketTimeoutException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.naming.ldap.LdapName
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class IrcManager(val ircConfig: IrcConfig) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    lateinit var ircSocket: Socket
        private set
    lateinit var ircReader: BufferedReader
        private set
    lateinit var ircWriter: BufferedWriter
        private set

    val outputRaw: OutputRaw = OutputRaw(this)
    val outputCap: OutputCap = OutputCap(this)
    val outputIrc: OutputIrc = OutputIrc(this)
    val ircHandler: IrcHandler = IrcHandler(this)

    val userList: UserList = UserList()
    val channelList: ChannelList = ChannelList()

    val connected: Boolean
        get() = ircSocket.isConnected

    var authenticated: Boolean = false

    val plugins: MutableList<AbstractPlugin> = mutableListOf()

    val eventBus = MBassador<Any>(IPublicationErrorHandler { error -> log.error("An error happened!", error) })

    init {
        if (!ircConfig.test) {
            addPlugin(EnableSaslPlugin())
            addPlugin(EnableCapPlugin("multi-prefix"))
            addPlugin(EnableCapPlugin("userhost-in-names"))
        }
    }

    fun addPlugin(abstractPlugin: AbstractPlugin) {
        if (plugins.contains(abstractPlugin)) return

        plugins.add(abstractPlugin)

        abstractPlugin.onCreate(this)

        eventBus.subscribe(abstractPlugin)
    }

    fun removePlugin(abstractPlugin: AbstractPlugin) {
        if (!plugins.contains(abstractPlugin)) return

        plugins.remove(abstractPlugin)

        abstractPlugin.onDestroy(this)

        eventBus.unsubscribe(abstractPlugin)
    }

    fun start() {
        log.debug("Starting IRC client...")

        if (ircConfig.ssl) {
            val factory: SSLSocketFactory = if (ircConfig.trustAllSsl) {
                // trust all teh ssl certificates
                val sslContext = SSLContext.getInstance("SSL")

                sslContext.init(arrayOf(), arrayOf(TrustingX509TrustManager()), SecureRandom())

                // returns the trust all socket factory
                sslContext.socketFactory
            } else {
                // returns the default socket factory
                SSLSocketFactory.getDefault() as SSLSocketFactory
            }

            ircSocket = factory.createSocket(ircConfig.server, ircConfig.port)

            val sslSocket = ircSocket as SSLSocket

            sslSocket.session.peerCertificates[0].let {
                if (it is X509Certificate) {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

                    log.debug("================")
                    log.debug("Subject: {}", LdapName(it.subjectDN.name).rdns.joinToString())
                    log.debug("Issuer: {}", LdapName(it.issuerDN.name).rdns.joinToString())
                    log.debug("Public key algorithm: {} ({} bits)", it.publicKey.algorithm, if (it.publicKey is RSAPublicKey) (it.publicKey as RSAPublicKey).modulus.bitLength() else "unknown")
                    log.debug("Sign algorithm: {}", it.sigAlgName)
                    log.debug("Valid since {} until {}", LocalDateTime.ofInstant(it.notBefore.toInstant(), ZoneId.systemDefault()).format(formatter), LocalDateTime.ofInstant(it.notAfter.toInstant(), ZoneId.systemDefault()).format(formatter))
                    log.debug("Cipher info: protocol: {}, cipher: {}", sslSocket.session.protocol, sslSocket.session.cipherSuite)
                    log.debug("================")
                }
            }
        } else {
            ircSocket = Socket(ircConfig.server, ircConfig.port)
        }

        ircSocket.soTimeout = ircConfig.socketTimeout.toInt()

        log.debug("Started IRC client")

        ircReader = ircSocket.inputStream.bufferedReader(Charsets.UTF_8)
        ircWriter = ircSocket.outputStream.bufferedWriter(Charsets.UTF_8)

        if (ircConfig.serverPassword.isNotBlank()) outputRaw.writeRaw("PASS ${ircConfig.serverPassword}", true)

        outputCap.sendList()

        outputRaw.writeRaw("NICK ${ircConfig.name}", true)
        outputRaw.writeRaw("USER ${ircConfig.ident} 8 * :${ircConfig.realName}", true)

        ircReader.buffered().let {
            while (true) {
                try {
                    it.lineSequence().forEach { ircHandler.handle(it) }
                } catch (e: SocketTimeoutException) {
                    outputRaw.writeRaw("PING ${System.currentTimeMillis() / 1000}")
                } catch (e: Exception) {
                    log.error("An exception happened!", e)

                    ircReader.close()
                    ircWriter.close()
                    ircSocket.close()

                    System.exit(1)
                }
            }
        }
    }
}