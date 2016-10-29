//при загрузке страницы вызываем setNickname();
window.onload = setNickname;
//никнейм по умолчанию
var nickname = "Anonymous";

//соединяем с нашим сервиром
var socket = new WebSocket("ws://localhost:8080/Chat_war_exploded/chat");
//при получении сообщения от сервера вызываем receiveMessage()
socket.onmessage = receiveMessage;

function receiveMessage(event) {
    //переводим JSON-строку в js-строку
    var message = JSON.parse(event.data);
    addNewMessageToWindow(message);
}

//обработчик события на кнопке Send
function sendMessage() {
    //елемент input
    var messageBox = document.getElementById("messageBox");
    //содержимое input
    var messageContent = messageBox.value;
    //объект сообщения
    var message = {
        nickname: nickname,
        content: messageContent
    };
    //отпрявляет на сервер объект сообщения в виде JSON-строки
    socket.send(JSON.stringify(message));
    //очистка input
    messageBox.value = "";
}

function addNewMessageToWindow(message) {
    var messagesArea = document.getElementById("messages");
    var messageLine = document.createElement("li");
    messageLine.innerHTML = "<b>" + message.nickname + ": </b>" + message.content;
    messagesArea.appendChild(messageLine);
}

function setNickname() {
    nickname = prompt("Please enter your nickname", nickname);
}

//при нажатии на Enter срабатывает sendMessage()
function handleEnterKey() {
    sendMessage();
    return false;
}