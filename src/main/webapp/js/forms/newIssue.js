let newIssue = {
    form: null,
    date: null,
    wrapper: null,
    bookId: 2,
    email: null,
    cost: null,
    books: null,
    swipe: true,
    discount: null,
    validated: 0,

    read(document) {
        this.form = document.getElementById("newIssue");
        this.date = document.querySelector('#preliminaryDate');
        this.wrapper = $(".books");
        this.email = document.getElementById('email');
        this.books = document.querySelectorAll('.bookName');
        this.cost = document.getElementById('cost');
        this.discount = document.getElementById('discount')
    },

    setDate() {
        const date = new Date();
        date.setDate(date.getDate() + 30);
        this.date.value = date.getFullYear() + '-' +
            (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
            (date.getDate() < 10 ? '0' : '') + (date.getDate());
    },

    addBook() {
        this.books = document.querySelectorAll('.bookName');
        if (this.books.length < 5) {
            $(this.wrapper).append('<div><input type="text" class="bookName" name="book' + this.bookId + '" id="book">\n' +
                '                <small></small><a href="#" class="delete">Удалить</a></div>');
            this.bookId++;
        } else {
            alert("Читателю нельзя выдать более 5 книг");
        }
    },

    validateReader() {
        let valid = false;
        let message = "";
        const value = this.email.value.trim();

        if (!isEmpty(value)) {
            $.get("/library/checkReaderExistence", {readerEmail: value}, function (responseJson) {
                valid = responseJson['valid'];
                message = responseJson['value'];

                if (valid) {
                    showSuccess(newIssue.email);
                } else {
                    showError(newIssue.email, message);
                }
                tab.currentTab = tab.swipeNext(valid, newIssue.form);
            });
        } else {
            showError(this.email, "Поле обязательно к заполнению");
        }
    },

    validateBooks() {
        let flag = true;
        this.books = document.querySelectorAll('.bookName');

        for (let i = 0; i < this.books.length; i++) {
            let value = this.books[i].value.trim();
            if (!isEmpty(value)) {
                for (let j = i + 1; j < this.books.length; j++) {
                    if (value === this.books[j].value.trim()) {
                        alert("Читателю нельзя выдать несколько одинаковых книг");
                        flag = false;
                    }
                }
            } else {
                showError(this.books[i], "Поле обязательно к заполнению");
                flag = false;
            }
        }

        if (flag) {
            let swipe = true;
            let amount = this.books.length;

            for (let i = 0; i < amount; i++) {
                let value = this.books[i].value.trim();
                $.get("/library/checkBook", {bookName: value}, function (responseJson) {
                    let valid = responseJson['valid'];
                    let message = responseJson['value'];

                    if (valid) {
                        showSuccess(newIssue.books[i]);
                    } else {
                        showError(newIssue.books[i], message);
                        swipe = false;
                    }

                    if (i === amount - 1 && swipe === true) {
                        newIssue.calculatePrice();
                        tab.swipeNext(true, this.form);
                    }

                });
            }
        }
    },

    calculatePrice() {
        let total = 0;
        let amount = this.books.length;
        let discount = 0;
        if (amount > 2 && amount <= 4) {
            discount = 10;
        } else if (amount > 4) {
            discount = 15;
        }
        newIssue.discount.setAttribute('value', discount + "");
        newIssue.cost.setAttribute('value', total);

        for (let i = 0; i < amount; i++) {
            $.get("/library/getCost", {bookName: newIssue.books[i].value.trim()}, function (responseJson) {
                total = 0;
                let value = responseJson['value'];
                total = +value * 30;

                total -= total / 100 * discount;
                total += +newIssue.cost.value;
                newIssue.cost.setAttribute('value', total);
            });
        }
    },
}