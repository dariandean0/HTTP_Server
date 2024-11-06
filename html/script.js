var enterButton = document.getElementById("enter");
var input = document.getElementById("userInput");
var ul = document.querySelector("ul");

function createListElement() {
    var li = document.createElement("li"); // creates an element "li"
    li.appendChild(document.createTextNode(input.value)); // makes text from input field the li text
    ul.appendChild(li); // adds li to ul
    input.value = ""; // Reset text input field

    // START STRIKETHROUGH
    function crossOut() {
        li.classList.toggle("done");
    }

    li.addEventListener("click", crossOut);
    // END STRIKETHROUGH

    // START ADD DELETE BUTTON
    var dBtn = document.createElement("button");
    dBtn.appendChild(document.createTextNode("X"));
    li.appendChild(dBtn);
    
    dBtn.addEventListener("click", function(event) {
        event.stopPropagation();  // Prevent the click event from reaching the li
        li.remove();  // Remove the li from the list
    });
    // END ADD DELETE BUTTON
}

function addListAfterClick() {
    if (input.value.trim().length > 0) {  // Ensure the input field is not empty
        createListElement();
    }
}

function addListAfterKeypress(event) {
    if (input.value.trim().length > 0 && event.key === "Enter") {  // Check for "Enter" key press
        createListElement();
    }
}

enterButton.addEventListener("click", addListAfterClick);
input.addEventListener("keypress", addListAfterKeypress);