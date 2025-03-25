package requests;

import java.util.ArrayList;
import model.GameData;

public record ListResponse(ArrayList<GameData> games) {

}
