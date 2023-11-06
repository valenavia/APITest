import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ServiceTest {

    @Test
    public void getExample(){
        Response response, response2, response3, response4, response5;

        // 1)
        response = given().get("https://swapi.dev/api/people/2/");
        assertEquals(response.getStatusCode(),200);

        response.then().assertThat().body("skin_color", equalTo("gold"));
        ArrayList<String> films = response.path("films");
        assertEquals(films.size(),6);

        //2)
        response2 = given().get(films.get(1));
        String date = response2.path("release_date");

        System.out.println(date);
        assertTrue(validateDateFormat(date));

        String  response2Body = response2.getBody().asString();

        assertTrue( response2Body.contains("characters"));
        assertTrue( response2Body.contains("planets"));
        assertTrue( response2Body.contains("starships"));
        assertTrue( response2Body.contains("vehicles"));
        assertTrue( response2Body.contains("species"));

        for (int i = 0; i < 5; i++) {
            String value;
            if (i==0) value="characters";
            else if (i==1) value="planets";
            else if (i==2) value="starships";
            else if (i==3) value="vehicles";
            else value="species";

            ArrayList<String> list = response2.path(value);
            assertTrue(list.size()>1);
        }

        //3)
        response3 = given().get(films.get(films.size()-1));

        ArrayList<String> listaplanetas = response3.path("planets");

        response4 = given().get(listaplanetas.get(0));

        response4.then().assertThat().body("gravity", is("1 standard"));
        response4.then().assertThat().body("terrain", is("desert"));

        //4)
        assertEquals(listaplanetas.get(0), response4.path("url"));

        //5)
        response5 = given().get("https://swapi.dev/api/films/7/");
        assertEquals(response5.getStatusCode(), 404);

        //Si me quiero dejar mensaje de que el tipo de error no es el esperado:
        /*response5 = given().get("https://swapi.dev/api/films/6/");
        assertEquals(response5.getStatusCode(), 404. "el error no es el esperado");*/
    }


    public static boolean validateDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            // Attempt to parse the date
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            // Parsing failed, indicating an invalid date format
            return false;
        }
    }


}
