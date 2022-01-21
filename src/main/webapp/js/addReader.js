$(document).ready(function () {
    reader.read(document);

    reader.form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (reader.isValid()) {
            reader.form.submit();
        }
    });

});



