import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class Personaje(
    val id: String,
    val name: String,
    val wizard: Boolean,
    val house: String?,
    val ancestry: String?,
    val patronus: String?,
    val wand: Varita,
    val actor: String
)

@Serializable
data class Varita(
    val wood: String,
    val core: String,
    val length: Double?
)

@Serializable
data class Hechizo(
    val name: String,
    val description: String
)

fun consultarAPI(url: String): String? {
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}


fun main() {
    val endpointPersonajes = "https://hp-api.onrender.com/api/characters"
    val endpointHechizos = "https://hp-api.onrender.com/api/spells"

    val datosPersonajes = consultarAPI(endpointPersonajes)
    val datosHechizos = consultarAPI(endpointHechizos)

    val json = Json { ignoreUnknownKeys = true }

    val personajes: List<Personaje> = json.decodeFromString(datosPersonajes ?:"")
    val hechizos: List<Hechizo> = json.decodeFromString(datosHechizos?:"")

    // Mostrar los primeros 3 personajes
    println("Consultas relacionadas con los personajes")

    // Filtrar personajes por cada casa
    val gryffindor = personajes.filter { it.house == "Gryffindor" }
    println("\n Personajes de Gryffindor:")
    gryffindor.forEach { println(it.name) }

    val ravenclaw = personajes.filter { it.house == "Ravenclaw" }
    println("\n Personajes de Ravenclaw:")
    ravenclaw.forEach { println(it.name) }

    val hufflepuff = personajes.filter { it.house == "Hufflepuff" }
    println("\n Personajes de Hufflepuff:")
    hufflepuff.forEach { println(it.name) }

    val slytherin = personajes.filter { it.house == "Slytherin" }
    println("\n Personajes de Slytherin:")
    slytherin.forEach { println(it.name) }

    // Mostrar actores de los personajes
    println("\n Actores de los personajes:")
    personajes.take(5).forEach { println("${it.name} es interpretado por ${it.actor}") }

    // Mostrar personajes con Patronus
    val conPatronus = personajes.filter { !it.patronus.isNullOrBlank() }
    println("\n Personajes con Patronus:")
    conPatronus.forEach { println("${it.name} - Patronus: ${it.patronus}") }

    println("\n Consultas relacionadas con los hechizos")
    println("\n Hechizos que comienzan con la letra A:")
    val hechizosConA = hechizos.filter { it.name.startsWith("A", ignoreCase = true) }
    hechizosConA.forEach { println(it.name) }

    println("\nHechizos que contienen la palabra 'jinx' en la descripción:")
    val hechizosConOpen = hechizos.filter { it.description.contains("jinx", ignoreCase = true) }
    hechizosConOpen.forEach { println(it.name) }
}
