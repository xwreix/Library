$(document).ready(function () {
    const date = new Date();
    document.querySelector('#returnDate').value = date.getFullYear() + '-' +
        (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
        (date.getDate() < 10 ? '0' : '') + (date.getDate());

    const form = document.getElementById("returnBook");
    let wrapper = $(".books");
    const add_button = $(".add");
    let bookId = 2;
    let currentTab = 0;
    const next = $('#nextBtn');
    const prev = $('#prevBtn');
    showTab(currentTab);

    form.addEventListener('submit', function (event) {
        event.preventDefault();
    });

    $(add_button).click(function () {
        if (document.getElementsByClassName("bookName").length < 5) {
            $(wrapper).append('<div class="copy"><label for="book">Наименование книги:</label>' +
                '<input type="text" class="bookName" name="book' + bookId + '" id="book" autocapitalize="on">' +
                '<small></small><label for="damage">Нарушения:</label>' +
                '<input type="text" class="damage" name="damage' + bookId + '" id="damage">' +
                '<input type="file" name="damagePhotos' + bookId + '[]" id="damagePhotos" multiple accept="image/*>">' +
                '<label for="rating">Рейтинг:</label>' +
                '<input type="number" class="rating" name="rating' + bookId + '" id="rating">' +
                '<a href="#" class="delete">Удалить</a></div>');
            bookId++;
        } else {
            alert("У читателя не может быть более 5 книг");
        }

    });

    $(wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    });


    $(prev).click(function () {
        currentTab = swipePrev(currentTab);
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


    function validateReader() {
        const email = document.getElementById('email');
        let valid = false;
        let message = "У читателя нет книг для возврата";
        const value = email.value.trim();

        if (!isEmpty(value)) {
            $.get("/library/countGivenBooks", {readerEmail: value}, function (responseJson) {
                let amount = responseJson['value'];

                if (amount > 0) {
                    showSuccess(email);
                    valid = true;
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
        const email = document.getElementById('email');
        const value = email.value.trim();
        let books = document.querySelectorAll('.bookName');
        let readerBooks = [];

        $.get("/library/getGivenBooks", {readerEmail: value}, function (responseJson) {
            $.each(responseJson, function (index, book) {
                let bookElem = {
                    name: book.name,
                    discount: book.discount,
                    preliminaryDate: book.preliminaryDate,
                    priceForDay: book.priceForDay
                }

                readerBooks.push(bookElem);
            });

            let flag = false;
            let valid = true;

            for (let i = 0; i < books.length; i++) {
                for (let j = 0; j < readerBooks.length; j++) {
                    if (books[i].value.trim() === readerBooks[j].name) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    showError(books[i], "Читатель не брал такую книгу");
                    valid = false;
                } else {
                    flag = false;
                }
            }

            if (valid) {
                let price = countPrice(readerBooks, books);
                const cost = document.getElementById('cost');
                cost.setAttribute('value', price + "");
            }

            currentTab = swipeNext(valid, currentTab, form);
        });
    }

    function countPrice(readerBooks, books) {
        let total = 0;

        for (let i = 0; i < books.length; i++) {
            for (let j = 0; j < readerBooks.length; j++) {
                if (books[i].value.trim() === readerBooks[j].name) {
                    const oneDay = 1000 * 60 * 60 * 24;
                    const preliminary = (Date.parse(readerBooks[j].preliminaryDate));
                    let diffInDays;
                    const price = +readerBooks[j].priceForDay;
                    let current = 0;

                    if (preliminary < date.getTime()) {
                        diffInDays = Math.round((date.getTime() - preliminary) / oneDay);
                        current += price * 30 + (price / 100 + price) * diffInDays;
                    } else {
                        diffInDays = Math.round((date.getTime() - (preliminary - oneDay * 30)) / oneDay);
                        current += price * diffInDays;
                    }

                    current -= current / 100 * (+readerBooks[j].discount);
                    total += current;
                }
            }
        }
        return total;
    }
});
