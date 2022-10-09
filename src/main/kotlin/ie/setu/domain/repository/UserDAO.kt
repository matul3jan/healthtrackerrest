package ie.setu.domain.repository

import ie.setu.domain.User

class UserDAO {

    private val users = arrayListOf(
        User(id = 0, name = "Alice", email = "alice@wonderland.com"),
        User(id = 1, name = "Bob", email = "bob@cat.ie"),
        User(id = 2, name = "Mary", email = "mary@contrary.com"),
        User(id = 3, name = "Carol", email = "carol@singer.com")
    )

    fun findAll(): ArrayList<User> = users

    fun finById(id: Int): User? = users.find { it.id == id }

    fun findByEmail(email: String): User? = users.find { it.email == email }

    fun save(user: User) = users.add(user)

    fun delete(id: Int): Boolean = users.removeIf { it.id == id }

    fun update(user: User, newUser: User) {
        user.name = newUser.name
        user.email = newUser.email
    }
}