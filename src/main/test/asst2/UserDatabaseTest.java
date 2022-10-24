package asst2;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class UserDatabaseTest {
    UserDatabase db = new UserDatabase();
    User anon = db.getAnon();

    @Test
    public void testanon() {
        String name = anon.getUsername();
        assertEquals(name, anon.getUsername());
    }

    @Test
    public void testgetDB() {
        boolean thrown = false;
        if (db.getDB() != null) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
