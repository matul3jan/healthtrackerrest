package ie.setu.helpers

import ie.setu.domain.User

const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"

val users = arrayListOf(
    User(id = 1, name = "Alice Wonderland", email = "alice@wonderland.com"),
    User(id = 2, name = "Bob Cat", email = "bob@cat.ie"),
    User(id = 3, name = "Mary Contrary", email = "mary@contrary.com"),
    User(id = 4, name = "Carol Singer", email = "carol@singer.com")
)