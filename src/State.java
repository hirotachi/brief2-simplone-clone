import netscape.javascript.JSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class State {


    public static void load() {
        User.parseAndLoad(new HashMap<>());
        Admin.parseAndLoad(new HashMap<>());
        Promotion.parseAndLoad(new HashMap<>());
        Brief.parseAndLoad(new HashMap<>());
        readJSONState();


        Formateur.add("formatteur", "formatteur", "formatteur");
        Apprenant.add("apprenant", "apprenant", "apprenant");
        Admin.add("admin", "admin");
        Promotion.add("Promotion 1", 2022);
        Promotion.add("Promotion 2", 2022);
    }

    private static JSObject readJSONState() {
        try {
            String text = Files.readString(Paths.get("test.json"));
//            convert text to JSObject



        } catch (IOException e) {

            Logger.errorln(e.toString());
        }
        return null;
    }


}
