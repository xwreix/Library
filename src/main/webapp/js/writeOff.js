$(document).ready(function () {
    copy.read(document);

    $($('#writeOff')).click(function () {
        copy.getInfo();
    });

    copy.form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (copy.isValid()) {
            copy.form.submit();
        } else {
            alert("Данные заполнены некорректно");
        }
    });
});