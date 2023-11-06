import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.testng.AssertJUnit.assertEquals;

public class ServicesTest {
    @Test
    public void getEjemplo() throws ParseException {
        Response response, response2, response3, response4, response5;
        HashMap<String, String> test = new HashMap<>();

        response = given().get("https://swapi.dev/api/people/2/");


        //System.out.println( "hola") ;
        String  responseBody = response.getBody().asString();

        /*Devuelve el body de la respuesta*/
        System.out.println("Cuerpo de la respuesta: " + responseBody);

        /*verifica que la respuesta uno el color de piel es gold y que las peliculas sean 6*/
        response.then().assertThat().body("skin_color", is("gold"));
        response.then().assertThat().body("films", hasSize(6));
        Assert.assertEquals(response.getStatusCode(), 200);

        /*guarda el arraylist de las peliculas*/
        ArrayList<String> value = response.path("films");

        /*obtiene el la segunda pelicula*/
        System.out.println(value.get(1).toString());
        response2 = given().get(value.get(1));

        String  response2Body = response2.getBody().asString();
        System.out.println("Cuerpo de la respuesta: " + response2Body);

        /* Obtenemos la fecha del film y la converyimos a fecha
        String value2 = response2.path("release_date");
        System.out.println(value2);
        LocalDate fecha = LocalDate.now();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(value2);
        System.out.println(parsedDate); */

        /*verifican que los siguientes elementos se encuentren dentro del body*/
        Assert.assertTrue( response2Body.contains("characters"));
        Assert.assertTrue( response2Body.contains("planets"));
        Assert.assertTrue( response2Body.contains("starships"));
        Assert.assertTrue( response2Body.contains("vehicles"));
        Assert.assertTrue( response2Body.contains("species"));


        /*Verifica que cada elemento del body tenga más de un elemento*/
        ArrayList<String> personajes = response2.path("characters");
        Assert.assertTrue(personajes.size() > 1);

        ArrayList<String> planetas = response2.path("planets");
        Assert.assertTrue(planetas.size() > 1);

        ArrayList<String> naves = response2.path("starships");
        Assert.assertTrue(naves.size() > 1);

        ArrayList<String> vehiculos = response2.path("vehicles");
        Assert.assertTrue(vehiculos.size() > 1);

        ArrayList<String> species = response2.path("species");
        Assert.assertTrue(species.size() > 1);

        /* obtiene el primer planeta de la ultima pelicula y verifica sus datos*/
        response3 = given().get(value.get(value.size()-1));
        ArrayList<String> listaplanetas = response3.path("planets");
        response4 = given().get(listaplanetas.get(0));
        response4.then().assertThat().body("gravity", is("1 standard"));
        response4.then().assertThat().body("terrain", is("desert"));


        assertEquals(listaplanetas.get(0), response4.path("url"));

        response5 = given().get("https://swapi.dev/api/films/7/");
        Assert.assertEquals(response5.getStatusCode(), 404);


    }


}
