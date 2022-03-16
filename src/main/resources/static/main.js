var welcomeMsg = 'Book App';
document.querySelector('h1').innerText = welcomeMsg;

fetch('/books').then(resp => resp.json()).then(books => {
        document.querySelector('#books').innerHTML = listBooks(books);
    }
);

function listBooks(json) {
    return `${json.map(listBook).join('\n')}`;
};

let listBook = function(book) {
    return '<p>' + book.bookId + ": " + book.name + '</p>';
};

function postBook() {
    let book = {
        "bookId": document.getElementById("bookId").value,
        "name": document.getElementById("name").value
    }
    console.log(book);
    fetch("/books", {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)
    }).then((result) => {
        if (result.status != 200) {
            throw new Error("Bad Server Response");
        }
        console.log(result.text());
    }).catch((error) => { console.log(error); })
    fetch('/books').then(resp => resp.json()).then(books => {
            document.querySelector('#books').innerHTML = listBooks(books);
        }
    );
}

let button = document.querySelector('button');
button.addEventListener('mouseenter', function() {
    button.textContent = "Go!";
})