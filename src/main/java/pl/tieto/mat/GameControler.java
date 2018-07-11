package pl.tieto.mat;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameControler {
	public static final String firstGamer = "X";
	public static final String secondGamer = "O";
	private boolean sym = true;
	private boolean newGame = true;
	private BoardOX board = new BoardOX();

	@GetMapping(path = "/games")
	public String home() {
		return "gameList";
	}

	@GetMapping(path = "/ox")
	public String ox(Model model) {
		if (sym == true && newGame) {
			sym = false;
			newGame = false;
			model.addAttribute("sym", firstGamer);
		} else if (sym == false && !newGame) {
			model.addAttribute("sym", secondGamer);
			sym = true;
		}

		return "ox";
	}

	@MessageMapping("/ox")
	@SendTo("/ox")
	public String handleMessage(String message) throws JSONException {
		JSONObject obj = new JSONObject();
		obj = new JSONObject(message);

		String symbol = (String) obj.get("symbol");
		Integer number = (Integer) obj.get("number");
		String turn = invertSymbol(symbol);
		board.updateBoad(number, symbol);
		if (!board.getWinner().equals("")) {
			newGame = true;
			board = new BoardOX();
			return "{\"number\":" + 10 + ",\"symbol\":\"" + symbol + "\"}";//number 10 = end of game
		}
		return message;

	}

	private String invertSymbol(String symbol) {
		if(symbol.equals("X")){
			return "O";
		}else {
			return "X";
		}
	}

}
