import io.restassured.RestAssured
import io.restassured.RestAssured.get
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import jdk.jfr.Label
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import java.util.*


const val USER_API_URL = "https://userapi.webinar.ru/v3/"

class UseApi {

    var email = RandomStringUtils.randomAlphabetic(7).lowercase(Locale.getDefault()) + "@mailinator.com"

    @Test
    @Label("Получить список курсов, GET")
    fun withoutToken() {
        get("organization/courses")
            .then().statusCode(401)
            .body("error.message", `is`("A Token was not found in the TokenStorage."))
    }

    @Test
    @Label("Получить список курсов, GET")
    fun withToken() {
        given().header("X-Auth-Token", "N")["organization/courses"]
            .then().statusCode(200)
    }

    @Test
    @Label("Получить список курсов, GET")
    fun checkFirstCourse() {
        given().header("X-Auth-Token", "N")["organization/courses"]
            .then()
            .body("data.id[0]", `is`(55905))
            .body("data.name[0]", `is`("Новый курс"))
            .body("data.owner[0].id", `is`(42548317))
            .body("data.isPublish[0]", `is`(false))
    }

    @Test
    @Label("Получить список курсов, GET")
    fun checkSecondCourse() {
        given().header("X-Auth-Token", "N")["organization/courses"]
            .then()
            .body("data.id[1]", `is`(59441))
            .body("data.name[1]", `is`("Новый курс"))
            .body("data.owner[1].id", `is`(42548317))
            .body("data.isPublish[1]", `is`(true))
    }


    @Test
    @Label("Пригласить участника в группу, POST")
    fun getInfoAboutCourse() {
        given().contentType(JSON)
            .body("{\"data\":{ \"email\": \"$email\"}}")
            .header("X-Auth-Token", "N")
            .post("groups/" + 72095 + "/invites?sendInvites=true")
            .then().statusCode(200)
            .body("contact.id[0]", notNullValue())
            .body("contact.email[0]", `is`(email))
            .body("contact.firstName[0]", nullValue())
            .body("contact.lastName[0]", nullValue())
    }

    companion object {
        init {
            RestAssured.baseURI = USER_API_URL
        }
    }
}