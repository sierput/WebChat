var stompClient = null;
let board= null;
let symbolUser = null;
let turn = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#ox").html("ox");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/ox', function (message) {
            showGreeting(message);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName(number, symbol) {
	if(turn == null || turn == symbolUser){
		stompClient.send("/ox", {}, JSON.stringify({'number': number,'symbol': symbolUser}));
		turn = invertSymbol(symbol);
}
}
function invertSymbol(symbol) {
	if(symbol == "X"){
		return "O";
	}else {
		return "X";
	}
}

function showGreeting(message) {

	
	var number = JSON.parse(message.body).number;
	var symbol = JSON.parse(message.body).symbol;
	turn = invertSymbol(symbol);
	document.getElementById("turn").innerText = turn;
	if(number == 10){
		document.getElementById("info").innerText = "Wygrał: " + symbol;
		window.location.reload();
	} else {

		board[number].innerHTML = symbol;
	}

}
function getBoard() {
	board = [document.getElementById("p1"), document.getElementById("p2"), document.getElementById("p3"),
			 document.getElementById("p4"), document.getElementById("p5"), document.getElementById("p6"),
			 document.getElementById("p7"), document.getElementById("p8"), document.getElementById("p9")];
	
	
}
function setSymbol() {
	symbolUser = document.getElementById("symbol").innerText;
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect(); 
    getBoard();
    setSymbol();
    $( "#p1" ).click(function() {sendName(0,symbolUser)});
    $( "#p2" ).click(function() { sendName(1,symbolUser)});
    $( "#p3" ).click(function() { sendName(2,symbolUser);});
    $( "#p4" ).click(function() { sendName(3,symbolUser);});
    $( "#p5" ).click(function() { sendName(4,symbolUser);});
    $( "#p6" ).click(function() { sendName(5,symbolUser);});
    $( "#p7" ).click(function() { sendName(6,symbolUser);});
    $( "#p8" ).click(function() { sendName(7,symbolUser);});
    $( "#p9" ).click(function() { sendName(8,symbolUser);});
});
