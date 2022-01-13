$(document).ready(function () {
    const date = new Date();
    date.setDate(date.getDate() + 30);
    document.querySelector('#preliminaryDate').value = date.getFullYear() + '-' +
        (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
        (date.getDate() < 10 ? '0' : '') + (date.getDate());

    let wrapper = $(".books");
    const add_button = $(".add");
    let bookId = 2;
    const form = document.getElementById("newIssue");
    const next = $('#nextBtn');
    const prev = $('#prevBtn');
    let currentTab = 0;

    showTab(currentTab);

    form.addEventListener('submit', function (event) {
        event.preventDefault();
    });

    $(add_button).click(function () {
        if (document.getElementsByClassName("bookName").length < 5) {
            $(wrapper).append('<div><input type="text" class="bookName" name="book' + bookId + '" id="book">\n' +
                '                <small></small><a href="#" class="delete">Удалить</a></div>');
            bookId++;
        } else {
            alert("Читателю нельзя выдать более 5 книг");
        }

    });

    $(wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    });

    $(next).click(function () {
        if (currentTab == 0) {
            validateReader();
        } else if (currentTab == 1) {
            validateBooks();
        } else {
            form.submit();
        }
    });

    $(prev).click(function () {
        currentTab = swipePrev(currentTab);
    });

    function validateReader() {
        const email = document.getElementById('email');
        let valid = false;
        let message = "";
        const value = email.value.trim();

        if (!isEmpty(value)) {
            $.get("/library/checkReaderExistence", {readerEmail: value}, function (responseJson) {
                valid = responseJson['valid'];
                message = responseJson['value'];

                if (valid) {
                    showSuccess(email);
                } else {
                    showError(email, message);
                }
                currentTab = swipeNext(valid, currentTab, form);
            });
        } else {
            showError(email, "Поле обязательно к заполнению");
        }
    }

    function validateBooks() {
        let books = document.querySelectorAll('.bookName');
        for (let i = 0; i < books.length; i++) {
            let value = books[i].value.trim();

            for (let j = i + 1; j < books.length; j++) {
                if (value === books[j].value.trim()) {
                    alert("Читателю нельзя выдать несколько одинаковых книг");
                    return false;
                }
            }

            if (!isEmpty(value)) {
                $.get("/library/checkBook", {bookName: value}, function (responseJson) {
                    let valid = responseJson['valid'];
                    let message = responseJson['value'];

                    if (valid) {
                        showSuccess(books[i]);
                    } else {
                        showError(books[i], message);
                    }

                    swipeBook(valid);
                });
            } else {
                showError(books[i], "Поле обязательно к заполнению");
            }
        }
    }

    let validated = 0;

    function swipeBook(valid) {
        const bookAmount = document.querySelectorAll(".bookName").length;

        if (valid) {
            validated++;
        }

        if (validated == bookAmount) {
            validated = 0;
            calculatePrice();
            currentTab = swipeNext(true, currentTab, form);
        }
    }

    function calculatePrice() {
        let cost = document.getElementById('cost');
        let books = document.querySelectorAll('.bookName');
        let total = 0;
        let amount = books.length;

        for (let i = 0; i < amount; i++) {
            $.get("/library/getCost", {bookName: books[i].value.trim()}, function (responseJson) {
                let value = responseJson['value'];
                total += +value * 30;
                let discount = 0;

                if (i == amount - 1) {
                     discount = 0;
                    if (amount > 2 && amount < 4) {
                        discount = 10;
                    } else if (amount > 4) {
                        discount = 15;
                    }
                    total -= total / 100 * discount;
                    cost.setAttribute('value', total);
                    document.getElementById('discount').setAttribute('value', discount + "");

                }
            });

        }
    }

});
