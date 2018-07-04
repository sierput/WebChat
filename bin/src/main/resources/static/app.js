var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#homePage").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/', function (message) {
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

function sendName() {
	let user =   document.getElementById("user").innerText;
    stompClient.send("/", {}, JSON.stringify({'sender': user,'content': $("#pole").val()}));
    
}

function showGreeting(message) {
	var actualUser = document.getElementById("user").innerText;
	
	var content = JSON.parse(message.body).content;
	var sender = JSON.parse(message.body).sender;
    $("#greetings").append("<tr><td class= pl-2 pr-2 bg-primary rounded text-white text-center send-msg mb-1 >" + content + "</td></tr>");
    let table = document.getElementById("conversation");
    let row = table.insertRow(-1);
    let newRow = JSON.stringify(sender)+ ': ' + JSON.stringify(content);
    let newRow2 = newRow.replace(/\"/g, "");
    let checkSpecialChar = newRow2.replace(/\</g, ""); // temp for HTML mark
    if(actualUser == sender) {
    let value = "<tr><td class= \"pl-2 pr-2 bg-primary rounded text-white text-center send-msg mb-1 \">" + checkSpecialChar + "</td></tr>";
    row.innerHTML = value;
    }
    else {
        let value = "<tr><td class= \"receive-msg rounded text-center mt-1 ml-1 pl-2 pr-2 mb-0 mt-1 pl-2 pr-2 rounded-top rounded-right \">" + checkSpecialChar + "</td></tr>";
        row.innerHTML = value;
    }
    
    document.getElementById("pole").value = "";
    
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect(); 
    $( "#send" ).click(function() { sendName(); });
});
