$(document).ready(function () {
    const form = document.querySelector('#newReader');

    const surname = document.querySelector('#surname');
    const name = document.querySelector('#name');
    const email = document.querySelector('#email');
    const dateOfBirth = document.querySelector('#dateOfBirth');
    const patronymic = document.querySelector('#patronymic');
    const passportNumber = document.querySelector('#passportNumber');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (areFieldsValid()) {
            form.submit();
        }

    });

    const isEmailValid = (email) => {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    };

    const checkEmail = () => {
        let valid = false;
        const val = email.value.trim();

        if (!isEmailValid(val)) {
            showError(email, "Email введён некорректно");
        } else {
            showSuccess(email);
            valid = true;
        }

        return valid;
    };

    const checkDate = () => {
        let valid = false;
        const val = new Date(dateOfBirth.value);

        if ((val.getFullYear() <= 1920) || (val.getFullYear() >= 2016)) {
            showError(dateOfBirth, "Недопустимый возраст");
        } else {
            showSuccess(dateOfBirth);
            valid = true;
        }

        return valid;
    };

    const checkPassport = () => {
        let valid = false;
        const val = passportNumber.value.trim();
        const re = /[A-Z]{2}[0-9]{7}/;
        if (!re.test(val) && !isEmpty(val)) {
            showError(passportNumber, "Номер паспорта введён некорректно");
        } else {
            showSuccess(passportNumber);
            valid = true;
        }

        return valid;
    };

    const areFieldsValid = () => {
        let isSurnameValid = false, isNameValid = false, isMailValid = false,
            isDateValid = false, isPatronymicValid, isPassportValid;

        if (checkRequired(surname)) {
            isSurnameValid = checkLength(surname, 50);
        }

        if (checkRequired(name)) {
            isNameValid = checkLength(name, 50);
        }

        if (checkRequired(email)) {
            isMailValid = checkEmail();
        }

        if (checkRequired(dateOfBirth)) {
            isDateValid = checkDate();
        }

        isPatronymicValid = checkLength(patronymic, 50);
        isPassportValid = checkPassport();

        return isSurnameValid && isNameValid && isMailValid &&
            isDateValid && isPatronymicValid && isPassportValid;

    }
});



