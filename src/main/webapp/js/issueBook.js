$(document).ready(function () {
    let wrapper = $(".books");
    const add_button = $(".add");
    let bookId = 2;

    $(add_button).click(function () {
        console.log(document.getElementsByClassName("bookNames").length);
        if (document.getElementsByClassName("bookNames").length < 5) {
            $(wrapper).append('<div><input type="text" class="bookNames" name="book' + bookId + '" id="book">\n' +
                '                <small></small><a href="#" class="delete">Удалить</a></div>');
            bookId++;
        } else {
            alert("Читателю нельзя выдать более 5 книг");
        }

    });

    $(wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    })

    $(document).on('click', "#checkReader", function () {
        const email = document.getElementById('email');
        let valid = false;
        let message = "";

        $.get("/library/checkReader", {readerEmail: email.value.trim()}, function (responseJson) {
            valid = responseJson['valid'];
            message = responseJson['mess'];

            if (valid) {
                showSuccess(email);
            } else {
                showError(email, message);
            }
        });

    });
});