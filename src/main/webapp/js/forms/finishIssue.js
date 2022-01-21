let finishIssue = {
    date: null,
    form: null,
    wrapper: null,
    bookId: 2,
    books: null,
    email: null,

    read(document) {
        this.form = document.getElementById("returnBook");
        this.date = document.querySelector('#returnDate');
        this.wrapper = $(".books");
        this.books = document.querySelectorAll('.bookName');
        this.email = document.getElementById('email');
    },

    setDate() {
        const date = new Date();
        this.date.value = date.getFullYear() + '-' +
            (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
            (date.getDate() < 10 ? '0' : '') + (date.getDate());
    },

    addBook() {
        this.books = document.querySelectorAll('.bookName');
        if (this.books.length < 5) {
            $(this.wrapper).append('<div class="copy"><label for="book">Наименование книги:</label>' +
                '<input type="text" class="bookName" name="book' + this.bookId + '" id="book" autocapitalize="on">' +
                '<small></small><label for="damage">Нарушения:</label>' +
                '<input type="text" class="damage" name="damage' + this.bookId + '" id="damage">' +
                '<input type="file" name="damagePhotos' + this.bookId + '[]" id="damagePhotos" multiple accept="image/*>">' +
                '<label for="rating">Рейтинг:</label>' +
                '<input type="number" class="rating" name="rating' + this.bookId + '" id="rating">' +
                '<a href="#" class="delete">Удалить</a></div>');
            this.bookId++;
        } else {
            alert("У читателя не может быть более 5 книг");
        }
    },

    validateReader() {
        const value = this.email.value.trim();

        if (!isEmpty(value)) {
            $.get("/library/countGivenBooks", {readerEmail: value}, function (responseJson) {
                let valid = responseJson['valid'];
                let message = responseJson['value'];

                if (valid) {
                    showSuccess(finishIssue.email);
                    valid = true;
                } else {
                    showError(finishIssue.email, message);
                }
                tab.currentTab = tab.swipeNext(valid, finishIssue.form);
            });
        } else {
            showError(this.email, "Поле обязательно к заполнению");
        }
    },

    validateBooks() {
        const value = this.email.value.trim();
        this.books = document.querySelectorAll('.bookName');
        let readerBooks = [];

        $.get("/library/getGivenBooks", {readerEmail: value}, function (responseJson) {
            $.each(responseJson, function (index, book) {
                let bookElem = {
                    name: book.name,
                    discount: book.discount,
                    preliminaryDate: book.date,
                    priceForDay: book.priceForDay
                }

                readerBooks.push(bookElem);
            });

            let flag = false;
            let valid = true;

            for (let i = 0; i < finishIssue.books.length; i++) {
                for (let j = 0; j < readerBooks.length; j++) {
                    if (finishIssue.books[i].value.trim() === readerBooks[j].name) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    showError(finishIssue.books[i], "Читатель не брал такую книгу");
                    valid = false;
                } else {
                    flag = false;
                    showSuccess(finishIssue.books[i]);
                }
            }

            if (valid) {
                let price = finishIssue.countPrice(readerBooks);
                const cost = document.getElementById('cost');
                cost.setAttribute('value', price + "");
            }

            tab.currentTab = tab.swipeNext(valid, finishIssue.form);
        });
    },

    countPrice(readerBooks) {
        const date = new Date();
        let total = 0;

        for (let i = 0; i < this.books.length; i++) {
            for (let j = 0; j < readerBooks.length; j++) {
                if (this.books[i].value.trim() === readerBooks[j].name) {
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
    },
}