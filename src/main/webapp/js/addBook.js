$(document).ready(function () {
    book.read(document);

    book.setDate();

    book.form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (book.isValid()) {
            book.form.submit();
        }

    });

    $($(".add")).click(function () {
        book.addAuthor();
    });

    $(book.wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    })

});



