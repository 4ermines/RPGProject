package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_bed extends SuperObject{

    public OBJ_bed() {
        name = "bed";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/bedbigger.png"));

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
