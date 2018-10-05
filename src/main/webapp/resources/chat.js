var poolIt = function () {
    $.ajax({
        type: "GET",
        url: "/rest/chat/" + $("#nick").val(),
        dataType: "text",
        success: function (message) {
            var obj = JSON.parse(message);
            var time = "[" + obj.date.hour + ":" + obj.date.minute + ":" + obj.date.second + "] ";
            // The message is added to the <li/> element when it is received.
            $("ul").append("<li>" + time + obj.sender + ": " + obj.content + "</li>");
            poolIt(); // Link to the re-polling when a message is consumed.
        },
        error: function () {
            poolIt(); // Start re-polling if an error occurs.
        }
    });
};

// When the submit button is clicked;
$('#post').click(function () {
    if ($(".nick").css("visibility") === "visible") { // If <tr> line is visible;
        $("textarea").prop("disabled", false); // Able to enter data
        $(".nick").css("visibility", "hidden"); // Make <tr> line invisible;
        $("span").html("Chat started..");  // Information message
        // Polling operation must be initiated at a time
        downloadOld();
        poolIt();
        document.getElementById("download").style.visibility = "visible";
    }
    else  // if it is not the first time ;
        $.ajax({
            type: "POST", // HTTP POST request
            url: "/rest/chat/" + $("#nick").val(),// access to the sendMessage(..) method.
            dataType: "text", // Incoming data type -> text
            data: $("textarea").val(), // Chat message to send
            contentType: "text/plain", // The type of the sent message
            success: function (data) {
                $("span").html(data); // It writes [Message is sent..] if successful.
                // Blink effect
                $("span").fadeOut('fast', function () {
                    $(this).fadeIn('fast');
                });
                document.getElementById("message").value = "";
            }
        });
});


$('#getUsers').click(function () {
    $.ajax({
        type: "GET",
        url: "/rest/chat/getUsers",
        dataType: "json",
        success: function (data) {
            alert(data)
        }
    });
});

var downloadOld = function () {
    $.ajax({
        type: "GET",
        url: "/rest/chat/oldMessages/10",
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var obj = data[i];
                var time = "[" + obj.date.hour + ":" + obj.date.minute + ":" + obj.date.second + "] ";
                $("ul").append("<li>" + time + data[i].sender + ": " + data[i].content + "</li>");
            }
        }
    });
};


