package me.fungames.fortnite.api

import me.fungames.fortnite.api.events.Event
import me.fungames.fortnite.api.exceptions.EpicErrorException
import me.fungames.fortnite.api.model.LoginResponse
import me.fungames.fortnite.api.network.services.*


interface FortniteApi {

    class Builder {
        private var email: String? = null
        private var password: String? = null
        private var loginAsUser : Boolean = true
        private var clientToken: String? = null
        private var loginResponse : LoginResponse? = null

        fun email(email: String): Builder {
            this.email = email
            return this
        }
        fun password(password: String): Builder {
            this.password = password
            return this
        }
        fun loginAsUser(loginAsUser : Boolean): Builder {
            this.loginAsUser = loginAsUser
            return this
        }

        fun initWithLogin(login : LoginResponse?): Builder {
            this.loginResponse = login
            return this
        }

        fun clientToken(token: String): Builder {
            this.clientToken = token
            return this
        }

        fun build() : FortniteApi = FortniteApiImpl().apply {
            if (clientToken != null) this.clientLauncherToken = clientToken!!
            if (this@Builder.email != null) this.email = this@Builder.email
            if (this@Builder.password != null) this.password = this@Builder.password
            if (this@Builder.loginResponse != null) this.loginSucceeded(loginResponse!!)
        }

        fun buildAndLogin(): FortniteApi {
            val api = build()
            if (loginAsUser && loginResponse == null) {
                check(email != null && password != null) { "Logging in as user requires email and password" }
                api.login()
            } else if (loginResponse == null) {
                api.loginClientCredentials()
            }
            return api
        }
    }

    val isLoggedIn: Boolean

    @Throws(EpicErrorException::class)
    fun loginClientCredentials()
    @Throws(EpicErrorException::class)
    fun login(rememberMe : Boolean = false)

    @Throws(EpicErrorException::class)
    fun logout()

    fun fireEvent(event: Event)

    //All the Services available
    val accountPublicService: AccountPublicService
    val affiliatePublicService : AffiliatePublicService
    val catalogPublicService : CatalogPublicService
    val epicGamesService : EpicGamesService
    val eventsPublicService : EventsPublicService
    val fortniteContentWebsiteService : FortniteContentWebsiteService
    val fortnitePublicService : FortnitePublicService
    val friendsPublicService : FriendsPublicService
    val launcherPublicService : LauncherPublicService
    val partyService : PartyService
    val personaPublicService : PersonaPublicService


    var language: String
    val accountTokenType: String
    val accountToken: String
    val accountExpiresAtMillis: Long
}