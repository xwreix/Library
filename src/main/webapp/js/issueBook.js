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

    let currentTab = 0;
    showTab(currentTab);

    function showTab(n) {
        let tabs = document.getElementsByClassName("tab");
        tabs[n].style.display = "block";

        if (n === 0) {
            document.getElementById("prevBtn").style.display = "none";
        } else {
            document.getElementById("prevBtn").style.display = "inline";
        }

        if (n === (tabs.length - 1)) {
            document.getElementById("nextBtn").innerHTML = "Отправить";
        } else {
            document.getElementById("nextBtn").innerHTML = "Вперед";
        }

        fixStepIndicator(n);

    }

    function fixStepIndicator(n) {
        let tabs = document.getElementsByClassName("step");
        for (let i = 0; i < tabs.length; i++) {
            tabs[i].className = tabs[i].className.replace(" active", "");
        }
        tabs[n].className += " active";
    }


    const next = document.getElementById('nextBtn');
    const prev = document.getElementById('prevBtn');

    next.addEventListener('click', function () {
        let tabs = document.getElementsByClassName("tab");
        let id = tabs[currentTab].getAttribute('id');
        if (id == 1) {
            validateReader();
        } else if (id == 2) {
            validateBooks();
        } else {
            form.submit();
        }
    });

    prev.addEventListener('click', function () {
        swipePrev();
    });

    function swipeNext(valid) {
        let tabs = document.getElementsByClassName("tab");
        if (!valid) return false;

        tabs[currentTab].style.display = "none";
        currentTab++;
        if (currentTab >= tabs.length) {
            form.submit();
        }
        showTab(currentTab);
    }

    function swipePrev() {
        let tabs = document.getElementsByClassName("tab");
        tabs[currentTab].style.display = "none";
        currentTab--;
        showTab(currentTab)
    }

    function validateReader() {
        const email = document.getElementById('email');
        let valid = false;
        let message = "";
        const value = email.value.trim();

        if (!isEmpty(value)) {
            $.get("/library/checkReader", {readerEmail: value}, function (responseJson) {
                valid = responseJson['valid'];
                message = responseJson['value'];

                if (valid) {
                    showSuccess(email);
                } else {
                    showError(email, message);
                }
                swipeNext(valid);
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
            swipeNext(true);
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
                    console.log("d"+discount+"t"+total);
                    total -= total / 100 * discount;
                    console.log(total);
                    cost.setAttribute('value', total);
                    document.getElementById('discount').setAttribute('value', discount + "");
                    console.log(document.getElementById('discount').value)

                }
            });

        }
    }

});
