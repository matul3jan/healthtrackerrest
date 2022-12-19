package ie.setu.config

import ie.setu.config.Properties.getProperty
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.checkPassword
import io.javalin.core.security.RouteRole
import io.javalin.core.util.Header
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.HttpCode
import io.javalin.http.UnauthorizedResponse

enum class Role : RouteRole { ADMIN, USER, TESTER, ANYONE }

object Auth {

    private var roles = fetchRoles()

    private val documentationPaths =
        listOf(getProperty("swagger.ui.url"), getProperty("swagger.docs.url"), getProperty("redoc.url"))

    fun accessManager(handler: Handler, ctx: Context, permittedRoles: Set<RouteRole>) {
        when {
            documentationPaths.contains(ctx.path()) -> handler.handle(ctx)
            permittedRoles.contains(Role.ANYONE) -> handler.handle(ctx)
            ctx.userRoles.contains(Role.ADMIN) -> handler.handle(ctx)
            ctx.userRoles.contains(Role.TESTER) -> handler.handle(ctx)
            ctx.userRoles.any { it in permittedRoles } -> handler.handle(ctx)
            else -> {
                ctx.header(Header.WWW_AUTHENTICATE, "Basic").status(HttpCode.UNAUTHORIZED)
                throw UnauthorizedResponse()
            }
        }
    }

    private val Context.userRoles: List<Role>
        get() {
            if (this.basicAuthCredentialsExist()) {
                return this.basicAuthCredentials().let { (username, password) ->
                    roles.keys.forEach { pair ->
                        if (pair.first == username) {
                            if (password == pair.second || checkPassword(password, pair.second)) {
                                return roles[pair] ?: listOf()
                            }
                        }
                    }
                    listOf()
                }
            }
            return listOf()
        }

    private fun fetchRoles(): Map<Pair<String, String>, List<Role>> {

        val pairs = mutableMapOf(
            Pair(getProperty("admin.user"), getProperty("admin.password")) to listOf(Role.ADMIN),
            Pair(getProperty("test.user"), getProperty("test.password")) to listOf(Role.TESTER)
        )

        UserDAO.findAll().forEach {
            pairs[Pair(it.email, it.password)] = listOf(Role.USER)
        }

        return pairs
    }

    fun updateKeyPairs() {
        roles = fetchRoles()
    }
}