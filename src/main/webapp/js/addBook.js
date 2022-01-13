$(document).ready(function () {
    let date = new Date();
    document.querySelector('#registrDate').value = date.getFullYear() + '-' +
        (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '-' +
        (date.getDate() < 10 ? '0' : '') + (date.getDate());

    const form = document.querySelector('#newBook');

    const nameInRus = document.querySelector('#nameInRus');
    const originalName = document.querySelector('#originalName');
    const genres = document.querySelector('#genres');
    const cost = document.querySelector('#cost');
    const amount = document.querySelector('#amount');
    date = document.querySelector('#registrDate');
    const priceForDay = document.querySelector('#priceForDay');
    const covers = document.querySelector('#covers');
    const year = document.querySelector('#publYear');

    let wrapper = $(".authors");
    const add_button = $(".add");
    let authorId = 2;

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (areRequiredValid()) {
            form.submit();
        }

    });

    $(add_button).click(function () {
        $(wrapper).append('<div class="author"> <label> Имя автора:\n' +
            '                        <input class="authorName" type="text" name="author' + authorId + '">' +
            '                     <small></small>\n' + '</label>\n' +
            '                    <label> Фото автора:\n' +
            '                        <input type="file" name="authorPhotos' + authorId + '[]" multiple accept="image/*>">\n' +
            '                    </label><a href="#" class="delete">Удалить</a></div>');
        authorId++;


    });

    $(wrapper).on("click", ".delete", function () {
        $(this).parent('div').remove();
    })

    function areRequiredValid() {
        let isNameInRusValid = false, isOriginalNameValid, isGenresValid, isCostValid,
            isAmountValid, isPriceForADayValid, isDateValid, isCoversValid, isYearValid = true,
            isAuthorsValid = true;

        if (checkRequired(nameInRus)) {
            isNameInRusValid = checkLength(nameInRus, 60);
        }

        isOriginalNameValid = checkLength(originalName, 60);
        isGenresValid = checkRequired(genres);
        isCostValid = checkRequired(cost);
        isAmountValid = checkRequired(amount);
        isCoversValid = checkRequired(covers);
        isPriceForADayValid = checkRequired(priceForDay);
        isDateValid = checkRequired(date);

        if (!isEmpty(year.value)) {
            console.log(isEmpty(year) + ' ' + year.value);
            isYearValid = checkYear();
        }

        let authors = document.querySelectorAll('.authorName');
        for (let i = 0; i < authors.length; i++) {
            if (!checkAuthor(authors[i])) {
                isAuthorsValid = false;
            }
        }

        return isNameInRusValid && isOriginalNameValid && isGenresValid && isCostValid &&
            isAmountValid && isPriceForADayValid && isDateValid && isCoversValid && isYearValid &&
            isAuthorsValid;
    }

    const isYearValid = (year) => year > 1000 && year < 2023;

    const checkYear = () => {
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
    }
});



