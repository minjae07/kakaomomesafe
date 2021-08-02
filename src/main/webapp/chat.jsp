<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
            <title>채팅</title>
            <link rel="stylesheet" href="./assets/css/chat.css"></head>
            <body>
                <div class="chat_wrap">
                    <div class="header" OnClick="window.open('./board.html','location=no,status=no,scrollbars=yes')" style="cursor:pointer;">
                        MOME CHAT
                    </div>
                    <div class="mWindow">
                        <textarea class="textbox" id="messageWindow" readonly="true"></textarea>
                    </div>
                    <div class="input-div">
                        <input id="inputMessage" placeholder="채팅을 입력해주세요." onkeydown="if(event.keyCode==13){send();}"/>
                        <input type="submit" class="submit" value="전송" onclick="send()"/>
                    </div>
                </div>
            </body>
            <div class="message">
                <script type="text/javascript">
                    var textarea = document.getElementById("messageWindow");
                    var webSocket = new WebSocket('ws://localhost:8080/broadcasting');
                    var inputMessage = document.getElementById('inputMessage');
                    webSocket.onerror = function (event) {
                        onError(event)
                    };
                    webSocket.onopen = function (event) {
                        onOpen(event)
                    };
                    function onOpen(event) {
                        textarea.value += "채팅방에 입장하셨습니다. \n[바른말 고운말 사용합시다.] \n";
                    }
                    function onError(event) {
                        alert(event.data);
                    }
                    webSocket.onmessage = function (event) {
                        onMessage(event)
                    };
                    function onMessage(event) {
                        textarea.value += "상대 : " + event.data + "\n";
                    }
                    function send() {
                        textarea.value += "나 : " + inputMessage.value + "\n";
                        webSocket.send(inputMessage.value);
                        inputMessage.value = "";
                    }
                    setInterval(function () {
                        textarea.scrollTop = textarea.scrollHeight;
                    }, 100);
                </script>
            </div>
        </html>
    </head>
</html>