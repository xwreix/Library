let reader = {
    surname: null,
    name: null,
    patronymic: null,
    passportNumber: null,
    dateOfBirth: null,
    email: null,
    form: null,

    read(document) {
        this.surname = document.querySelector('#surname');
        this.name = document.querySelector('#name');
        this.patronymic = document.querySelector('#patronymic');
        this.passportNumber = document.querySelector('#passportNumber');
        this.dateOfBirth = document.querySelector('#dateOfBirth');
        this.email = document.querySelector('#email');
        this.form = document.querySelector('#newReader');
    },

    isValid() {
        let isSurnameValid = false, isNameValid = false, isMailValid = false,
            isDateValid = false, isPatronymicValid, isPassportValid;

        if (checkRequired(this.surname)) {
            isSurnameValid = checkLength(this.surname, 50);
        }

        if (checkRequired(this.name)) {
            isNameValid = checkLength(this.name, 50);
        }

        if (checkRequired(this.email)) {
            isMailValid = checkEmail(this.email);
        }

        if (checkRequired(this.dateOfBirth)) {
            isDateValid = checkDate(this.dateOfBirth);
        }

        isPatronymicValid = checkLength(this.patronymic, 50);
        isPassportValid = checkPassport(this.passportNumber);

        return isSurnameValid && isNameValid && isMailValid &&
            isDateValid && isPatronymicValid && isPassportValid;
    }

};

const isEmailValid = (email) => {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

const checkEmail = (email) => {
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
const checkDate = (dateOfBirth) => {
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

const checkPassport = (passportNumber) => {
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
