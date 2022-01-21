let book = {
    nameInRus: null,
    originalName: null,
    genres: null,
    cost: null,
    amount: null,
    date: null,
    priceForDay: null,
    covers: null,
    year: null,
    authors: null,
    form: null,
    wrapper: null,
    authorId: null,

    read(document) {
        this.nameInRus = document.querySelector('#nameInRus');
        this.originalName = document.querySelector('#originalName');
        this.genres = document.querySelector('#genres');
        this.cost = document.querySelector('#cost');
        this.amount = document.querySelector('#amount');
        this.date = document.querySelector('#registrDate');
        this.priceForDay = document.querySelector('#priceForDay');
        this.covers = document.querySelector('#covers');
        this.year = document.querySelector('#publYear');
        this.form = document.querySelector('#newBook');
        this.wrapper = $(".authors");
        this.authorId = 2;
    },

    setDate() {
        let date = new Date();
        this.date.value = date.getFullYear() + '-' +
            (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
            (date.getDate() < 10 ? '0' : '') + (date.getDate());
    },

    addAuthor() {
        $(this.wrapper).append('<div class="author"> <label> Имя автора:\n' +
            '                        <input class="authorName" type="text" name="author' + this.authorId + '">' +
            '                     <small></small>\n' + '</label>\n' +
            '                    <label> Фото автора:\n' +
            '                        <input type="file" name="authorPhotos' + this.authorId + '[]" multiple accept="image/*>">\n' +
            '                    </label><a href="#" class="delete">Удалить</a></div>');
        this.authorId++;
    },

    isValid() {
        let isNameInRusValid = false, isOriginalNameValid, isGenresValid, isCostValid,
            isAmountValid, isPriceForADayValid, isDateValid, isCoversValid, isYearValid = true,
            isAuthorsValid = true;

        if (checkRequired(this.nameInRus)) {
            isNameInRusValid = checkLength(this.nameInRus, 60);
        }

        isOriginalNameValid = checkLength(this.originalName, 60);
        isGenresValid = checkRequired(this.genres);
        isCostValid = checkRequired(this.cost);
        isAmountValid = checkRequired(this.amount);
        isCoversValid = checkRequired(this.covers);
        isPriceForADayValid = checkRequired(this.priceForDay);
        isDateValid = checkRequired(this.date);

        if (!isEmpty(this.year.value)) {
            isYearValid = checkYear(this.year);
        }

        this.authors = document.querySelectorAll('.authorName');
        for (let i = 0; i < this.authors.length; i++) {
            if (!checkAuthor(this.authors[i])) {
                isAuthorsValid = false;
            }
        }

        return isNameInRusValid && isOriginalNameValid && isGenresValid && isCostValid &&
            isAmountValid && isPriceForADayValid && isDateValid && isCoversValid && isYearValid &&
            isAuthorsValid;
    }
};


const checkAuthor = (param) => {
    let valid = false,
        val = param.value.trim(),
        message;

    if (isEmpty(val)) {
        message = 'Поле обязательно к заполнению';
    } else if (isNotBetween(val, 70)) {
        message = 'Введено слишком длинное значение';
    } else {
        showSuccess(param);
        valid = true;
    }

    if (!valid) {
        param.classList.remove('success');
        param.classList.add('error');

        const error = param.parentElement.querySelector('small');
        error.textContent = message;
    }

    return valid;
};

const isYearValid = (year) => year > 1000 && year < 2023;

const checkYear = (year) => {
    let valid = false;
    const val = year.value.trim();

    if (!isYearValid(val)) {
        showError(year, "Год введён неверно");
    } else {
        showSuccess(year);
        valid = true;
    }

    return valid;
};