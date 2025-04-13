'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// connect to the WebSocket server after the name is entered
function connect(event) {
    username = document.querySelector('#name').value.trim();
    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}
// when connected subscribe to the public topic and send a JOIN message
function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({ sender: username, type: 'JOIN' })
    );

    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    // if the message is not empty, send it to the server
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    // parse the payload to get the message
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    // checks the messege type whether it is a chat message or an event message
    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        var textElement = document.createElement('p');
        textElement.textContent = message.sender + ' joined!';
        messageElement.appendChild(textElement);
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        if(message.content === null){
            messageElement.style.display = 'none';
            return;
        }
        var textElement = document.createElement('p');
        // var senderName = message.content;
        textElement.textContent = message.content + ' left!';
        messageElement.appendChild(textElement);
    } else {
        messageElement.classList.add('chat-message');
        messageElement.style.display = 'flex';
        messageElement.style.alignItems = 'flex-start';
        messageElement.style.fontSize = '12px';
        // messageElement.style.margin = '10px 0';

        var avatarElement = document.createElement('div');
        avatarElement.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" fill="white" viewBox="0 0 24 24" width="24" height="24">
                <path d="M12 12c2.67 0 8 1.34 8 4v2h-16v-2c0-2.66 5.33-4 8-4zm0-2a4 4 0 100-8 4 4 0 000 8z"/>
            </svg>
        `;
        avatarElement.style.backgroundColor = getAvatarColor(message.sender);
        avatarElement.style.borderRadius = '50%';
        avatarElement.style.width = '40px';
        avatarElement.style.height = '40px';
        avatarElement.style.display = 'flex';
        avatarElement.style.alignItems = 'center';
        avatarElement.style.justifyContent = 'center';
        avatarElement.style.overflow = 'hidden';

        messageElement.appendChild(avatarElement);

        var contentElement = document.createElement('div');
        contentElement.style.display = 'flex';
        contentElement.style.flexDirection = 'column';
        // contentElement.style.marginLeft = '10px';

        var usernameElement = document.createElement('span');
        usernameElement.textContent = message.sender;
        usernameElement.style.fontSize = '16px';
        usernameElement.style.fontWeight = 'bold';
        usernameElement.style.color = getAvatarColor(message.sender);

        var textElement = document.createElement('p');
        textElement.textContent = message.content;
        textElement.style.margin = '0';
        textElement.style.fontSize = '16px';

        contentElement.appendChild(usernameElement);
        contentElement.appendChild(textElement);
        messageElement.appendChild(contentElement);
    }

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// Handle tab/window close to send LEAVE message
window.addEventListener('beforeunload', function () {
    if (stompClient && stompClient.connected && username) {
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'LEAVE' }));
        stompClient.disconnect();
    }
});

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
